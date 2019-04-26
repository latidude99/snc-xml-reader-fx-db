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

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * Utility methods for reading/writing to config.properties file.
 */

public class FileUtils {
	private static final org.apache.log4j.Logger log = Logger.getLogger(FileUtils.class);

	public static String readXMLPath(String configPath, String defaultXmlPath) {
		String xmlFileProperty = ConfigPaths.XML_FILE_PROPERTY.getPath();
		Map<String, String> properties;
		properties = readConfig(configPath);
		String xmlFilePath = properties.getOrDefault(xmlFileProperty, defaultXmlPath);
		return xmlFilePath.trim();
	}
	
	public static String readDBPath(String configPath, String defaultDbPath) {
		String dbFileProperty = ConfigPaths.DB_FILE_PROPERTY.getPath();
		Map<String, String> properties;
		properties = readConfig(configPath);
		String dbFilePath = properties.getOrDefault(dbFileProperty, defaultDbPath);
		return dbFilePath.trim();
	}
	
	public static String readApiKey(String configPath, String defaultApiKey) {
		String apiKeyProperty = ConfigPaths.API_KEY_PROPERTY.getPath();
		Map<String, String> properties;
		properties = readConfig(configPath);
		ConfigPaths.API_KEY.setPath(properties.getOrDefault(apiKeyProperty, defaultApiKey));
		return ConfigPaths.API_KEY.getPath().trim();
	}

