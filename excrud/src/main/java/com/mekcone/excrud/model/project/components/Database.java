package com.mekcone.excrud.model.project.components;

import lombok.Data;

import java.util.List;

@Data
public class Database {
    private String type;
    private String host;
    private String username;
    private String password;
    private List<Table> tables;
}
