package com.exist.app;

import com.exist.app.util.InputUtil;
import com.exist.service.TableService;

import java.util.InputMismatchException;
import java.util.Map;

/**
 * Created by NazIsEvil on 30/07/2016.
 */
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
        } else {
            initTable();
        }
        menu();
    }

    public void menu() {
        int opt;
        boolean cont;
        do {
            cont = true;
            try {
                String menu = "Menu: [1 Search] [2 Edit] [3 Print] [4 Add Row] [5 Sort] [6 Reset] [7 Exit]";
                opt = InputUtil.getMenuOption(menu, 1, 2, 3, 4, 5, 6, 7);
            } catch (InputMismatchException | IllegalArgumentException ex) {
                System.out.println("[Invalid option]");
                break;
            }
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
//                    addRow();
                    break;
                case 5:
//                    sort();
                    break;
                case 6:
                    initTable();
                    break;
                case 7:
                    cont = false;
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
        tableService.searchTable(keyword);
    }

    public void printTable() {
        for (Map<String, String> row : tableService.getTable()) {
            for (Map.Entry<String, String> entry : row.entrySet()) {
                System.out.print("[" + entry.getKey() + ":" + entry.getValue() + "]\t");
            }
            System.out.println("");
        }
    }

    public void editTable(){
        System.out.println("Enter index of set to edit: ");
        int rowIndex = InputUtil.getEditIndex("Row", tableService.getTable().size());
        int columnIndex = InputUtil.getEditIndex("Column", tableService.getTable().get(rowIndex).size());
        int opt;
        try {
            System.out.println("Which would you like to edit? [1 Key] [2 Value]");
            opt = InputUtil.getEditKV(1,2);
        } catch (InputMismatchException ex){
            System.out.println("[Invalid option]");
        }

    }
}
