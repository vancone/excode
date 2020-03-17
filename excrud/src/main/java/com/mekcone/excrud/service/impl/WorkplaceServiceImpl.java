package com.mekcone.excrud.service.impl;

import com.mekcone.excrud.service.WorkplaceService;
import com.mekcone.excrud.service.ProjectService;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkplaceServiceImpl implements WorkplaceService {
    @Autowired
    private ProjectService projectService;

    private TabPane workplaceTabPane;

    public void setWorkplaceTabPane(TabPane workplaceTabPane) {
        this.workplaceTabPane = workplaceTabPane;
    }

    private List<Pair> tabPairList = new ArrayList<>();

    @Override
    public void createEditorTab(String path, String content) {
        for (Pair tabPair: this.tabPairList) {
            if (tabPair.getKey().equals(path)) {
                SelectionModel selectionModel = workplaceTabPane.getSelectionModel();
                selectionModel.select(tabPair.getValue());
                return;
            }
        }
        Tab tab = new Tab("raw.json");
        CodeArea codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.replaceText(content);
        VirtualizedScrollPane virtualizedScrollPane = new VirtualizedScrollPane(codeArea);
        tab.setContent(virtualizedScrollPane);
        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                for (Pair tabPair: tabPairList) {
                    if (tabPair.getValue() == event.getSource()) {
                        tabPairList.remove(tabPair);
                    }
                }
            }
        });
        workplaceTabPane.getTabs().add(tab);
        tabPairList.add(new Pair<>(path, tab));
    }

    @Override
    public void createTab(TabPane parentPane, Pane childPane) {
        Tab tab = new Tab("Spring Boot Project");
        tab.setGraphic(new ImageView("/icons/spring-boot.png"));
        tab.setContent(childPane);
        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                for (int i = 0; i < tabPairList.size(); i ++) {
                    if (tabPairList.get(i).getValue() == event.getSource()) {
                        tabPairList.remove(tabPairList.get(i));
                    }
                }
            }
        });
        parentPane.getTabs().add(tab);
        //tabPairList.add(new Pair<>(path, tab));
    }


}
