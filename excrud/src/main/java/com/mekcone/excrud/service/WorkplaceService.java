package com.mekcone.excrud.service;

import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

public interface WorkplaceService {
    void createEditorTab(String path, String content);
    void createTab(TabPane tabPane, Pane pane);
    void setWorkplaceTabPane(TabPane workplaceTabPane);
}
