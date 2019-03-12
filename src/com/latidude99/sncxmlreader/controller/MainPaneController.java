/**Copyright (C) 2019  Piotr Czapik.
* @author Piotr Czapik
* @version 1.0
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
*  along with SncXmlReader.  If not, see <http://www.gnu.org/licenses/>
*  or write to: latidude99@gmail.com
*/

package com.latidude99.sncxmlreader.controller;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import com.latidude99.sncxmlreader.utils.ChartUtils;
import com.latidude99.sncxmlreader.utils.FileLoadTask;
import com.latidude99.sncxmlreader.utils.FormatUtils;
import com.latidude99.sncxmlreader.utils.Info;
import com.latidude99.sncxmlreader.utils.MessageBox;
import com.latidude99.sncxmlreader.db.DB;
import com.latidude99.sncxmlreader.db.DBLoader;
import com.latidude99.sncxmlreader.db.DBLoaderTask;
import com.latidude99.sncxmlreader.model.AppDTO;
import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;

import javafx.fxml.Initializable;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainPaneController implements Initializable{
	private static final String FILE_PATH = "user_data/snc_catalogue.xml";
	private static final String DB_PATH = "user_data/snc_catalogue.db";
	
	ObjectRepository<StandardNavigationChart> chartRepository;
    ObjectRepository<BaseFileMetadata> metaRepository;
    ObjectRepository<AppDTO> appDTORepository;
    Nitrite database;
    FileLoadTask fileLoadTask;
    DBLoader dbLoader;
    DBLoaderTask dbLoaderTask;
	
	@FXML
	InputPaneController inputPaneController;
	@FXML
	SearchPaneController searchPaneController;
	@FXML
	ContentPaneController contentPaneController;
	@FXML
	WebPaneController webPaneController;
	@FXML
	CataloguePaneController cataloguePaneController;
	
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH.mm");
    ChartUtils chartUtils;
    Task<Void> loadTask;
    File fileTmp;
    
  
    TextArea textResult;
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
    Button buttonCatInfo;
    Hyperlink linkHelp;
    Hyperlink linkAbout;
    CheckBox checkboxInfo;
    Button buttonRefresh;
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
//		MessageBox.show(splashPaneController.toString(), "");
		
//		System.out.println(splashPaneController);
		
//		configurePaneSplash();
//		displaySplash();
		configureControls();
		dbInit();
//		startup();
		configureIO();
		configureProcessing();
		startup();
//		test();
	}
