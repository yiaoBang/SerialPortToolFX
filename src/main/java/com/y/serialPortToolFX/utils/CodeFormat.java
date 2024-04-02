package com.y.serialPortToolFX.utils;

import java.nio.charset.StandardCharsets;

public class CodeFormat {
    private CodeFormat() {
    }

    public static String utf8(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
    public static byte[] utf8(String msg) {
        return msg.getBytes(StandardCharsets.UTF_8);
    }
    public static String hex(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        for (byte aByte : bytes) {
            stringBuilder.append(String.format("%02X ", aByte));
        }
        return stringBuilder.toString();
    }
}
