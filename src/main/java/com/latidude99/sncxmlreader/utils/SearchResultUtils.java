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
