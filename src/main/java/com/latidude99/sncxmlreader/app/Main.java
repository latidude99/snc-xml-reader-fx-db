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

package com.latidude99.sncxmlreader.app;

import com.latidude99.sncxmlreader.utils.ConfigPaths;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
	public static void main(String[] args) {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		final String appName = "Standard Navigation Charts XML Reader " + ConfigPaths.APP_VERSION.getPath();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		       @Override
		       public void handle(WindowEvent e) {
		          Platform.exit();
		          System.exit(0);
		       }
		    });

			Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/fxml/MainPane.fxml"));
			Scene scene = new Scene(parent);
			primaryStage.setScene(scene);
			primaryStage.setTitle(appName);
			primaryStage.setMaxWidth(732);
			primaryStage.setMinHeight(730);
			primaryStage.setResizable(false);
			primaryStage.show();
		
	}
	
	
	
}
