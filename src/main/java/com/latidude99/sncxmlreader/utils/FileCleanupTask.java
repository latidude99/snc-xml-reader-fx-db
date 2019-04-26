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
import org.apache.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Deletes all old database files that were created automatically when creating
 * and loading up database with objects from parsed xml file.
 * Leaves only the latest and the file with the name passed as parameter.
 */
public class FileCleanupTask extends Task<Void> {
    private static final org.apache.log4j.Logger log = Logger.getLogger(FileCleanupTask.class);
    private String dbName;
    int count;

    public FileCleanupTask(String dbPath){
        if(dbPath != null && dbPath.contains("/")){
            int startIndex = dbPath.lastIndexOf('/') + 1;
            dbName = dbPath.substring(startIndex);
        }
        else
            dbName = dbPath;
    }

    @Override
    protected Void call() {
        try{
            deleteFiles(ConfigPaths.USER_DB_FOLDER.getPath(), dbName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void failed() {
        log.error("Error while deleting old database files");
    }

    @Override
    protected void succeeded() {
        log.info("Successfully deleted: " + count + " files");
    }

    public void deleteFiles(String path, String dbName){
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

        log.warn("Files to be deleted (" + names.size() + "): " + names);

        for(File file : files){
            String name = file.getName();
            String fileDateTimeString = name.substring(name.lastIndexOf('_') + 1, name.lastIndexOf('.'));

            if(name.contains("snc_catalogue_date_") && name.contains("_loaded_on_")){
                try{
                    fileDateTime = LocalDateTime.parse(fileDateTimeString, formatter);
                    filesToClean.put(fileDateTime, file);
                    if(fileDateTime.isAfter(latestDateTime))
                        latestDateTime = fileDateTime;
                } catch (DateTimeParseException e){
                    file.delete();
                   log.error(e.getMessage());
                }
            }else if (!name.equals(dbName)){
                file.delete();
                count++;
            }
        }

        log.info("File with latestDateTime will not be deleted: " + latestDateTime);

        for(LocalDateTime dateTime : filesToClean.keySet()){
            if(!dateTime.equals(latestDateTime)){
                fileToDelete = filesToClean.get(dateTime);
                fileToDelete.delete();
                count++;
            }
        }
    }


}

















