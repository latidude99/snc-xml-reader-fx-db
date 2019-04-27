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

import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.ContactInfo;

/*
 * Text for 'Help' and 'About' hyperlinks, accessible from the main app window.
 */
public class Info {
	public static String CATALOGUE_INFO_FULL;
	
	
	public static String about() {
		StringBuilder sb = new StringBuilder();
		sb.append("Standard Navigation ChartUtils_old XML Reader (SncXmlReader) " + ConfigPaths.APP_VERSION.getPath() + "\r\n");
		sb.append("--------------------------------------------------------------------------\r\n\n");
		
		sb.append("Copyright (C) 2019  Piotr Czapik.\r\n" + 
				"  author Piotr Czapik\r\n" + 
				"  version " + ConfigPaths.APP_VERSION.getPath() + "\r\n" +
				" \r\n" + 
				"  SncXmlReaderFXDB is free software: you can redistribute it and/or modify\r\n" +
				"  it under the terms of the GNU General Public License as published by\r\n" + 
				"  the Free Software Foundation, either version 3 of the License, or\r\n" + 
				"  (at your option) any later version.\r\n" + 
				"  SncXmlReaderFXDB is distributed in the hope that it will be useful,\r\n" +
				"  but WITHOUT ANY WARRANTY; without even the implied warranty of\r\n" + 
				"  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\r\n" + 
				"  GNU General Public License for more details.\r\n" + 
				"  You should have received a copy of the GNU General Public License\r\n" + 
				"  along with SncXmlReaderFXDB.  If not, see <http://www.gnu.org/licenses/>\r\n" +
				"  or write to: latidude99@gmail.com\r\n" +
				"  \r\n" +
				"  \r\n" +
				"  RESTRICTIONS\n" +
				"\t\t\n" +
				"\t\tGoogle Maps Javasript API KEY is restricted in use to the IP's of the computer's domain (domain " +
				"that\n" +
				"\t\tthe SncXmlReaderFXDB application is installed on) and will not work on computers connected to \n" +
				"\t\ta different network.\n" +
				"\t\t\n" +
				"\t\tIf you want to use this funcionality (displaying charts) on computers connected to a different network \n" +
				"\t\tyou need to\tprovide your own Google Maps Javascript API key. You can supply your own API key by" +
				" replacing\n" +
				"\t\tthe original API key manually in config.properties file.\n" +
				"\t\t\n" +
				"\t\tThe same restriction applies to all generated HTML files, they will not work if copied and " +
				"opened on \n" +
				"\t\tcomputers connected to a different network.\n" +
				"\t\t\n" +
				"\t\tNOTE\n" +
				"\t\t\n" +
				"\t\tAn HTML file will still be generated but it will not work until the API key is not replaced\n" +
				"\t\t(in the file itself) with a valid one.\n" +
				"\t\t\n" +
				"\t\t");
		return sb.toString();
	}
	
