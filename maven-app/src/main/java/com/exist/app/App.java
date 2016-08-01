package com.exist.app;

import com.exist.app.util.InputUtil;
import com.exist.service.TableService;

import java.util.LinkedHashMap;
import java.util.Map;

public class App {
    private TableService tableService;

    public App() {
        tableService = new TableService();
    }

    public static void main(String[] args) {
        new App().run();
    }

    public void run() {
        if (tableService.tableFileExist()) {
            tableService.loadTable();
            System.out.println("File loaded!");
        } else {
            System.out.println("[File does not exist]");
            initTable();
        }
        menu();
    }

    public void menu() {

        boolean cont;
        do {
            int opt = 0;
            cont = true;
            String menu = "\nMenu: [1 Search] [2 Edit] [3 Print] [4 Add Row] [5 Sort] [6 Reset] [7 Exit]";
            opt = InputUtil.getMenuOption(menu);
            switch (opt) {
                case 1:
                    searchTable();
                    break;
                case 2:
                    editTable();
                    break;
                case 3:
                    printTable();
                    break;
                case 4:
                    addRow();
                    break;
                case 5:
                    sort();
                    break;
                case 6:
                    initTable();
                    break;
                case 7:
                    cont = false;
                    break;
                default:
                    System.out.println("[Choose only from 1 to 7]");
                    break;
            }
        } while (cont);
        System.out.println("Exiting...");
    }

    public void initTable() {
        System.out.println("Set new table dimensions:");
        int row = InputUtil.getNewIndex("Row");
        int column = InputUtil.getNewIndex("Column");
        tableService.initTable(row, column);
    }

    public void searchTable() {
        System.out.print("Search for: ");
        String keyword = InputUtil.getKeyword();
        //Check if char is found on list of map
        if (tableService.searchTable(keyword).size() > 0) {
            int keyCount = 0;
            int valueCount = 0;
            System.out.print("Result(s): ");
            for (Map<String, String> entry : tableService.searchTable(keyword)) {
                System.out.print("\n" + "[" + entry.get("x") + "," + entry.get("y") + "] : " + entry.get("entryType"));
                if (entry.get("entryType").equals("KEY")) {
                    keyCount++;
                } else {
                    valueCount++;
                }
            }
            System.out.println("\nOccurrences as KEY: " + keyCount);
            System.out.println("Occurrences as VALUE: " + valueCount);
            System.out.println("Total occurrences: " + (keyCount + valueCount));
        } else {
            System.out.println("[No data found]");
        }
    }

    public void printTable() {
        for (Map<String, String> row : tableService.getTable()) {
            for (Map.Entry<String, String> entry : row.entrySet()) {
                System.out.print("[" + entry.getKey() + ":" + entry.getValue() + "]\t");
            }
            System.out.println("");
        }
    }

    public void editTable() {
        System.out.println("[Enter index of set to edit]");
        int rowIndex = InputUtil.getEditIndex("Row", tableService.getTable().size());
        int columnIndex = InputUtil.getEditIndex("Column", tableService.getTable().get(rowIndex).size());
        String kvMenu = "Which would you like to edit? [1 Key] [2 Value]";
        int kv = InputUtil.getEditKV(kvMenu);
        String newKV = InputUtil.getNewValue(tableService.getTable().get(rowIndex).keySet(), kv);
        tableService.addNewValue(rowIndex, columnIndex, kv, newKV);
    }

    public void addRow() {
        System.out.println("Adding new row...\nInput " + tableService.getTable().get(0).size()
                + " key and value pairs separated by commas.\nex: key,value");
        //Get KV pairs for new row
        Map<String, String> newRow = new LinkedHashMap<String, String>();
        for (int y = 0; y < tableService.getTable().get(0).size(); y++) {
            boolean isValid;
            do {
                try {
                    System.out.print("[Pair #" + (y + 1) + "] ");
                    isValid = InputUtil.addNewValidPair(newRow);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    isValid = false;
                    System.out.println("[Invalid pair input]");
                }
            } while (!isValid);

        }
        tableService.addRow(newRow);
    }

    public void sort() {
        tableService.sort();
        System.out.println("Table sorted!");
    }
}
