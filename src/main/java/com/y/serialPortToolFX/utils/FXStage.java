package com.y.serialPortToolFX.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.concurrent.atomic.AtomicReference;

public class FXStage {
    public static Stage create(Scene scene) {
        AtomicReference<Double> offsetX = new AtomicReference<>((double) 0);
        AtomicReference<Double> offsetY = new AtomicReference<>((double) 0);
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.getIcons().add(FX.image("ico.png"));
        stage.setScene(scene);
        //设置拖动界面
        scene.setOnMousePressed(event -> {
            offsetX.set(event.getSceneX());
            offsetY.set(event.getSceneY());
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - offsetX.get());
            stage.setY(event.getScreenY() - offsetY.get());
        });
        return stage;
    }
}
