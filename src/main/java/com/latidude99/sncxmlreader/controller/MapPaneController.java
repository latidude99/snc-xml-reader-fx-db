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
import com.latidude99.sncxmlreader.map.HTMLContent;
import com.latidude99.sncxmlreader.map.ScriptBlock;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.utils.ConfigPaths;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/*
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLConnection;
*/

public class MapPaneController implements Initializable{
	
	@FXML
    private WebView webView;
	
	private static final int MAX_DISPLAYED = 10_000;
//	private static final String ConfigPaths.HTML.getPath() = "user.data/html/";
	
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
		String contentUndisplayableCharts = contentError("You are trying to display charts 4000, 4006 or 4009",
				"Due to their scale or projection it is not possible to display them on standard Google Maps projection (Web Mercator)");

        webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);

		Map<String, StandardNavigationChart> chartsToDisplay = ChartMap.display;
        if(checkUndisplayableCharts(chartsToDisplay) && filterOutUndisplayableCharts(chartsToDisplay).isEmpty()){
            webEngine.loadContent(contentUndisplayableCharts, "text/html");
            return;
        }else if(chartsToDisplay.isEmpty())
            webEngine.loadContent(contentNoChartsFound, "text/html");

        ScriptBlock scriptBlock = new ScriptBlock(filterOutUndisplayableCharts(chartsToDisplay));
		HTMLContent htmlContent =  new HTMLContent(scriptBlock);
		String content =  htmlContent.getContent();



		if (chartsToDisplay.size() > MAX_DISPLAYED)
			webEngine.loadContent(contentTooManyChartsFound, "text/html");
		else if(chartsToDisplay.size() == ChartMap.all.size()) {
			webEngine.loadContent(content, "text/html");
			boolean fileSaved = saveHTMLInFile(ConfigPaths.HTML.getPath(), content);
		}else {
			webEngine.loadContent(content, "text/html");
			saveHTMLInFile(ConfigPaths.HTML.getPath(), content);

		}
		System.out.println(content);
	}

	private boolean checkUndisplayableCharts ( Map<String, StandardNavigationChart>  charts){
		if (charts.containsKey("4000") || charts.containsKey("4006") || charts.containsKey("4009"))
			return true;
		else
			return false;
	}

	private Map<String, StandardNavigationChart> filterOutUndisplayableCharts
			( Map<String, StandardNavigationChart>  charts){
		charts.remove("4000");
		charts.remove("4006");
		charts.remove("4009");
		return charts;
	}

	private boolean saveHTMLInFile(String filePath, String content) {
		String fileName = filePath + "charts";
		Set<String> shortNames = ChartMap.display.keySet();
		for(String shortName : shortNames) {
			fileName = fileName + "-" + shortName;
		}
		if(fileName.length() > 240) {
			List<Integer>  shortNamesList0_9Only = new ArrayList<>();
			List<String>  shortNamesList0_9AndA_z = new ArrayList<>();
			shortNames.forEach(n -> {if(!n.matches(".*[A-z].*"))
										shortNamesList0_9Only.add(Integer.parseInt(n.trim()));
						});
			shortNames.forEach(n -> {if(n.matches(".*[A-z].*"))
										shortNamesList0_9AndA_z.add(n);
						});
			Collections.sort(shortNamesList0_9Only);
			Collections.sort(shortNamesList0_9AndA_z);
			if(shortNamesList0_9AndA_z != null && !shortNamesList0_9AndA_z.isEmpty()) {
				fileName = filePath + "charts_" 
						+ shortNamesList0_9Only.get(0) + "-to-" + shortNamesList0_9AndA_z.get(shortNamesList0_9AndA_z.size() -1);
			}else {
				fileName = filePath + "charts_" 
						+ shortNamesList0_9Only.get(0) + "-to-" + shortNamesList0_9Only.get(shortNamesList0_9Only.size() -1);
			}		
		}
		fileName = fileName + ".html";
		writeFile(fileName, content);
		return true;
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
		
	
	










