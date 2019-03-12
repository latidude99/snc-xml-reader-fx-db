package com.latidude99.sncxmlreader.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.concurrent.Task;

public class DownloadTask extends Task<Void> {
	public final String FILE_PATH = "user_data/snc_catalogue.xml";
    private String downloadPageString;
    private String downloadLinkBase;

    public DownloadTask(String downloadLinkBase, String downloadPageString) {
    	this.downloadLinkBase = downloadLinkBase;
        this.downloadPageString = downloadPageString;
    }

    @Override
    protected Void call() throws Exception {
        String url = URLParser.getUrlByIdJSOUP(downloadLinkBase, downloadPageString);
        URLConnection connection = new URL(url).openConnection();
        
      //the UKHO website doesn't send 'content-lenght' parameter so ~value hardcoded to get visual progress
        long fileLength = 19_858_062;  //connection.getContentLengthLong(); 
        try (	InputStream is = connection.getInputStream();
                OutputStream os = Files.newOutputStream(Paths.get(FILE_PATH))) 
        	{

            long nread = 0L;
            byte[] buf = new byte[8192];
            int n;
            while ((n = is.read(buf)) > 0) {
                os.write(buf, 0, n);
                nread += n;
                this.updateProgress(nread, fileLength);
                this.updateMessage("Downloaded: " + nread + " bytes");
//                System.out.println("nread " + nread);
//                progressBox.getLbl().setText("Downloaded: " + nread);
            }
//        }catch(IOException e) {
//        	e.printStackTrace();
        	
        }

        return null;
    }

    @Override
    protected void failed() {
        System.out.println("download failed");
    }

    @Override
    protected void succeeded() {
        System.out.println("downloaded");
    }
 /*  
    private String getUrlByIdJSOUP(String downloadLinkBase, String downloadPageString) {
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
 */
}
















