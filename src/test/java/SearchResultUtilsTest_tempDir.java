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

import com.latidude99.sncxmlreader.utils.SearchResultUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchResultUtilsTest_tempDir {
    String expected =  "";
    File file;


    @BeforeEach
    public void init(){

    }

    @AfterEach
    public void tearDown(){
        expected = null;
        file = null;
    }

    @Test
    @DisplayName("SearchResultUtils Test 01 - read from file")
    public void searchSaveTest_01(){
        file = new File("src/test/resources/temp/SearchResultUtils/test_read_file.txt");
        expected = "Searched for 1 chart\n" +
                "7  \n" +
                "\n" +
                "Found 1 chart\n" +
                "7  \n" +
                "-------------------------------------------------\n" +
                "\n" +
                "Chart Number: 7\n" +
                "Chart International Number: 7164\n" +
                "Chart Scale: 1:25,000\n" +
                "Chart Title: Aden Harbour and Approaches\n" +
                "Chart Status: Edition, Date: 2013-10-31\n" +
                "\n" +
                "Additional panels: none\n" +
                "\n" +
                "-------------------------------------------------\n" +
                "\n" +
                "Geographic Limits: \n" +
                "               Polygon: \n" +
                "                          Position:      latitude = 12.69 longitude = 44.84167\n" +
                "                          Position:      latitude = 12.83503 longitude = 44.84165\n" +
                "                          Position:      latitude = 12.83502 longitude = 44.95505\n" +
                "                          Position:      latitude = 12.83503 longitude = 45.0683\n" +
                "                          Position:      latitude = 12.69 longitude = 45.06833\n" +
                "                          Position:      latitude = 12.69002 longitude = 44.95665\n" +
                "                          Position:      latitude = 12.69 longitude = 44.84167\n" +
                "\n" +
                "Notices To Mariners: \n" +
                "           Year: 2016, Week: 3, Number: 452, Type: ChartUpdate\n" +
                "           Year: 2017, Week: 35, Number: 4150, Type: ChartUpdate\n" +
                "           Year: 2018, Week: 12, Number: 1445, Type: ChartUpdate\n" +
                "           Year: 2018, Week: 18, Number: 2053, Type: ChartUpdate\n" +
                "================================================="
        ;

        final String actual = SearchResultUtils.readFromFile(file);
        assertEquals(expected, actual, "error while reading from file");

    }

    @Test
    @DisplayName("SearchResultUtils Test 02 - write and read from file")
    public void searchSaveTest_02(@TempDir Path tempDir){
        int random  = new Random().nextInt(100000);
        Path output = tempDir.resolve("test_write_file_" + random + ".txt");
        file = new File(output.toString());
        System.out.println(file.getAbsolutePath());
        /*
        used instead @TempDir annotation
        File file = new File("src/test/resources/temp/SearchResultUtils/test_write_file_" + random + ".txt");
        */
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        expected = "Searched for 1 chart\n" +
                "7  \n" +
                "\n" +
                "Found 1 chart\n" +
                "7  \n" +
                "-------------------------------------------------\n" +
                "\n" +
                "Chart Number: 7\n" +
                "Chart International Number: 7164\n" +
                "Chart Scale: 1:25,000\n" +
                "Chart Title: Aden Harbour and Approaches\n" +
                "Chart Status: Edition, Date: 2013-10-31\n" +
                "\n" +
                "Additional panels: none\n" +
                "\n" +
                "-------------------------------------------------\n" +
                "\n" +
                "Geographic Limits: \n" +
                "               Polygon: \n" +
                "                          Position:      latitude = 12.69 longitude = 44.84167\n" +
                "                          Position:      latitude = 12.83503 longitude = 44.84165\n" +
                "                          Position:      latitude = 12.83502 longitude = 44.95505\n" +
                "                          Position:      latitude = 12.83503 longitude = 45.0683\n" +
                "                          Position:      latitude = 12.69 longitude = 45.06833\n" +
                "                          Position:      latitude = 12.69002 longitude = 44.95665\n" +
                "                          Position:      latitude = 12.69 longitude = 44.84167\n" +
                "\n" +
                "Notices To Mariners: \n" +
                "           Year: 2016, Week: 3, Number: 452, Type: ChartUpdate\n" +
                "           Year: 2017, Week: 35, Number: 4150, Type: ChartUpdate\n" +
                "           Year: 2018, Week: 12, Number: 1445, Type: ChartUpdate\n" +
                "           Year: 2018, Week: 18, Number: 2053, Type: ChartUpdate\n" +
                "================================================="
                ;
        try {
            SearchResultUtils.saveToFile(file, expected);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String actual = SearchResultUtils.readFromFile(file);
        assertEquals(expected, actual, "error writing or reading from file");
        file.delete();
    }



}























































