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

import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import javafx.concurrent.Task;
import org.apache.log4j.Logger;

import java.io.File;

/*
 * Parses UKHOCatalogueFile objects from an xml file on a separate thread.
 */

public class FileLoadTask extends Task<UKHOCatalogueFile> {
    private static final org.apache.log4j.Logger log = Logger.getLogger(FileLoadTask.class);
    File file;
	UKHOCatalogueFile ukhoCatalogueFile;
	String fileName;
	
	public FileLoadTask(String fileName) {
		this.fileName = fileName;
    }

    @Override
    protected UKHOCatalogueFile call() throws Exception {
    	
    	ukhoCatalogueFile = XMLFileParser.parse(fileName);
        return ukhoCatalogueFile;
    }

    @Override
    protected void failed() {
    log.error("Could not load the catalogue file");
    }

    @Override
    protected void succeeded() {
       log.info("Catalogue file loaded from: " + file.getAbsolutePath() + ", charts: " +
    		   				ukhoCatalogueFile.getProducts().getPaper().getCharts().size());
    }
    
   
}

















