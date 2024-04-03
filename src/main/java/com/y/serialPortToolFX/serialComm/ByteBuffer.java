package com.y.serialPortToolFX.serialComm;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ByteBuffer implements AutoCloseable {
    private final int maxSize;
    private final Queue<Byte> messageQueue = new ConcurrentLinkedQueue<>();

    public ByteBuffer(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(byte[] bytes) {
        if (messageQueue.size() + bytes.length > maxSize) {
            for (int i = 0; i < maxSize / 3; i++) {
                messageQueue.poll();
            }
        }
        for (byte aByte : bytes) {
            messageQueue.add(aByte);
        }
    }

    public byte[] getBuffer() {
        int size = messageQueue.size();
        byte[] byteArray = new byte[size];
        int index = 0;
        for (Byte b : messageQueue) {
            byteArray[index++] = b;
        }
        return byteArray;
    }
    @Override
    public void close() {
        messageQueue.clear();
    }
}
