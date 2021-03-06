package com.exist.service.util;

import com.exist.model.TableModel;
import com.exist.model.constants.AppConstants;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileUtil {
    private static final String FILE_NAME = "table";
    private static final String CONFIG = "config.properties";

    public static TableModel readFile(){
        List<Map<String,String>> table = new LinkedList<Map<String,String>>();
        Path file = FileSystems.getDefault().getPath(getFilePath(), FILE_NAME);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
            String line;
            while((line = reader.readLine()) != null){
                Map<String,String> row = new LinkedHashMap<String,String>();
                for (String entry : line.split(AppConstants.ENTRY_DELIMITER)){
                    String[] keyValue = entry.split(AppConstants.KEY_VALUE_DELIMITER);
                    row.put(keyValue[0],keyValue[1]);
                }
                table.add(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("[File does not exist]");
            return null;
        }

        return new TableModel(table);
    }
    public static void writeFile(TableModel tableModel){
        try {
            File file = new File(getFilePath()+File.separator+FILE_NAME);
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Map<String, String> row : tableModel.getTable()){
                StringBuilder newRow = new StringBuilder();
                for(Map.Entry<String, String> entry : row.entrySet()){
                    newRow.append(entry.getKey()).append(AppConstants.KEY_VALUE_DELIMITER).append(entry.getValue()).append(AppConstants.ENTRY_DELIMITER);
                }
                bw.write(newRow.toString());
                bw.newLine();
            }
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error writing file!");
        }
    }

    public static boolean checkFileExist(){
        File file = new File(getFilePath()+File.separator+FILE_NAME);
        return file.exists();
    }

    private static String getFilePath(){
        Properties prop = new Properties();
        try (InputStream in = ClassLoader.getSystemResourceAsStream(CONFIG)){
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty("file.path");
    }
}
