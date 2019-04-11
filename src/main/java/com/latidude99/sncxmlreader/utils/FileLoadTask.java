package com.latidude99.sncxmlreader.utils;

import java.io.File;
import java.io.FileInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;

import javafx.concurrent.Task;

public class FileLoadTask extends Task<UKHOCatalogueFile> {
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
//    	System.out.println("Could not load the catalogue file");
//        MessageBox.show("Loading XML file unsuccessful", "Error");
    }

    @Override
    protected void succeeded() {
       System.out.println("Catalogue file loaded from: " + file.getAbsolutePath() + ", charts: " + 
    		   				ukhoCatalogueFile.getProducts().getPaper().getCharts().size());
    }
    
   
}

















