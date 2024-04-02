package com.y.serialPortToolFX.controller;

import com.y.serialPortToolFX.serialComm.SerialComm;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Content implements AutoCloseable {
    private final SerialComm serialComm = new SerialComm();
    @FXML
    private AnchorPane root;
    @FXML
    private Label receiveNumber;
    @FXML
    private Label sendNumber;
    private final Timeline circularSending = new Timeline();

    @FXML
    void cleanReceiveNumber(MouseEvent event) {
        serialComm.clearReceive();
    }

    @FXML
    void cleanSendNumber(MouseEvent event) {
        serialComm.clearSend();
    }

    @FXML
    void close(ActionEvent event) {
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    void min(ActionEvent event) {
        ((Stage) root.getScene().getWindow()).setIconified(true);
    }

    @FXML
    void initialize() {

    }

    @Override
    public void close() throws Exception {
        serialComm.close();
    }
}
