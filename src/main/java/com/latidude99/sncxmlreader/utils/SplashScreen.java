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

package com.latidude99.sncxmlreader.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
 * Shows progress indicator wheel.
 */

public class SplashScreen extends Application {
	 
    @Override
    public void start(Stage stage) throws Exception {
        try {
            Label lbl = new Label("LABEL");
            VBox vBox = new VBox(lbl);
 
            // Makes the background of the label white and opaque
            lbl.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
 
            // Adds some borders to visualise the element's locations
            lbl.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null, null)));
            vBox.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
 
            Scene scene = new Scene(vBox);
            stage.setScene(scene);

            // The three layers must be made transparent:
            // Makes the VBox transparent (the 4th parameter is the alpha)
            vBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
            // Sets the scene fill to transparent
            scene.setFill(null);
            // Sets the stage background to transparent
            stage.initStyle(StageStyle.TRANSPARENT);
 
            stage.setWidth(200);
            stage.setHeight(100);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

















