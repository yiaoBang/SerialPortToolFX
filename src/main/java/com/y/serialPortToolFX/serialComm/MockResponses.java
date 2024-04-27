package com.y.serialPortToolFX.serialComm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.y.serialPortToolFX.utils.CodeFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public final class MockResponses {
    public static final Gson JSON = new Gson();
    private static final Type type = new TypeToken<Map<String, String>>() {
    }.getType();
    private int packSize = 0;
    private byte[] delimiter;
    private Map<String, byte[]> replays;

    private MockResponses() {
    }

    public byte[] reply(byte[] bytes) {
        return replays.get(CodeFormat.utf8(bytes));
    }

    public static MockResponses parseJson(File file) {
        try {
            MockResponses mockResponses = new MockResponses();
            List<String> strings = Files.readAllLines(file.toPath());
            StringBuilder stringBuilder = new StringBuilder();
            strings.forEach(stringBuilder::append);
            //json  字符串
            String json = stringBuilder.toString();
            Map<String, String> maps = JSON.fromJson(json, type);


            //编码格式
            String encode = maps.get("encode");
            //结束符
            String delimiter = maps.get("delimiter");
            String packSize = maps.get("packSize");
            maps.remove("encode");
            maps.remove("delimiter");
            maps.remove("packSize");

            //如果数据包大小和结束符都是空的 则不必继续
            if (packSize.isEmpty() && delimiter.isEmpty()) {
                return null;
            }
            if (maps.isEmpty()) {
                return null;
            }


            //回复的数据
            Map<String, byte[]> replay = new HashMap<>();
            if ("HEX".equalsIgnoreCase(encode)) {
                //16进制
                try {
                    mockResponses.setPackSize(Integer.parseInt(packSize));
                } catch (NumberFormatException e) {
                    byte[] hex = CodeFormat.hex(delimiter);
                    if (hex == null)
                        return null;
                    mockResponses.setDelimiter(hex);
                }
                maps.forEach((k, v) -> replay.put(CodeFormat.hexToUtf8(k), CodeFormat.hex(v)));
                mockResponses.setReplays(replay);
            } else {
                try {
                    mockResponses.setPackSize(Integer.parseInt(packSize));
                } catch (NumberFormatException e) {
                    mockResponses.setDelimiter(CodeFormat.utf8(delimiter));
                }
                maps.forEach((k, v) -> replay.put(CodeFormat.utf8(CodeFormat.utf8(k)), CodeFormat.utf8(v)));
                mockResponses.setReplays(replay);
            }
            return mockResponses;
        } catch (IOException e) {
            return null;
        }
    }

}
