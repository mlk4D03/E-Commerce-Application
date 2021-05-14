package com.udacity.examples.Testing;

import junit.framework.TestCase;
import org.junit.*;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

public class HelperTest {

    @Test
    public void test() {
        Assert.assertEquals(3,3);
    }

    @Test
    public void verify_getCount() {
        List<String> empNames = Arrays.asList("Mika", "Bianca");
        long actual = Helper.getCount(empNames);
        Assert.assertEquals(2,actual);
    }

    @Test
    public void verify_getStats(){
        List<Integer> yrsOfExperience = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        List<Integer> expectedList = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        IntSummaryStatistics stats = Helper.getStats(yrsOfExperience);
        Assert.assertEquals(19,stats.getMax());
        Assert.assertEquals(expectedList,yrsOfExperience);
    }

    @Test
    public void compare_arrays() {
        int [] yrs = {10,14,2};
        int [] expectedYrs = {10,14,2};
        Assert.assertArrayEquals(expectedYrs,yrs);
    }

    @Test
    public void verify_getMergedList() {
        List<String> inputNames = Arrays.asList("Mika","Bianca","Tom","Maria","Robin");
        String expectedString = "Mika, Bianca, Tom, Maria, Robin";
        String actual = Helper.getMergedList(inputNames);
        Assert.assertEquals(expectedString,actual);

    }

    @Test
    public void verify_getStringWithLenghtOf3() {
        List<String> empName = Arrays.asList("Tom", "Mika", "Ole", "Bianca");
        long stringsOfLength3 = Helper.getStringsOfLength3(empName);
        Assert.assertEquals(2,stringsOfLength3);
    }

    @Test
    public void verify_getSquaredIntList() {
        List<Integer> nums = Arrays.asList(3,6,2,8,9,7);
        List<Integer> expectedList = Arrays.asList(3*3,6*6,2*2,8*8,9*9,7*7);
        List<Integer> squareList = Helper.getSquareList(nums);

        Assert.assertEquals(expectedList,squareList);
    }

    @Test
    public void verify_getFilteredList() {
        List<String> input = Arrays.asList("Mika","Bianca","","Tom");
        List<String> expectedOutput = Arrays.asList("Mika","Bianca","Tom");
        List<String> filteredList = Helper.getFilteredList(input);

        Assert.assertEquals(expectedOutput,filteredList);
    }

    @Before
    public void init() {
        System.out.println("Runs before each method");
    }

    @BeforeClass
    public static void setup() {
        System.out.println("Runs before each class");
    }

    @After
    public void initEnd() {
        System.out.println("Runs after each method");
    }

    @AfterClass
    public static void teardown() {
        System.out.println("Runs after class");
    }

	
}
