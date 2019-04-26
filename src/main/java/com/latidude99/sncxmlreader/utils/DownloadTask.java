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

import javafx.concurrent.Task;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * Downloads chart catalogue xml file and saves it according to the name pattern:
 * ConfigPaths.USER.getPath() + "xml/snc_catalogue_date__downloaded_on_" + downloadDate + ".xml"
 */

public class DownloadTask extends Task<Void> {
    private static final org.apache.log4j.Logger log = Logger.getLogger(DownloadTask.class);
	private String downloadPageString;
    private String downloadLinkBase;
	LocalDateTime localDateTime;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");

    public DownloadTask(String downloadLinkBase, String downloadPageString) {
    	this.downloadLinkBase = downloadLinkBase;
        this.downloadPageString = downloadPageString;
    }

    @Override
    protected Void call() throws Exception {
		localDateTime = LocalDateTime.now();
		String downloadDate = localDateTime.format(formatter);
		String newFilePath = ConfigPaths.USER.getPath() + "xml/snc_catalogue_date_" +
				"_downloaded_on_" + downloadDate + ".xml";
		FileUtils.writeXMLPathToConfig(ConfigPaths.CONFIG.getPath(), newFilePath);
		
        String url = URLParser.getUrlByIdJSOUP(downloadLinkBase, downloadPageString);
        URLConnection connection = new URL(url).openConnection();
        
		// The UKHO website doesn't send 'content-lenght' parameter so approx. value
		// is hardcoded to a get visual progress
        long fileLength = 19_900_000;  //connection.getContentLengthLong();
        try (	InputStream is = connection.getInputStream();
                OutputStream os = Files.newOutputStream(Paths.get(newFilePath))) 
        	{
        	String nreadFormatted;
            long nread = 0L;
            byte[] buf = new byte[8192];
            int n;
            while ((n = is.read(buf)) > 0) {
                os.write(buf, 0, n);
                nread += n;
                nreadFormatted = String.format("%,d", nread);
                this.updateProgress(nread, fileLength);
                this.updateMessage("Downloaded: " + nreadFormatted + " bytes");
            }
        }catch(IOException e) {
        	log.error(e.getStackTrace());
        }
        return null;
    }

    @Override
    protected void failed() {
        log.error("Download failed");
    }

    @Override
    protected void succeeded() {
        log.info("Downloaded");

    }

}
















