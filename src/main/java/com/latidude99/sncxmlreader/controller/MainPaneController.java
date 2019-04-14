/**
 * Copyright (C) 2019  Piotr Czapik.
 *
 * @author Piotr Czapik
 * @version 1.0
 * <p>
 * This file is part of SncXmlReaderFXDB.
 * SncXmlReaderFXDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * SncXmlReaderFXDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with SncXmlReader.  If not, see <http://www.gnu.org/licenses/>
 * or write to: latidude99@gmail.com
 */

package com.latidude99.sncxmlreader.controller;

import com.latidude99.sncxmlreader.utils.ConfigPaths;
import com.latidude99.sncxmlreader.db.ChartMap;
import com.latidude99.sncxmlreader.db.DBLoaderTask;
import com.latidude99.sncxmlreader.db.Database;
import com.latidude99.sncxmlreader.model.AppDTO;
import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import com.latidude99.sncxmlreader.utils.*;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;



public class MainPaneController implements Initializable {
//    private static String API_KEY = ConfigPaths.API_KEY.getPath();
//   private static final String ConfigPaths.CONFIG.getPath() = "user.data/config.properties";
//    private static String ConfigPaths.XML.getPath() = "user.data/xml/snc_catalogue.xml";
//    private static String DB_PATH = "user.data/db/snc_catalogue.db";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
    private static final String SINGLE = "=";
    private static final String SMALLER_SCALE = "-";
    private static final String LARGER_SCALE = "+";
    private static final String RANGE = "range";


    ObjectRepository<StandardNavigationChart> chartRepository;
    ObjectRepository<BaseFileMetadata> metaRepository;
    ObjectRepository<AppDTO> appDTORepository;
    Nitrite database;
    FileLoadTask fileLoadTask;
    DBLoaderTask dbLoaderTask;
    ChartMapLoadTask chartMapLoadTask;
    ChartsLoadedCheckTask chartsLoadedCheckTask;

    public void setDatabase(Nitrite database) {
        this.database = database;
    }

    @FXML
    InputPaneController inputPaneController;
    @FXML
    SearchPaneController searchPaneController;
    @FXML
    ContentPaneController contentPaneController;
    @FXML
    CataloguePaneController cataloguePaneController;


    WebPaneController webPaneController;

    SplashPaneController splashPaneController;
    Button buttonSplash;
    Label labelSplash;
    ProgressIndicator progressSplash;
    Stage stageSplash;

    public void setLabelSplash(Label labelSplash) {
        this.labelSplash = labelSplash;
    }

    private File fileSave = null;
    private File fileLoad = null;

    Charset charset;
    private FileChooser fileChooser = new FileChooser();
    UKHOCatalogueFile ukhoCatalogueFile;
    Map<String, StandardNavigationChart> standardCharts;
    BaseFileMetadata meta;
    String schemaVersion;
    LocalDateTime localDateTime;
    ChartUtils chartUtils;
    Task<Void> loadTask;
    Task<String> chartSearchTask;
    File fileTmp;


