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
