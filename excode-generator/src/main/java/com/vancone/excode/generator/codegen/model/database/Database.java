package com.vancone.excode.generator.codegen.model.database;

import lombok.Data;

import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class Database {
    private String type;

    private String timezone;

    private String host;

    private String name;

    private String username;

    private String password;

    private List<Table> tables;

    public boolean hasTable() {
        return !tables.isEmpty();
    }
}