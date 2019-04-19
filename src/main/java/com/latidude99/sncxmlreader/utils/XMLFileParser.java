package com.latidude99.sncxmlreader.utils;

import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class XMLFileParser {
	private static UKHOCatalogueFile ukhoCatalogueFile;
	
	public static UKHOCatalogueFile parse(String filePath) throws FileNotFoundException {
		try {
			File file = new File(filePath);
	    	FileInputStream fis = new FileInputStream(file);
			JAXBContext jaxbContext;
			jaxbContext = JAXBContext.newInstance(UKHOCatalogueFile.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ukhoCatalogueFile = (UKHOCatalogueFile) unmarshaller.unmarshal(fis);
			FileUtils.writeConfig(filePath, ConfigPaths.DATABASE.getPath(), ConfigPaths.API_KEY.getPath());
		} catch (JAXBException e) {
			MessageBox.show("Parsing from XML format failed (file corrupted or not in correct format)",
					"Error");
			e.printStackTrace();
		}
		return ukhoCatalogueFile;
	}
	

}
