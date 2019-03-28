package com.latidude99.sncxmlreader.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils2 {
	public static String CONFIG_PATH = "user_data/config.txt";
	private static String FILE_PATH = "user_data/snc_catalogue.xml";
	private static String DB_PATH = "user_data/snc_catalogue.db";
	private static String API_KEY = "AIzaSyC_wf8M3SFAJrr6jy_4nK04jUzVgw-TpEY2";
/*	
	public static void main(String[] args) {
		
		System.out.println(readXMLPath("user_data/config.txt") + ", " + readDBPath("user_data/config.txt"));
		System.out.println(writeConfig("xmwlxmlxml.xml", "dbdbwbdb.db"));
		System.out.println(readXMLPath("user_data/config.txt") + ", " + readDBPath("user_data/config.txt"));
	}
*/	
	public static String readXMLPath(String configPath, String defaultFilePath) {
		String[] names = readConfig(configPath);
		if (names.length != 4 || names[1].trim().equals("")){
//			MessageBoxOn.show("Incorrect config.txt format. Catalogue not found.\n\n"
//					+ "(check config.txt for information about its correct format", "Error");
			return  defaultFilePath;
		}	
		return names[1].trim();
	}
	public static String readDBPath(String configPath, String defaultDBPath) {
		String names[] = readConfig(configPath);
		if (names.length != 4 || names[3].trim().equals(""))
			return defaultDBPath;
		return names[3].trim();
	}
	
	public static String[] readConfig(String configPath) {
		BufferedReader br = null;
		String line = "";
		String[] names = null;
		try {
	        File configFile = new File(configPath);
	        if(!configFile.isFile()) {
	        	configFile.createNewFile();
	        	System.out.println("File Created: " + configFile.length());
	        	writeConfig(FILE_PATH, DB_PATH);
	        	System.out.println("File Written: " + configFile.length());
	        }
            
	        br = new BufferedReader(new FileReader(configFile));	
	        while ((line = br.readLine()) != null) {
//	            System.out.println(line);
	            if(!line.startsWith("#") && (!line.equals(""))) {
	            	names = line.split(",");
	            	return names;
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
		return names;
	}
	
	
	public static boolean writeConfig(String xmlPath, String dbPath) {
		boolean isDone = false;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			String content = "# Do not delete this file.\r\n" +
							 "\r\n" +
							 "# Format (CSV): \r\n" + 
							 "\r\n" +
							 "# Catalogue:, \"catalogue .xml file path\"  ,  Database:, \"database .db file path\". \r\n" + 
							 "# (__ , .xml file , __ , .db file --> comma separated, files are on the 1st and 3rd place)" +
							 "\r\n" +
							 "# If there is no .xml or .db file in this foler (\"/user_data/...)\" \r\n" + 
							 "# and you have either of the files (or both) in another folder \r\n" + 
							 "# copy it into this folder and change the files names below accordingly.\r\n" + 
							 "\r\n" + 
							 "# Otherwise you can go and download a new chart catalogue from the UKHO website." + 
							 "\r\n" + 
							 "\r\n" + 
							 "\r\n" + 
							 "# Please leave the next lines uncommented:\r\n" + 
							 "\r\n" +
							 "Catalogue:, " + xmlPath + ", Database:, " + dbPath + 
							 "\r\n" + 
							 "\r\n" + 
							 "\r\n" + 
							 "\r\n" + 
							 "# Please do not leave uncommented lines (without \'#\' character in front \r\n"
							 + "the software will attempt to read files names \r\n"
							 + "from the first uncommented line (other than a blank line)!"; 

			fw = new FileWriter(CONFIG_PATH);
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
		String filePath = FileUtils2.readXMLPath(configPath, FILE_PATH);
		boolean isDone = false;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			String content = "# Do not delete this file.\r\n" +
							 "\r\n" +
							 "# Format (CSV): \r\n" + 
							 "\r\n" +
							 "# Catalogue:, \"catalogue .xml file path\"  ,  Database:, \"database .db file path\". \r\n" + 
							 "# (__ , .xml file , __ , .db file --> comma separated, files are on the 1st and 3rd place)" +
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
							 "Catalogue:, " + filePath + ", Database:, " + dbPath + 
							 "\r\n" + 
							 "\r\n" + 
							 "\r\n" + 
							 "\r\n" + 
							 "# Please do not leave uncommented lines (without \'#\' character in front \r\n"
							 + "the software will attempt to read files names \r\n"
							 + "from the first uncommented line (other than a blank line)!"; 

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
	
	public static boolean writeXMLPathToConfig(String configPath, String filePath) {
		String dbPath = FileUtils2.readXMLPath(configPath, FILE_PATH);
		boolean isDone = false;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			String content = "# Do not delete this file.\r\n" +
							 "\r\n" +
							 "# Format (CSV): \r\n" + 
							 "\r\n" +
							 "# Catalogue:, \"catalogue .xml file path\"  ,  Database:, \"database .db file path\". \r\n" + 
							 "# (__ , .xml file , __ , .db file --> comma separated, files are on the 1st and 3rd place)" +
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
							 "Catalogue:, " + filePath + ", Database:, " + dbPath + 
							 "\r\n" + 
							 "\r\n" + 
							 "\r\n" + 
							 "\r\n" + 
							 "# Please do not leave uncommented lines (without \'#\' character in front \r\n"
							 + "the software will attempt to read files names \r\n"
							 + "from the first uncommented line (other than a blank line)!"; 

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
	
	

}

