/*	
	private void test() {
		loadPaneSplash();
	    System.out.println(splashPaneController.getLabelSplash().getText());
	    splashPaneController.getLabelSplash().setText("label test");
	    System.out.println(splashPaneController.getLabelSplash().getText());
	    splashPaneController.getLabelSplash().setText("label test two");	
	}
*/
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
	    buttonCatInfo = searchPaneController.getButtonCatInfo();
	    checkboxInfo = searchPaneController.getCheckboxInfo();
	    
	}
	
	public void dbInit() {
		org.apache.log4j.BasicConfigurator.configure();
		database = DB.getDB(DB_PATH);
		chartRepository = database.getRepository(StandardNavigationChart.class);
		metaRepository = database.getRepository(BaseFileMetadata.class);
		appDTORepository = database.getRepository(AppDTO.class);
		System.out.println("appDTORepository.find().size(): " + appDTORepository.find().size());
	}
	
	// Loading data parsed from  snc_catalogue.xml to Nitrite DB (object repository) 
	// if there is no catalogue loaded in DB
	public void startup() {
		if(appDTORepository.find().size() < 1 || chartRepository.size() < 100) {
			textResult.setText("Wait for the app to finish loading chart database..............");
			loadDBFromFile(ukhoCatalogueFile, FILE_PATH, DB_PATH);	
		}else {
			setInfoAfterDBLoaded();
		}
	}	
	
	public void configureIO(){
		chartUtils = new ChartUtils();
		fileTmp = new File("user_data/.");
		
						
		buttonLoadFile.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				fileChooser.setInitialDirectory(fileTmp);
				fileLoad = fileChooser.showOpenDialog(null);
				fileChooser.setTitle("Load SNC Catalogue from XML File Into Database");
				if(fileLoad != null){
					loadDBFromFile(ukhoCatalogueFile, fileLoad.getAbsolutePath(), DB_PATH);
				}
			}
		});
		
		buttonSaveResult.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				fileChooser.setTitle("Choose location for saved search");
				fileChooser.setInitialDirectory(fileTmp);
				if(fileLoad != null){
					String name = fileLoad.getName();
					fileChooser.setInitialFileName(name.substring(0, name.lastIndexOf(".")) + 
							"_chart search_" + LocalDateTime.now().format(formatter)	+ ".txt");
					fileSave = fileChooser.showSaveDialog(null);
					saveSearch(fileSave);
				} else {
					fileChooser.setInitialFileName("_chart search_" + LocalDateTime.now().format(formatter)	+ ".txt");
					fileSave = fileChooser.showSaveDialog(null);
					saveSearch(fileSave);
				}
				fileSave = null;
				
			}
		});
		
		buttonCatInfo.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				if(meta != null && schemaVersion != null) {
					setInfoAfterDBLoaded();	
				} else {
					MessageBox.show("The UKHO Standard Navigation ChartUtils catalogue has not been loaded yet.", "Info");
				}				
			}
		});	
	}
	
	
	private void configureProcessing() {
						
		buttonClearSearch.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				textResult.clear();
			}
		});
		
		buttonSearchChart.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				if(meta == null) {
					MessageBox.show("The UKHO Standard Navigation ChartUtils catalogue has not been loaded yet.\n"
							  + "          Load the catalogue first and then search for charts!", "Info");
					return;
				}
				boolean fullInfo = checkboxInfo.isSelected();
				textResult.clear();
				String searchInput = textSearchChart.getText().trim();
				textResult.setText(chartUtils.displayChartRange(searchInput, fullInfo));
			}
		});
		
		textSearchChart.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
					if(meta == null) {
						MessageBox.show("The UKHO Standard Navigation ChartUtils catalogue has not been loaded yet.\n"
								  + "          Load the catalogue first and then search for charts!", "Info");
						return;
					}
					boolean fullInfo = checkboxInfo.isSelected();
					textResult.clear();
					String searchInput = textSearchChart.getText().trim();
					textResult.setText(chartUtils.displayChartRange(searchInput, fullInfo));
				}
			}
		});
			
	}
	
	
	
	/////////////////////////////////////////////////////////////////////
	
