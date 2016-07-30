package com.exist.app.util;

import sun.misc.Service;

import java.util.*;

/**
 * Created by NazIsEvil on 30/07/2016.
 */
public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getNewIndex(String type){
        return getNewIndex(type, 1);
    }

    public static int getNewIndex(String type, int min){
        boolean isValid = false;
        int index = 0;
        do {
            isValid = true;
            try {
                System.out.print(type + ": ");
                index = scanner.nextInt();
                scanner.nextLine();
                if (index < min){
                    isValid = false;
                    System.out.printf("[%s count should be at least %d]\n", type, min);
                }
            } catch (InputMismatchException ex){
                isValid = false;
                System.out.printf("[Invalid %s value]\n", type);
            }
        } while(!isValid);
        return index;
    }

    public static int getMenuOption(String menu, Integer... validOpts){
        System.out.println(menu);
        int opt = scanner.nextInt();
        scanner.nextLine();

        List<Integer> validOptList = Arrays.asList(validOpts);
        if(!validOptList.contains(opt)){
            throw new IllegalArgumentException();
        }
        return opt;
    }
    public static String getKeyword(){
        return scanner.nextLine();
    }

    public static int getEditIndex(String type, int size) {
        boolean isValid;
        int index = 0;
        do {
            try {
                isValid = true;
                System.out.print(type + ": ");
                index = scanner.nextInt();
                if (index > size){
                    isValid = false;
                    System.out.println("[Invalid " + type + " value]");
                }
            } catch (InputMismatchException ex) {
                System.out.println("[Invalid " + type + " value]");
                scanner.nextLine();
                isValid = false;
            }
        } while (!isValid);
        return index;
    }

    public static int getEditKV(Integer... validOpts) {
        int opt = scanner.nextInt();
        scanner.nextLine();
        List<Integer> validOptList = Arrays.asList(validOpts);
        if(!validOptList.contains(opt)){
            throw new IllegalArgumentException();
        }
        return opt;
    }
}
