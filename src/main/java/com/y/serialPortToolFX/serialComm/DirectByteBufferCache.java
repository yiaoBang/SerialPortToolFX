package com.y.serialPortToolFX.serialComm;
import java.nio.ByteBuffer;
public final class DirectByteBufferCache implements AutoCloseable {
    private final ByteBuffer buffer;
    private final int capacity;
    public DirectByteBufferCache(int capacity) {
        this.capacity = capacity;
        this.buffer = ByteBuffer.allocateDirect(capacity);
    }
    public synchronized void add(byte[] bytes) {
        int bytesToAdd = bytes.length;
        if (bytesToAdd > capacity) {
            System.out.println("无法添加，字节数超出缓存容量");
            return;
        }

        while (buffer.position() + bytesToAdd > capacity) {
            remove();
        }

        buffer.put(bytes);
    }
    private synchronized void remove() {
        byte[] temp = new byte[buffer.remaining()];
        buffer.flip();
        buffer.get(temp);
        buffer.clear();
        buffer.put(temp, 1, temp.length - 1);
    }
    public synchronized byte[] getBuffer() {
        byte[] result = new byte[buffer.position()];
        buffer.flip();
        buffer.get(result);
        buffer.flip();
        return result;
    }
    @Override
    public void close() {
        buffer.clear();
    }
}
