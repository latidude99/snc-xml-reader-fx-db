import com.latidude99.sncxmlreader.utils.FileCleanupTask;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileCleanupTaskTests_tempDir {
    @TempDir
    Path tempDir;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
    int filesNumber;
    int randomInt;
    LocalDateTime dateTime;
    String dateTimeString;
    File file;
    Set<File> files;
    FileCleanupTask fileCleanupTask;
    String dbName;

    @BeforeAll
    public static void initAll(){

    }

    @BeforeEach
    public void init(){
        dbName = "snc_catalogue_initial_in_config.db";

        Path dbPath = tempDir.resolve("snc_catalogue_initial_in_config.db");
        file = new File(dbPath.toString());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.getMessage();
        }
        fileCleanupTask = new FileCleanupTask(dbPath.toString());

        files = new HashSet<>();
        Path catalogue1 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.30.45.db");
        Path catalogue2 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 08.30.45.db");
        Path catalogue3 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 07.30.45.db");
        Path catalogue4 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 09.30.45.db");
        Path catalogue5 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.27.45.db");
        Path catalogue6 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.14.45.db");
        Path catalogue7 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.07.45.db");
        Path catalogue8 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.30.44.db");
        Path catalogue9 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.30.34.db");
        Path catalogue10 = tempDir.resolve("snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.30.15.db");

        File file1 = new File(catalogue1.toString());
        File file2 = new File(catalogue2.toString());
        File file3 = new File(catalogue3.toString());
        File file4 = new File(catalogue4.toString());
        File file5 = new File(catalogue5.toString());
        File file6 = new File(catalogue6.toString());
        File file7 = new File(catalogue7.toString());
        File file8 = new File(catalogue8.toString());
        File file9 = new File(catalogue9.toString());
        File file10 = new File(catalogue10.toString());

        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);
        files.add(file6);
        files.add(file7);
        files.add(file8);
        files.add(file9);
        files.add(file10);

        for(File file :files){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @AfterEach
    public void tearDown(){
        filesNumber = 0;
        dateTime = null;
        dateTimeString = null;
        file = null;
        files = null;
        fileCleanupTask = null;

    }

    @Test
    @DisplayName("FileCleanupTask Test 03 - deleting old database files using tempDir")
    public void deleteFilesTest_01(){
        List<String> filesNamesAfterDeleting = new ArrayList<>();

        int expected_FilesNumberBeforeCleanup = files.size() + 1;
        int actual_FilesNumberBeforeCleanup = new File(tempDir.toAbsolutePath().toString()).listFiles().length;

        fileCleanupTask.deleteFiles(tempDir.toAbsolutePath().toString(), dbName);

        int expected_FilesNumberAfterCleanup = 2;
        int actual_FilesNumberAfterCleanup = new File(tempDir.toAbsolutePath().toString()).listFiles().length;

        String expected_dbName = "snc_catalogue_initial_in_config.db";
        String expected_lastName = "snc_catalogue_date_2019-04-09_loaded_on_2019-04-20 10.30.45.db";

        File[] filesAfterDeleting = new File(tempDir.toAbsolutePath().toString()).listFiles();
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






















