package com.y.serialPortToolFX.serialComm.listener;

import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.y.serialPortToolFX.serialComm.SerialComm;

public class MessageListenerWithPacketSize implements SerialPortPacketListener {
    private final SerialComm serialComm;
    public MessageListenerWithPacketSize(SerialComm serialComm) {
        this.serialComm = serialComm;
    }
    @Override
    public int getPacketSize() {
        return 0;
    }

    @Override
    public int getListeningEvents() {
        return 0;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {

    }
}
