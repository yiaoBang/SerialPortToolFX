package com.yiaoBang.serialPortToolFX.serialComm.listener.cus;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MessageListenerWithDelimiter implements SerialPortDataListener {

    private static final byte[] END_DELIMITER = {'\r', '\n'}; // Define your end delimiter
    private List<Byte> buffer = new ArrayList<>();
    // Buffer to store incoming data
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
            byte[] newData = event.getReceivedData();
            for (byte b : newData) {
                buffer.add(b);
                // Check for end delimiter
                if (checkForEndDelimiter()) {
                    processCompleteMessage();
                }
            }
        }
    }

    private boolean checkForEndDelimiter() {
        if (buffer.size() < END_DELIMITER.length) {
            return false;
        }
        for (int i = 0; i < END_DELIMITER.length; i++) {
            if (buffer.get(buffer.size() - END_DELIMITER.length + i) != END_DELIMITER[i]) {
                return false;
            }
        }
        return true;
    }

    private void processCompleteMessage() {
        byte[] completeMessage = new byte[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            completeMessage[i] = buffer.get(i);
        }
        // Call the method to process the complete message
        handleCompleteMessage(completeMessage);
        // Clear the buffer
        buffer.clear();
    }

    private void handleCompleteMessage(byte[] message) {
        String receivedData = new String(message, StandardCharsets.UTF_8);
        System.out.println("Complete message received: " + receivedData);
        // Add more processing logic as needed
    }

    public static void main(String[] args) {
        SerialPort comPort = SerialPort.getCommPorts()[0]; // Get the first available serial port
        comPort.addDataListener(new MessageListenerWithDelimiter());

        if (comPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.out.println("Unable to open the port.");
        }
    }
}