	public static Map<String, String> readConfig(String configPath) {
		String xmlFileProperty = ConfigPaths.XML_FILE_PROPERTY.getPath();
		String dbFileProperty = ConfigPaths.DB_FILE_PROPERTY.getPath();
		String apiKeyProperty = ConfigPaths.API_KEY_PROPERTY.getPath();
		Map<String, String> properties = new HashMap<>();
		
		BufferedReader br = null;
		String line = "";
		String[] names;
		try {
	        File configFile = new File(configPath);
	        if(!configFile.isFile()) {
	        	configFile.createNewFile();
	        	log.info("Config file created: " + configFile.getName());
				writeConfig(ConfigPaths.XML.getPath(), ConfigPaths.DATABASE.getPath(), ConfigPaths.API_KEY.getPath());
				log.info("Config file written size: " + configFile.length());
	        }
	        br = new BufferedReader(new FileReader(configFile));	
	        while ((line = br.readLine()) != null) {
	            if(!line.startsWith("#") && (!line.equals(""))) {
	            	if(line.contains("xml_file_path")) {
	            		names = line.split("=");
	            		if(names.length > 1)
		            		properties.put(xmlFileProperty, pathCleanup(names[1]));
	            	}
	            	if(line.contains("db_file_path")) {
	            		names = line.split("=");
						if(names.length > 1)
		            		properties.put(dbFileProperty, pathCleanup(names[1]));
	            	}
					if (line.contains("api_key")) {
						names = line.split("=");
						if(names.length > 1)
							properties.put(apiKeyProperty, pathCleanup(names[1]));
					}
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }finally {
			try {
				br.close();
			} catch (IOException e) {
				log.error(e.getStackTrace());
			}
	    }
		return properties;
	}
	
	
	public static boolean writeConfig(String xmlPath, String dbPath, String apiKey) {
		boolean isDone = false;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			String content = "# Do not delete this file.\r\n" + "\r\n"
					+ "# Format : \r\n" + "\r\n"
					+ "# xml_file_path = \"catalogue .xml file path\" \r\n"
					+ "# db_file_path = \"database .db file path\" \r\n"
					+ "# api_key = Google Maps Javascript API Key \r\n" + "\r\n" 
					+ "# (delivered in a separate file, needs to be manually copied/pasted below) \r\n"
					+ "\r\n"
					+ "# If there is no .xml or .db file in this foler (\"/user_data/...)\" \r\n"
					+ "# and you have either of the files (or both) in another folder \r\n"
					+ "# copy it into this folder and change the files names below accordingly.\r\n" + "\r\n"
					+ "# Otherwise you can go and download a new chart catalogue from the UKHO website." + "\r\n"
					+ "\r\n" + "\r\n" + "# Please leave the next lines uncommented:\r\n"
					+ "\r\n" 
					+ "xml_file_path = " + xmlPath + "\r\n"
					+ "\r\n"
					+ "db_file_path = " + dbPath + "\r\n" 
					+ "\r\n"
					+ "api_key = " + apiKey + "\r\n" +
					"\r\n" + "\r\n" + "\r\n" + "\r\n"
					+ "# Please do not leave uncommented lines (without \'#\' character in front) \r\n"
					+ "# the software will attempt to read properties \r\n"
					+ "# from the uncommented lines (icons than blank lines)!";

			fw = new FileWriter(ConfigPaths.CONFIG.getPath());
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("Done");
			isDone = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return isDone;
	}


	public static boolean writeDBPathToConfig(String configPath, String dbPath) {
		String xmlPath = FileUtils.readXMLPath(configPath, ConfigPaths.XML.getPath());
		String apiKEY = FileUtils.readApiKey(configPath, ConfigPaths.API_KEY.getPath());
		boolean isDone = false;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			String content = "# Do not delete this file.\r\n" + "\r\n"
					+ "# Format : \r\n" + "\r\n"
					+ "# xml_file_path = \"catalogue .xml file path\" \r\n"
					+ "# db_file_path = \"database .db file path\" \r\n"
					+ "# api_key = Google Maps Javascript API Key \r\n" + "\r\n"
					+ "# (delivered in a separate file, needs to be manually copied/pasted below) \r\n"
					+ "\r\n"
					+ "# If there is no .xml or .db file in this foler (\"/user_data/...)\" \r\n"
					+ "# and you have either of the files (or both) in another folder \r\n"
					+ "# copy it into this folder and change the files names below accordingly.\r\n" + "\r\n"
					+ "# Otherwise you can go and download a new chart catalogue from the UKHO website." + "\r\n"
					+ "\r\n" + "\r\n" + "# Please leave the next lines uncommented:\r\n"
					+ "\r\n"
					+ "xml_file_path = " + xmlPath + "\r\n"
					+ "\r\n"
					+ "db_file_path = " + dbPath + "\r\n"
					+ "\r\n"
					+ "\r\n" + "\r\n" + "\r\n" + "\r\n"
					+ "# Please do not leave uncommented lines (without \'#\' character in front) \r\n"
					+ "# the software will attempt to read properties \r\n"
					+ "# from the uncommented lines (icons than blank lines)!";

			fw = new FileWriter(configPath);
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("Done");
			isDone = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return isDone;
	}
	
	public static boolean writeXMLPathToConfig(String configPath, String xmlPath) {
		String dbPath = FileUtils.readDBPath(configPath, ConfigPaths.DATABASE.getPath());
		String apiKEY = FileUtils.readApiKey(configPath, ConfigPaths.API_KEY.getPath());
		boolean isDone = false;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			String content = "# Do not delete this file.\r\n" + "\r\n"
					+ "# Format : \r\n" + "\r\n"
					+ "# xml_file_path = \"catalogue .xml file path\" \r\n"
					+ "# db_file_path = \"database .db file path\" \r\n"
					+ "# api_key = Google Maps Javascript API Key \r\n" + "\r\n"
					+ "# (delivered in a separate file, needs to be manually copied/pasted below) \r\n"
					+ "\r\n"
					+ "# If there is no .xml or .db file in this foler (\"/user_data/...)\" \r\n"
					+ "# and you have either of the files (or both) in another folder \r\n"
					+ "# copy it into this folder and change the files names below accordingly.\r\n" + "\r\n"
					+ "# Otherwise you can go and download a new chart catalogue from the UKHO website." + "\r\n"
					+ "\r\n" + "\r\n" + "# Please leave the next lines uncommented:\r\n"
					+ "\r\n"
					+ "xml_file_path = " + xmlPath + "\r\n"
					+ "\r\n"
					+ "db_file_path = " + dbPath + "\r\n"
					+ "\r\n"
					+ "\r\n" + "\r\n" + "\r\n" + "\r\n"
					+ "# Please do not leave uncommented lines (without \'#\' character in front) \r\n"
					+ "# the software will attempt to read properties \r\n"
					+ "# from the uncommented lines (icons than blank lines)!";

			fw = new FileWriter(configPath);
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("Done");
			isDone = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return isDone;
	}

	/*
	 * To prevent accidental ";" or ":" etc. at the end of a property line.
	 */
	private static String pathCleanup(String path) {
		String pathCleaned = "";
		pathCleaned = path.trim();
		char[] invalidChars = {';', ':', ',', '.'};
		for(char ch : invalidChars) {
			if(pathCleaned.length() > 1 && pathCleaned.charAt(pathCleaned.length() - 1) == ch)
				pathCleaned = pathCleaned.substring(0, pathCleaned.length() -1);
		}	
		return pathCleaned;
	}

}

















