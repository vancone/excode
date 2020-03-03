package com.mekcone.incrud.controller;

import com.mekcone.incrud.InCRUD;
import com.mekcone.incrud.service.ProjectModelService;
import com.mekcone.incrud.service.ProjectService;
import com.mekcone.incrud.service.impl.SpringBootProjectServiceImpl;
import com.mekcone.incrud.util.LogUtil;
import com.mekcone.incrud.model.project.components.Column;
import com.mekcone.incrud.model.project.components.Dependency;
import com.mekcone.incrud.model.project.ProjectModel;
import com.mekcone.incrud.model.project.components.Table;
import com.mekcone.incrud.util.PathUtil;
import com.mekcone.incrud.view.ProjectStructureWindowView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sun.rmi.runtime.Log;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class MainWindowController implements Initializable {

    @FXML
    private List<Pair> tabPairList = new ArrayList<>();

    @FXML
    private HBox terminalWrapper;

    @FXML
    private TreeItem rootTreeItem;

    @FXML
    private TreeItem mavenRootTreeItem;

    @FXML
    private TreeItem mavenGroupIdTreeItem;

    @FXML
    private TreeItem mavenArtifactIdTreeItem;

    @FXML
    private TreeItem mavenDependencyTreeItem;

    @FXML
    public HBox bottomBar;

    @FXML
    private CodeArea terminal;

    @FXML
    private TabPane editorTabPane;

    @FXML
    private Label infoLabel;

    @Autowired
    private ProjectModelService projectModelService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SpringBootProjectServiceImpl springBootProjectService;

    private FileChooser fileChooser = new FileChooser();


    public void refresh() {
        ProjectModel projectModel = projectModelService.getProjectModel();
        rootTreeItem.setValue(projectModel.getGroupId() + "." + projectModel.getArtifactId());
        for (Table table: projectModel.getTables()) {
            TreeItem treeItem = new TreeItem(table.getName());
            treeItem.setGraphic(new ImageView("icons/table.png"));
            treeItem.setExpanded(true);
            for (Column column: table.getColumns()) {
                TreeItem subTreeItem = new TreeItem(column.getName());
                if (column.isPrimaryKey()) {
                    subTreeItem.setGraphic(new ImageView("icons/key.png"));
                } else {
                    subTreeItem.setGraphic(new ImageView("icons/column.png"));
                }

                treeItem.getChildren().add(subTreeItem);
            }
            rootTreeItem.getChildren().add(treeItem);
            rootTreeItem.setGraphic(new ImageView("icons/folder.png"));
        }

        mavenGroupIdTreeItem.getChildren().add(
                new TreeItem(projectModel.getGroupId())
        );

        mavenArtifactIdTreeItem.getChildren().add(
                new TreeItem(projectModel.getArtifactId())
        );

        for (Dependency dependency: projectModel.getDependencies()) {
            // LogUtil.debug(dependency.getGroupId());
            String dependencyId = dependency.getGroupId() + ":" + dependency.getArtifactId();
            if (dependency.getVersion() == null) {
                dependencyId += ":" + dependency.getVersion();
            }
            TreeItem treeItem = new TreeItem(dependencyId);
            treeItem.setGraphic(new ImageView("icons/table.png"));
            treeItem.setExpanded(true);
            mavenDependencyTreeItem.getChildren().add(treeItem);
            // treeItem.setGraphic(new ImageView("icons/folder.png"));
        }

        //codeArea.replaceText(project.toString());
    }


    public void initialize(URL location, ResourceBundle resources) {
        if (PathUtil.getPath("PROJECT_MODEL_PATH") != null) {
            projectModelService.load();
        }

        terminal.prefWidthProperty().bind(terminalWrapper.widthProperty());
        terminal.prefHeightProperty().bind(terminalWrapper.heightProperty());

        LogUtil.getLogConsoleTextProperty().addListener((obs, oldVal, val) -> {
            terminal.replaceText(LogUtil.getLogConsoleTextProperty().getValue());
        });

        refresh();

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON File", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        if (projectModelService.getProjectModel() != null) {
            InCRUD.getStage().setTitle(projectModelService.getProjectModel().getGroupId() + "." + projectModelService.getProjectModel().getArtifactId() + " - MekCone InCRUD");
            // codeArea.replaceText(projectModelService.getProjectModel().toString());
        }
    }


    public void handleExit(ActionEvent actionEvent) {
        //System.exit(0);
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("InCRUD");
        alert.setHeaderText("Exit");
        alert.showAndWait();
    }


    public void handleOpenFile(ActionEvent actionEvent) {
        Stage stage = InCRUD.getStage();

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            projectModelService.load(file.getPath());
        } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Open file fained: " + file.getPath());
                alert.showAndWait();
        }
    }


    public void handleProjectStructure(ActionEvent actionEvent) {
        InCRUD.showView(ProjectStructureWindowView.class, Modality.NONE);
    }

    public void createEditorTab(String path, String content) {
        for (Pair tabPair: this.tabPairList) {
            if (tabPair.getKey().equals(path)) {
                SelectionModel selectionModel = editorTabPane.getSelectionModel();
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
        editorTabPane.getTabs().add(tab);
        tabPairList.add(new Pair<>(path, tab));
    }


    public void closeEditorTab(Event event) {
        for (Pair tabPair: this.tabPairList) {
            if (tabPair.getValue() == event.getSource()) {
                this.tabPairList.remove(tabPair);
            }
        }
    }


    public void showRawProjectModel(MouseEvent mouseEvent) {
        String content = projectModelService.getProjectModel().toString();
        this.createEditorTab(PathUtil.getPath("PROJECT_MODEL_PATH"), content);
    }


    public void generate(ActionEvent actionEvent) {
        springBootProjectService.generate();
        infoLabel.setText("Generate completed successfully");
        //createEditorTab("pom.xml", springBootProjectService.getPomXmlFileModel().toString());
    }

    public void buildProject(ActionEvent actionEvent) {
        springBootProjectService.build();
    }
}
