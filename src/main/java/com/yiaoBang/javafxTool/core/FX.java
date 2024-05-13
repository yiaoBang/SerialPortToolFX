package com.yiaoBang.javafxTool.core;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public final class FX {
    private FX() {
    }

    /**
     * 资源文件前缀(可由用户定义)
     */
    public static String PREFIX = "/assets/";

    public static void run(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

    /**
     * 获取url
     *
     * @param resource 用户传入的字符串
     * @return URL
     */
    public static URL getResourceURL(String resource) {
        return FX.class.getResource(resolve(resource));
    }

    /**
     * 获取资源流
     *
     * @param resource 用户传入的字符串
     * @return InputStream
     */
    public static InputStream getResourceAsStream(String resource) {
        return FX.class.getResourceAsStream(resolve(resource));
    }
    /**
     * 拼接字符串
     *
     * @param resource 用户输入的字符串
     * @return 正确的字符串
     */
    public static String resolve(String resource) {
        return resource.startsWith("/") ? resource : PREFIX + resource;
    }
    /**
     * 获取fxml文件的URL
     *
     * @param fxmlName FXML 名称
     * @return {@code URL }
     */
    private static URL fxml(String fxmlName) {
        return getResourceURL("fxml/" + fxmlName);
    }

    /**
     * 加载 Scene
     * @param obj 用户传入的对象
     * @return Scene
     */
    public static Scene loadScene(Object obj) {
        return switch (obj) {
            case String fxml -> scene(fxml);
            case FXMLLoader loader -> scene(loader);
            case Parent parent -> new Scene(parent);
            default -> throw new RuntimeException("不支持转换的对象:" + obj.getClass().getName());
        };
    }
    public static FXMLLoader fxmlLoader(String fxmlName) {
        return new FXMLLoader(fxml(fxmlName));
    }
    public static Parent parent(String fxmlName) {
        try {
            return fxmlLoader(fxmlName).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Node node(String fxmlName) {
        return parent(fxmlName);
    }
    private static Scene scene(String fxmlName) {
        return new Scene(parent(fxmlName));
    }
    private static Scene scene(FXMLLoader loader) {
        try {
            return new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String css(String cssName) {
        return resolve("css/" + cssName);
    }
    public static Image image(String imageName) {
        return new Image(getResourceAsStream("images/" + imageName));
    }
    /**
     * 设置窗口拖拽
     *
     * @param scene 现场
     * @param stage 阶段
     * @return {@code Stage }
     */
    public static Stage stageDrag(Scene scene, Stage stage) {
        AtomicReference<Double> offsetX = new AtomicReference<>((double) 0);
        AtomicReference<Double> offsetY = new AtomicReference<>((double) 0);
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

    /**
     * 通过Scene获取Stage
     *
     * @param scene scene
     * @return {@code Stage }
     */
    public static Stage getStage(Scene scene) {
        return (Stage) scene.getWindow();
    }

    /**
     * 获取Node获取Stage
     *
     * @param node 节点
     * @return {@code Stage }
     */
    public static Stage getStage(Node node) {
        return getStage(node.getScene());
    }
}
