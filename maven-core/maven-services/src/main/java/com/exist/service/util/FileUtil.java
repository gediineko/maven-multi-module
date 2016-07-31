package com.exist.service.util;

import com.exist.model.TableModel;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by NazIsEvil on 30/07/2016.
 */
public class FileUtil {
    private static final String DIR_NAME = ".";
    private static final String FILE_NAME = "table";
    public static final String ENTRY_DELIMITER = " \u037e "; //greek question mark
    public static final String KEY_VALUE_DELIMITER = " \u2261 "; //triple bar
    public static TableModel readFile(){
        List<Map<String,String>> table = new LinkedList<Map<String,String>>();
        Path file = FileSystems.getDefault().getPath(DIR_NAME, FILE_NAME);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
            String line = null;
            while((line = reader.readLine()) != null){
                Map<String,String> row = new LinkedHashMap<String,String>();
                for (String entry : line.split(ENTRY_DELIMITER)){
                    String[] keyValue = entry.split(KEY_VALUE_DELIMITER);
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
            File file = new File(DIR_NAME+File.separator+FILE_NAME);
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(fw);
            for(Map<String, String> row : tableModel.getTable()){
                StringBuilder newRow = new StringBuilder();
                for(Map.Entry<String, String> entry : row.entrySet()){
                    newRow.append(entry.getKey()).append(KEY_VALUE_DELIMITER).append(entry.getValue()).append(ENTRY_DELIMITER);
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
        File file = new File(DIR_NAME+File.separator+FILE_NAME);
        return file.exists();
    }
}
