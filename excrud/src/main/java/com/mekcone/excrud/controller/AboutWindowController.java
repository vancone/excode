package com.mekcone.excrud.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

@FXMLController
public class AboutWindowController implements Initializable {
    @FXML private  Label applicationNameLabel;
    @FXML private Label buildInfoLabel;
    @FXML private Label copyrightLabel;
    @FXML private Label openSourceDescriptionLabel;
    @FXML private Label versionLabel;

    @Value("${organization.name}")
    private String organizationName;

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.buildTime}")
    private String buildTime;

    @Value("${application.version}")
    private String applicationVersion;

    public void initialize(URL location, ResourceBundle resources) {
        Date buildDate = DateUtil.parse(buildTime);
        Date initialDate = DateUtil.parse("2020-01-01 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(buildDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] monthNames = new String[] {
          "January", "February", "March", "April", "May", "June",
          "July", "August", "September", "October", "November", "December"
        };
        long weekDuration = DateUtil.between(buildDate, initialDate, DateUnit.WEEK);
        long minuteDuration = DateUtil.between(buildDate, initialDate, DateUnit.MINUTE) - weekDuration * 7 * 24 * 60;
        String buildId = weekDuration + "." + minuteDuration;
                versionLabel.setText(organizationName + " " + applicationName + " " + applicationVersion);

        applicationNameLabel.setText(applicationName);
        buildInfoLabel.setText("Build " + buildId + ", built on " + monthNames[month - 1] + " " +
                day + ", " + year);
        copyrightLabel.setText("Copyright © " + year + " " + organizationName + ". All Rights Reserved.");
        openSourceDescriptionLabel.setText(applicationName + " and its exports are powered by open-source projects.");
    }
}
