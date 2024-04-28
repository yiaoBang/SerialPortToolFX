package com.y.serialPortToolFX.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Y
 * @version 1.0
 * @date 2023/11/9 16:04
 */

public class FX {
    private FX() {

    }
    public static void run(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }
    public static Parent parent(String fxml) {
        try {
            return FXMLLoader.load(loadUrl("/assets/fxml/" + fxml + ".fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static FXMLLoader FXMLLoader(String fxml) {
        return new FXMLLoader(loadUrl("/assets/fxml/" + fxml + ".fxml"));
    }
    public static Node node(String fxml) {
        return parent(fxml);
    }

    public static Scene scene(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 设置透明背景色
        scene.setFill(Color.TRANSPARENT);
        return scene;
    }
    public static Scene scene(String fxml) {
        Scene scene = new Scene(parent(fxml));
        // 设置透明背景色
        scene.setFill(Color.TRANSPARENT);
        return scene;
    }
    public static Image image(String name) {
        return new Image(loadStream("/assets/images/" + name));
    }
    public static URL loadUrl(String path) {
        return FX.class.getResource(path);
    }
    public static InputStream loadStream(String name) {
        return FX.class.getResourceAsStream(name);
    }
}
