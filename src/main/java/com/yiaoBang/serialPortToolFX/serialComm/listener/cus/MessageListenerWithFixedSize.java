package com.yiaoBang.serialPortToolFX.serialComm.listener.cus;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MessageListenerWithFixedSize implements SerialPortDataListener {

    private static final int FIXED_PACKET_SIZE = 64; // Define your fixed packet size
    private List<Byte> buffer = new ArrayList<>();   // Buffer to store incoming data

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
                // Check for fixed packet size
                if (buffer.size() >= FIXED_PACKET_SIZE) {
                    processCompleteMessage();
                }
            }
        }
    }

    private void processCompleteMessage() {
        byte[] completeMessage = new byte[FIXED_PACKET_SIZE];
        for (int i = 0; i < FIXED_PACKET_SIZE; i++) {
            completeMessage[i] = buffer.get(i);
        }
        // Call the method to process the complete message
        handleCompleteMessage(completeMessage);
        // Remove the processed message from the buffer
        buffer.subList(0, FIXED_PACKET_SIZE).clear();
    }

    private void handleCompleteMessage(byte[] message) {
        String receivedData = new String(message, StandardCharsets.UTF_8);
        System.out.println("Complete message received: " + receivedData);
        // Add more processing logic as needed
    }

    public static void main(String[] args) {
        SerialPort comPort = SerialPort.getCommPorts()[0]; // Get the first available serial port
        comPort.addDataListener(new MessageListenerWithFixedSize());

        if (comPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.out.println("Unable to open the port.");
        }
    }
}
