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

import com.latidude99.sncxmlreader.utils.FileCleanupTask;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileCleanupTaskTests {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
    int filesNumber;
    int randomInt;
    LocalDateTime dateTimeLast;
    LocalDateTime dateTimeRandom;
    String dateTimeString;
    File file;
    File fileDB;
    File fileLast;
    Set<File> files;
    FileCleanupTask fileCleanupTask;
    String dbName;

    @BeforeAll
    public static void initAll(){
        for (File file : new File("src/test/resources/temp/FileCleanupTask").listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
        if(new File("src/test/resources/temp/FileCleanupTask").listFiles().length > 0){
            System.out.println("Temporary folder src/test/resources/temp/FileCleanupTask/ is not empty");
            System.exit(1);
        }
    }

    @BeforeEach
    public void init(){
        dateTimeLast = LocalDateTime.of(2019, 4, 20, 10, 30, 45);
        dbName = "snc_catalogue_initial_in_config.db";

        fileCleanupTask = new FileCleanupTask(
                "src/test/resources/temp/FileCleanupTask/snc_catalogue_initial_in_config.db");
        fileDB = new File("src/test/resources/temp/FileCleanupTask/snc_catalogue_initial_in_config.db");
        fileLast = new File("src/test/resources/temp/FileCleanupTask/snc_catalogue_date_2019-04-09_loaded_on_"
                + dateTimeLast.format(formatter) + ".db");

        try {
            fileDB.createNewFile();
            fileLast.createNewFile();
        } catch (IOException e) {
            e.getMessage();
        }

        files = new HashSet<>();
        int filesNumber = 0;
        while(filesNumber < 12){
            randomInt = new Random().nextInt(12);
            dateTimeRandom =dateTimeLast.minusHours(randomInt);
            dateTimeString = dateTimeRandom.format(formatter);
            file = new File("src/test/resources/temp/FileCleanupTask/snc_catalogue_date_2019-04-09_loaded_on_"
                    + dateTimeString + ".db");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            files.add(file);
            filesNumber = new File("src/test/resources/temp/FileCleanupTask").listFiles().length;
        }
    }

    @AfterEach
    public void tearDown(){
        filesNumber = 0;
        dateTimeLast = null;
        dateTimeRandom = null;
        dateTimeString = null;
        fileDB = null;
        fileLast = null;
        file = null;
        files = null;
        fileCleanupTask = null;

    }

    @Test
    @DisplayName("FileCleanupTask Test 01 - deleting old database files")
    public void deleteFilesTest_01(){
        List<String> filesNamesAfterDeleting = new ArrayList<>();

        int expected_FilesNumberBeforeCleanup = 12;
        int actual_FilesNumberBeforeCleanup =
                new File("src/test/resources/temp/FileCleanupTask").listFiles().length;
        fileCleanupTask.deleteFiles("src/test/resources/temp/FileCleanupTask", dbName);

        int expected_FilesNumberAfterCleanup = 2;
        int actual_FilesNumberAfterCleanup =
                new File("src/test/resources/temp/FileCleanupTask").listFiles().length;

        String expected_dbName = "snc_catalogue_initial_in_config.db";
        String expected_lastName = "snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.30.45.db";

        File[] filesAfterDeleting = new File("src/test/resources/temp/FileCleanupTask").listFiles();
        if(filesAfterDeleting != null && filesAfterDeleting.length > 1){
            filesNamesAfterDeleting.add(filesAfterDeleting[0].getName());
            filesNamesAfterDeleting.add(filesAfterDeleting[1].getName());
        }

        assertEquals(expected_FilesNumberBeforeCleanup, actual_FilesNumberBeforeCleanup,
                "incorrect number of files before clean up");

        assertEquals(expected_FilesNumberAfterCleanup, actual_FilesNumberAfterCleanup,
                "incorrect number of files after clean up");

        assertTrue(filesNamesAfterDeleting.contains(expected_dbName),
                "snc_catalogue_initial_in_config.db - deleted");

        assertTrue(filesNamesAfterDeleting.contains(expected_lastName),
                "snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.30.45.db - deleted");
    }
}






















