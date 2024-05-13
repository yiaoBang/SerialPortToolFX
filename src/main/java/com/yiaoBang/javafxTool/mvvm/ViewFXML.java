package com.yiaoBang.javafxTool.mvvm;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ViewFXML<ViewModelType extends ViewModel> implements Initializable {
    private ViewModelType viewModel;

    abstract ViewModelType createViewModel();

    public ViewModelType getViewModel() {
        return viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = this.createViewModel();
        addListener();
        bind();
        addTask();
    }

    /**
     * 添加侦听器
     */
    abstract void addListener();

    /**
     * 绑定
     */
    abstract void bind();

    /**
     * 添加任务
     */
    abstract void addTask();
}
