package com.mekcone.incrud;

import com.mekcone.incrud.core.controller.Logger;
import com.mekcone.incrud.core.controller.ProjectLoader;
import com.mekcone.incrud.gui.view.MainWindowView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InCRUD extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        if (args.length > 0) {
            ProjectLoader projectLoader = new ProjectLoader();
            projectLoader.load(args[0]);
            if (projectLoader.project != null) {
                Logger.info("Load project " + ProjectLoader.project.getGroupId() + "." + ProjectLoader.project.getArtifactId().getData() +" complete");
            } else {
                Logger.warn("Load project failed");
            }

//            ProjectBuilder pb2 = new ProjectBuilder();
//            Logger.info("pb2: " + pb2.getProject().getGroupId());
        }
        launch(InCRUD.class, MainWindowView.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setMaximized(true);
        stage.setTitle("MekCone InCRUD");
        super.start(stage);
    }
}

/*
public class AutoCRUD extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainWindow.fxml"));

        primaryStage.setTitle("MekCone com.mekcone.autocrud.AutoCRUD");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 1000, 600));
        //DrawUtil.addDrawFunc(primaryStage, root);
        MainWindowController mainWindowController = (MainWindowController) fxmlLoader.getController();
//        DrawUtil.addDrawFunc(primaryStage, mainWindowController.getWrapper());
        DrawUtil.addDrawFunc2(mainWindowController.getTablePaneWrapper());
        Context.wrapper = mainWindowController.getWrapper();
        Context.mainWindowController = mainWindowController;
        DrawUtil.addDrawFunc(primaryStage, mainWindowController.bottomBar);
        DrawUtil.addDrawFunc(primaryStage, mainWindowController.rightTabBar);
        primaryStage.show();
    }


    public static void main(String[] args) {
//        ApplicationContext applicationContext =
//                new AnnotationConfigApplicationContext(BeanConfig.class);
//        CustomApplicationContext.context = (AnnotationConfigApplicationContext)applicationContext;
//
//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//        for (String beanName : beanDefinitionNames) {
//            System.out.println("beanName: " + beanName);
//        }
//        Project project = (Project)applicationContext.getBean("project");

        if (args.length > 0) {
            ProjectLoader projectLoader = new ProjectLoader();
            projectLoader.load(args[0]);
            if (projectLoader.project != null) {
                Logger.info("Load project " + ProjectLoader.project.getGroupId() + "." + ProjectLoader.project.getArtifactId().getData() +" complete");
            } else {
                Logger.warn("Load project failed");
            }

//            ProjectBuilder pb2 = new ProjectBuilder();
//            Logger.info("pb2: " + pb2.getProject().getGroupId());
        }


        launch();
        */
/*if (args.length == 0) {
            Logger.error("Project path required.");
        }
        final String projectUrl = args[0];
        ProjectBuilder projectBuilder = new ProjectBuilder(projectUrl);
        projectBuilder.buildSpringBootProject();
        projectBuilder.buildVueAdminProject();*//*

    }
*/
