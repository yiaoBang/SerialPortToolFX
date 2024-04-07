package com.y.serialPortToolFX.controller;


import com.y.serialPortToolFX.serialComm.MockResponses;
import com.y.serialPortToolFX.serialComm.SerialComm;
import com.y.serialPortToolFX.serialComm.SerialPortMonitor;
import com.y.serialPortToolFX.utils.CodeFormat;
import com.y.serialPortToolFX.utils.FXStage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import static com.y.serialPortToolFX.AppLauncher.FILE_CHOOSER;

public class Content {
    private final SerialComm serialComm = new SerialComm();
    private final Timeline circularSending = new Timeline();
    private volatile long waitTime = 1000;
    private volatile byte[] bytes;
    @FXML
    private AnchorPane root;
    @FXML
    private ComboBox<String> serialPortNamePicker;
    @FXML
    private ComboBox<Integer> baudRatePicker;
    @FXML
    private ComboBox<Integer> dataBitsPicker;
    @FXML
    private ComboBox<String> stopBitsPicker;
    @FXML
    private ComboBox<String> parityPicker;
    @FXML
    private ComboBox<String> flowControlPicker;
    //16进制接收显示开关
    @FXML
    private CheckBox hexReceive;
    //16进制发送开关
    @FXML
    private CheckBox hexSend;
    //保存接收的字节
    @FXML
    private CheckBox receiveSave;
    //保存发送的字节
    @FXML
    private CheckBox sendSave;
    //接收框
    @FXML
    private TextArea receive;
    //接收的字节数
    @FXML
    private Label receiveNumber;
    //接收显示开关
    @FXML
    private CheckBox receiveShow;
    //发送框
    @FXML
    private TextArea send;
    //发送的字节数
    @FXML
    private Label sendNumber;
    //串口指示灯
    @FXML
    private Circle serialPortLight;
    //发送的时间或者回复的延时
    @FXML
    private TextField time;
    //定时发送开关
    @FXML
    private CheckBox timedDispatch;
    //串口开关
    @FXML
    private Button serialPortSwitch;
    @FXML
    private Circle analogLight;

    @FXML
    void min(ActionEvent event) {
        ((Stage) root.getScene().getWindow()).setIconified(true);
    }

    @FXML
    void close(ActionEvent event) {
        ((Stage) root.getScene().getWindow()).close();
        serialComm.close();
    }

