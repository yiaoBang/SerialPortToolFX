open module SerialPortToolFX.main {
    requires javafx.fxml;
    requires javafx.controls;
    requires atlantafx.base;
    requires com.google.gson;
    requires org.apache.commons.text;
    requires com.fazecast.jSerialComm;
    requires lombok;
    requires com.sun.jna;
    requires com.sun.jna.platform;


    exports com.y.serialPortToolFX;
}