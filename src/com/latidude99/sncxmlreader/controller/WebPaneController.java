package com.latidude99.sncxmlreader.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

import com.latidude99.sncxmlreader.db.DB;
import com.latidude99.sncxmlreader.db.DBLoaderTask;
import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import com.latidude99.sncxmlreader.utils.ConnectionUtils;
import com.latidude99.sncxmlreader.utils.DownloadTask;
import com.latidude99.sncxmlreader.utils.Downloader2;
import com.latidude99.sncxmlreader.utils.FileLoadTask;
import com.latidude99.sncxmlreader.utils.MessageBox;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebPaneController implements Initializable{
	private static final String FILE_PATH = "user_data/snc_catalogue.xml";
	private static String DB_PATH = "user_data/snc_catalogue.db";
	public final String UKHO_HOME = "https://enavigator.ukho.gov.uk/";
	public final String UKHO_LOGIN = "https://enavigator.ukho.gov.uk/Login";
	public final String UKHO_DOWNLOAD = "https://enavigator.ukho.gov.uk/Download";
	public final String FILE_PARAM = "?file=";
	
    String setUsername =  "document.getElementsByName('un')[0].value='" + "PiotrC" + "'";
    String setPassword = "document.getElementsByName('pw')[0].value='" + "flamenco10ST" + "';";
	
    WebEngine webEngine;
    final BooleanProperty loginAttempted = new SimpleBooleanProperty(false);
    Downloader2 downloader = new Downloader2();
    Preferences userPreferences = Preferences.userRoot().node("/sncxmlreader");
    Task<Void> downloadTask;
    Task<UKHOCatalogueFile> fileLoadTask;
    DBLoaderTask dbLoaderTask;
    UKHOCatalogueFile ukhoCatalogueFile;
    File file;
    Nitrite database;
    int existingChartsNum;
    String existingCatalogueDate;
    
	
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
	@FXML
	MainPaneController mainPaneController;
/*	
	public MainPaneController getMainPaneController() {
		return mainPaneController;
	}
	public void setMainPaneController(MainPaneController mainPaneController) {
		this.mainPaneController = mainPaneController;
	}
*/	
	
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		configureCredentials();
		connectionConfig();	
		configureWebView();
		readExistingDB();
		configureButtons();            		
	}
	
	
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
	
	private void readExistingDB() {
		database = DB.getDB(DB_PATH);
		ObjectRepository<StandardNavigationChart> chartRepository = database.getRepository(StandardNavigationChart.class);
		existingChartsNum = chartRepository.find().size();
		ObjectRepository<BaseFileMetadata>metaRepository = database.getRepository(BaseFileMetadata.class);
		existingCatalogueDate = metaRepository.find().firstOrDefault().getMD_DateStamp();
	}
	
	private void configureButtons() {
				
		buttonDownload.setDisable(true);
		buttonCancel.setDisable(true);
		
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
		     @Override
		     public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
		    	 String newURL = webEngine.getLocation();
		         if (newState == Worker.State.SUCCEEDED) {
		        	 System.out.println("after login new URL: " + newURL);
		        	 afterLogin();
		         }
//		         boolean loggedIn = downloader.loginCheck(UKHO_DOWNLOAD);
		         
		     }
		});
		
		buttonSignIn.setOnAction(new EventHandler<ActionEvent>() {
			 @Override
	         public void handle(ActionEvent t) {
				 progressBar.progressProperty().bind(webView.getEngine().getLoadWorker().progressProperty());
				 progressBar.visibleProperty().bind(webView.getEngine().getLoadWorker().runningProperty());
				 if (notEmpty(textPassword.getText()) && notEmpty(textPassword.getText())) {
	            	 loginAttempted.set(false);
	                 webEngine.load(UKHO_HOME);
	             }else {
	             	MessageBox.show("Please enter your Username  and Password.", "Info");
	             }
				 
/*
			        int delay = 5;
			        scheduler.schedule(afterLoginTask, delay, TimeUnit.SECONDS);
			        //scheduler.shutdown();
*/			
	         }
			 
	    });

	    
	    buttonDownload.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){	
				rectangleCover.setVisible(true);
				buttonSignIn.setDisable(true);
				buttonDownload.setDisable(true);
				buttonCancel.setDisable(false);
				webView.setOpacity(0.3);
								
				downloadTask = new DownloadTask(UKHO_HOME, UKHO_DOWNLOAD);
				progressIndicator.setVisible(true);
				labelDownloaded.setVisible(true);
				progressIndicator.progressProperty().bind(downloadTask.progressProperty());
				labelDownloaded.textProperty().bind(downloadTask.messageProperty());
				
				downloadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
	                       						new EventHandler<WorkerStateEvent>() {
	                           @Override
	                           public void handle(WorkerStateEvent t) {
	                        	   ukhoCatalogueFile = loadFileFromPath(FILE_PATH);
	                        	   String downloadedCatalogueDate = 
	                        			   ukhoCatalogueFile.getBaseFileMetadata().getMD_DateStamp();
	                        	   int downloadedChartsNum = 
	                        			   ukhoCatalogueFile.getProducts().getPaper().getCharts().size();
	                           	   labelDownloaded.textProperty().unbind();
	                           	   progressIndicator.progressProperty().unbind();
	                        	   labelDownloaded.setText("Download complete. Catalogue Date: " + downloadedCatalogueDate);
	                        	   buttonUpdate.setVisible(true);         
	                        	   if(downloadedCatalogueDate.equals(existingCatalogueDate) 
	                        			   && existingChartsNum == downloadedChartsNum)
	                        		   MessageBox.show
	                        		   ("Dowloaded catalogue version is the same as the one already in Database",  "Info");
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
						    	   
						    	   
						    	   buttonDelete.setText("Delete file: " + FILE_PATH);
						    	   buttonDelete.setVisible(true);
						    	   MessageBox.show("Could not save the file. Delete the old file and try again. ", "Error");
						       }
						   });
				Thread thread = new Thread(downloadTask);
	            thread.setDaemon(true);
	            thread.start();
			}
		});
	    
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
	    
	    buttonUpdate.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				buttonDownload.setDisable(true);
				buttonCancel.setDisable(true);
				dbLoaderTask = new DBLoaderTask(ukhoCatalogueFile, FILE_PATH);
		    	progressIndicator.progressProperty().unbind();
		    	progressIndicator.setStyle(" -fx-progress-color: green;");
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
						}
					});
				dbLoaderTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, 
									new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						String chartLoadedNum = dbLoaderTask.messageProperty().getValue().toString();
						labelDownloaded.setText("Process interrupted. " + chartLoadedNum);
					}
				});
				
				Thread thread = new Thread(dbLoaderTask);
		        thread.setDaemon(true);
		        thread.start();
				
