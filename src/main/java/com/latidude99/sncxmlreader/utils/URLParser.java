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

import java.io.IOException;
import java.net.URL;

/*
 * Identifies and extracts download link by HTML element id.
 */

public class URLParser {
	
	public static String getUrlByIdJSOUP(String downloadLinkBase, String downloadPageString) {
		String url = "";
		Element link;
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
