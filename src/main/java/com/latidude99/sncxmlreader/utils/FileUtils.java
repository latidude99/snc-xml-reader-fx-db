package com.latidude99.sncxmlreader.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
//	public static String CONFIG_PATH = "src/main/config/user_data/config.properties";
//	private static String FILE_PATH = "src/main/config/user_data/snc_catalogue.xml";
//	private static String DB_PATH = "src/main/config/user_data/snc_catalogue.db";
//	private static String API_KEY = "no default key";



	public static String readXMLPath(String configPath, String defaultXmlPath) {
		String xmlFileProperty = ConfigPaths.XML_FILE_PROPERTY.getPath();
		Map<String, String> properties = new HashMap<>();
		properties = readConfig(configPath);
		String xmlFilePath = properties.getOrDefault(xmlFileProperty, defaultXmlPath);
		return xmlFilePath.trim();
	}
	
	public static String readDBPath(String configPath, String defaultDbPath) {
		String dbFileProperty = ConfigPaths.DB_FILE_PROPERTY.getPath();
		Map<String, String> properties = new HashMap<>();
		properties = readConfig(configPath);
		String dbFilePath = properties.getOrDefault(dbFileProperty, defaultDbPath);
		return dbFilePath.trim();
	}
	
	public static String readApiKey(String configPath, String defaultApiKey) {
		String apiKeyProperty = ConfigPaths.API_KEY_PROPERTY.getPath();
		Map<String, String> properties = new HashMap<>();
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
		String[] names = new String[2];
		try {
	        File configFile = new File(configPath);
	        if(!configFile.isFile()) {
	        	configFile.createNewFile();
	        	System.out.println("File Created: " + configFile.length());
				writeConfig(ConfigPaths.XML.getPath(), ConfigPaths.DATABASE.getPath(), ConfigPaths.API_KEY.getPath());
	        	System.out.println("File Written: " + configFile.length());
	        }
            
	        br = new BufferedReader(new FileReader(configFile));	
	        while ((line = br.readLine()) != null) {
//	            System.out.println(line);
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
				e.printStackTrace();
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
			String content = "# Do not delete this file.\r\n" +
					 "\r\n" +
					 "# Format : \r\n" + 
					 "\r\n" +
					 "# xml_file_path = \"catalogue .xml file path\" \r\n" +
					 "#  db_file_path = \"database .db file path\" \r\n" + 
					 "\r\n" +
					 "\r\n" +
					 "# If there is no .xml or .db file in this foler (\"/user_data/...)\" \r\n" + 
					 "# and you have either of the files (or both) in another folder \r\n" + 
					 "# copy it into this folder and change the files names below accordingly.\r\n" + 
					 "\r\n" + 
					 "# Otherwise you can go and download a new chart catalogue from the UKHO website." + 
					 "\r\n" + 
					 "\r\n" + 
					 "\r\n" + 
					 "# Please leave the next line uncommented:\r\n" + 
					 "\r\n" +
					 "xml_file_path = " + xmlPath + "\r\n" +
					 "db_file_path = " + dbPath +  "\r\n" +
					"api_key = " + apiKEY + "\r\n" +
					 "\r\n" + 
					 "\r\n" + 
					 "\r\n" + 
					 "\r\n" + 
					 "# Please do not leave uncommented lines (without \'#\' character in front) \r\n" +
					 "# the software will attempt to read files names \r\n" +
					 "# from the uncommented lines (icons than a blank line)!";

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
			String content = "# Do not delete this file.\r\n" +
					 "\r\n" +
					 "# Format : \r\n" + 
					 "\r\n" +
					 "# xml_file_path = \"catalogue .xml file path\" \r\n" +
					 "#  db_file_path = \"database .db file path\" \r\n" + 
					 "\r\n" +
					 "\r\n" +
					 "# If there is no .xml or .db file in this foler (\"/user_data/...)\" \r\n" + 
					 "# and you have either of the files (or both) in another folder \r\n" + 
					 "# copy it into this folder and change the files names below accordingly.\r\n" + 
					 "\r\n" + 
					 "# Otherwise you can go and download a new chart catalogue from the UKHO website." + 
					 "\r\n" + 
					 "\r\n" + 
					 "\r\n" + 
					 "# Please leave the next line uncommented:\r\n" + 
					 "\r\n" +
					 "xml_file_path = " + xmlPath + "\r\n" +
					 "db_file_path = " + dbPath +  "\r\n" +
					"api_key = " + apiKEY + "\r\n" +
					 "\r\n" + 
					 "\r\n" + 
					 "\r\n" + 
					 "\r\n" + 
					 "# Please do not leave uncommented lines (without \'#\' character in front) \r\n" +
					"# the software will attempt to read properties \r\n"
					+
					 "# from the uncommented lines (icons than a blank line)!";

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
	
	// to prevent accidental ";" or ":" at the end of a property line
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
/*	
	
	public static void main(String[] args) {
		
		System.out.println(FileUtils.pathCleanup("efeffwr:;"));
		
		
		Map<String, String> config = readConfig("user_data/config.properties");
		String xml = config.get("xml_file_path");
		String db = config.get("db_file_path");
		
		writeConfig(xml, db);
		
		System.out.println(xml +"\n" + db);
	}
*/	

}

















