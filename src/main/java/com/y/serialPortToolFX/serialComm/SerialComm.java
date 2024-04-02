package com.y.serialPortToolFX.serialComm;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.y.serialPortToolFX.serialComm.listener.MessageListenerWithDelimiter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SerialComm implements AutoCloseable {

    /**
     * 发送保存(写入本地文件)
     */
    private volatile boolean sendSave;
    /**
     * 接收保存(写入本地文件)
     */
    private volatile boolean receiveSave;

    //发送的数据量
    private final SimpleLongProperty SEND_LONG_PROPERTY = new SimpleLongProperty(0);

    //接收的数据量
    private final SimpleLongProperty RECEIVE_LONG_PROPERTY = new SimpleLongProperty(0);

    //串口状态
    private final SimpleBooleanProperty serialPortState = new SimpleBooleanProperty(false);


    private volatile SerialPort serialPort;
    @Setter
    private volatile String serialPortName;
    private volatile int baudRate = 9600;
    private volatile int dataBits = 8;
    private volatile int stopBits = 1;
    private volatile int parity = 0;
    private volatile int flowControl = 0;
    private volatile SerialPortDataListener listener;
    private volatile byte[] messageDelimiter = new byte[0];

    public SerialComm() {
        this.listener = new MessageListenerWithDelimiter(this);
    }

    public SerialComm(byte[] messageDelimiter) {
        this.messageDelimiter = messageDelimiter;
        this.listener = new MessageListenerWithDelimiter(this);
    }

    public final void updateListener(byte[] messageDelimiter) {
        if (messageDelimiter != null) {
            this.messageDelimiter = messageDelimiter;
            this.listener = new MessageListenerWithDelimiter(this);
            serialPort.removeDataListener();
            serialPort.addDataListener(this.listener);
        }
    }

    public final void findSerialPort() {
        close();
        for (SerialPort serial : SerialPortMonitor.commPorts) {
            if (serial.getSystemPortName().equals(serialPortName)) {
                serialPort = serial;
                return;
            }
        }
        serialPort = null;
    }


    @Override
    public void close() {

    }
}
