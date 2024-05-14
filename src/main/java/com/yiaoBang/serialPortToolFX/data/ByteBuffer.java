package com.yiaoBang.serialPortToolFX.data;

public final class ByteBuffer {
    private final CircularQueue CircularQueue;

    public ByteBuffer(int maxSize) {
        CircularQueue = new CircularQueue(maxSize);
    }

    public void add(byte[] bytes) {
        CircularQueue.add(bytes);
    }

    public byte[] getBuffer() {
        return CircularQueue.getBytes();
    }

    public void close() {
        CircularQueue.clear();
    }
}
