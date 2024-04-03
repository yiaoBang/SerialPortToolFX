package com.y.serialPortToolFX.serialComm;

import com.fazecast.jSerialComm.SerialPort;
import com.y.serialPortToolFX.utils.FX;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import java.util.Arrays;
import java.util.StringJoiner;

public class SerialPortMonitor {
    public static volatile SerialPort[] commPorts = SerialPort.getCommPorts();
    private static StringJoiner sj = new StringJoiner("\n");
    public static final SimpleStringProperty serialPorts = new SimpleStringProperty(serialPortsToString());
    public static final Thread serialPortMonitorThread;

    static {
//        serialPorts.addListener((observable, oldValue, newValue) -> {
//            System.out.println("=========================");
//            System.out.println(newValue);
//            System.out.println("=========================");
//        });
        serialPortMonitorThread = new Thread(() -> {
            while (true) {
                scanSerialPort();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {

                }
            }
        }, "串口监控线程");
    }

    /**
     * 扫描串口
     */
    public static void scanSerialPort() {
        SerialPort[] newPorts = SerialPort.getCommPorts();
        if (!checkSerialPort(newPorts)) {
            commPorts = newPorts;
            FX.run(() -> serialPorts.set(serialPortsToString()));
            sj = new StringJoiner("\n");
        }
    }

    private static String serialPortsToString() {
        for (SerialPort commPort : commPorts) {
            sj.add(commPort.getSystemPortName());
        }
        return sj.toString();
    }

    /**
     * 检查这次扫描到的串口的列表和上次的列表是否一致从而决定是否更新串口的列表
     *
     * @param newPorts 最新扫描到的串口列表
     * @return boolean  false=更新  true = 不更新
     */
    private static boolean checkSerialPort(SerialPort[] newPorts) {
        if (commPorts.length != newPorts.length) {
            return false;
        }
        for (int i = 0; i < newPorts.length; i++) {
            if (!commPorts[i].getSystemPortName().equals(newPorts[i].getSystemPortName())) {
                return false;
            }
        }
        return true;
    }

    public static void init() {
        serialPortMonitorThread.start();
    }
}
