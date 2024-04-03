package com.y.serialPortToolFX.serialComm;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.y.serialPortToolFX.serialComm.listener.MessageListenerWithDelimiter;
import com.y.serialPortToolFX.serialComm.listener.MessageListenerWithPacketSize;
import com.y.serialPortToolFX.utils.FX;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class SerialComm implements AutoCloseable {
    private DataWriteFile dataWriteFile;
    @Setter
    /**
     * 发送保存(写入本地文件)
     */
    private volatile boolean sendSave;
    @Setter
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
    public static final Integer[] BAUD_RATE = {9600, 19200, 38400, 115200, 128000, 230400, 256000, 460800, 921600, 1382400};
    public static final Integer[] DATA_BITS = {8, 7, 6, 5};
    public static final String[] STOP_BITS = {"1", "1.5", "2"};
    public static final String[] PARITY = {"无", "奇校验", "偶校验", "标记校验", "空格校验"};
    public static final String[] FLOW_CONTROL = {"无", "RTS/CTS", "DSR/DTR", "XoN/XoFF"};

    //最多显示10000个接收到的字节
    private static final int maxShowByteNumber = 102_400;
    private final DirectByteBufferCache directByteBufferCache = new DirectByteBufferCache(maxShowByteNumber);

    private volatile SerialPort serialPort;
    private volatile String serialPortName;
    private volatile int baudRate = 9600;
    private volatile int dataBits = 8;
    private volatile int stopBits = 1;
    private volatile String stopSting = "1";
    private volatile int parity = 0;
    private volatile String paritySting = "无";
    private volatile int flowControl = 0;
    private volatile String flowControlSting = "无";
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

    public void updateListener(byte[] messageDelimiter) {
        if (messageDelimiter != null) {
            this.messageDelimiter = messageDelimiter;
            this.listener = new MessageListenerWithDelimiter(this);
            serialPort.removeDataListener();
            serialPort.addDataListener(this.listener);
        }
    }

    public void updateListener(int packSize) {
        if (messageDelimiter != null) {
            this.packSize = packSize;
            this.listener = new MessageListenerWithPacketSize(this);
            serialPort.removeDataListener();
            serialPort.addDataListener(this.listener);
        }
    }

    public void findSerialPort() {
        close();
        for (SerialPort serial : SerialPortMonitor.commPorts) {
            if (serial.getSystemPortName().equals(serialPortName)) {
                serialPort = serial;
                return;
            }
        }
        serialPort = null;
    }

    public void openSerialPort() {
        findSerialPort();
        if (serialPort == null) {
            FX.run(() -> serialPortState.set(false));
            return;
        }
        serialPort.setComPortParameters(baudRate, dataBits, stopBits, parity);
        serialPort.setFlowControl(flowControl);
        serialPort.addDataListener(this.listener);
        boolean b = serialPort.openPort();
        dataWriteFile = b ? new DataWriteFile(serialPortName) : null;
        FX.run(() -> serialPortState.set(serialPort.isOpen()));
    }

    public void write(byte[] bytes) {
        if (bytes != null && serialPort != null && serialPort.isOpen()) {
            int sendNumber = serialPort.writeBytes(bytes, bytes.length);
            if (sendNumber > 0) {
                FX.run(() -> SEND_LONG_PROPERTY.set(SEND_LONG_PROPERTY.get() + sendNumber));
                if (sendSave)
                    Thread.startVirtualThread(() -> dataWriteFile.serialCommSend(bytes));
            }
        }
    }

    public void listen(byte[] bytes) {
        FX.run(() -> RECEIVE_LONG_PROPERTY.set(RECEIVE_LONG_PROPERTY.get() + bytes.length));
        if (receiveSave)
            Thread.startVirtualThread(() -> dataWriteFile.serialCommReceive(bytes));
    }

    public void clearSend() {
        FX.run(() -> SEND_LONG_PROPERTY.set(0));
    }

    public void clearReceive() {
        FX.run(() -> RECEIVE_LONG_PROPERTY.set(0));
    }

    public void setSerialPortName(String serialPortName) {
        this.serialPortName = serialPortName;
        openSerialPort();
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
        openSerialPort();
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
        openSerialPort();
    }

    public void setStopBits(String stopBits) {
        this.stopSting = stopBits;
        this.stopBits = switch (stopBits) {
            case "1.5" -> SerialPort.ONE_POINT_FIVE_STOP_BITS;
            case "2" -> SerialPort.TWO_STOP_BITS;
            default -> SerialPort.ONE_STOP_BIT;
        };
        openSerialPort();
    }

    public void setParity(String parity) {
        this.paritySting = parity;
        this.parity = switch (parity) {
            case "奇校验" -> SerialPort.ODD_PARITY;
            case "偶校验" -> SerialPort.EVEN_PARITY;
            case "标记校验" -> SerialPort.MARK_PARITY;
            case "空格校验" -> SerialPort.SPACE_PARITY;
            default -> SerialPort.NO_PARITY;
        };
        openSerialPort();
    }

    public void setFlowControl(String flowControl) {
        this.flowControlSting = flowControl;
        this.flowControl = switch (flowControl) {
            case "RTS/CTS" -> SerialPort.FLOW_CONTROL_RTS_ENABLED | SerialPort.FLOW_CONTROL_CTS_ENABLED;
            case "DSR/DTR" -> SerialPort.FLOW_CONTROL_DSR_ENABLED | SerialPort.FLOW_CONTROL_DTR_ENABLED;
            case "ON/OFF" -> SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED | SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED;
            default -> SerialPort.FLOW_CONTROL_DISABLED;
        };
        openSerialPort();
    }

    @Override
    public void close() {
        if (serialPort != null) {
            serialPort.closePort();
            FX.run(() -> serialPortState.set(serialPort.isOpen()));
        }
    }
}
