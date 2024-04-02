package com.y.serialPortToolFX;

import atlantafx.base.theme.PrimerLight;
import com.y.serialPortToolFX.utils.FX;
import com.y.serialPortToolFX.utils.FXStage;
import com.y.serialPortToolFX.serialComm.SerialPortMonitor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class AppLauncher extends Application {
    public static final File ROOT_FILE_PATH;

    static {
        ROOT_FILE_PATH = new File(System.getProperty("java.home")).getParentFile();
    }

    public static JavaFXBuilderFactory javaFXBuilderFactory;

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.allowhidpi", "false");
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        javaFXBuilderFactory = new JavaFXBuilderFactory();
        //串口监控
        SerialPortMonitor.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXStage.create(FX.scene("content")).show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        System.exit(0);
    }
}
