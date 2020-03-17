package com.mekcone.excrud;

import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.PathUtil;
import com.mekcone.excrud.view.CustomSplashScreen;
import com.mekcone.excrud.view.MainWindowView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        if (args.length > 0) {
            PathUtil.addPath("PROJECT_MODEL_PATH", args[0]);
        }
        PathUtil.addPath("PROGRAM_PATH",  System.getProperty("user.dir") + "/");
        CustomSplashScreen customSplashScreen = new CustomSplashScreen();
        try {
            launch(Application.class, MainWindowView.class, customSplashScreen, args);
        } catch(Exception e) {
            LogUtil.warn("WARN FROM ENTRY: " + e.getMessage());
        }
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