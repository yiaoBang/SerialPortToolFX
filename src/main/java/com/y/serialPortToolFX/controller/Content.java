package com.y.serialPortToolFX.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Content implements AutoCloseable {
    @FXML
    private AnchorPane root;
    /**
     * 接收计数
     */
    @FXML
    private Label receiveNumber;
    /**
     * 发送计数
     */
    @FXML
    private Label sendNumber;

    @FXML
    void cleanReceiveNumber(MouseEvent event) {

    }

    @FXML
    void cleanSendNumber(MouseEvent event) {

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

    }
}
