package com.mekcone.incrud;

import com.mekcone.incrud.util.PathUtil;
import com.mekcone.incrud.view.CustomSplashScreen;
import com.mekcone.incrud.view.MainWindowView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InCRUD extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        if (args.length > 0) {
            PathUtil.addPath("PROJECT_MODEL_PATH", args[0]);
        }
        CustomSplashScreen customSplashScreen = new CustomSplashScreen();
        launch(InCRUD.class, MainWindowView.class, customSplashScreen, args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setMaximized(true);
        stage.setTitle("MekCone InCRUD");
        super.start(stage);
    }
}