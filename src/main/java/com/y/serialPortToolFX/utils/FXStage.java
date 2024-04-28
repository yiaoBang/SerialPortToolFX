package com.y.serialPortToolFX.utils;

import com.y.serialPortToolFX.controller.Content;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import java.util.concurrent.atomic.AtomicReference;
import static com.y.serialPortToolFX.AppLauncher.JAVAFX_BUILDER_FACTORY;

@Getter
public class FXStage {
    private final Stage Stage;
    private final Content content;

    public FXStage(javafx.stage.Stage stage, Content content) {
        Stage = stage;
        this.content = content;
    }
    public static FXStage create() {
        AtomicReference<Double> offsetX = new AtomicReference<>((double) 0);
        AtomicReference<Double> offsetY = new AtomicReference<>((double) 0);
        Stage stage = new Stage(StageStyle.TRANSPARENT);

        FXMLLoader loader = FX.FXMLLoader("content");
        loader.setBuilderFactory(JAVAFX_BUILDER_FACTORY);
        //Content content = new Content();
        //loader.setController(content);
        Scene scene = FX.scene(loader);
        stage.setScene(scene);
        stage.getIcons().add(FX.image("ico.png"));
        Content content = loader.getController();


        //设置拖动界面
        scene.setOnMousePressed(event -> {
            offsetX.set(event.getSceneX());
            offsetY.set(event.getSceneY());
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - offsetX.get());
            stage.setY(event.getScreenY() - offsetY.get());
        });
        return new FXStage(stage,content);
    }
}
