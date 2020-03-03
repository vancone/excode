package com.mekcone.incrud.model.file;

import com.mekcone.incrud.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FileModel {
    @Value("${incrud.source.description}")
    public void setDescription(String description) {
        Date date = new Date();
        this.description = description + date;
    }

    private static String description;

    public static String getDescription() {
        return description;
    }


}
