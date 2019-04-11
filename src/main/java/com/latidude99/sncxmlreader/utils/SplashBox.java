/**Copyright (C) 2017  Piotr Czapik.
* @author Piotr Czapik
* @version 2.0
* 
*  This file is part of SncXmlReader.
*  Subs Converter is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
* 
*  SncXmlReader is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
* 
*  You should have received a copy of the GNU General Public License
*  along with SncXmlReader.  If not, see <http://www.gnu.org/licenses/>
*  or write to: latidude99@gmail.com
*/

package com.latidude99.sncxmlreader.utils;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashBox {
    public static void show() {
        
        try {
        	Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
 
            Label lbl = new Label("LABEL");
            VBox vBox = new VBox(lbl);
 
            //make the background of the label white and opaque
            lbl.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
 
            //add some borders to visualise the element' locations
            lbl.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null, null)));
            vBox.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
 
            Scene scene = new Scene(vBox);
            stage.setScene(scene);
 
            
            vBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
            scene.setFill(null);
            stage.initStyle(StageStyle.TRANSPARENT);
 
            stage.setWidth(400);
            stage.setHeight(400);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}















