package com.latidude99.sncxmlreader.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;

public class XMLFileParser {
	private static UKHOCatalogueFile ukhoCatalogueFile;
	
	public static UKHOCatalogueFile parse(String fileName) throws FileNotFoundException {
		try {
	    	File file = new File(fileName);
	        FileInputStream fis = new FileInputStream(file);
	       
			long fileSize = file.length();
			System.out.println(fileSize);
			
	        JAXBContext jaxbContext;
			jaxbContext = JAXBContext.newInstance(UKHOCatalogueFile.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ukhoCatalogueFile = (UKHOCatalogueFile) unmarshaller.unmarshal(fis);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return ukhoCatalogueFile;
	}
	

}
