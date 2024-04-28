package com.y.serialPortToolFX.serialComm.listener;

import com.fazecast.jSerialComm.SerialPort;
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
        return serialComm.getPackSize();
    }
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }
    @Override
    public void serialEvent(SerialPortEvent event) {
        serialComm.listen(event.getReceivedData());
    }
}