//				
			}
		});
	    
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
	    
	    buttonClose.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){	
				progressIndicator.progressProperty().unbind();
				Stage stage = (Stage) buttonClose.getScene().getWindow();
				stage.close();
			}
		});
	    
	    buttonDelete.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){	
				try {
					Files.deleteIfExists(Paths.get("user_data/snc_catalogue.xml"));
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
	
	// not used for the time being
	private UKHOCatalogueFile loadFileFromPathWithTask(String FILE_PATH) {
		fileLoadTask = new FileLoadTask(FILE_PATH);
		
		fileLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
				new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {					
				ukhoCatalogueFile = fileLoadTask.getValue();
				}
			});
		fileLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, 
							new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
			
			}
		});
		
		Thread thread = new Thread(fileLoadTask);
        thread.setDaemon(true);
        thread.start();
		
		return ukhoCatalogueFile;
	}
	
	private void connectionConfig() {
		ConnectionUtils connUtils = new ConnectionUtils();
		connUtils.sslOff();
	}
	
	private boolean notEmpty(String s) {
        return s != null && !"".equals(s);
    }
	
	// not used
	Runnable afterLoginTask = new Runnable() {
        public void run() {
            afterLogin();
        }
    };
    
    
    private void afterLogin() {
    	if(downloader.loginCheck(UKHO_DOWNLOAD)) {
       	 System.out.println("file link: " + downloader.getUrlByIdJSOUP(UKHO_DOWNLOAD) + 
       			 " contains: "  + FILE_PARAM); 
       	 buttonDownload.setDisable(false);
//       	 buttonCancel.setDisable(false);
       	 
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
    
    
	
	
}
	
	
	
	

//	https://enavigator.ukho.gov.uk  MDSPages_linkDlPaperXml













