package com.latidude99.sncxmlreader.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;

import javafx.concurrent.Task;

public class FileLoadTask extends Task<Void> {
	File file;
	UKHOCatalogueFile ukhoCatalogueFile;
	
	String fileName;
	
	public FileLoadTask(String fileName) {
		this.fileName = fileName;
    }

    @Override
    protected Void call() throws Exception {

        try {
        	File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
           
			long fileSize = file.length();
			System.out.println(fileSize);
			
	        JAXBContext jaxbContext;
			jaxbContext = JAXBContext.newInstance(UKHOCatalogueFile.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	        ukhoCatalogueFile = (UKHOCatalogueFile) unmarshaller.unmarshal(fis);
	        ChartUtils.setUkhoCatalogueFile(ukhoCatalogueFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

        return null;
    }

    @Override
    protected void failed() {
        System.out.println("Could not load the catalogue file");
    }

    @Override
    protected void succeeded() {
        System.out.println("Catalogue file loaded");
    }
    
   
}

















