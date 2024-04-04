package com.y.serialPortToolFX;

import atlantafx.base.theme.PrimerLight;
import com.y.serialPortToolFX.serialComm.SerialPortMonitor;
import com.y.serialPortToolFX.utils.FXStage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class AppLauncher extends Application {
    public static final File ROOT_FILE_PATH;
    public static final FileChooser FILE_CHOOSER = new FileChooser();

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
        FILE_CHOOSER.setTitle("选择json文件");
        FILE_CHOOSER.setInitialDirectory(ROOT_FILE_PATH);
        FILE_CHOOSER.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("json文件 (*.json)", "*.json")
        );
    }
    @Override
    public void start(Stage primaryStage) {
        FXStage.create().getStage().show();
    }
    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        System.exit(0);
    }
}
