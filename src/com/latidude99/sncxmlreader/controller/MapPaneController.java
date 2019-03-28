package com.latidude99.sncxmlreader.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Reader;
import java.net.URL;
//import java.net.URLConnection;
import java.util.ResourceBundle;

import com.latidude99.sncxmlreader.db.ChartMap;
import com.latidude99.sncxmlreader.web.HTMLContent;
import com.latidude99.sncxmlreader.web.ScriptBlock;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
		String contentNoChartsFound = contentError("No Charts Found", "");
		String contentTooManyChartsFound = contentError(
				"Maximum 10 charts allowed", "You are trying to display " + ChartMap.found.size() + " charts");
		
        webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);
		
		ScriptBlock scriptBlock = new ScriptBlock(ChartMap.found); 
		HTMLContent htmlContent =  new HTMLContent(scriptBlock);
		String content =  htmlContent.getContent();
				
		if(ChartMap.found.isEmpty())	
			webEngine.loadContent(contentNoChartsFound, "text/html");	
		else if (ChartMap.found.size() > 10)
			webEngine.loadContent(contentTooManyChartsFound, "text/html");
		else {
			webEngine.loadContent(content, "text/html");
			System.out.println("webEngine.getLocation(): " + webEngine.getLocation());
			String fileName = "user_data/searched/charts";
			for(String shortName : ChartMap.found.keySet())
				fileName = fileName + "-" + shortName;
			fileName = fileName + ".html";
			writeFile(fileName, content);
		}
		
//		ChartCentreCalc chartsCentre = new ChartCentreCalc(ChartMap.map);
//		chartsCentre.chartsCentreCoords.forEach((s, c) -> System.out.println(s + ", " + c));
		System.out.println(content);
		
	}
	
	private static boolean writeFile(String filePath, String content) {
		boolean isDone = false;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(filePath);
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("Done");
			isDone = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return isDone;
	}
	
	private String contentError(String message1, String message2) {
		String content = "<!DOCTYPE html>\r\n" + 
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
				"         h2{\r\n" + 
				"             color: white;\r\n" + 
				"             font-family: Verdana;\r\n" + 
				"             text-align: center;\r\n" + 
				"             font-size: 20px;\r\n" + 
				"         }\r\n" + 				
				"         h1{\r\n" + 
				"             color: white;\r\n" + 
				"             font-family: Verdana;\r\n" + 
				"             text-align: center;\r\n" + 
				"             font-size: 30px;\r\n" + 
				"         }\r\n" + 
				"      </style>\r\n" + 
				"  </head>\r\n" + 
				"  <body>\r\n" + 
				"    <div>\r\n" + 
				"    	<br><br><br><br><br><br><br><br><br><br><br><br>\r\n" + 
				"    	<h1>" + message1 + "</h1>\r\n" + 
				"    	<h2>" + message2 + "</h2>\r\n" + 
				"    </div>\r\n" + 
				"  </body>\r\n" + 
				"</html>"
				;
		return content;
	}
		
}	
		
	
	










