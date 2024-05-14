package com.yiaoBang.serialPortToolFX.serialComm;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.yiaoBang.serialPortToolFX.data.ByteBuffer;
import com.yiaoBang.serialPortToolFX.data.DataWriteFile;
import com.yiaoBang.serialPortToolFX.data.MockResponses;
import com.yiaoBang.serialPortToolFX.serialComm.listener.MessageListenerWithDelimiter;
import com.yiaoBang.serialPortToolFX.serialComm.listener.MessageListenerWithPacketSize;
import com.yiaoBang.javafxTool.core.FX;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 串行通信
 *
 * @author Y
 * @date 2024/05/14
 */
@Getter
public final class SerialComm implements AutoCloseable {
    //最多显示4096个接收到的字节(再多的话滚动条会出问题)
    private static final int maxShowByteNumber = 4096;
    private final ByteBuffer buffer = new ByteBuffer(maxShowByteNumber);
    //模拟回复
    @Setter
    private MockResponses mockResponses;
    @Setter
    private volatile long waitTime = 1000;
    private DataWriteFile dataWriteFile;
    /**
     * 发送保存(写入本地文件)
     */
    @Setter
    private volatile boolean sendSave;

    /**
     * 接收保存(写入本地文件)
     */
    @Setter
    private volatile boolean receiveSave;
    @Setter
    private volatile boolean receiveShow = false;

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


    private SerialPort serialPort;
    private String serialPortName;
    private int baudRate = 9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private String stopSting = "1";
    private int parity = 0;
    private String paritySting = "无";
    private int flowControl = 0;
    private String flowControlSting = "无";
    private SerialPortDataListener listener;
    private byte[] messageDelimiter = new byte[0];
    private int packSize = 0;

    public SerialComm() {
        this.listener = new MessageListenerWithDelimiter(this);
    }

    /**
     * 更新侦听器
     *
     * @param messageDelimiter 消息分隔符
     */
    public void updateListener(byte[] messageDelimiter) {
        if (messageDelimiter != null) {
            this.messageDelimiter = messageDelimiter;
            this.listener = new MessageListenerWithDelimiter(this);
            serialPort.removeDataListener();
            serialPort.addDataListener(this.listener);
        }
    }

    /**
     * 更新侦听器
     *
     * @param packSize 包大小
     */
    public void updateListener(int packSize) {
        if (messageDelimiter != null) {
            this.packSize = packSize;
            this.listener = new MessageListenerWithPacketSize(this);
            serialPort.removeDataListener();
            serialPort.addDataListener(this.listener);
        }
    }

    /**
     * 查找串口
     */
    public void findSerialPort() {
        close();
        for (SerialPort serial : SerialPort.getCommPorts()) {
            if (serial.getSystemPortName().equals(serialPortName)) {
                serialPort = serial;
                return;
            }
        }
        serialPort = null;
    }

    /**
     * 打开串口
     */
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
        FX.run(() -> serialPortState.set(b));
    }

    /**
     * 写
     *
     * @param bytes 字节
     * @return int
     */
    public int write(byte[] bytes) {
        if (bytes != null && serialPort != null && serialPort.isOpen()) {
            int sendNumber = serialPort.writeBytes(bytes, bytes.length);
            if (sendNumber > 0) {
                FX.run(() -> SEND_LONG_PROPERTY.set(SEND_LONG_PROPERTY.get() + sendNumber));
                if (sendSave)
                    Thread.startVirtualThread(() -> dataWriteFile.serialCommSend(bytes));
            } else {
                this.close();
            }
            return sendNumber;
        }
        return 0;
    }

    /**
     * 听
     *
     * @param bytes 字节
     */
    public void listen(byte[] bytes) {
        //模拟回复
        if (mockResponses != null) {
            Thread.startVirtualThread(() -> {
                byte[] reply = mockResponses.reply(bytes);
                if (reply != null) {
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    write(reply);
                }
            });
        }
        if (receiveShow)
            buffer.add(bytes);
        FX.run(() -> RECEIVE_LONG_PROPERTY.set(RECEIVE_LONG_PROPERTY.get() + bytes.length));
        if (receiveSave)
            Thread.startVirtualThread(() -> dataWriteFile.serialCommReceive(bytes));
    }

    /**
     * 获取数据
     *
     * @return {@code byte[] }
     */
    public byte[] getData() {
        return buffer.getBuffer();
    }

    /**
     * 清除发送
     */
    public void clearSend() {
        FX.run(() -> SEND_LONG_PROPERTY.set(0));
    }

    /**
     * 清除接收
     */
    public void clearReceive() {
        FX.run(() -> RECEIVE_LONG_PROPERTY.set(0));
    }

    /**
     * 设置串口名称
     *
     * @param serialPortName 串口名称
     */
    public void setSerialPortName(String serialPortName) {
        this.serialPortName = serialPortName;
        openSerialPort();
    }

    /**
     * 设置波特率
     *
     * @param baudRate 波特率
     */
    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
        openSerialPort();
    }

    /**
     * 设置数据位
     *
     * @param dataBits 数据位
     */
    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
        openSerialPort();
    }

    /**
     * 设置停止位
     *
     * @param stopBits 停止位
     */
    public void setStopBits(String stopBits) {
        this.stopSting = stopBits;
        this.stopBits = switch (stopBits) {
            case "1.5" -> SerialPort.ONE_POINT_FIVE_STOP_BITS;
            case "2" -> SerialPort.TWO_STOP_BITS;
            default -> SerialPort.ONE_STOP_BIT;
        };
        openSerialPort();
    }

    /**
     * 设置奇偶校验
     *
     * @param parity 平价
     */
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

    /**
     * 设置流量控制
     *
     * @param flowControl 流控制
     */
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

    /**
     * 关闭
     */
    @Override
    public void close() {
        if (serialPort != null) {
            serialPort.closePort();
        }
        FX.run(() -> serialPortState.set(false));
    }
}
