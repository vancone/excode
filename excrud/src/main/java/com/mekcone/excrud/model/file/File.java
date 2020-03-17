package com.mekcone.excrud.model.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class File {
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
