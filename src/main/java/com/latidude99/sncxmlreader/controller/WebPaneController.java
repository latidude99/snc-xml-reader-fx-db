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

package com.latidude99.sncxmlreader.controller;

import com.latidude99.sncxmlreader.db.ChartMap;
import com.latidude99.sncxmlreader.db.DBLoaderTask;
import com.latidude99.sncxmlreader.db.Database;
import com.latidude99.sncxmlreader.model.AppDTO;
import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import com.latidude99.sncxmlreader.utils.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class WebPaneController implements Initializable{
	private static final org.apache.log4j.Logger log = Logger.getLogger(WebPaneController.class);
	LocalDateTime localDateTime;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");

    WebEngine webEngine;
    final BooleanProperty loginAttempted = new SimpleBooleanProperty(false);
    LoginCheck downloader = new LoginCheck();
    Preferences userPreferences = Preferences.userRoot().node(ConfigPaths.PREFERENCES.getPath());
    Task<Void> downloadTask;
    Task<UKHOCatalogueFile> fileLoadTask;
    DBLoaderTask dbLoaderTask;
    UKHOCatalogueFile ukhoCatalogueFile;
    File file;
    Nitrite database;
    int existingChartsNum;
    String existingCatalogueDate;

    public void setDatabase(Nitrite database) {
    	this.database = database;
    }


	@FXML
    private WebView webView;
	@FXML
	private Button buttonDownload;
	@FXML
	private Button buttonSignIn;
	@FXML
	private TextField textUsername;
	@FXML
	private TextField textPassword;
	@FXML
	private CheckBox checkboxDontRememeber;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private Label labelDownloaded;
	@FXML
	private Button buttonCancel;
	@FXML
	Button buttonUpdate;
	@FXML
	Button buttonDelete;
	@FXML
	Button buttonClose;
	@FXML
	Button buttonStop;
	@FXML
	Rectangle rectangleCover;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		configurePaths();
		configureCredentials();
		connectionConfig();
		configureWebView();
		readExistingDB();
		configureButtons();
	}


	/*
	 * Checks if there are any file paths in ./user.data/config.properties file and
	 * if so assignes them to the right property in ConfigPaths enum type
	 */
	private void configurePaths() {
		ConfigPaths.XML.setPath(FileUtils.readXMLPath(ConfigPaths.CONFIG.getPath(), ConfigPaths.XML.getPath()));
		ConfigPaths.DATABASE.setPath(FileUtils.readDBPath(ConfigPaths.CONFIG.getPath(), ConfigPaths.DATABASE.getPath()));
		ConfigPaths.API_KEY.setPath(FileUtils.readApiKey(ConfigPaths.CONFIG.getPath(), ConfigPaths.API_KEY.getPath()));

	}


	/*
	 * Username ans Password are kept in Registry on Windows based systems and are
	 * unique to each logged in user. java.util.prefs.Preferences offers ~ the same
	 *  level of protection as applies to all user files - only the system administrator
	 * can view it. For the purpose of this application it is considered sufficient.
	 */
	private void configureCredentials() {
		checkboxDontRememeber.setSelected(false);
		if(!userPreferences.get("username", "").equals("") && !userPreferences.get("password", "").equals("")) {
        	System.out.println(userPreferences.toString() +
        			", " + userPreferences.get("username", "") +
        			", " + userPreferences.get("password", ""));
        	textUsername.setText(userPreferences.get("username", ""));
            textPassword.setText(userPreferences.get("password", ""));
        }
	}


	private void configureWebView() {
		rectangleCover.setVisible(true);
		progressIndicator.setVisible(false);
		labelDownloaded.setVisible(false);
		buttonUpdate.setVisible(false);
		buttonDelete.setVisible(false);
		buttonClose.setVisible(false);
		buttonStop.setVisible(false);
        webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);


		/*
		 * Listens for a change of loaded URL, extracts form input fields and fills
		 * them in with username and password. Input fields ids "un" and "pw"
		 * have been taken from UKHO login website.
		 */
		webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> ov, Document oldDoc, Document doc) {
                if (doc != null && !loginAttempted.get()) {
                    if (doc.getElementsByTagName("form").getLength() > 0) {
                        HTMLFormElement form = (HTMLFormElement) doc.getElementsByTagName("form").item(0);

                            HTMLInputElement username = null;
                            HTMLInputElement password = null;
                            NodeList nodes = form.getElementsByTagName("input");
                            for (int i = 0; i < nodes.getLength(); i++) {
                                HTMLInputElement input = (HTMLInputElement) nodes.item(i);
                                switch (input.getId()) {
                                    case "un":
                                        username = input;
                                        break;
                                    case "pw":
                                        password = input;
                                        break;
                                }
                            }

                            if (username != null && password != null) {
                                loginAttempted.set(true);
                                username.setValue(textUsername.getText());
                                password.setValue(textPassword.getText());
                                form.submit();
                            }
                    }
                }
            }
        });
		webEngine.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {
            @Override
            public void changed(ObservableValue<? extends Throwable> ov, Throwable oldException, Throwable exception) {
                System.out.println("Load Exception: " + exception);
            }
        });
	}

	/*
	 * Load existing chart database characteristic info in order to compare it later
	 * with the one available on the UKHO download page
	 */
	private void readExistingDB() {
		String dbPath = FileUtils.readDBPath(ConfigPaths.CONFIG.getPath(), ConfigPaths.DATABASE.getPath());
		database = Database.getDatabaseInstance(dbPath);
		ObjectRepository<StandardNavigationChart> chartRepository = database.getRepository(StandardNavigationChart.class);
		ObjectRepository<BaseFileMetadata>metaRepository = database.getRepository(BaseFileMetadata.class);
		if(chartRepository != null && metaRepository != null) {
			existingChartsNum = chartRepository.find().size();
			existingCatalogueDate = metaRepository.find().firstOrDefault().getMD_DateStamp();
		}
	}

	/*
	 * Checks if the log in was successful and if so enabling 'Download' button.
	 * Also erases username and password from Registry (Preference class)
	 * if the box is ticked off.
	 */
	private void configureButtons() {

		buttonDownload.setDisable(true);
		buttonCancel.setDisable(true);

		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
		     @Override
		     public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
		    	 String newURL = webEngine.getLocation();
		         if (newState == Worker.State.SUCCEEDED) {
		        	 afterLogin();
		         }
		     }
		});

		buttonSignIn.setOnAction(new EventHandler<ActionEvent>() {
			 @Override
	         public void handle(ActionEvent t) {
				 progressBar.progressProperty().bind(webView.getEngine().getLoadWorker().progressProperty());
				 progressBar.visibleProperty().bind(webView.getEngine().getLoadWorker().runningProperty());
				 if (notEmpty(textPassword.getText()) && notEmpty(textPassword.getText())) {
	            	 loginAttempted.set(false);
	                 webEngine.load(ConfigPaths.UKHO_HOME.getPath());
	             }else {
	             	MessageBox.show("Please enter your Username  and Password.", "Info");
	             }
	         }

	    });

		/*
		 * Attempts to download the latest available chart catalogue showing progress,
		 * Once that is completed checks id this catalogue is newer than the existing
		 * one and offers to update the database, also showing progress.
		 */
	    buttonDownload.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				rectangleCover.setVisible(true);
				buttonSignIn.setDisable(true);
				buttonDownload.setDisable(true);
				buttonCancel.setDisable(false);
				webView.setOpacity(0.3);

				downloadTask = new DownloadTask(ConfigPaths.UKHO_HOME.getPath(), ConfigPaths.UKHO_DOWNLOAD.getPath());
				progressIndicator.setVisible(true);
				labelDownloaded.setVisible(true);
				progressIndicator.progressProperty().bind(downloadTask.progressProperty());
				labelDownloaded.textProperty().bind(downloadTask.messageProperty());

				downloadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
	                       						new EventHandler<WorkerStateEvent>() {
	                           @Override
	                           public void handle(WorkerStateEvent t) {
								String newfilePath = FileUtils.readXMLPath(ConfigPaths.CONFIG.getPath(), ConfigPaths.XML.getPath());
								   ConfigPaths.XML.setPath(newfilePath);
								ukhoCatalogueFile = loadFileFromPath(newfilePath);
	                        	   String downloadedCatalogueDate =
	                        			   ukhoCatalogueFile.getBaseFileMetadata().getMD_DateStamp();
	                        	   int downloadedChartsNum =
	                        			   ukhoCatalogueFile.getProducts().getPaper().getCharts().size();
	                           	   labelDownloaded.textProperty().unbind();
	                           	   progressIndicator.progressProperty().unbind();
	                        	   labelDownloaded.setText("Download complete. Catalogue Date: " + downloadedCatalogueDate);
	                        	   buttonUpdate.setVisible(true);
	                        	   if(downloadedCatalogueDate.equals(existingCatalogueDate)
	                        			   && existingChartsNum == downloadedChartsNum) {
	                        		   MessageBox.show
	                        		   ("Dowloaded catalogue version is the same as the one already in Database",  "Info");
	                        	   buttonUpdate.setText("Update Catalog Anyway");
	                        	   buttonStop.setVisible(true);
	                        	   buttonStop.setOnAction(new EventHandler<ActionEvent>(){
		                       			@Override
		                    			public void handle(ActionEvent e){
		                    				progressIndicator.progressProperty().unbind();
		                    				labelDownloaded.textProperty().unbind();
		                    				buttonStop.setVisible(false);
		                    				buttonUpdate.setVisible(true);
		                    			}
		                    		});
	                        	   }
	                           }
	                       });
				downloadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
   												new EventHandler<WorkerStateEvent>() {
						       @Override
						       public void handle(WorkerStateEvent t) {
						    	   buttonSignIn.setDisable(false);
						    	   buttonDownload.setDisable(false);
						    	   buttonCancel.setDisable(true);
						    	   buttonUpdate.setVisible(false);
						    	   webView.setOpacity(1);
						    	   downloadTask.cancel(true);
						    	   progressIndicator.progressProperty().unbind();
						    	   labelDownloaded.textProperty().unbind();
						    	   progressIndicator.setVisible(false);
						    	   labelDownloaded.setVisible(false);
						    	   MessageBox.show("Could not save the file. Delete the old file and try again. ", "Error");
						       }
						   });
				Thread thread = new Thread(downloadTask);
	            thread.setDaemon(true);
	            thread.start();
			}
		});

		/*
		 * This cancels the download process (not the database updating process)
		 */
	    buttonCancel.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				buttonSignIn.setDisable(false);
				buttonDownload.setDisable(false);
				buttonCancel.setDisable(true);
				buttonUpdate.setVisible(false);
				webView.setOpacity(1);
				downloadTask.cancel(true);
				progressIndicator.progressProperty().unbind();
				labelDownloaded.textProperty().unbind();
				progressIndicator.setVisible(false);
				labelDownloaded.setVisible(false);
			}
		});

		/*
		 * Updates the database with newly downloaded catalogue and loads
		 * charts into memory as a Map<String, Chart> for faster access.
		 * Also writes paths of the downloaded file and updated database
		 * into config.properties file with a time stamped name.
		 */
		buttonUpdate.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				final String catalogueDate = ukhoCatalogueFile.getBaseFileMetadata().getMD_DateStamp();
				localDateTime = LocalDateTime.now();
				String loadDate = localDateTime.format(formatter);
				String dbPathNew = ConfigPaths.USER.getPath() + "snc_catalogue_date_" +
						catalogueDate + "_loaded_on_" +
						loadDate + ".db";
				buttonDownload.setDisable(true);
				buttonCancel.setDisable(true);
				dbLoaderTask = new DBLoaderTask(ukhoCatalogueFile, dbPathNew);
		    	progressIndicator.progressProperty().unbind();
		    	progressIndicator.setStyle(" -fx-progress-color: cornflowerblue;");
		    	progressIndicator.progressProperty().bind(dbLoaderTask.progressProperty());
		    	labelDownloaded.textProperty().bind(dbLoaderTask.messageProperty());
		    	buttonUpdate.setVisible(false);
		    	buttonStop.setVisible(true);

		    	dbLoaderTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
								String chartLoadedNum = dbLoaderTask.messageProperty().getValue().toString();
								labelDownloaded.textProperty().unbind();
								labelDownloaded.setText(chartLoadedNum + ". Databse updated.");
								buttonStop.setVisible(false);
								buttonClose.setVisible(true);
								Database.databaseInstance = dbLoaderTask.getValue();
								database = Database.databaseInstance;
								loadChartsIntoMemory();
								System.out.println("ConfigPaths.DATABASE.getPath(): " + ConfigPaths.DATABASE.getPath());
								System.out.println("dbPathNew: " + dbPathNew);
								FileUtils.writeConfig(ConfigPaths.XML.getPath(), dbPathNew, ConfigPaths.API_KEY.getPath());
						}
					});
				dbLoaderTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, 
									new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						String chartLoadedNum = dbLoaderTask.messageProperty().getValue().toString();
						labelDownloaded.textProperty().unbind();
						labelDownloaded.setText("Process interrupted. " + chartLoadedNum);
					}
				});
				
				Thread thread = new Thread(dbLoaderTask);
		        thread.setDaemon(true);
		        thread.start();
			}
		});

		/*
		 * Cancels updating database process.
		 */
		buttonStop.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){	
				progressIndicator.progressProperty().unbind();
				labelDownloaded.textProperty().unbind();
				dbLoaderTask.cancel();
				buttonStop.setVisible(false);
				buttonUpdate.setVisible(true);		
			}
		});

		/*
		 * Closes the mapPane window
		 */
		buttonClose.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){	
				progressIndicator.progressProperty().unbind();
				Stage stage = (Stage) buttonClose.getScene().getWindow();
				stage.close();
			}
		});

		/*
		 * Attempt to delete existing catalogue file if it blocks saving
		 * the new one.
		 */
		buttonDelete.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){	
				try {
					Files.deleteIfExists(Paths.get(ConfigPaths.USER.getPath() + "xml/snc_catalogue.xml"));
					buttonDownload.setDisable(false);
					buttonCancel.setDisable(true);
					buttonUpdate.setVisible(false);
					webView.setOpacity(1);
					downloadTask.cancel(true);
					progressIndicator.progressProperty().unbind();
					labelDownloaded.textProperty().unbind();
					progressIndicator.setVisible(false);
					labelDownloaded.setVisible(false);
					buttonDelete.setVisible(false);
					MessageBox.show("File deleted. Try downloading again. ", "Info");
					 
				} catch (IOException eio) {
					eio.printStackTrace();
					buttonDownload.setDisable(false);
					buttonCancel.setDisable(true);
					buttonUpdate.setVisible(false);
					webView.setOpacity(1);
					downloadTask.cancel(true);
					progressIndicator.progressProperty().unbind();
					labelDownloaded.textProperty().unbind();
					progressIndicator.setVisible(false);
					labelDownloaded.setVisible(false);
					buttonDelete.setVisible(false);
					MessageBox.show("AccessDenied. Change file name or delete it manually (" +file.getAbsolutePath() + ")" , "Error");
				}
			}
		});
	        
	}

	/*
	 * Parsing chart catalogue from an xml file as UKHOCatalogueFile object.
	 */
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
			MessageBox.show("Catalogue file not found (" + FILE_PATH + ").", "Error");
			io.printStackTrace();
		} catch (JAXBException e) {
			MessageBox.show("Parsing from XML format failed (file corrupted or not in correct format)", "Error");
			e.printStackTrace();
		}

        return ukhoCatalogueFile;
	}

	/*
	 * Switches off SSL requirements while logging in to UKHO website
	 */
	private void connectionConfig() {
		ConnectionUtils connUtils = new ConnectionUtils();
		connUtils.sslOff();
	}
	
	private boolean notEmpty(String s) {
        return s != null && !"".equals(s);
    }

	/*
	 * Saves or clears user's login details after successful login.
	 */
	private void afterLogin() {
    	if(downloader.check(ConfigPaths.UKHO_DOWNLOAD.getPath())) {
       	 log.info("Catalogue file link: " + downloader.getUrlByIdJSOUP(ConfigPaths.UKHO_DOWNLOAD.getPath()) +
       			 " contains: "  + ConfigPaths.FILE_PARAM.getPath());
       	 buttonDownload.setDisable(false);
       	 if(checkboxDontRememeber.isSelected()){
	        	 try {
					userPreferences.clear();
					MessageBox.show("Your credentials have been cleared.", "Info");
				} catch (BackingStoreException e) {
					MessageBox.show("An error occurred when clearing your credentials.", "Info");
					e.getMessage();
				}
            }else {
           	 userPreferences.put("username",textUsername.getText());
           	 userPreferences.put("password",textPassword.getText());
            }
        }
    }

	/*
	 * Loads charts into a Map<String, StandardNauticalChart> from database
	 */
	public void loadChartsIntoMemory() {
		if (database == null || !database.hasRepository(AppDTO.class)) {
			System.out.println("--------------------database == null || !database.hasRepository(AppDTO.class)");
			return;
		} else {
			ChartMapLoadTask chartMapLoadTask = new ChartMapLoadTask(database);

			chartMapLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
					new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent t) {
							ChartMap.all = chartMapLoadTask.getValue();
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

}
	
	
	













