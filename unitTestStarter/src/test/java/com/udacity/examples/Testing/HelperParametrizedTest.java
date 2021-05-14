package com.udacity.examples.Testing;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HelperParametrizedTest {


    private String input;
    private String actual;

    public HelperParametrizedTest(String input,String actual) {
        super();
        this.input = input;
        this.actual = actual;
    }

    @Parameters
    public static Collection initData() {
        String [][] empNames = {{"Mika","Bianca"},{"Mika","Jeff"}};
        return Arrays.asList(empNames);
    }

    @Test
    public void verify_input_name_is_not_the_same_as_the_output_name() {
        Assert.assertNotEquals(input, actual);
    }
}
