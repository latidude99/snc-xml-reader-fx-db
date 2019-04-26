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

import com.latidude99.sncxmlreader.utils.Info;
import com.latidude99.sncxmlreader.utils.MessageBox;
import com.latidude99.sncxmlreader.utils.MessageBoxOn;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class CataloguePaneController implements Initializable{
	
	
	@FXML
	private Button buttonWebView;
	@FXML
	private Label labelWebAddress;
	@FXML
    private Hyperlink linkHelp;
    @FXML
    private Hyperlink linkAbout;
    @FXML
    MainPaneController mainController;
    @FXML
    WebPaneController webPaneController;
    
    public Hyperlink getLinkHelp() {
		return linkHelp;
	}
	public Hyperlink getLinkAbout() {
		return linkAbout;
	}	
	public Button getButtonWebView() {
		return buttonWebView;
	}
	public Label getLabelWebAddress() {
		return labelWebAddress;
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		configureButtons();
		configureLinks();
		
	}

	/*
	 * Opens pane for logging in and downloading chart catalogue.
	 */
	private void configureButtons() {
		buttonWebView.setOnAction(new EventHandler<ActionEvent>(){
			@Override
            public void handle(ActionEvent event) {
 
				try {
			        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/webPane.fxml"));
			        Parent root1 = (Parent) fxmlLoader.load();
			        Stage stage = new Stage();
			        stage.setScene(new Scene(root1));
			        stage.setResizable(false);
			        stage.show();
			    } catch(Exception e) {
			        e.printStackTrace();
			    }
            }
        });
	}
	
	private void configureLinks() {
		
		linkAbout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MessageBox.show(Info.about(), "About");
			}
		});
		
		linkHelp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MessageBoxOn.show(Info.help(), "Help");
			}
		});
	}
		
}











