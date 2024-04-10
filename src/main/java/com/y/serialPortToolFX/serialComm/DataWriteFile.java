package com.y.serialPortToolFX.serialComm;


import com.y.serialPortToolFX.AppLauncher;
import com.y.serialPortToolFX.utils.CodeFormat;
import com.y.serialPortToolFX.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author Y
 * @version 1.0
 * @date 2024/3/23 14:56
 */
public class DataWriteFile {
    private static final File dataFile = new File(AppLauncher.ROOT_FILE_PATH, "logs");

    static {
        if (!dataFile.exists()) {
            dataFile.mkdirs();
        }
    }

    private DataWriteFile() {

    }

    private static final OpenOption[] options = {StandardOpenOption.CREATE, StandardOpenOption.APPEND};
    private Path readFile;
    private Path writeFile;

    public DataWriteFile(String serialPortName) {
        File read = new File(dataFile, serialPortName + "-" + TimeUtils.getFileName() + ".read");
        File write = new File(dataFile, serialPortName + "-" + TimeUtils.getFileName() + ".write");
        read.delete();
        write.delete();
        readFile = read.toPath();
        writeFile = write.toPath();
    }
    public void serialCommReceive(byte[] bytes) {
        Thread.startVirtualThread(() -> {
            try {
                Files.writeString(readFile, CodeFormat.hex(bytes), options);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void serialCommSend(byte[] bytes) {
        Thread.startVirtualThread(() -> {
            try {
                Files.writeString(writeFile, CodeFormat.hex(bytes), options);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
