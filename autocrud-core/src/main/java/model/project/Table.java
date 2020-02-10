package model.project;

import controller.Logger;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private Value name;
    private Value primaryKey;
    private List<Column> columns = new ArrayList<>();

    public Value getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = Value.String(name);
    }

    public Value getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = Value.String(primaryKey);
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }
}