	public static String help() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t\tTo use ScnXmlReader you need to download a UKHO SNC catalogue file first (from within the app) or load a catalogue file \n" +
				"\t\tfrom your computer. The location of the catalogue will be written and saved in the config" +
				".properties file and used next \n" +
				"\t\ttime when the ScnXmlReader starts. Although a sample catalogue file is provided it is out of " +
				"date.\n" +
				"\t\tYou can re-load a catalogue file manually at any point, you can also download it from the UKHO website - a new updated \n" +
				"\t\tcatalogue is available every week.\n" +
				"\t\t\n" +
				"\t\tSEARCHING FOR CHARTS\n" +
				"\t\t\n" +
				"\t\t* if you are looking for an exact match enter in the search field:\n" +
				"\t\t\t\t  - a single chart number followed by '=' (equals) eg. '23='\n" +
				"\t\t\t\t  - a few chart numbers separated with ',' (comma) eg. '23, 25 ,27'\n" +
				"\t\t\t\t  - a range of charts separated with '-' (hyphen) eg. '67-45' \n" +
				"\t\t\t\t  - any combimation of the above eg. 23,45,67,254-256,nz67-45, aus12-aus34'\n" +
				"\t\t\t\t\t(spaces between digits will be removed during processing, \n" +
				"\t\t\t\t\t numbers joined and treated as one chart number)\t\t \n" +
				"\t\t\n" +
				"\t\t* if you want to broaden your search you can enter:\n" +
				"\t\t\t\t  - a single number not followed by '=' eg. '23' - finds all the charts that overlap or " +
				"border \n" +
				"\t\t\t\t\t\t\twith the searched one\t  \n" +
				"\t\t\t\t  - a single number followed by '+' (plus) eg. '23+' - finds all the charts that:\n" +
				"\t\t\t\t\t\t\t-> overlap or border with the searched chart\n" +
				"\t\t\t\t\t\t\tor\n" +
				"\t\t\t\t\t\t\t-> have roughly equal or larger scale\n" +
				"\t\t\t\t\t\t\tand\n" +
				"\t\t\t\t\t\t\t-> their centre is not farther than 300km from the centre of the searched chart\n" +
				"\t\t\t\t  - a single number followed by '-' (minus) eg. '23-' - finds all the charts that:\n" +
				"\t\t\t\t\t\t\t-> overlap or border with the searched chart\n" +
				"\t\t\t\t\t\t\tor\n" +
				"\t\t\t\t\t\t\t-> have roughly equal or smaller scale \n" +
				"\t\t\t\t\t\t\tand\n" +
				"\t\t\t\t\t\t\t-> their centre is not farther than 300km from the centre of the searched chart\n\n" +
				"\t\tDISPLAYING CHARTS COVERAGE POLYGONS\n" +
				"\t\t\n" +
				"\t\tYou can display charts coverage in a separate window as polygons on top of Google Maps. Charts shown on " +
				"\n" +
				"\t\tthe map are the same as in the search result. That means to display any chart you need to " +
				"search for it first.\n" +
				"\t\tIf you try to display too many charts (more than 100) the map might be a bit less " +
				"responsive \n" +
				"\t\ton older computers.\n" +
				"\t\tEvery time you try to display charts an HTML file is generated and saved in /user.data/html/... folder.\n" +
				"\t\tThe file's name contains of all the charts numbers that have been found during your last search. \n" +
				"\t\tIf the length of the name exceedes 240 characters the range of chart numbers(the first and the last one) \n" +
				"\t\tis used for the name.\n" +
				"\t\tThe generated HTML files are full, stand alone HTML pages with Javascript script and Google Maps Javasript \n" +
				"\t\tAPI KEY included. You can open those files in any major internet browser; however, IE and Edge have not\n" +
				"\t\tbeen tested.");
		return sb.toString();
	}
	
	
	public static String catalogueBasic(BaseFileMetadata meta, String schemaVersion) {
		return "catalogue date: " + meta.getMD_DateStamp() + 
				",  schema version: " + schemaVersion;
	}
	
	public static String catalogueFull(BaseFileMetadata meta, String schemaVersion) {
		ContactInfo contact = meta.getMD_PointOfContact().getResponsibleParty().getContactInfo();
		StringBuilder sb = new StringBuilder();
		
		sb.append("UKHO Standard Navigation Charts Catalogue, loaded from https://enavigator.ukho.gov.uk/Download/snc_catalogue.xml  \n\n");
		sb.append("Schema Version: " + schemaVersion);
		sb.append("Catalogue Date: " + meta.getMD_DateStamp() + "\n");
		sb.append("MD FileIdentifier: " + meta.getMD_FileIdentifier() + "\n");
		sb.append("MD CharacterSet: " + meta.getMD_CharacterSet() + "\n");
		sb.append("Organisation Name: " + meta.getMD_PointOfContact().getResponsibleParty().getOrganisationName() + "\n\n");
		sb.append("ContactInfo: " + "\n");
		sb.append("        Fax: " + contact.getFax() + "\n");
		sb.append("        Phone: " + contact.getPhone() + "\n");
		sb.append("        Email: " + contact.getAddress().getElectronicMailAddress() + "\n\n");
		sb.append("Address: " + contact.getAddress() + "\n");
		sb.append(" \n");
		
		CATALOGUE_INFO_FULL = sb.toString();
		
		return CATALOGUE_INFO_FULL;
	}

}



















