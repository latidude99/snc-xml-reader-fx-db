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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Downloader2 {
	public final String DOWNLOAD_LINK_BASE = "https://enavigator.ukho.gov.uk/";
//	public final String FILE_NAME = "snc_catalogue.xml";
	
	
	public boolean loginCheck(String downloadPageString) {
		Element link = null;
		try {
			URL downloadPageUrl = new URL(downloadPageString);
			Document doc = Jsoup.parse(downloadPageUrl, 5000);
			link = doc.getElementById("MDSPages_linkDlPaperXml");
			if(link == null) {
//				MessageBox.show("Username or/and Password incorrect", "Warning");
				return false;
			}
		} catch (IOException e) {
			e.getMessage();
		}
		return true;	
	}
	
	public String downloadXML(String url) {
		URL downloadUrl = null;
		try {
			downloadUrl = new URL(getUrlByIdJSOUP(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("Downloading file ...  " + downloadUrl);
		File file = new File(ConfigPaths.XML.getPath());
		file = download(downloadUrl, file);
		return "File saved in " + file.getAbsolutePath() + ", " + file.toString();
	}
	
	public String getUrlByIdJSOUP(String downloadPageString) {
		String url = "";
		Element link = null;
		try {
			URL downloadPageUrl = new URL(downloadPageString);
			Document doc = Jsoup.parse(downloadPageUrl, 5000);
			link = doc.getElementById("MDSPages_linkDlPaperXml");
			url = link.attr("href");
			System.out.println(link.text());
			System.out.println(url);
						
		} catch (IOException | NullPointerException e) {
			e.getMessage();
//			MessageBox.show("Username or/and Password incorrect", "Warning");
		}
		return DOWNLOAD_LINK_BASE + url;	
	}
	
	private File download(URL url, File downFile) {
		Reader reader = null;
		StringBuilder buffer = new StringBuilder();
		try {
	        URLConnection con = url.openConnection();
	        reader = new InputStreamReader(con.getInputStream());
	        int ch;
	        while ((ch = reader.read()) != -1) {
		        buffer.append((char) ch);
		    }
	        
	        reader.close();
	        Writer writer = new FileWriter(downFile);
		    writer.write(buffer.toString());
		    writer.close();
		}catch(Exception ex){
        	ex.getMessage();
        }
		return downFile;
	}

}








