package com.yiaoBang.serialPortToolFX.data;

public class CircularQueue {
    private final byte[] array;
    private int front; // 头部指针
    private int rear; // 尾部指针
    private int size; // 队列当前大小
    private final int capacity; // 队列容量

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        array = new byte[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }


    /**
     * 加
     *
     * @param bytes 字节
     */
    public void add(byte[] bytes) {
        if (bytes.length > capacity) {
            throw new IllegalArgumentException("Byte array size exceeds queue capacity");
        }

        // 如果队列剩余空间不足以容纳整个字节数组，则直接计算需要出队的元素个数
        int elementsToDequeue = Math.max(0, size + bytes.length - capacity);

        // 出队需要的元素个数
        for (int i = 0; i < elementsToDequeue; i++) {
            dequeue();
        }

        // 插入整个字节数组到循环队列中
        for (byte b : bytes) {
            rear = (rear + 1) % capacity; // 尾部指针向后移动，如果到达数组末尾则回到数组开头
            array[rear] = b; // 在尾部插入新元素
            size++;
        }
    }

    /**
     * 清楚
     */
    public void clear(){
        front = 0;
        rear = -1;
        size = 0;
    }

    /**
     * 获取字节数
     *
     * @return {@code byte[] }
     */
    public byte[] getBytes() {
        byte[] result = new byte[size];
        int index = 0;

        // 复制第一段：从头部指针到数组末尾
        for (int i = front; i < capacity && index < size; i++) {
            result[index++] = array[i];
        }

        // 如果数组末尾还有剩余元素，并且第一段没有完全填满结果数组，则继续复制第一段的剩余元素
        for (int i = 0; i <= rear && index < size; i++) {
            result[index++] = array[i];
        }

        return result;
    }

    /**
     * 大小
     *
     * @return int
     */
    public int size() {
        return size;
    }


    private void dequeue() {
        if (size == 0) {
            throw new IllegalStateException("Queue is empty");
        }
        front = (front + 1) % capacity; // 头部指针向后移动，如果到达数组末尾则回到数组开头
        size--;
    }
}
