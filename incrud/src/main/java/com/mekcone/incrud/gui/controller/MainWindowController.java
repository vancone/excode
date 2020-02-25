package com.mekcone.incrud.gui.controller;

import com.mekcone.incrud.InCRUD;
import com.mekcone.incrud.core.controller.Logger;
import com.mekcone.incrud.core.controller.ProjectLoader;
import com.mekcone.incrud.core.model.project.Column;
import com.mekcone.incrud.core.model.project.Dependency;
import com.mekcone.incrud.core.model.project.Project;
import com.mekcone.incrud.core.model.project.Table;
import com.mekcone.incrud.gui.view.ProjectStructureWindowView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class MainWindowController implements Initializable {

    @FXML
    private BorderPane wrapper;

    @FXML
    private HBox topBarWrapper;

    @FXML
    private CodeArea codeArea;

    @FXML
    private HBox terminalWrapper;

    @FXML
    private VBox dataTablesPane;

    @FXML
    public VBox rightTabBar;

    @FXML
    private TreeItem rootTreeItem;

    @FXML
    private TreeItem mavenRootTreeItem;

    @FXML
    public HBox bottomBar;

    @FXML
    private TextArea terminal;

    @FXML
    private Project project = ProjectLoader.project;

    private double xOffset;
    private double yOffset;

    private Stage stage;
    private FileChooser fileChooser = new FileChooser();

    public HBox getTopBarWrapper() {
        if (topBarWrapper == null) {
            Logger.error("topBarWrapper is null");
        }
        return topBarWrapper;
    }

    public Stage getStage() {
        return (Stage) topBarWrapper.getScene().getWindow();
    }

    public BorderPane getWrapper() {
        return this.wrapper;
    }

    public VBox getTablePaneWrapper() {
        return this.dataTablesPane;
    }

    public void refresh() {
        rootTreeItem.setValue(ProjectLoader.project.getGroupId() + "." + ProjectLoader.project.getArtifactId().getData());
        for (Table table: ProjectLoader.project.getTables()) {
            TreeItem treeItem = new TreeItem(table.getName().getData());
            treeItem.setGraphic(new ImageView("icons/table.png"));
            treeItem.setExpanded(true);
            for (Column column: table.getColumns()) {
                TreeItem subTreeItem = new TreeItem(column.getName().getData());
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

        mavenRootTreeItem.setValue(ProjectLoader.project.getArtifactId().getData());
        for (Dependency dependency: ProjectLoader.project.getDependencies()) {
            String dependencyId = dependency.getGroupId() + ":" + dependency.getArtifactId();
            if (dependency.getVersion() == null) {
                dependencyId += ":" + dependency.getVersion();
            }
            TreeItem treeItem = new TreeItem(dependencyId);
            treeItem.setGraphic(new ImageView("icons/table.png"));
            treeItem.setExpanded(true);
            mavenRootTreeItem.getChildren().add(treeItem);
            mavenRootTreeItem.setGraphic(new ImageView("icons/folder.png"));
        }
    }


    public void initialize(URL location, ResourceBundle resources) {
        terminal.prefWidthProperty().bind(terminalWrapper.widthProperty());
        terminal.prefHeightProperty().bind(terminalWrapper.heightProperty());

        Logger.debug("IntegerProperty @ MainWindow is " + project.getIntegerProperty().getValue());

        refresh();

        project.getIntegerProperty().addListener((obs, oldVal, val) -> {
            Logger.debug("IntegerProperty updated to " + val);
            refresh();
        });

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON File", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        if (ProjectLoader.project != null) {
            InCRUD.getStage().setTitle(ProjectLoader.project.getGroupId() + "." + ProjectLoader.project.getArtifactId().getData() + " - MekCone InCRUD");
            codeArea.replaceText(ProjectLoader.project.toString());
        }
    }

    public void press(MouseEvent mouseEvent) {
        if (this.stage == null) {
            this.stage = (Stage) topBarWrapper.getScene().getWindow();
        }
        xOffset = stage.getX() - mouseEvent.getScreenX();
        yOffset = stage.getY() - mouseEvent.getScreenY();
    }

    public void drag(MouseEvent mouseEvent) {
        if (this.stage == null) {
            this.stage = (Stage) topBarWrapper.getScene().getWindow();
        }
        stage.setX(mouseEvent.getScreenX() + xOffset);
        stage.setY(mouseEvent.getScreenY() + yOffset);
    }

    public void handleExit(ActionEvent actionEvent) throws Exception {
        //AutoCRUD.getStage().setTitle("asdfasdfasdf");
        System.exit(0);
        //AutoCRUD.showView(MainWindowView.class, Modality.NONE);
//        System.exit(0);
        /*Parent root = FXMLLoader.load(getClass().getResource("/fxml/MessageBox.fxml"));
        Stage stage2 = new Stage();
        stage2.setTitle("com.mekcone.autocrud.AutoCRUD MSG");
        stage2.initStyle(StageStyle.UNDECORATED);
        //stage.initOwner(stage);
        stage2.setScene(new Scene(root, 500, 200));
        stage2.show();*/
    }


    public void handleOpenFile(ActionEvent actionEvent) {
        if (this.stage == null) {
            this.stage = (Stage) topBarWrapper.getScene().getWindow();
        }

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            ProjectLoader projectLoader = new ProjectLoader();
            projectLoader.load(file.getPath());
            codeArea.replaceText(projectLoader.project.toString());
        } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Open file fained: " + file.getPath());
                alert.showAndWait();
        }
    }

    public void handleMinimizeWindow(ActionEvent actionEvent) {
        Stage stage = getStage();
        //stage.setMaximized(true);
        stage.setIconified(true);
        // stage.setIconified(false);  // Icon in TaskBar

    }

    public void handleMaximizeWindow(ActionEvent actionEvent) {
        Stage stage = getStage();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
            stage.setFullScreen(false);
        }
    }

    public void handleProjectStructure(ActionEvent actionEvent) {
        InCRUD.showView(ProjectStructureWindowView.class, Modality.NONE);
    }
}
