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

import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import com.latidude99.sncxmlreader.utils.XMLFileParser;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class XMLFileParserTests {
    UKHOCatalogueFile ukhoCatalogueFile;
    StandardNavigationChart chart;
    int expected_NumberOfCharts;
    String expected_shortName;
    String expected_scale;
    String expected_Position_0_latitdude;
    int expected_PanelsNumber;
    String expected_Panel_1_Name;
    String expected_Panel_2_Scale;
    String expected_Panel_2_Position_3_longitude;
    int expected_NoticesToMarinersNumber;
    int expected_Notice_2018_16_Number;


    @BeforeEach
    public void init(){
        expected_NumberOfCharts = 3993;
        expected_shortName = "1457";
        expected_scale = "50000";
        expected_Position_0_latitdude = "53.16917";
        expected_PanelsNumber = 2;
        expected_Panel_1_Name = "A Approach Harlingen";
        expected_Panel_2_Scale = "35000";
        expected_Panel_2_Position_3_longitude = "5.14033";
        expected_NoticesToMarinersNumber = 17;
        expected_Notice_2018_16_Number = 1834; // 2
    }

    @AfterEach
    public void tearDown(){
        ukhoCatalogueFile = null;
    }

    @Test
    @DisplayName("JAXB XML parser test 01 - parsing xml file to UKHOCatalogueFile object")
    public void XMLFileParseTest_01(){
        String filePath = "src/test/resources/snc_catalogue.xml";
        try {
            ukhoCatalogueFile = XMLFileParser.parse(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<StandardNavigationChart> charts = ukhoCatalogueFile.getProducts().getPaper().getCharts();
        chart = charts.stream()
                .filter(c -> c.getShortName().equals("1457"))
                .collect(Collectors.toList())
                .get(0);

        String actual_shortName = chart.getShortName();
        String actual_scale = chart.getMetadata().getScale();
        String actual_Position_0_latitdude = chart
                .getMetadata()
                .getGeographicLimit()
                .getPolygons()
                .get(0)
                .getPositions()
                .get(0)
                .getLatitude();
        int actual_PanelsNumber = chart.getMetadata().getPanels().size();
        String actual_Panel_1_Name = chart.getMetadata().getPanels().get(0).getPanelName();
        String actual_Panel_2_Scale = chart.getMetadata().getPanels().get(1).getPanelScale();
        String actual_Panel_2_Position_3_longitude = chart
                .getMetadata()
                .getPanels()
                .get(1)
                .getPolygons()
                .get(0)
                .getPositions()
                .get(3)
                .getLongitude();
        int actual_NoticesToMarinersNumber = chart.getMetadata().getNotices().size();
        int actual_Notice_2018_16_Number = chart.getMetadata().getNotices().get(2).getNumberMapper();

        assertAll("UKHOCatalogueFile properties",
                () -> assertNotNull(ukhoCatalogueFile, "ukhoCatalogueFile object not created from xml file"),
                () -> assertEquals(expected_NumberOfCharts, charts.size(), "number of charts incorrect"),
                () -> assertEquals(expected_shortName, actual_shortName, "wrong chart number (ShortName)"),
                () -> assertEquals(expected_scale, actual_scale, "chart's main scale incorrect"),
                () -> assertEquals(expected_Position_0_latitdude, actual_Position_0_latitdude,
                        "main panel vertex 1 latitude incorrect"),
                () -> assertEquals(expected_PanelsNumber, actual_PanelsNumber, "number of panels incorrect"),
                () -> assertEquals(expected_Panel_1_Name, actual_Panel_1_Name, "panel 1 name incorrect"),
                () -> assertEquals(expected_Panel_2_Scale, actual_Panel_2_Scale, "panel 2 scale incorrect"),
                () -> assertEquals(expected_Panel_2_Position_3_longitude, actual_Panel_2_Position_3_longitude,
                        "panel 2 vertex 4 longitude incorrect"),
                () -> assertEquals(expected_NoticesToMarinersNumber, actual_NoticesToMarinersNumber,
                        "number of NoticesToMariners"),
                () -> assertEquals(expected_Notice_2018_16_Number, actual_Notice_2018_16_Number,
                        "number of the year 2018, week 16 notice")
        );

    }


}























