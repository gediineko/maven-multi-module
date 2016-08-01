package com.exist.model;

import java.util.List;
import java.util.Map;

public class TableModel {
    private List<Map<String,String>> table;

    public TableModel() {
    }

    public TableModel(List<Map<String, String>> table) {
        this.table = table;
    }

    public List<Map<String, String>> getTable() {
        return table;
    }

    public void setTable(List<Map<String, String>> table) {
        this.table = table;
    }
}
