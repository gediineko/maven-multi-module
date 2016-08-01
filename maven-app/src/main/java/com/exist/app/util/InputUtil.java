package com.exist.app.util;

import com.exist.model.constants.AppConstants;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getNewIndex(String type) {
        return getNewIndex(type, 1);
    }

    public static int getNewIndex(String type, int min) {
        boolean isValid;
        int index = 0;
        do {
            isValid = true;
            try {
                System.out.print(type + ": ");
                index = scanner.nextInt();

                if (index < min) {
                    isValid = false;
                    System.out.printf("[%s count should be at least %d]\n", type, min);
                }
            } catch (InputMismatchException ex) {
                isValid = false;
                System.out.printf("[Invalid %s value]\n", type);
            }
            scanner.nextLine();
        } while (!isValid);
        return index;
    }

    public static int getMenuOption(String menu) {
        System.out.println(menu);
        int opt = 0;
        try {
            opt = scanner.nextInt();
        } catch (InputMismatchException | IllegalArgumentException ex) {
            //Exception handled by switch default
        }
        scanner.nextLine();
        return opt;
    }

    public static String getKeyword() {
        return scanner.nextLine();
    }

    public static int getEditIndex(String type, int max) {
        boolean isValid;
        int index = 0;
        do {
            try {
                isValid = true;
                System.out.print(type + ": ");
                index = scanner.nextInt();
                if (index > max || index < 0) {
                    isValid = false;
                    System.out.println("[Table coordinates out of bounds. Input: " + index + ", Max: " + max + "]");
                }
            } catch (InputMismatchException | IndexOutOfBoundsException ex) {
                System.out.println("[Invalid " + type + " value]");
                isValid = false;
            }
            scanner.nextLine();
        } while (!isValid);
        return index - 1;
    }

    public static int getEditKV(String menu) {
        boolean repeat;
        int option = 0;
        do {
            repeat = true;
            try {
                System.out.println(menu);
                option = scanner.nextInt();
                switch (option) {
                    case 1:
                        repeat = false;
                        break;
                    case 2:
                        repeat = false;
                        break;
                    default:
                        throw new InputMismatchException();
                }
            } catch (InputMismatchException ex) {
                System.out.println("[Choose only from 1 to 2]");
                repeat = true;
            }
            scanner.nextLine();
        } while (repeat);
        return option;
    }

    public static String getNewValue(Set<String> keySet, int kv) {
        boolean valid;
        String newValue;
        do {
            valid = true;
            System.out.println("Enter new info: ");
            newValue = scanner.nextLine();
            if (newValue.length() != AppConstants.MAX_LEN && AppConstants.MAX_LEN != -1) {
                System.out.println("[New value should be " + AppConstants.MAX_LEN + " characters]");
                valid = false;
            }
            if (newValue.equals(AppConstants.ENTRY_DELIMITER) || newValue.equals(AppConstants.KEY_VALUE_DELIMITER)) {
                System.out.println("Input is used as delimiter on file. Try a different one.");
                valid = false;
            }
            if (kv == 1 && keySet.contains(newValue)){
                System.out.println("[Row already contains the same key]");
                valid = false;
            }
        } while (!valid);
        return newValue;
    }

    public static boolean addNewValidPair(Map<String,String> newRow) {
        String entry = scanner.nextLine();
        String[] keyValue = entry.split(",");
        if (newRow.containsKey(keyValue[0])){
            System.out.println("[Keys per row should be unique]");
            return false;
        } else if ((keyValue[0].length() != AppConstants.MAX_LEN && AppConstants.MAX_LEN != -1)
                || (keyValue[1].length() != AppConstants.MAX_LEN && AppConstants.MAX_LEN != -1)){
            System.out.println("[Keys and Values should be " + AppConstants.MAX_LEN + " characters only]");
            return false;
        } else if (keyValue[0].equals(AppConstants.ENTRY_DELIMITER) || keyValue[0].equals(AppConstants.KEY_VALUE_DELIMITER)
                || keyValue[1].equals(AppConstants.ENTRY_DELIMITER) || keyValue[1].equals(AppConstants.KEY_VALUE_DELIMITER)){
            System.out.println("One or both of the data input is used as a delimiter on file.\nTry a different one.");
            return false;
        } else {
            newRow.put(keyValue[0],keyValue[1]);
        }
        return true;
    }
}
