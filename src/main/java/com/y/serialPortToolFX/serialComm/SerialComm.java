package com.y.serialPortToolFX.serialComm;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.y.serialPortToolFX.serialComm.listener.MessageListenerWithDelimiter;
import com.y.serialPortToolFX.serialComm.listener.MessageListenerWithPacketSize;
import com.y.serialPortToolFX.utils.FX;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Getter
public class SerialComm implements AutoCloseable {
    protected DataWriteFile dataWriteFile;
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
    public static final int[] BAUD_RATE = {9600, 19200, 38400, 115200, 128000, 230400, 256000, 460800, 921600, 1382400};
    public static final int[] DATA_BITS = {8, 7, 6, 5};
    public static final String[] STOP_BITS = {"1", "1.5", "2"};
    public static final String[] PARITY = {"无", "奇校验", "偶校验", "标记校验", "空格校验"};
    public static final String[] FLOW_CONTROL = {"无", "RTS/CTS", "DSR/DTR", "XoN/XoFF"};


    private volatile SerialPort serialPort;
    private volatile String serialPortName;
    private volatile int baudRate = 9600;
    private volatile int dataBits = 8;
    private volatile int stopBits = 1;
    private volatile int parity = 0;
    private volatile int flowControl = 0;
    private volatile SerialPortDataListener listener;
    private volatile byte[] messageDelimiter = new byte[0];
    private volatile int packSize = 0;

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
    public final void updateListener(int packSize) {
        if (messageDelimiter != null) {
            this.packSize = packSize;
            this.listener = new MessageListenerWithPacketSize(this);
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

    public final boolean openSerialPort() {
        findSerialPort();
        if (serialPort != null) {
            serialPort.setComPortParameters(baudRate, dataBits, stopBits, parity);
            serialPort.setFlowControl(flowControl);
            serialPort.addDataListener(this.listener);
            boolean b = serialPort.openPort();
            dataWriteFile = b ? new DataWriteFile(serialPortName) : null;
            return b;
        }
        return false;
    }

    public final void write(byte[] bytes) {
        if (serialPort != null && serialPort.isOpen()) {
            int sendNumber = serialPort.writeBytes(bytes, bytes.length);
            if (sendNumber > 0) {
                FX.run(() -> SEND_LONG_PROPERTY.set(SEND_LONG_PROPERTY.get() + sendNumber));
                if (sendSave)
                    Thread.startVirtualThread(() -> dataWriteFile.serialCommSend(bytes));
            }

        }
    }

    public final void listen(byte[] bytes) {
        FX.run(() -> RECEIVE_LONG_PROPERTY.set(RECEIVE_LONG_PROPERTY.get() + bytes.length));
        if (receiveSave)
            Thread.startVirtualThread(() -> dataWriteFile.serialCommReceive(bytes));
    }

    @Override
    public void close() {
        if (serialPort != null) {
            serialPort.closePort();
        }
    }
}