    @FXML
    void analogReply(ActionEvent event) {
        File file = FILE_CHOOSER.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            MockResponses mockResponses = MockResponses.parseJson(file);
            if (mockResponses == null) {
                analogLight.setFill(Color.RED);
                serialComm.setMockResponses(null);
            } else {
                analogLight.setFill(Color.LIME);
                if (mockResponses.getPackSize() > 0) {
                    serialComm.updateListener(mockResponses.getPackSize());
                } else {
                    serialComm.updateListener(mockResponses.getDelimiter());
                }
                serialComm.setMockResponses(mockResponses);
            }
        } else {
            serialComm.setMockResponses(null);
            analogLight.setFill(Color.RED);
        }
    }

    @FXML
    void cleanReceive(ActionEvent event) {
        serialComm.getBuffer().close();
        receive.clear();
    }

    @FXML
    void cleanSend(ActionEvent event) {
        send.clear();
    }

    @FXML
    void createStage(ActionEvent event) {
        Stage window = (Stage) root.getScene().getWindow();
        FXStage fxStage = FXStage.create();
        fxStage.getContent().initSerialPort(serialComm.getBaudRate(), serialComm.getDataBits(), serialComm.getStopSting(), serialComm.getParitySting(), serialComm.getFlowControlSting());
        fxStage.getStage().setX(window.getX() + 100);
        fxStage.getStage().setY(window.getY() + 100);
        fxStage.getStage().show();
    }

    @FXML
    void sendData(ActionEvent event) {
        serialComm.write(bytes);
    }

    @FXML
    void serialPortSwitch(ActionEvent event) {
        if (serialComm.getSerialPortState().get()) {
            serialComm.close();
        } else {
            serialComm.openSerialPort();
        }
    }

    @FXML
    void cleanReceiveNumber(MouseEvent event) {
        serialComm.clearReceive();
    }

    @FXML
    void cleanSendNumber(MouseEvent event) {
        serialComm.clearSend();
    }

    @FXML
    void initialize() {
        //无限循环发送
        circularSending.setCycleCount(Timeline.INDEFINITE);
        //接收保存的开关
        receiveSave.selectedProperty().addListener((observable, oldValue, newValue) -> serialComm.setReceiveSave(newValue));
        //发送保存的开关
        sendSave.selectedProperty().addListener((observable, oldValue, newValue) -> serialComm.setSendSave(newValue));
        //计数绑定
        sendNumber.textProperty().bind(serialComm.getSEND_LONG_PROPERTY().asString());
        receiveNumber.textProperty().bind(serialComm.getRECEIVE_LONG_PROPERTY().asString());


        //串口开关绑定
        serialPortSwitch.textProperty().bind(serialComm.getSerialPortState().map(state -> state ? "关闭串口" : "打开串口"));
        //串口指示灯绑定
        serialPortLight.fillProperty().bind(serialComm.getSerialPortState().map(state -> state ? Color.LIME : Color.RED));

        //初始化列表
        baudRatePicker.setValue(serialComm.getBaudRate());
        dataBitsPicker.setValue(serialComm.getDataBits());
        stopBitsPicker.setValue(serialComm.getStopSting());
        parityPicker.setValue(serialComm.getParitySting());
        flowControlPicker.setValue(serialComm.getFlowControlSting());


        //串口参数更新
        serialPortNamePicker.valueProperty().addListener((observable, oldValue, newValue) -> serialComm.setSerialPortName(newValue));
        baudRatePicker.valueProperty().addListener((observable, oldValue, newValue) -> serialComm.setBaudRate(newValue));
        dataBitsPicker.valueProperty().addListener((observable, oldValue, newValue) -> serialComm.setDataBits(newValue));
        stopBitsPicker.valueProperty().addListener((observable, oldValue, newValue) -> serialComm.setStopBits(newValue));
        parityPicker.valueProperty().addListener((observable, oldValue, newValue) -> serialComm.setParity(newValue));
        flowControlPicker.valueProperty().addListener((observable, oldValue, newValue) -> serialComm.setFlowControl(newValue));


        //绑定列表,动态更新
        serialPortNamePicker.itemsProperty().bind(Bindings.createObjectBinding(() -> FXCollections.observableArrayList(SerialPortMonitor.serialPorts.get().split("\n")), SerialPortMonitor.serialPorts));
        //添加参数列表
        baudRatePicker.getItems().addAll(SerialComm.BAUD_RATE);
        dataBitsPicker.getItems().addAll(SerialComm.DATA_BITS);
        stopBitsPicker.getItems().addAll(SerialComm.STOP_BITS);
        parityPicker.getItems().addAll(SerialComm.PARITY);
        flowControlPicker.getItems().addAll(SerialComm.FLOW_CONTROL);

        //选择串口号
        serialPortNamePicker.setValue(serialPortNamePicker.getItems().isEmpty() ? "" : serialPortNamePicker.getItems().getFirst());

        //延迟时间
        time.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                waitTime = Integer.parseInt(newValue);
                if (waitTime < 1) {
                    waitTime = 1;
                }
                serialComm.setWaitTime(waitTime);
            } catch (NumberFormatException e) {
                time.setText(oldValue);
            }
        });
        //定时发送开关
        timedDispatch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                //清理帧
                circularSending.getKeyFrames().clear();
                circularSending.getKeyFrames().add(new KeyFrame(Duration.millis(waitTime), s -> {
                    if (serialComm.write(bytes) < 1) {
                        circularSending.stop();
                        timedDispatch.setSelected(false);
                    }
                }));
                circularSending.play();
            } else {
                circularSending.stop();
            }
        });


        //更新要发送的数据
        send.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                bytes = hexSend.isSelected() ? CodeFormat.hex(newValue) : CodeFormat.utf8(newValue);
            } else {
                bytes = null;
            }
        });
        //16进制发送
        hexSend.selectedProperty().addListener((observable, oldValue, newValue) -> {
            String text = send.getText();
            if (!text.isEmpty()) {
                bytes = newValue ? CodeFormat.hex(text) : CodeFormat.utf8(text);
            } else {
                bytes = null;
            }
        });

        //是否显示
        receiveShow.selectedProperty().addListener((observable, oldValue, newValue) -> serialComm.setReceiveShow(newValue));

        //更新显示
        serialComm.getRECEIVE_LONG_PROPERTY().addListener((observable, oldValue, newValue) -> {
            if (receiveShow.isSelected()) {
                receive.setText(hexReceive.isSelected() ? CodeFormat.hex(serialComm.getData()) : CodeFormat.utf8(serialComm.getData()));
                receive.setScrollTop(Double.MAX_VALUE);
            }
        });
        //16进制接收显示
        hexReceive.selectedProperty().addListener((observable, oldValue, newValue) -> {
            byte[] data = serialComm.getData();
            receive.setText(newValue ? CodeFormat.hex(data) : CodeFormat.utf8(data));
            receive.setScrollTop(Double.MAX_VALUE);
        });

        //检查串口是否还在
        SerialPortMonitor.serialPorts.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.contains(serialComm.getSerialPortName())) {
                serialComm.close();
                serialPortNamePicker.setValue("");
            }
        });
    }

    public void initSerialPort(int baudRate, int dateBits, String stop, String parity, String flow) {
        baudRatePicker.setValue(baudRate);
        dataBitsPicker.setValue(dateBits);
        stopBitsPicker.setValue(stop);
        parityPicker.setValue(parity);
        flowControlPicker.setValue(flow);
    }
}