/*	
	private void loadDBFromFile(String filePath) {	
		textResult.setText("Wait for the app to finish loading chart database..............");
		Task<UKHOCatalogueFile> fileLoadTask = new FileLoadTask(filePath);
		
		fileLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
				new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {					
				for(StandardNavigationChart chart : fileLoadTask.getValue().getProducts().getPaper().getCharts()) {
					chartRepository.insert(chart);
				}
				metaRepository.insert(fileLoadTask.getValue().getBaseFileMetadata());
				AppDTO appDTO = new AppDTO();
				appDTO.setSchemaVersion(fileLoadTask.getValue().getSchemaVersion());
				appDTORepository.insert(appDTO);
				MessageBox.show("Catalogue has been successfully loaded into Database", "Info");
				}
			});
		fileLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, 
							new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				textResult.setText("Catalogue not loaded. Please load the file manually \n"
						+ "or \n"
						+ "download the latest catalogue file from UKHO website.");
				}
			});
		
		Thread thread = new Thread(fileLoadTask);
        thread.setDaemon(true);
        thread.start();
	}
*/
	
	private void setInfoAfterDBLoaded() {
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
	
	
	private void setDateLabels(BaseFileMetadata meta){
		String catalogueDateString = meta.getMD_DateStamp();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate catalogueDate = LocalDate.parse(catalogueDateString, formatter);
		LocalDate currentDate = LocalDate.now();
		long daysBetween = Duration.between(catalogueDate.atStartOfDay(), currentDate.atStartOfDay()).toDays();
		if(daysBetween < 9) {
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
		if(textResult.getText() == null){
			MessageBox.show("Nothing to save!", "Warning");
			return;
		}
		try(OutputStream os = new FileOutputStream(file);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));) {
				for(String str: FormatUtils.stringToList(textResult.getText())){
					bw.write(str);
					bw.newLine();
				}
				pathSaveResult.setText(file.getAbsolutePath());
				MessageBox.show("File saved!", "Confirmation");
			} catch (Exception ex){
				MessageBox.show("File not saved", "Error");
				ex.printStackTrace();
			}
	}

	
	private void loadPaneSplash() {
		try {
			URL splashPaneUrl = getClass().getResource("/com/latidude99/sncxmlreader/pane/splashPane.fxml");
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
		    
		} catch(Exception e) {
		       e.printStackTrace();
		   }
	}
	
	private UKHOCatalogueFile loadFileFromPath(String FILE_PATH) {
		File file;
		UKHOCatalogueFile ukhoCatalogueFile = null;
		
		try {
	       	file = new File(FILE_PATH);
	        FileInputStream fis = new FileInputStream(file);
	         
	        long fileSize = file.length();
			System.out.println(fileSize);
				
		    JAXBContext jaxbContext;
			jaxbContext = JAXBContext.newInstance(UKHOCatalogueFile.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		    ukhoCatalogueFile = (UKHOCatalogueFile) unmarshaller.unmarshal(fis);
		} catch (FileNotFoundException io) {
			textResult.setText("Catalogue file not found (" + FILE_PATH + "). \n\n"
					+ "Please load the file manually or download the latest catalogue file from UKHO website.");
			io.printStackTrace();
		} catch (JAXBException e) {
			MessageBox.show("Parsing from XML format failed (file corrupted or not in correct format)", "Error");
			e.printStackTrace();
		}

        return ukhoCatalogueFile;
	}
	
	private void loadDBFromFile(UKHOCatalogueFile ukhoCatalogueFile, String filePath, String dbPath) {
		
		ukhoCatalogueFile = loadFileFromPath(filePath);
		dbLoaderTask = new DBLoaderTask(ukhoCatalogueFile, dbPath);
		loadPaneSplash();
		splashPaneController.getProgressSplash().progressProperty().unbind();
		splashPaneController.getProgressSplash().setStyle(" -fx-progress-color: blue;");
    	splashPaneController.getProgressSplash().progressProperty().bind(dbLoaderTask.progressProperty());
    	splashPaneController.getLabelSplash().textProperty().bind(dbLoaderTask.messageProperty());
    	
    	splashPaneController.getButtonSplash().setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) splashPaneController.getButtonSplash().getScene().getWindow();
				stage.close();
				}
		});
	
		dbLoaderTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
				new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				splashPaneController.getProgressSplash().progressProperty().unbind();	
				splashPaneController.getLabelSplash().textProperty().unbind();
				splashPaneController.getLabelSplash().setText("Databse updated.");
				splashPaneController.getButtonSplash().setText("Close window.");
				setInfoAfterDBLoaded();
				}
			});
		dbLoaderTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, 
							new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				database.getRepository(StandardNavigationChart.class).drop();
				database.getRepository(BaseFileMetadata.class).drop();
				database.getRepository(AppDTO.class).drop();
				database.close();
				textResult.setText("Catalogue not loaded.");
			}
		});
		
		Thread thread = new Thread(dbLoaderTask);
        thread.setDaemon(true);
        thread.start();
	}
	
	
	
}
	






















		
	