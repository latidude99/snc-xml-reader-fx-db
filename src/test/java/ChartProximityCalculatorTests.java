import com.latidude99.sncxmlreader.map.ChartProximityCalculator;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import com.latidude99.sncxmlreader.utils.XMLFileParser;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ChartProximityCalculatorTests {
    static UKHOCatalogueFile ukhoCatalogueFile;
    static String filePath = "src/test/resources/snc_catalogue.xml";
    static List<StandardNavigationChart> allChartsLoadedList;
    static Map<String, StandardNavigationChart> allChartsLoadedMap;
    ChartProximityCalculator chartProximityCalculator;
    Set<String> inputSet;
    Set<String> expectedSet;
    Set<String> actualSet;
    Map<String, StandardNavigationChart> inputMap;


    @BeforeAll
    public static void initAll(){
        allChartsLoadedMap = new TreeMap<>();
        try {
            ukhoCatalogueFile = XMLFileParser.parse(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        allChartsLoadedList = ukhoCatalogueFile.getProducts().getPaper().getCharts();
        for(StandardNavigationChart chart : allChartsLoadedList)
            allChartsLoadedMap.put(chart.getShortName(), chart);
    }

    @BeforeEach
    public void init(){
        chartProximityCalculator = new ChartProximityCalculator();
        expectedSet = new TreeSet<>();
        actualSet = new TreeSet<>();
        inputSet = new TreeSet<>();
        inputMap = new TreeMap<>();
    }

    @AfterEach
    public void tearDown(){
        chartProximityCalculator = null;
        inputMap = null;
        expectedSet = null;
        actualSet = null;
    }

    @AfterAll
    public static void tearDownAll(){
        ukhoCatalogueFile = null;
        allChartsLoadedList = null;
        allChartsLoadedMap = null;
    }

    @Test
    @DisplayName("findChartsFromRepository - search type: '=' (SINGLE)")
    public void findChartsFromRepository_Test00(){
        final String searchType = "=";

        inputMap.put("5", allChartsLoadedMap.get("5"));

        expectedSet.add("5");
        actualSet = chartProximityCalculator.totalChartsProximal(inputMap, allChartsLoadedMap, searchType).keySet();

        assertNotNull(actualSet, "charts found numbers set is null");
        assertEquals(expectedSet.size(),  actualSet.size(), "incorrect number of charts found");
        assertEquals(expectedSet, actualSet, "incorrect chart numbers found");
    }

    @Test
    @DisplayName("findChartsFromRepository - search type: 'range' (RANGE)")
    public void findChartsFromRepository_Test01(){
        final String searchType = "range";

        inputMap.put("5", allChartsLoadedMap.get("5"));
        final String[] numbers = {"5", "100", "2964", "2970", "3784", "4000", "4001", "4002", "4003",
                "4004", "4005", "4007", "4008", "4016", "4052", "4071", "4072", "4703", "4705", "4809"};

        expectedSet.addAll(Arrays.asList(numbers));
        actualSet = chartProximityCalculator.totalChartsProximal(inputMap, allChartsLoadedMap, searchType).keySet();

        assertNotNull(actualSet, "charts found numbers set is null");
        assertEquals(expectedSet.size(),  actualSet.size(), "incorrect number of charts found");
        assertEquals(expectedSet, actualSet, "incorrect chart numbers found");
    }

    @Test
    @DisplayName("findChartsFromRepository - search type: '-' (SMALLER_SCALE)")
    public void findChartsFromRepository_Test02(){
        final String searchType = "-";

        inputMap.put("5", allChartsLoadedMap.get("5"));
        final String[] numbers = {"5", "100", "143", "157", "2837", "2851", "2887", "2889", "2964",
                "2969", "2970", "3784", "3785", "4703", "4705", "6"};

        expectedSet.addAll(Arrays.asList(numbers));
        actualSet = chartProximityCalculator.totalChartsProximal(inputMap, allChartsLoadedMap, searchType).keySet();

        assertNotNull(actualSet, "charts found numbers set is null");
        assertEquals(expectedSet.size(),  actualSet.size(), "incorrect number of charts found");
        assertEquals(expectedSet, actualSet, "incorrect chart numbers found");
    }

    @Test
    @DisplayName("findChartsFromRepository - search type: '+' (LARGER_SCALE)")
    public void findChartsFromRepository_Test03(){
        final String searchType = "+";

        inputMap.put("5", allChartsLoadedMap.get("5"));
        final String[] numbers = {"5", "100", "15", "16", "168", "171", "1925", "1926", "1955", "2442", "2443",
                "2444", "263", "264", "265", "2853", "2854", "2887", "2889", "2895", "2896", "3175", "3176",
                "3177", "3178", "3179", "3404", "3405", "3412", "3413", "3414", "3511", "3518", "3519", "3520",
                "3522", "3523", "3530", "3660", "3661", "3709", "3713", "3715", "3723", "3739", "3752", "3762",
                "3763", "3772", "3778", "3779", "3780", "3781", "3782", "3783", "3787", "3789", "3950", "3951",
                "434", "452", "453", "542", "548", "7"};

        expectedSet.addAll(Arrays.asList(numbers));
        actualSet = chartProximityCalculator.totalChartsProximal(inputMap, allChartsLoadedMap, searchType).keySet();

        assertNotNull(actualSet, "charts found numbers set is null");
        assertEquals(expectedSet.size(),  actualSet.size(), "incorrect number of charts found");
        assertEquals(expectedSet, actualSet, "incorrect chart numbers found");
    }

    @Test
    @DisplayName("findChartsFromRepository - search type: '=' (RANGE)\", search input: non existent chart number")
    public void findChartsFromRepository_Test04(){
        final String searchType = "range";
        inputMap.put("19", allChartsLoadedMap.get("19"));

        assertThrows(NullPointerException.class,
                () -> chartProximityCalculator.totalChartsProximal(inputMap, allChartsLoadedMap, searchType),
                "expeced NullPointerException to be thrown but it was not");
    }


}















