package com.mekcone.excrud;

import com.mekcone.excrud.util.PathUtil;
import com.mekcone.excrud.view.CustomSplashScreen;
import com.mekcone.excrud.view.MainWindowView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExCRUD extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        if (args.length > 0) {
            PathUtil.addPath("PROJECT_MODEL_PATH", args[0]);
        }
        PathUtil.addPath("PROGRAM_PATH",  System.getProperty("user.dir") + "/");
        CustomSplashScreen customSplashScreen = new CustomSplashScreen();
        launch(ExCRUD.class, MainWindowView.class, customSplashScreen, args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());

        stage.setMaximized(true);
        stage.setTitle("MekCone InCRUD");
        super.start(stage);
    }
}