import com.latidude99.sncxmlreader.utils.FormatUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormatUtilsTests {
    Set<String> expected;

    @BeforeEach
    public void init(){
        expected = new HashSet<>();
    }

    @AfterEach
    public void tearDown(){
        expected = null;
    }

    @Test
    @DisplayName("Input String 01  : single number")
    public void parseInputTest_1(){
        final String input = "3452";
        expected.add("3452");
        final Set<String> actual = FormatUtils.parseInput(input);
        assertEquals(expected, actual, () -> "single number");
    }

    @Test
    @DisplayName("Input String 02 : multiple numbers, comma sepatated with white space")
    public void parseInputTest_2(){
        final String input = "3452 , 564, 5678,2187 ";
        expected.add("3452");
        expected.add("564");
        expected.add("5678");
        expected.add("2187");
        final Set<String> actual = FormatUtils.parseInput(input);
        assertEquals(expected, actual, () -> "comma separated with white space");
    }

    @Test
    @DisplayName("Input String 03 : multiple numbers, dash separated with white space")
    public void parseInputTest_3(){
        final String input = "3452-564";
        for(int i = 564; i < 3453; i++)
            expected.add("" + i);
        final Set<String> actual = FormatUtils.parseInput(input);
        assertEquals(expected, actual, () -> "dash separated with white space, " +
                "no other characters");
    }

    @Test
    @DisplayName("Input String 04 : multiple numbers, dash with white space")
    public void parseInputTest_4(){
        final String input = "124,324,34-56 ,3452 - 564";
        expected.add("124");
        expected.add("324");
        for(int i = 34; i < 57; i++)
            expected.add("" + i);
        for(int i = 564; i < 3453; i++)
            expected.add("" + i);
        final Set<String> actual = FormatUtils.parseInput(input);
        assertEquals(expected, actual, () -> "Multiple numbers, comma and dash separated with white space");
    }

    @Test
    @DisplayName("Input String 05 : multiple numbers, comma at the beginning")
    public void parseInputTest_5(){
        final String input = "124,324,34-39 ,570 - 564";
        expected.add("124");
        expected.add("324");
        for(int i = 34; i < 40; i++)
            expected.add("" + i);
        for(int i = 564; i < 571; i++)
            expected.add("" + i);
        final Set<String> actual = FormatUtils.parseInput(input);
        assertEquals(expected, actual, () -> "comma at the beginning");
    }

    @Test
    @DisplayName("Input String 06 : multiple numbers, dash at the beginning")
    public void parseInputTest_6(){
        final String input = "-124,324,34-39 ,570 - 564";
        expected.add("124");
        expected.add("324");
        for(int i = 34; i < 40; i++)
            expected.add("" + i);
        for(int i = 564; i < 571; i++)
            expected.add("" + i);
        final Set<String> actual = FormatUtils.parseInput(input);
        assertEquals(expected, actual, () -> "comma at the beginning");
    }

    @ParameterizedTest
    @ValueSource(strings = {"!123,345", "-!123,345", "-,!123,345", "-,!123,,345", "-,,,-, !123,345"})
    @DisplayName("Input String 07 : multiple numbers, combination dash and comma at the beginning")
 //   @Disabled
    public void parseInputTest_7(String input){
        expected.add("!123");
        expected.add("345");
        final Set<String> actual = FormatUtils.parseInput(input);
        assertEquals(expected, actual, () -> "comma at the beginning");
    }



}





























