package com.latidude99.sncxmlreader.utils;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class URLParser {
	
	public static String getUrlByIdJSOUP(String downloadLinkBase, String downloadPageString) {
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
			MessageBox.show("Username or/and Password incorrect", "Warning");
		}
		return downloadLinkBase + url;	
	}

}
