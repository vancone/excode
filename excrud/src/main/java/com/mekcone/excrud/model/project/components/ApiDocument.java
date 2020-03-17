package com.mekcone.excrud.model.project.components;

import lombok.Data;

@Data
public class ApiDocument {
    private String title;
    private String description;
    private String createKeyword;
    private String retrieveKeyword;
    private String retrieveListKeyword;
    private String updateKeyword;
    private String deleteKeyword;
}
