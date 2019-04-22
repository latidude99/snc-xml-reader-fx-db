/**Copyright (C) 2017  Piotr Czapik.
 * @author Piotr Czapik
 * @version 2.3
 *
 *  This file is part of SncXmlReaderFXDB.
 *  SncXmlReaderFXDB is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SncXmlReaderFXDB is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SncXmlReaderFXDB.  If not, see <http://www.gnu.org/licenses/>
 *  or write to: latidude99@gmail.com
 */

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
    private String dbName;

    public FileCleanupTask(String dbPath){
        if(dbPath != null && dbPath.contains("/")){
            int startIndex = dbPath.lastIndexOf('/') + 1;
            dbName = dbPath.substring(startIndex);
        }
        else
            dbName = dbPath;
    }

    public String getDbName(){
        return dbName;
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
    public void deleteFiles(String path, String dbName){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
        Map<LocalDateTime, File> filesToClean = new HashMap<>();
        File fileToDelete;
        LocalDateTime fileDateTime;
        LocalDateTime latestDateTime = LocalDateTime.of(1500, Month.JANUARY, 01, 0, 0, 0);
        List<String> names = new ArrayList<>();
        File[] files = new File(path).listFiles();
        System.out.println("files.length: " + files);
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
 //                   System.out.println("latestDateTime: " + latestDateTime);
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

















