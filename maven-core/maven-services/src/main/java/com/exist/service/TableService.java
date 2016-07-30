package com.exist.service;

import com.exist.model.TableModel;
import com.exist.service.util.FileUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

/**
 * Created by NazIsEvil on 30/07/2016.
 */
public class TableService {
    private TableModel tableModel;
    private static final int MAX_LEN = 3;

    public TableService() {
        tableModel = new TableModel();
    }

    public void initTable(int rowIndex, int columnIndex) {
        List<Map<String,String>> table = new LinkedList<Map<String, String>>();
        for (int x = 0; x < rowIndex; x++){
            Map<String,String> row = new LinkedHashMap<String,String>();
            for (int y = 0; y < columnIndex; y++){
                row.put(RandomStringUtils.randomAscii(MAX_LEN != -1 ? MAX_LEN : 3)
                        , RandomStringUtils.randomAscii(MAX_LEN != -1 ? MAX_LEN : 3));
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

    public void searchTable(String keyword){
        List<Map<String,String>> result = new ArrayList<Map<String,String>>();
        int x = 0;
        int y = 0;
        //Check if key or value contains the char searched
        for(Map<String, String> row : tableModel.getTable()){
            y = 0;
            for(Map.Entry<String, String> entry : row.entrySet()){
                if (entry.getKey().contains(keyword)) {
                    Map<String,String> keyFound = new HashMap<String,String>();
                    keyFound.put("x", (x+1) + "");
                    keyFound.put("y", (y+1) + "");
                    keyFound.put("entryType", "KEY");
                    result.add(keyFound);
                }
                if (entry.getValue().contains(keyword)) {
                    Map<String,String> valueFound = new HashMap<String,String>();
                    valueFound.put("x", (x+1) + "");
                    valueFound.put("y", (y+1) + "");
                    valueFound.put("entryType", "VALUE");
                    result.add(valueFound);
                }
                y++;
            }
            x++;
        }
        //Check if char is found on list of map
        if(result.size() > 0){
            int keyCount = 0;
            int valueCount = 0;
            System.out.print("Result(s): ");
            for (Map<String,String> entry : result){
                System.out.print("\n"  + "[" + entry.get("x") + "," + entry.get("y") + "] : " + entry.get("entryType"));
                if (entry.get("entryType").equals("KEY")){
                    keyCount++;
                } else {
                    valueCount++;
                }
            }
            System.out.println("\nOccurences as KEY: " + keyCount);
            System.out.println("Occurences as VALUE: " + valueCount);
            System.out.println("Total occurences: " + (keyCount+valueCount));
        } else {
            System.out.println("[No data found]");
        }
    }
}
