package com.mekcone.excrud.controller;

import com.mekcone.excrud.Application;
import com.mekcone.excrud.constant.ResultConstants;
import com.mekcone.excrud.exception.ExportException;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.Database;
import com.mekcone.excrud.model.project.components.Table;
import com.mekcone.excrud.service.WorkplaceService;
import com.mekcone.excrud.service.ProjectService;
import com.mekcone.excrud.service.SettingService;
import com.mekcone.excrud.service.impl.SpringBootProjectServiceImpl;
import com.mekcone.excrud.util.DragUtil;
import com.mekcone.excrud.util.ExceptionUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.PathUtil;
import com.mekcone.excrud.view.AboutWindowView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import org.fxmisc.richtext.CodeArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class MainWindowController implements Initializable {
    @FXML private BorderPane mainBorderPane;

    @FXML private CodeArea console;

    @FXML private GridPane springBootProjectPane;

    @FXML private HBox bottomBar;
    @FXML private HBox consoleWrapper;

    @FXML private Label infoLabel;

    @FXML private TabPane bottomTabPane;
    @FXML private TabPane leftTabPane;
    @FXML private TabPane workplaceTabPane;

    @FXML private TextField groupIdTextField;
    @FXML private TextField artifactIdTextField;
    @FXML private TextField versionTextField;

    @FXML private TreeView projectTreeView;

    @FXML private VBox bottomAreaVBox;
    @FXML private VBox projectTreeViewParent;

    @Value("${organization.name}")
    private String organizationName;

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.version}")
    private String applicationVersion;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private WorkplaceService workplaceService;

    @Autowired
    private SpringBootProjectServiceImpl springBootProjectService;


    public void buildProject(ActionEvent actionEvent) {
        springBootProjectService.build();
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void generateProject(ActionEvent actionEvent) {
        try {
            springBootProjectService.generate();
            infoLabel.setText(ResultConstants.GENERATE_SUCCESS);
        } catch (ExportException ex) {
            ExceptionUtil.handle(ex);
        }

    }

    public void initialize(URL location, ResourceBundle resources) {
        settingService.load();

        if (PathUtil.getProjectModelPath() != null) {
            projectService.load();
        } else {
            LogUtil.info(ResultConstants.PROJECT_PATH_NOT_FOUND);
        }

        DragUtil.addDrawFunc(projectTreeView);

        projectTreeView.widthProperty().addListener(event -> {
            double leftTabPaneHeight = projectTreeView.getWidth() + 30;
            Platform.runLater(()-> {
                leftTabPane.setMinHeight(leftTabPaneHeight);
                leftTabPane.setMaxHeight(leftTabPaneHeight);
                leftTabPane.setPrefHeight(leftTabPaneHeight);
            });
        });

        // double mainBorderPaneWidth = mainBorderPane.getWidth();
        consoleWrapper.prefWidthProperty().bind(bottomTabPane.widthProperty());
        console.prefWidthProperty().bind(consoleWrapper.widthProperty());
        console.prefHeightProperty().bind(consoleWrapper.heightProperty());

        Application.getStage().widthProperty().addListener((ebs, oldVal, val) -> {
            bottomTabPane.setPrefWidth(Application.getStage().getWidth() - 18);
        });

        LogUtil.logConsoleTextProperty().addListener((obs, oldVal, val) -> {
            console.replaceText(LogUtil.logConsoleTextProperty().getValue());
            console.moveTo(console.getText().length() - 1);
            console.requestFollowCaret();
        });

        // Update main window title
        Project project = projectService.getProject();
        String mainWindowTitle = organizationName + " " + applicationName + " " + applicationVersion;
        if (project != null) {
            Application.getStage().setTitle(project.getGroupId() + "." + project.getArtifactId() + " - " + mainWindowTitle);
            project.modifiedStateProperty().addListener(event -> {
                Application.getStage().setTitle(project.getGroupId() + "." + project.getArtifactId() + " - " + mainWindowTitle);
            });
            groupIdTextField.textProperty().bindBidirectional(project.groupIdProperty());
            artifactIdTextField.textProperty().bindBidirectional(project.artifactIdProperty());
            versionTextField.textProperty().bindBidirectional(project.versionProperty());
        }
    }

    public void showOpenFileDialog(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON File", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = fileChooser.showOpenDialog(Application.getStage());
        if (file != null) {
            projectService.load(file.getPath());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Open file failed: " + file.getPath());
            alert.showAndWait();
        }
    }

    /*public void handleProjectStructure(ActionEvent actionEvent) {
        ExCRUD.showView(ProjectStructureWindowView.class, Modality.NONE);
    }*/

    public void showRawProjectModel(MouseEvent mouseEvent) {
        String content = projectService.getProject().toString();
        workplaceService.createEditorTab(PathUtil.getProjectModelPath(), content);
    }

    public void runProject(ActionEvent actionEvent) {
        springBootProjectService.run();
    }

    public void showAboutWindow(ActionEvent actionEvent) {
        Application.showView(AboutWindowView.class, Modality.NONE);
    }
}