    public TextArea textResult;
    Button buttonClearSearch;
    Label labelTitle;
    TextField pathLoadFile;
    Button buttonLoadFile;
    TextField pathSaveResult;
    Button buttonSaveResult;
    Label labelLoadedDate;
    Label labelInputError;
    TextField textSearchChart;
    Button buttonSearchChart;
    Line lineSeparator;
    Button buttonChartMap;
    Hyperlink linkHelp;
    Hyperlink linkAbout;
    CheckBox checkboxInfo;
    Button buttonRefresh;
    ProgressBar progressSearch;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        initialSettings();
        configureControls();
        dbInit();
        startup();
        loadChartsIntoMemory();
        configureIO();
        configureProcessing();
    }


    public void initialSettings() {
        String apiKey = FileUtils.readApiKey(ConfigPaths.CONFIG.getPath(), ConfigPaths.API_KEY.getPath());
        ConfigPaths.API_KEY.setPath(apiKey);
        System.out.println(ConfigPaths.API_KEY.getPath() + ", " + ConfigPaths.API_KEY.getPath());
    }

    public void configureControls() {
        textResult = contentPaneController.getTextResult();
        buttonClearSearch = contentPaneController.getButtonClearSearch();
        labelTitle = inputPaneController.getLabelTitle();
        pathLoadFile = inputPaneController.getPathLoadFile();
        buttonLoadFile = inputPaneController.getButtonLoadFile();
        pathSaveResult = inputPaneController.getPathSaveResult();
        buttonSaveResult = inputPaneController.getButtonSaveResult();
        labelLoadedDate = inputPaneController.getLabelLoadedDate();
        labelInputError = inputPaneController.getLabelInputError();
        textSearchChart = searchPaneController.getTextSearchChart();
        buttonSearchChart = searchPaneController.getButtonSearchChart();
        lineSeparator = inputPaneController.getLineSeparator();
        buttonChartMap = searchPaneController.getButtonChartMap();
        checkboxInfo = searchPaneController.getCheckboxInfo();
        progressSearch = contentPaneController.getProgressSearch();
    }


    public void dbInit() {
        org.apache.log4j.BasicConfigurator.configure();
        buttonSearchChart.setDisable(true);
        String dbPath = FileUtils.readDBPath(ConfigPaths.CONFIG.getPath(), ConfigPaths.DATABASE.getPath());
        database = Nitrite.builder()
                .compressed()
                .filePath(dbPath)
                .openOrCreate("user", "password");
        Database.databaseInstance = database;
        chartRepository = database.getRepository(StandardNavigationChart.class);
        metaRepository = database.getRepository(BaseFileMetadata.class);
        appDTORepository = database.getRepository(AppDTO.class);
        System.out.println("appDTORepository.find().size(): " + appDTORepository.find().size());
        dbFilesCleanup(dbPath);
    }

    /*
     Loading data parsed from  snc_catalogue.xml to Nitrite DB (object repository)
     if there is no catalogue loaded in DB
    */
    public void startup() {
        if (appDTORepository.find().size() < 1 || chartRepository.size() < 3900) {
            String filePath = FileUtils.readXMLPath(ConfigPaths.CONFIG.getPath(), ConfigPaths.XML.getPath());
            String dbPath = FileUtils.readDBPath(ConfigPaths.CONFIG.getPath(), ConfigPaths.DATABASE.getPath());
            System.out.println(filePath + ", " + dbPath);
            loadDBFromFile(ukhoCatalogueFile, filePath, dbPath);
        } else {
            setInfoAfterDBLoaded();
        }
    }

    public void loadChartsIntoMemory() {
//		database = Database.databaseInstance;
//		database = Database.databaseInstance;
        if (database == null || !database.hasRepository(AppDTO.class)) {
            System.out.println("--------------------database == null || !database.hasRepository(AppDTO.class)");
            return;
        } else {
            chartMapLoadTask = new ChartMapLoadTask(database);
            buttonSearchChart.setDisable(true);

            chartMapLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                    new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {
                            ChartMap.all = chartMapLoadTask.getValue();
                            buttonSearchChart.setDisable(false);
                            System.out.println("+++++++++++++++++++++ChartMap loaded into memory, " + ChartMap.all.size());
                        }
                    });
            chartMapLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                    new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {

                        }
                    });
            Thread thread = new Thread(chartMapLoadTask);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void configureIO() {
        chartUtils = new ChartUtils();
        fileTmp = new File(ConfigPaths.USER.getPath() + ".");


        buttonLoadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                fileChooser.setInitialDirectory(fileTmp);
                fileLoad = fileChooser.showOpenDialog(null);
                fileChooser.setTitle("Load SNC Catalogue from XML File Into Database");
                if (fileLoad != null) {
                    loadDBFromFile(ukhoCatalogueFile, fileLoad.getAbsolutePath(), ConfigPaths.DATABASE.getPath());
                }
            }
        });

        buttonSaveResult.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                fileChooser.setTitle("Choose location for saved search");
                fileChooser.setInitialDirectory(fileTmp);
                if (fileLoad != null) {
                    String name = fileLoad.getName();
                    fileChooser.setInitialFileName(name.substring(0, name.lastIndexOf(".")) +
                            "_chart search_" + LocalDateTime.now().format(formatter) + ".txt");
                    fileSave = fileChooser.showSaveDialog(null);
                    saveSearch(fileSave);
                } else {
                    fileChooser.setInitialFileName("_chart search_" + LocalDateTime.now().format(formatter) + ".txt");
                    fileSave = fileChooser.showSaveDialog(null);
                    saveSearch(fileSave);
                }
                fileSave = null;

            }
        });

        buttonChartMap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if((textSearchChart.getText() == null || "".equals(textSearchChart.getText() == null))
                                    && ChartMap.display == null){
                    MessageBox.show("Choose a chart number!", "Info");
                    return;
                }else{
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mapPane.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root1));
                        stage.setResizable(true);
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void configureProcessing() {

        buttonClearSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (ChartMap.all != null && ChartMap.all.size() > 3800)
                    setInfoAfterDBLoaded();
            }
        });

        buttonSearchChart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchCharts();
            }
        });

        textSearchChart.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    searchCharts();
                    if (ChartMap.all != null)
                        buttonSearchChart.setDisable(false);
                }
            }
        });
    }


    /////////////////////////////////////////////////////////////////////


    private void setInfoAfterDBLoaded() {
//		String dbPath = FileUtils.readDBPath(ConfigPaths.CONFIG.getPath(), ConfigPaths.DATABASE.getPath());
        database = Database.databaseInstance;
        metaRepository = database.getRepository(BaseFileMetadata.class);
        appDTORepository = database.getRepository(AppDTO.class);
        Cursor<BaseFileMetadata> metaResults = metaRepository.find();
        Cursor<AppDTO> appDTOResults = appDTORepository.find();
        AppDTO appDTOFound = appDTOResults.firstOrDefault();
        meta = metaResults.firstOrDefault();
        schemaVersion = appDTOFound.getSchemaVersion();
        textResult.setText(Info.catalogueFull(meta, schemaVersion));
        labelLoadedDate.setText(Info.catalogueBasic(meta, schemaVersion));
        setDateLabels(meta);
    }


    private void setDateLabels(BaseFileMetadata meta) {
        String catalogueDateString = meta.getMD_DateStamp();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate catalogueDate = LocalDate.parse(catalogueDateString, formatter);
        LocalDate currentDate = LocalDate.now();
        long daysBetween = Duration.between(catalogueDate.atStartOfDay(), currentDate.atStartOfDay()).toDays();
        if (daysBetween < 9) {
            labelLoadedDate.setTextFill(Color.NAVY);
            lineSeparator.setStroke(Color.NAVY);

            labelLoadedDate.setText("                     catalogue date: " + meta.getMD_DateStamp() +
                    ", catalogue  " + daysBetween + " days old.                    ");
        } else {
            labelLoadedDate.setTextFill(Color.RED);
            lineSeparator.setStroke(Color.RED);

            labelLoadedDate.setText("catalogue date: " + meta.getMD_DateStamp() +
                    ", catalogue  " + daysBetween + " days old, check for update!");

        }
    }


    private void saveSearch(File file) {
        if (textResult.getText() == null) {
            MessageBox.show("Nothing to save!", "Warning");
            return;
        }
        try (OutputStream os = new FileOutputStream(file);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));) {
            for (String str : FormatUtils.stringToList(textResult.getText())) {
                bw.write(str);
                bw.newLine();
            }
            pathSaveResult.setText(file.getAbsolutePath());
            MessageBox.show("File saved!", "Confirmation");
        } catch (Exception ex) {
            MessageBox.show("File not saved", "Error");
            ex.printStackTrace();
        }
    }


    private void loadPaneSplash() {
        try {
            URL splashPaneUrl = getClass().getResource("/fxml/splashPane.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();

            splashPaneController = new SplashPaneController();
            //	    splashPaneController.setMainPaneController(this);

            fxmlLoader.setController(splashPaneController);
            fxmlLoader.setLocation(splashPaneUrl);

            Parent paneSplash = (Parent) fxmlLoader.load();
            paneSplash.setStyle("-fx-background-color: transparent;");
            stageSplash = new Stage();
            stageSplash.initStyle(StageStyle.UNDECORATED);

            Scene scene = new Scene(paneSplash);
//	     	scene.setFill(Color.TRANSPARENT);
            scene.setFill(null);
            stageSplash.initStyle(StageStyle.TRANSPARENT);
            stageSplash.setScene(scene);
            stageSplash.setAlwaysOnTop(true);
            stageSplash.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UKHOCatalogueFile loadFileFromPath(String filePath) {
        File file;
        UKHOCatalogueFile ukhoCatalogueFile = null;

        try {
            file = new File(filePath);
            System.out.println("398: " + filePath);
            long fileSize = file.length();
            System.out.println(fileSize);
            if (fileSize < 1) {
                textResult.setText("Catalogue and Database files not found. \n\n"
                        + "Please choose catalogue file manually \n"
                        + "or download the latest catalogue file from UKHO website.\n\n\n"
                        + "You may also want to check the config.txt file - it might be incorrectly formed. \r\n" +
                        "(see APP_FOLDER/user_data/config.txt for information about how to do it correctly)");
                return ukhoCatalogueFile;
            }
            FileInputStream fis = new FileInputStream(file);
            JAXBContext jaxbContext;
            jaxbContext = JAXBContext.newInstance(UKHOCatalogueFile.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ukhoCatalogueFile = (UKHOCatalogueFile) unmarshaller.unmarshal(fis);
            FileUtils.writeConfig(filePath, ConfigPaths.DATABASE.getPath(), ConfigPaths.API_KEY.getPath());
        } catch (FileNotFoundException io) {
            textResult.setText("Catalogue and Database files not found. \n\n"
                    + "Please choose catalogue file manually \n"
                    + "or download the latest catalogue file from UKHO website.\n\n\n"
                    + "You may also want to check the config.txt file - it might be incorrectly formed. \r\n" +
                    "(see APP_FOLDER/user_data/config.txt for information about how to do it correctly)");
            io.printStackTrace();
        } catch (JAXBException e) {
            MessageBox.show("Parsing from XML format failed (file corrupted or not in correct format)",
                              "Error");
            e.printStackTrace();
        }

        return ukhoCatalogueFile;
    }


    private Nitrite loadDBFromFile(UKHOCatalogueFile ukhoCatalogueFile, String filePath, String dbPath) {
        database.close();
        ukhoCatalogueFile = loadFileFromPath(filePath);
        if (ukhoCatalogueFile != null) {
            final String catalogueDate = ukhoCatalogueFile.getBaseFileMetadata().getMD_DateStamp();
            localDateTime = LocalDateTime.now();
            String loadDate = localDateTime.format(formatter);
            String dbPathNew = ConfigPaths.USER_DB.getPath() + "snc_catalogue_date_" +
                    catalogueDate + "_loaded_on_" +
                    loadDate + ".db";
            textResult.setText("Database file not found. Creating database and saving it as:\n" +
                    "                     " + dbPathNew +
                    "\n\nand loading up chart collection from:\n" +
                    "                     " + filePath +
                    "\n");

            dbLoaderTask = new DBLoaderTask(ukhoCatalogueFile, dbPathNew);
            loadPaneSplash();
            splashPaneController.getProgressSplash().progressProperty().unbind();
            splashPaneController.getProgressSplash().setStyle(" -fx-progress-color: cornflowerblue;");
            splashPaneController.getProgressSplash().progressProperty().bind(dbLoaderTask.progressProperty());
            splashPaneController.getLabelSplash().textProperty().bind(dbLoaderTask.messageProperty());

            splashPaneController.getButtonSplash().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
/*					textResult.setText(dbLoaderTask.messageProperty().getValue().toString()
							+ "\n\nLoading charts from an XML catalogue into the Database has been interrupted.\n"
							+ "Load the XML catalogue manually or restart the SncXmlReader to try again.\n"
							+ "If you do not have an XML catalogue file please download it form the UKHO website.\n");
*/
                    splashPaneController.getButtonSplash().setText("Close Window");
                    dbLoaderTask.cancel();
                    Stage stage = (Stage) splashPaneController.getButtonSplash().getScene().getWindow();
                    stage.close();
                }
            });

            dbLoaderTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                    new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {
                            Nitrite database = dbLoaderTask.getValue();
                            setDatabase(database);
                            Database.databaseInstance = database;
                            ConfigPaths.DATABASE.setPath(dbPathNew);
                            System.out.println(database.getContext().toString());
                            chartRepository = database.getRepository(StandardNavigationChart.class);
                            metaRepository = database.getRepository(BaseFileMetadata.class);
                            appDTORepository = database.getRepository(AppDTO.class);
                            System.out.println("NEW - appDTORepository.find().size(): " + appDTORepository.find().size());
                            splashPaneController.getProgressSplash().progressProperty().unbind();
                            splashPaneController.getLabelSplash().textProperty().unbind();
                            splashPaneController.getLabelSplash().setText("Database updated.");
                            splashPaneController.getButtonSplash().setText("Close Info.");
                            setInfoAfterDBLoaded();
                            loadChartsIntoMemory();
                            System.out.println("ConfigPaths.DATABASE.getPath(): " + ConfigPaths.DATABASE.getPath());
                            System.out.println("dbPathNew: " + dbPathNew);
                            FileUtils.writeConfig(filePath, dbPathNew, ConfigPaths.API_KEY.getPath());
                        }
                    });

            dbLoaderTask.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED,
                    new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {
                            database.close();
                            textResult.setText(dbLoaderTask.messageProperty().getValue().toString()
                                    + "\n\nLoading charts from an XML catalogue into the Database has been interrupted.\n"
                                    + "Load the XML catalogue manually or restart the SncXmlReader to try again.\n"
                                    + "If you do not have an XML catalogue file please download it form the UKHO website.\n");
                            splashPaneController.getButtonSplash().setText("Close Window");
                            //		FileUtils.writeConfig(filePath, ConfigPaths.DATABASE.getPath());
                        }
                    });

            dbLoaderTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                    new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {
                            database.close();
                            textResult.setText("\n\nLoading charts from an XML catalogue into the Database has been interrupted.\n"
                                    + "Load the XML catalogue manually or restart the SncXmlReader to try again.\n"
                                    + "If you do not have an XML catalogue file please download it form the UKHO website.\n");
                            splashPaneController.getButtonSplash().setText("Close Window");
                            FileUtils.writeConfig(filePath, ConfigPaths.DATABASE.getPath(), ConfigPaths.API_KEY.getPath());
//					dbLoaderTask.getException().printStackTrace();
                        }
                    });

            Thread thread = new Thread(dbLoaderTask);
            thread.setDaemon(true);
            thread.start();
        } else {
//			MessageBox.show("No catalogue and database files found, please check config.txt for details.\n"
//					+ "Follow instructions in config.txt file or download a new catalogue file from UKHO website.", "Info");
        }
        return database;
    }

    private void searchCharts() {
        String input = textSearchChart.getText().trim();
        if(input == null || "".equals(input)){
            MessageBox.show("Choose a chart number!", "Info");
            return;
        }

        String inputRaw = input;
        String searchType = inputRaw.substring(inputRaw.length() - 1, inputRaw.length());
        if (searchType.equals(SINGLE) || searchType.equals(SMALLER_SCALE) || searchType.equals(LARGER_SCALE))
            input = inputRaw.substring(0, inputRaw.length() - 1);

        if (ChartMap.all == null) {

            MessageBox.show("The UKHO Standard Navigation Chart catalogue has not been loaded into memory yet.\n"
                    + "          Load the catalogue first and then search for charts", "Info");
            return;
        }
        if ("all".equals(input.toLowerCase())) {
            ChartMap.display = ChartMap.all;
            textResult.setText("All charts loaded and ready to be displayed.\r\n\r\n"
                    + "It may take a minute or two especially if your Internet connection is slow\r\n"
                    + "or your computer does not have sufficient amount of RAM");
            return;
        }

        boolean fullInfo = checkboxInfo.isSelected();
        textResult.clear();

        System.out.println("searchType: " + searchType + ", " + "input: " + input);

        switch (searchType) {
            case SINGLE:
                chartSearchTask = new ChartSearchTask(database, input, fullInfo, SINGLE);
                break;
            case SMALLER_SCALE:
                chartSearchTask = new ChartSearchTask(database, input, fullInfo, SMALLER_SCALE);
                break;
            case LARGER_SCALE:
                chartSearchTask = new ChartSearchTask(database, input, fullInfo, LARGER_SCALE);
                break;
            default:
                chartSearchTask = new ChartSearchTask(database, input, fullInfo, RANGE);
        }


        // same as chartSearchTask but on the main app thread
//		textResult.setText(chartUtils.displayChartRange(ConfigPaths.DATABASE.getPath(), searchInput, fullInfo));

        textResult.textProperty().bind(chartSearchTask.messageProperty());
        progressSearch.progressProperty().bind(chartSearchTask.progressProperty());

        chartSearchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        textResult.textProperty().unbind();
                        String chartSearchResult = chartSearchTask.getValue().toString();
                        textResult.setText(chartSearchResult);
                    }
                });
        chartSearchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {

                    }
                });

        Thread thread = new Thread(chartSearchTask);
        thread.setDaemon(true);
        thread.start();
    }

    // not used now
    private void chartsLoadedCheck() {

        chartsLoadedCheckTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        textResult.textProperty().unbind();
                        setInfoAfterDBLoaded();
                        buttonSearchChart.setVisible(true);
                    }
                });
        chartsLoadedCheckTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {

                    }
                });

        Thread thread = new Thread(chartsLoadedCheckTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void dbFilesCleanup(String dbPath){

        FileCleanupTask fileCleanupTask = new FileCleanupTask(dbPath);

        fileCleanupTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {

                    }
                });
        fileCleanupTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {

                    }
                });

        Thread thread = new Thread(fileCleanupTask);
        thread.setDaemon(true);
        thread.start();
    }


}
	






















		
	