package com.y.serialPortToolFX;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;

public class AppLauncher extends Application {
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
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        System.exit(0);
    }
}
