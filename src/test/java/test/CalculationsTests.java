package test;

import domain.Calculations;
import domain.Data;
import org.junit.Assert;
import org.junit.Test;
import persistency.Loading;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;


/**
 * A test suite for the Calculations class.
 */
public class CalculationsTests {

    /**
     * Checks the calculation output values of sim1.ktn against the original program's values.
     *
     * @throws Exception if test fails
     */
    @Test
    public void calculationsTest1() throws Exception {
        Data data = Loading.load("src/test/resources/sim1.ktn");

        // Apply calculations and get result:
        Calculations.fit(data);
        Map<String, Double> result = data.getTextData();

        // Compare output values:
        assertLessThan(2.79e-8, result.get("wes"), 3);
        assertLessThan(2.92e-4, result.get("chis"), 3);
        Assert.assertEquals(5.01e0, result.get("vm"), 0.1);
        assertLessThan(6.66e-2, result.get("sevm"), 3);

        Assert.assertEquals(4.01e0, result.get("km"), 0.1);
        assertLessThan(4.53e-2, result.get("sekm"), 3);
    }

    /**
     * Checks the calculation output values of sim2.ktn against the original program's values.
     *
     * @throws Exception if test fails
     */
    @Test
    public void calculationsTest2() throws Exception {
        Data data = Loading.load("src/test/resources/sim2.ktn");

        // Apply calculations and get result:
        Calculations.fit(data);
        Map<String, Double> result = data.getTextData();

        // Compare output values:
        assertLessThan(1.02e-5, result.get("wes"), 3);
        assertLessThan(0.00161, result.get("chis"), 3);
        Assert.assertEquals(4.91e0, result.get("vm"), 0.1);
        assertLessThan(0.0141, result.get("sevm"), 3);

        Assert.assertEquals(3.90e0, result.get("ka"),0.11);
        assertLessThan(0.0101, result.get("seka"), 3);
        Assert.assertEquals(1.94e0, result.get("kb"), 0.1);
        assertLessThan(0.0091, result.get("sekb"), 2);
        Assert.assertEquals(3.05e0, result.get("kia"), 0.1);
        assertLessThan(0.0038, result.get("sekia"), 2);
    }

    /**
     * Checks the calculation output values of sim3.ktn against the original program's values.
     *
     * @throws Exception if test fails
     */
    @Test
    public void calculationsTest3() throws Exception {
        Data data = Loading.load("src/test/resources/sim3.ktn");

        // Apply calculations and get result:
        Calculations.fit(data);
        Map<String, Double> result = data.getTextData();

        // Compare output values:
        assertLessThan(3.83e-7, result.get("wes"), 3);
        assertLessThan(0.00053, result.get("chis"), 2);
        Assert.assertEquals(5.00e0, result.get("vm"), 0.1);
        assertLessThan(0.0015, result.get("sevm"), 2);

        Assert.assertEquals(4.00e0, result.get("ka"),0.1);
        assertLessThan(0.0013, result.get("seka"), 2);
        Assert.assertEquals(2.00e0, result.get("kb"), 0.1);
        assertLessThan(0.0011, result.get("sekb"), 2);
    }

    /**
     * Checks the calculation output values of sim4.ktn against the original program's values.
     *
     * @throws Exception if test fails
     */
    @Test
    public void calculationsTest4() throws Exception {
        Data data = Loading.load("src/test/resources/sim4.ktn");

        // Apply calculations and get result:
        Calculations.fit(data);
        Map<String, Double> result = data.getTextData();

        // Compare output values:
        assertLessThan(1.12e-7, result.get("wes"), 3);
        assertLessThan(0.00049, result.get("chis"), 2);
        Assert.assertEquals(5.00e0, result.get("vm"), 0.1);
        assertLessThan(0.0006, result.get("sevm"), 1);

        Assert.assertEquals(4.00e0, result.get("km"), 0.1);
        assertLessThan(0.0004, result.get("sekm"), 1);
        Assert.assertEquals(2.00e0, result.get("kis"), 0.1);
        assertLessThan(0.0007, result.get("sekis"), 1);
    }

    /**
     * Checks the calculation output values of sim5.ktn against the original program's values.
     *
     * @throws Exception if test fails
     */
    @Test
    public void calculationsTest5() throws Exception {
        Data data = Loading.load("src/test/resources/sim5.ktn");

        // Apply calculations and get result:
        Calculations.fit(data);
        Map<String, Double> result = data.getTextData();

        // Compare output values:
        assertLessThan(7.15e-8, result.get("wes"), 3);
        assertLessThan(0.00043, result.get("chis"), 2);
        Assert.assertEquals(5.00e0, result.get("vm"), 0.1);
        assertLessThan(0.0009, result.get("sevm"), 1);

        Assert.assertEquals(4.00e0, result.get("km"), 0.1);
        assertLessThan(0.0006, result.get("sekm"), 1);
        Assert.assertEquals(2.98e0, result.get("kii"), 0.1);
        assertLessThan(0.00844, result.get("sekii"), 3);
        Assert.assertEquals(2.00e0, result.get("kis"), 0.1);
        assertLessThan(0.0024, result.get("sekis"), 2);
    }

    /**
     * Checks the calculation output values of sim6.ktn against the original program's values.
     *
     * @throws Exception if test fails
     */
    @Test
    public void calculationsTest6() throws Exception {
        Data data = Loading.load("src/test/resources/sim6.ktn");

        // Apply calculations and get result:
        Calculations.fit(data);
        Map<String, Double> result = data.getTextData();

        // Compare output values:
        assertLessThan(6.01e-4, result.get("wes"), 3);
        assertLessThan(0.057, result.get("chis"), 2);
        Assert.assertEquals(5.20e0, result.get("vm"), 0.1);
        assertLessThan(0.0426, result.get("sevm"), 3);

        Assert.assertEquals(4.52e0, result.get("km"), 0.1);
        assertLessThan(0.0345, result.get("sekm"), 3);
        Assert.assertEquals(2.93e0, result.get("kii"), 0.1);
        assertLessThan(0.0414, result.get("sekii"), 3);
    }

    /**
     * Asserts that the actual value is less than the expected value.
     *
     * @param expectedMax expected maximum
     * @param actual actual value
     * @param sf significant figures to round the actual value to
     */
    private void assertLessThan(double expectedMax, double actual, int sf){
        // Round actual value to 3sf:
        BigDecimal bd = new BigDecimal(actual);
        bd = bd.round(new MathContext(sf));
        actual = bd.doubleValue();

        boolean withinRange = actual <= expectedMax;
        Assert.assertTrue(withinRange);
    }
}
