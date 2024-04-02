module SerialPortToolFX.main {
    requires javafx.fxml;
    requires javafx.controls;
    requires atlantafx.base;
    requires com.google.gson;
    requires org.apache.commons.text;
    requires com.fazecast.jSerialComm;

    opens com.y.serialPortToolFX to javafx.fxml;

    exports com.y.serialPortToolFX;
}