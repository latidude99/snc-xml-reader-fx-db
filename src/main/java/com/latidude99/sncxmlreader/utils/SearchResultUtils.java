package com.latidude99.sncxmlreader.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SearchResultUtils {

    public static String readFromFile(File file){
        BufferedReader br = null;
        String line;
        String lines = "";

        try {
//            System.out.println(file.getAbsolutePath());
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                lines = lines + line + "\n";
            }
            lines = lines.substring(0, lines.lastIndexOf('\n'));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    public static String saveToFile(File file, String text) throws IOException{
        if(!file.isFile())
            file.createNewFile();
        OutputStream os = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        for (String str : FormatUtils.stringToList(text)) {
            bw.write(str);
            bw.newLine();
        }
        bw.flush();
        bw.close();

        return file.getAbsolutePath();
    }


}
