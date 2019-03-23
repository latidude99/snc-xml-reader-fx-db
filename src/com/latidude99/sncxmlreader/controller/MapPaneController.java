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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
//import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
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

import com.latidude99.sncxmlreader.app.Main;
import com.latidude99.sncxmlreader.db.ChartMap;
import com.latidude99.sncxmlreader.db.DBLoaderTask;
import com.latidude99.sncxmlreader.db.Database;
import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import com.latidude99.sncxmlreader.utils.ConnectionUtils;
import com.latidude99.sncxmlreader.utils.DownloadTask;
import com.latidude99.sncxmlreader.utils.Downloader2;
import com.latidude99.sncxmlreader.utils.FileLoadTask;
import com.latidude99.sncxmlreader.utils.FileUtils;
import com.latidude99.sncxmlreader.utils.MessageBox;
import com.latidude99.sncxmlreader.web.HTMLContent;
import com.latidude99.sncxmlreader.web.ScriptBlock;

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

public class MapPaneController implements Initializable{
	
	@FXML
    private WebView webView;
	
	WebEngine webEngine;

	MainPaneController mainPaneController;
	
	public MainPaneController getMainPaneController() {
		return mainPaneController;
	}
	public void setMainPaneController(MainPaneController mainPaneController) {
		this.mainPaneController = mainPaneController;
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		showChart();

	}

	public void showChart() {
		String contentNoChartsFound = "<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"  <head>\r\n" + 
				"    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\r\n" + 
				"    <meta charset=\"utf-8\">\r\n" + 
				"    <title>Chart Display</title>\r\n" + 
				"     <style>\r\n" + 
				"      html, body {\r\n" + 
				"        height: 100%;\r\n" + 
				"        margin: 0px;\r\n" + 
				"        padding: 0px;\r\n" + 
				"        background-color: #99bbff;\r\n" + 
				"      }\r\n" + 
				"         h1{\r\n" + 
				"             color: white;\r\n" + 
				"             font-family: Verdana;\r\n" + 
				"             text-align: center;\r\n" + 
				"             text-shadow: 3px 2px navy;\r\n" + 
				"             font-size: 70px;\r\n" + 
				"         }\r\n" + 
				"      </style>\r\n" + 
				"  </head>\r\n" + 
				"  <body>\r\n" + 
				"    <div>\r\n" + 
				"    	<br><br><br><br><br><br><br><br><br><br><br><br>\r\n" + 
				"    	<h1>No Charts Found</h1>\r\n" + 
				"    </div>\r\n" + 
				"  </body>\r\n" + 
				"</html>"
				;

        webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);
		
		ScriptBlock scriptBlock = new ScriptBlock(ChartMap.found); //(ChartMap.map.get("2"));
		HTMLContent htmlContent =  new HTMLContent(scriptBlock);
		String content =  htmlContent.getContent();
		if(!ChartMap.found.isEmpty())		
			webEngine.loadContent(content, "text/html");
		else
			webEngine.loadContent(contentNoChartsFound, "text/html");
		
		System.out.println(content);
		
	}
	
		
}	
		
	
	










