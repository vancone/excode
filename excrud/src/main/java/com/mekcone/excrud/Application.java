package com.mekcone.excrud;

import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.PathUtil;
import com.mekcone.excrud.view.CustomSplashScreen;
import com.mekcone.excrud.view.MainWindowView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.Collection;

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
        super.start(stage);
    }

    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        // super.beforeInitialView(stage, ctx);
        stage.setMaximized(true);
        stage.setTitle("MekCone InCRUD");
    }

    @Override
    public Collection<Image> loadDefaultIcons() {
        return Arrays.asList(new Image("/icons/logo.png"));
    }
}