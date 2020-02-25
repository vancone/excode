package com.mekcone.incrud.core.model.project;

public class Column {
    private Value name = new Value();
    private String type;
    private boolean primaryKey;

    public Value getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setData(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
