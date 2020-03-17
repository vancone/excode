package com.mekcone.excrud.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;

import javax.annotation.PostConstruct;

@FXMLView(value = "/fxml/MainWindow.fxml")
public class MainWindowView extends AbstractFxmlView {
    @PostConstruct
    protected void initUI() throws Exception {
        // Add WebView here
        // UIUtils.configUI((BorderPane)this.getView(), config);
    }
}
