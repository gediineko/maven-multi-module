package com.exist.service;

import com.exist.model.TableModel;
import com.exist.model.constants.AppConstants;
import com.exist.service.util.FileUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TableService {
    private TableModel tableModel;

    public TableService() {
        tableModel = new TableModel();
    }

    public void initTable(int rowIndex, int columnIndex) {
        List<Map<String, String>> table = new LinkedList<Map<String, String>>();
        for (int x = 0; x < rowIndex; x++) {
            Map<String, String> row = new LinkedHashMap<>();
            for (int y = 0; y < columnIndex; y++) {
                row.put(RandomStringUtils.randomAscii(AppConstants.MAX_LEN != -1 ? AppConstants.MAX_LEN : 3)
                        , RandomStringUtils.randomAscii(AppConstants.MAX_LEN != -1 ? AppConstants.MAX_LEN : 3));
            }
            table.add(row);
        }
        tableModel.setTable(table);
        FileUtil.writeFile(tableModel);
    }

    public boolean tableFileExist() {
        return FileUtil.checkFileExist();
    }

    public void loadTable() {
        tableModel = FileUtil.readFile();
    }

    public List<Map<String, String>> getTable() {
        return tableModel.getTable();
    }

    public List<Map<String, String>> searchTable(String keyword) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        int x = 0;
        int y = 0;
        //Check if key or value contains the char searched
        for (Map<String, String> row : tableModel.getTable()) {
            y = 0;
            for (Map.Entry<String, String> entry : row.entrySet()) {
                if (entry.getKey().contains(keyword)) {
                    Map<String, String> keyFound = new HashMap<String, String>();
                    keyFound.put("x", (x + 1) + "");
                    keyFound.put("y", (y + 1) + "");
                    keyFound.put("entryType", "KEY");
                    result.add(keyFound);
                }
                if (entry.getValue().contains(keyword)) {
                    Map<String, String> valueFound = new HashMap<>();
                    valueFound.put("x", (x + 1) + "");
                    valueFound.put("y", (y + 1) + "");
                    valueFound.put("entryType", "VALUE");
                    result.add(valueFound);
                }
                y++;
            }
            x++;
        }
        return result;
    }

    public void addNewValue(int rowIndex, int columnIndex, int kv, String newValue) {
        Map<String,String> editedMap = new LinkedHashMap<String,String>();
        int x = 0;
        for (Map.Entry<String,String> entry : tableModel.getTable().get(rowIndex).entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            if (x == columnIndex){
                if(kv == 1){
                    key = newValue;
                } else {
                    value = newValue;
                }
            }
            editedMap.put(key, value);
            x++;
        }
        tableModel.getTable().set(rowIndex, editedMap);
        FileUtil.writeFile(tableModel);
    }

    public void addRow(Map<String,String> newRow) {
        tableModel.getTable().add(newRow);
        FileUtil.writeFile(tableModel);
    }

    public void sort() {
        //Sort per row
        List<Map<String,String>> sortedTable = tableModel.getTable()
                .stream()
                .map(row -> row.entrySet()
                        .stream()
                        .sorted((e1, e2) ->
                                (e1.getKey() + e1.getValue())
                                        .compareTo(e2.getKey() + e2.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1,e2) -> e1,
                                LinkedHashMap::new)))
                .collect(Collectors.toCollection(LinkedList::new));
        tableModel.setTable(sortedTable);
        FileUtil.writeFile(tableModel);
    }
}
