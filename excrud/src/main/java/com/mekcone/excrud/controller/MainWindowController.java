package com.mekcone.excrud.controller;

import com.mekcone.excrud.ExCRUD;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.Column;
import com.mekcone.excrud.model.project.components.Dependency;
import com.mekcone.excrud.model.project.components.Table;
import com.mekcone.excrud.service.ProjectModelService;
import com.mekcone.excrud.service.SettingService;
import com.mekcone.excrud.service.impl.SpringBootProjectServiceImpl;
import com.mekcone.excrud.util.DragUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.PathUtil;
import com.mekcone.excrud.view.ProjectStructureWindowView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class MainWindowController implements Initializable {

    @FXML
    private HBox terminalWrapper;

    @FXML
    private VBox bottomAreaVBox;

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
    private Label infoLabel;

    @FXML
    private TabPane leftTabPane;

    @FXML
    private TabPane bottomTabPane;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private GridPane springBootProjectPane;

    @FXML
    private VBox projectTreeViewParent;

    @FXML
    private TextField groupIdTextField;

    @FXML
    private TextField artifactIdTextField;

    @FXML
    private TextField versionTextField;

    @Autowired
    private ProjectModelService projectModelService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private SpringBootProjectServiceImpl springBootProjectService;

    private FileChooser fileChooser = new FileChooser();

    public void refresh() {
        Project project = projectModelService.getProject();
         rootTreeItem.setValue(project.getGroupId() + "." + project.getArtifactId());
        for (Table table: project.getDatabases().get(0).getTables()) {
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
                new TreeItem(project.getGroupId())
        );

        mavenArtifactIdTreeItem.getChildren().add(
                new TreeItem(project.getArtifactId())
        );

        /*for (Dependency dependency: project.getDependencies()) {
            String dependencyId = dependency.getGroupId() + ":" + dependency.getArtifactId();
            if (dependency.getVersion() == null) {
                dependencyId += ":" + dependency.getVersion();
            }
            TreeItem treeItem = new TreeItem(dependencyId);
            treeItem.setGraphic(new ImageView("icons/table.png"));
            treeItem.setExpanded(true);
            mavenDependencyTreeItem.getChildren().add(treeItem);
            // treeItem.setGraphic(new ImageView("icons/folder.png"));
        }*/
    }

    public void initialize(URL location, ResourceBundle resources) {
        // Load settings
        settingService.load();

        //mainBorderPane.setCenter(editorTabPane);
        //projectTreeViewParent.getChildren().add(projectTreeView);

        if (PathUtil.getProjectModelPath() != null) {
            if (projectModelService.load()) {
                refresh();
            }
        } else {
            LogUtil.info("No project model path found");
        }

        DragUtil.addDrawFunc(leftTabPane);

        double mainBorderPaneWidth = mainBorderPane.getWidth();
        terminalWrapper.prefWidthProperty().bind(bottomTabPane.widthProperty());
        terminal.prefWidthProperty().bind(terminalWrapper.widthProperty());
        terminal.prefHeightProperty().bind(terminalWrapper.heightProperty());
        LogUtil.debug("Stage Width: " + ExCRUD.getStage().getWidth());

        ExCRUD.getStage().widthProperty().addListener((ebs, oldVal, val) -> {
            LogUtil.debug("Stage Width: " + ExCRUD.getStage().getWidth());
//            bottomAreaVBox.setPrefWidth(ExCRUD.getStage().getWidth() - 300);
            bottomTabPane.setPrefWidth(ExCRUD.getStage().getWidth() - 18);
            //LogUtil.debug("Bounds: " + Screen.getPrimary().getVisualBounds().getWidth());
        });

        LogUtil.getLogConsoleTextProperty().addListener((obs, oldVal, val) -> {
            terminal.replaceText(LogUtil.getLogConsoleTextProperty().getValue());
            terminal.moveTo(terminal.getText().length() - 1);
            terminal.requestFollowCaret();
        });

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON File", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        // Update main window title
        Project project = projectModelService.getProject();
        if (project != null) {
            ExCRUD.getStage().setTitle(project.getGroupId() + "." + project.getArtifactId() + " - MekCone ExCRUD");
            project.modifiedStateProperty().addListener(event -> {
                ExCRUD.getStage().setTitle(project.getGroupId() + "." + project.getArtifactId() + " - MekCone ExCRUD");
            });
            groupIdTextField.textProperty().bindBidirectional(project.groupIdProperty());
            artifactIdTextField.textProperty().bindBidirectional(project.artifactIdProperty());
            versionTextField.textProperty().bindBidirectional(project.versionProperty());
        }
    }

    public void handleExit(ActionEvent actionEvent) {
        //System.exit(0);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ExCRUD");
        alert.setHeaderText("Exit");
        alert.showAndWait();
    }

    public void handleOpenFile(ActionEvent actionEvent) {
        Stage stage = ExCRUD.getStage();

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
        ExCRUD.showView(ProjectStructureWindowView.class, Modality.NONE);
    }

    @FXML
    private TabPane workplaceTabPane;

    private List<Pair> tabPairList = new ArrayList<>();

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

    public void addSpringBootProjectPane(Pane pane) {
        Tab tab = new Tab("Spring Boot Project");
        tab.setGraphic(new ImageView("/icons/spring-boot.png"));
        tab.setContent(springBootProjectPane);
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
        workplaceTabPane.getTabs().add(tab);
        //tabPairList.add(new Pair<>(path, tab));
    }

    public void showRawProjectModel(MouseEvent mouseEvent) {
        String content = projectModelService.getProject().toString();
        createEditorTab(PathUtil.getPath("PROJECT_MODEL_PATH"), content);
    }


    public void generate(ActionEvent actionEvent) {
        springBootProjectService.generate();
        infoLabel.setText("Generate completed successfully");
        //createEditorTab("pom.xml", springBootProjectService.getPomXmlFileModel().toString());
    }

    public void buildProject(ActionEvent actionEvent) {
        springBootProjectService.build();
    }

    public void runProject(ActionEvent actionEvent) {
        springBootProjectService.run();
    }
}
