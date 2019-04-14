package com.latidude99.sncxmlreader.utils;

import javafx.concurrent.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileCleanupTask extends Task<Void> {
    String dbName;

    public FileCleanupTask(String dbPath){
        if(dbPath != null && dbPath.contains("/"))
            dbName = dbPath.substring(dbPath.lastIndexOf('/' + 1));
        else
            dbName = dbPath;
    }

    @Override
    protected Void call() {
        try{
//            deleteFiles(ConfigPaths.USER_XML_FOLDER.getPath());
            deleteFiles(ConfigPaths.USER_DB_FOLDER.getPath(), dbName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void failed() {

    }

    @Override
    protected void succeeded() {

    }
    private void deleteFiles(String path, String dbPath){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
        Map<LocalDateTime, File> filesToClean = new HashMap<>();
        File fileToDelete;
        LocalDateTime fileDateTime;
        LocalDateTime latestDateTime = LocalDateTime.of(1500, Month.JANUARY, 01, 0, 0, 0);
        List<String> names = new ArrayList<>();
        File[] files = new File(path).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                names.add(file.getName());
            }
        }
        System.out.println(names);

        for(File file : files){
            String name = file.getName();
            String fileDateTimeString = name.substring(name.lastIndexOf('_') + 1, name.lastIndexOf('.'));

            if(name.contains("snc_catalogue_date_") && name.contains("_loaded_on_")){
                try{
                    fileDateTime = LocalDateTime.parse(fileDateTimeString, formatter);
                    System.out.println(fileDateTimeString + ", " + fileDateTime);
                    filesToClean.put(fileDateTime, file);
                    if(fileDateTime.isAfter(latestDateTime))
                        latestDateTime = fileDateTime;
                    System.out.println("latestDateTime: " + latestDateTime);
                } catch (DateTimeParseException e){
                   // System.out.println(e.getMessage());
                }

            }else if (!name.equals(dbName)){
                file.delete();
            }
        }

        for(LocalDateTime dateTime : filesToClean.keySet()){
            if(!dateTime.equals(latestDateTime)){
                fileToDelete = filesToClean.get(dateTime);
                fileToDelete.delete();
            }
        }
    }


}

















