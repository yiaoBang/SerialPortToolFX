package com.y.serialPortToolFX.serialComm.listener;

import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListenerWithExceptions;
import com.y.serialPortToolFX.serialComm.SerialComm;

public class MessageListenerWithDelimiter implements SerialPortMessageListenerWithExceptions {
    private final SerialComm serialComm;

    public MessageListenerWithDelimiter(SerialComm serialComm) {
        this.serialComm = serialComm;
    }
    @Override
    public void catchException(Exception e) {

    }

    @Override
    public byte[] getMessageDelimiter() {
        return new byte[0];
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        //true表示结束符,false表示开始符号
        return true;
    }

    @Override
    public int getListeningEvents() {
        return 0;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {

    }
}
