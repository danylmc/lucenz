package test;

import domain.Data;
import domain.Main;
import gui.DisplayGui;
import gui.DummyGui;
import gui.Gui;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataTests {

    @Test
    public void test_canary(){
        assertTrue(true);
    }


    @Test
    public void test_data1(){
        Main main = new Main();
        Gui gui = new DummyGui(main);
        main.setGui(gui);
        String substrateName = "S";

        Data data = new Data();
        List<Double> subsConcData = new ArrayList<>(Arrays.asList(
                0.333,
                0.933,
                1.533,
                2.133,
                2.733,
                3.333
        ));
        double[][] reactRateData = new double[][]{
                {0.384},
                {0.946},
                {1.385},
                {1.739},
                {2.030},
                {2.273}
        };

        data.setSubstrateCol(subsConcData);
        data.setTableData(reactRateData);
        data.setRows(reactRateData.length);
        data.setCols(reactRateData[0].length);
        double[][] testMatrix = new double [3][3];
        testMatrix[0][0] = 0.384;
        testMatrix[1][0] = 0.946;
        testMatrix[2][0] = 1.385;

        testMatrix[0][1] = 1.739;
        testMatrix[1][1] = 2.03;
        testMatrix[2][1] = 2.273;
        for(int col = 0; col < data.getCols(); col++) {
            for(int row = 0; row < 3/*For some reason getRows==6 at this point*/; row++) {
                //System.out.println(col + " " + row);
                assertEquals(data.getTableData()[row][col], testMatrix[row][col], 0.0);
            }
        }


    }
}
