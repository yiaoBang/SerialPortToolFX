package com.yiaoBang.serialPortToolFX.utils;

import com.yiaoBang.serialPortToolFX.view.SerialPortView;
import com.yiaoBang.javafxTool.core.FX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicReference;

import static com.yiaoBang.serialPortToolFX.AppLauncher.JAVAFX_BUILDER_FACTORY;

@Getter
public class SerialPortStage {
    private final Stage Stage;
    private final SerialPortView serialPortView;

    public SerialPortStage(Stage stage, SerialPortView serialPortView) {
        Stage = stage;
        this.serialPortView = serialPortView;
    }

    public static SerialPortStage create() {
        AtomicReference<Double> offsetX = new AtomicReference<>((double) 0);
        AtomicReference<Double> offsetY = new AtomicReference<>((double) 0);
        Stage stage = new Stage(StageStyle.TRANSPARENT);

        FXMLLoader loader = FX.fxmlLoader("serialPortView.fxml");
        loader.setBuilderFactory(JAVAFX_BUILDER_FACTORY);
        Scene scene = FX.loadScene(loader);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.getIcons().add(FX.image("ico.png"));
        SerialPortView serialPortView1 = loader.getController();


        //设置拖动界面
        scene.setOnMousePressed(event -> {
            offsetX.set(event.getSceneX());
            offsetY.set(event.getSceneY());
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - offsetX.get());
            stage.setY(event.getScreenY() - offsetY.get());
        });
        return new SerialPortStage(stage, serialPortView1);
    }
}
