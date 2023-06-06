package test;

import domain.Data;
import domain.KineticModel;
import org.junit.Test;
import persistency.Loading;
import persistency.Saving;

import java.nio.file.NoSuchFileException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PersistencyTests {

    /**
     * Testing Loading class,
     * valid file sim1.ktn
     */
    @Test
    public void loadTest1() throws Exception {
        Data data = Loading.load("src/test/resources/sim1.ktn");

        //checking first row values.
        assertEquals(1, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.UninhibitedOneSub, data.getModelType());

        //Checking second row value.
        assertEquals(0, data.getCoSubInhibRow().get(0), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values
        assertEquals(0.384260327717517, data.getTableData()[0][0], 0.0001);
        assertEquals(0.945672004865194, data.getTableData()[1][0], 0.0001);
        assertEquals(1.38532441713356, data.getTableData()[2][0], 0.0001);
        assertEquals(1.73895320397848, data.getTableData()[3][0], 0.0001);
        assertEquals(2.02955591860983, data.getTableData()[4][0], 0.0001);
        assertEquals(2.27260330015001, data.getTableData()[5][0], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim2.ktn
     */
    @Test
    public void loadTest2() throws Exception {
        Data data = Loading.load("src/test/resources/sim2.ktn");

        //checking first row values.
        assertEquals(5, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.UninhibitedTwoSubOrderedBiBi, data.getModelType());

        //Checking second row values.
        assertEquals(0.333, data.getCoSubInhibRow().get(0), 0.0);
        assertEquals(0.583, data.getCoSubInhibRow().get(1), 0.0);
        assertEquals(0.833, data.getCoSubInhibRow().get(2), 0.0);
        assertEquals(1.083, data.getCoSubInhibRow().get(3), 0.0);
        assertEquals(1.333, data.getCoSubInhibRow().get(4), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(6.83749648071394E-02, data.getTableData()[0][0], 0.0001);
        assertEquals(0.253184736945537, data.getTableData()[1][1], 0.0001);
        assertEquals(0.466906774111482, data.getTableData()[2][2], 0.0001);
        assertEquals(0.683118544971419, data.getTableData()[3][3], 0.0001);
        assertEquals(0.8911191081845, data.getTableData()[4][4], 0.0001);
        assertEquals(0.989909312416277, data.getTableData()[5][4], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim3.ktn
     */
    @Test
    public void loadTest3() throws Exception {
        Data data = Loading.load("src/test/resources/sim3.ktn");

        //checking first row values.
        assertEquals(5, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.UninhibitedTwoSubPingPong, data.getModelType());

        //Checking second row values.
        assertEquals(0.333, data.getCoSubInhibRow().get(0), 0.0);
        assertEquals(0.583, data.getCoSubInhibRow().get(1), 0.0);
        assertEquals(0.833, data.getCoSubInhibRow().get(2), 0.0);
        assertEquals(1.083, data.getCoSubInhibRow().get(3), 0.0);
        assertEquals(1.333, data.getCoSubInhibRow().get(4), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(0.262908574135481, data.getTableData()[0][0], 0.0001);
        assertEquals(0.573540697170503, data.getTableData()[1][1], 0.0001);
        assertEquals(0.831915850302847, data.getTableData()[2][2], 0.0001);
        assertEquals(1.05886997653749, data.getTableData()[3][3], 0.0001);
        assertEquals(1.26136228368927, data.getTableData()[4][4], 0.0001);
        assertEquals(1.35117054801599, data.getTableData()[5][4], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim4.ktn
     */
    @Test
    public void loadTest4() throws Exception {
        Data data = Loading.load("src/test/resources/sim4.ktn");

        //checking first row values.
        assertEquals(5, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.InhibitedCompetitive, data.getModelType());

        //Checking second row values.
        assertEquals(0.0, data.getCoSubInhibRow().get(0), 0.0);
        assertEquals(0.25, data.getCoSubInhibRow().get(1), 0.0);
        assertEquals(0.5, data.getCoSubInhibRow().get(2), 0.0);
        assertEquals(0.75, data.getCoSubInhibRow().get(3), 0.0);
        assertEquals(1.0, data.getCoSubInhibRow().get(4), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(0.384260327717517, data.getTableData()[0][0], 0.0001);
        assertEquals(0.858641634456102, data.getTableData()[1][1], 0.0001);
        assertEquals(1.17327414664013, data.getTableData()[2][2], 0.0001);
        assertEquals(1.39722258613913, data.getTableData()[3][3], 0.0001);
        assertEquals(1.56475437993817, data.getTableData()[4][4], 0.0001);
        assertEquals(1.78559948569592, data.getTableData()[5][4], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim5.ktn
     */
    @Test
    public void loadTest5() throws Exception {
        Data data = Loading.load("src/test/resources/sim5.ktn");

        //checking first row values.
        assertEquals(3, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.InhibitedNonCompetitive, data.getModelType());

        //Checking second row values.
        assertEquals(0.0, data.getCoSubInhibRow().get(0), 0.0);
        assertEquals(0.25, data.getCoSubInhibRow().get(1), 0.0);
        assertEquals(0.5, data.getCoSubInhibRow().get(2), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(0.384, data.getTableData()[0][0], 0.0001);
        assertEquals(0.847, data.getTableData()[1][1], 0.0001);
        assertEquals(1.129, data.getTableData()[2][2], 0.0001);
        assertEquals(1.424, data.getTableData()[3][2], 0.0001);
        assertEquals(1.832, data.getTableData()[4][1], 0.0001);
        assertEquals(2.273, data.getTableData()[5][0], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim6.ktn
     */
    @Test
    public void loadTest6() throws Exception {
        Data data = Loading.load("src/test/resources/sim6.ktn");

        //checking first row values.
        assertEquals(5, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.InhibitedUnCompetitive, data.getModelType());

        //Checking second row values.
        assertEquals(0.0, data.getCoSubInhibRow().get(0), 0.0001);
        assertEquals(1.43999, data.getCoSubInhibRow().get(1), 0.0001);
        assertEquals(2.87999, data.getCoSubInhibRow().get(2), 0.0001);
        assertEquals(4.32, data.getCoSubInhibRow().get(3), 0.0001);
        assertEquals(5.75999, data.getCoSubInhibRow().get(4), 0.0001);

        //Checking substrate column
        assertEquals(1.0855, data.getSubstrateCol().get(0), 0.0001);
        assertEquals(3.0415, data.getSubstrateCol().get(1), 0.0001);
        assertEquals(4.9975, data.getSubstrateCol().get(2), 0.0001);
        assertEquals(6.9535, data.getSubstrateCol().get(3), 0.0001);
        assertEquals(8.9095, data.getSubstrateCol().get(4), 0.0001);
        assertEquals(10.8655, data.getSubstrateCol().get(5), 0.0001);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(0.93528, data.getTableData()[0][0], 0.0001);
        assertEquals(1.8551, data.getTableData()[1][1], 0.0001);
        assertEquals(1.88567, data.getTableData()[2][2], 0.0001);
        assertEquals(1.6094, data.getTableData()[3][3], 0.0001);
        assertEquals(1.51679, data.getTableData()[4][4], 0.0001);
        assertEquals(1.52109, data.getTableData()[5][4], 0.0001);
    }

    /**
     * Testing Loading class,
     * Invalid file notAFile.ktn.
     * The exception checking will work as long as the junit version doesn't change.
     */
    @Test(expected = NoSuchFileException.class)
    public void loadTest7() throws Exception {

        //initialise data to check failed load doesn't corrupt data.
        Data data = Loading.load("src/test/resources/sim6.ktn");
        assertEquals(KineticModel.InhibitedUnCompetitive, data.getModelType());

        data = Loading.load("src/test/resources/notAFile.ktn");

        //checking first row values.
        assertEquals(5, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.InhibitedUnCompetitive, data.getModelType());

        //Checking second row values.
        assertEquals(0.0, data.getCoSubInhibRow().get(0), 0.0001);
        assertEquals(1.43999, data.getCoSubInhibRow().get(1), 0.0001);
        assertEquals(2.87999, data.getCoSubInhibRow().get(2), 0.0001);
        assertEquals(4.32, data.getCoSubInhibRow().get(3), 0.0001);
        assertEquals(5.75999, data.getCoSubInhibRow().get(4), 0.0001);

        //Checking substrate column
        assertEquals(1.0855, data.getSubstrateCol().get(0), 0.0001);
        assertEquals(3.0415, data.getSubstrateCol().get(1), 0.0001);
        assertEquals(4.9975, data.getSubstrateCol().get(2), 0.0001);
        assertEquals(6.9535, data.getSubstrateCol().get(3), 0.0001);
        assertEquals(8.9095, data.getSubstrateCol().get(4), 0.0001);
        assertEquals(10.8655, data.getSubstrateCol().get(5), 0.0001);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(0.93528, data.getTableData()[0][0], 0.0001);
        assertEquals(1.8551, data.getTableData()[1][1], 0.0001);
        assertEquals(1.88567, data.getTableData()[2][2], 0.0001);
        assertEquals(1.6094, data.getTableData()[3][3], 0.0001);
        assertEquals(1.51679, data.getTableData()[4][4], 0.0001);
        assertEquals(1.52109, data.getTableData()[5][4], 0.0001);
    }
    
    /**
     * Testing the Saving class of sim1.csv
     * by saving the data and then loading it back in
     * to make sure it is the same as the original
     * @throws Exception
     */
    @Test
    public void saveCSVTest1() throws Exception {
    	Data data = Loading.load("src/test/resources/csvFiles/sim1csv.csv");
    	Saving.save("src/test/resources/csvFiles/sim1savecsv.csv", data);
    	Data data1 = Loading.load("src/test/resources/csvFiles/sim1savecsv.csv");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveCsvTest2() throws Exception {
    	Data data = Loading.load("src/test/resources/csvFiles/sim2csv.csv");
    	Saving.save("src/test/resources/csvFiles/sim2savecsv.csv", data);
    	Data data1 = Loading.load("src/test/resources/csvFiles/sim2savecsv.csv");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveCsvTest3() throws Exception {
    	Data data = Loading.load("src/test/resources/csvFiles/sim3csv.csv");
    	Saving.save("src/test/resources/csvFiles/sim3savecsv.csv", data);
    	Data data1 = Loading.load("src/test/resources/csvFiles/sim3savecsv.csv");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveCsvTest4() throws Exception {
    	Data data = Loading.load("src/test/resources/csvFiles/sim4csv.csv");
    	Saving.save("src/test/resources/csvFiles/sim4savecsv.csv", data);
    	Data data1 = Loading.load("src/test/resources/csvFiles/sim4savecsv.csv");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveCsvTest5() throws Exception {
    	Data data = Loading.load("src/test/resources/csvFiles/sim5csv.csv");
    	Saving.save("src/test/resources/csvFiles/sim5savecsv.csv", data);
    	Data data1 = Loading.load("src/test/resources/csvFiles/sim5savecsv.csv");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveCsvTest6() throws Exception {
    	Data data = Loading.load("src/test/resources/csvFiles/sim6csv.csv");
    	Saving.save("src/test/resources/csvFiles/sim6savecsv.csv", data);
    	Data data1 = Loading.load("src/test/resources/csvFiles/sim6savecsv.csv");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveCsvTest7() throws Exception {
    	Data data = Loading.load("src/test/resources/csvFiles/sim5csv.csv");
    	List<Double> cosub = data.getCoSubInhibRow();
    	cosub.set(0, Double.NaN);
    	cosub.set(1, Double.NaN);
    	List<Double> subs = data.getSubstrateCol();
    	subs.set(0, Double.NaN);
    	double[][] tableD = data.getTableData();
    	tableD[0][0] = Double.NaN;
    	Saving.save("src/test/resources/csvFiles/sim7savecsv.csv", data);
    	Data data1 = Loading.load("src/test/resources/csvFiles/sim7savecsv.csv");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			System.out.println(table1[row][col]);
    			System.out.println(table2[row][col]);
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }

    /**
     * Testing Loading class,
     * valid file sim1csv.csv
     */
    @Test
    public void loadCSVTest1() throws Exception {
        Data data = Loading.load("src/test/resources/csvFiles/sim1csv.csv");

        //checking first row values.
        assertEquals(1, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.UninhibitedOneSub, data.getModelType());

        //Checking second row value.
        assertEquals(0, data.getCoSubInhibRow().get(0), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values
        assertEquals(0.384260327717517, data.getTableData()[0][0], 0.0001);
        assertEquals(0.945672004865194, data.getTableData()[1][0], 0.0001);
        assertEquals(1.38532441713356, data.getTableData()[2][0], 0.0001);
        assertEquals(1.73895320397848, data.getTableData()[3][0], 0.0001);
        assertEquals(2.02955591860983, data.getTableData()[4][0], 0.0001);
        assertEquals(2.27260330015001, data.getTableData()[5][0], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim2csv.csv
     */
    @Test
    public void loadCSVTest2() throws Exception {
        Data data = Loading.load("src/test/resources/csvFiles/sim2csv.csv");

        //checking first row values.
        assertEquals(5, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.UninhibitedTwoSubOrderedBiBi, data.getModelType());

        //Checking second row values.
        assertEquals(0.333, data.getCoSubInhibRow().get(0), 0.0);
        assertEquals(0.583, data.getCoSubInhibRow().get(1), 0.0);
        assertEquals(0.833, data.getCoSubInhibRow().get(2), 0.0);
        assertEquals(1.083, data.getCoSubInhibRow().get(3), 0.0);
        assertEquals(1.333, data.getCoSubInhibRow().get(4), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(6.83749648071394E-02, data.getTableData()[0][0], 0.0001);
        assertEquals(0.253184736945537, data.getTableData()[1][1], 0.0001);
        assertEquals(0.466906774111482, data.getTableData()[2][2], 0.0001);
        assertEquals(0.683118544971419, data.getTableData()[3][3], 0.0001);
        assertEquals(0.8911191081845, data.getTableData()[4][4], 0.0001);
        assertEquals(0.989909312416277, data.getTableData()[5][4], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim3csv.csv
     */
    @Test
    public void loadCSVTest3() throws Exception {
        Data data = Loading.load("src/test/resources/csvFiles/sim3csv.csv");

        //checking first row values.
        assertEquals(5, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.UninhibitedTwoSubPingPong, data.getModelType());

        //Checking second row values.
        assertEquals(0.333, data.getCoSubInhibRow().get(0), 0.0);
        assertEquals(0.583, data.getCoSubInhibRow().get(1), 0.0);
        assertEquals(0.833, data.getCoSubInhibRow().get(2), 0.0);
        assertEquals(1.083, data.getCoSubInhibRow().get(3), 0.0);
        assertEquals(1.333, data.getCoSubInhibRow().get(4), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(0.262908574135481, data.getTableData()[0][0], 0.0001);
        assertEquals(0.573540697170503, data.getTableData()[1][1], 0.0001);
        assertEquals(0.831915850302847, data.getTableData()[2][2], 0.0001);
        assertEquals(1.05886997653749, data.getTableData()[3][3], 0.0001);
        assertEquals(1.26136228368927, data.getTableData()[4][4], 0.0001);
        assertEquals(1.35117054801599, data.getTableData()[5][4], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim4csv.csv
     */
    @Test
    public void loadCSVTest4() throws Exception {
        Data data = Loading.load("src/test/resources/csvFiles/sim4csv.csv");

        //checking first row values.
        assertEquals(5, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.InhibitedCompetitive, data.getModelType());

        //Checking second row values.
        assertEquals(0.0, data.getCoSubInhibRow().get(0), 0.0);
        assertEquals(0.25, data.getCoSubInhibRow().get(1), 0.0);
        assertEquals(0.5, data.getCoSubInhibRow().get(2), 0.0);
        assertEquals(0.75, data.getCoSubInhibRow().get(3), 0.0);
        assertEquals(1.0, data.getCoSubInhibRow().get(4), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(0.384260327717517, data.getTableData()[0][0], 0.0001);
        assertEquals(0.858641634456102, data.getTableData()[1][1], 0.0001);
        assertEquals(1.17327414664013, data.getTableData()[2][2], 0.0001);
        assertEquals(1.39722258613913, data.getTableData()[3][3], 0.0001);
        assertEquals(1.56475437993817, data.getTableData()[4][4], 0.0001);
        assertEquals(1.78559948569592, data.getTableData()[5][4], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim5csv.csv
     */
    @Test
    public void loadCSVTest5() throws Exception {
        Data data = Loading.load("src/test/resources/csvFiles/sim5csv.csv");

        //checking first row values.
        assertEquals(3, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.InhibitedNonCompetitive, data.getModelType());

        //Checking second row values.
        assertEquals(0.0, data.getCoSubInhibRow().get(0), 0.0);
        assertEquals(0.25, data.getCoSubInhibRow().get(1), 0.0);
        assertEquals(0.5, data.getCoSubInhibRow().get(2), 0.0);

        //Checking substrate column
        assertEquals(0.333, data.getSubstrateCol().get(0), 0.0);
        assertEquals(0.933, data.getSubstrateCol().get(1), 0.0);
        assertEquals(1.533, data.getSubstrateCol().get(2), 0.0);
        assertEquals(2.133, data.getSubstrateCol().get(3), 0.0);
        assertEquals(2.733, data.getSubstrateCol().get(4), 0.0);
        assertEquals(3.333, data.getSubstrateCol().get(5), 0.0);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(0.384, data.getTableData()[0][0], 0.0001);
        assertEquals(0.847, data.getTableData()[1][1], 0.0001);
        assertEquals(1.129, data.getTableData()[2][2], 0.0001);
        assertEquals(1.424, data.getTableData()[3][2], 0.0001);
        assertEquals(1.832, data.getTableData()[4][1], 0.0001);
        assertEquals(2.273, data.getTableData()[5][0], 0.0001);
    }

    /**
     * Testing Loading class,
     * valid file sim6csv.csv
     */
    @Test
    public void loadCSVTest6() throws Exception {
        Data data = Loading.load("src/test/resources/csvFiles/sim6csv.csv");

        //checking first row values.
        assertEquals(5, data.getCols());
        assertEquals(6, data.getRows());
        assertEquals(KineticModel.InhibitedUnCompetitive, data.getModelType());

        //Checking second row values.
        assertEquals(0.0, data.getCoSubInhibRow().get(0), 0.0001);
        assertEquals(1.43999, data.getCoSubInhibRow().get(1), 0.0001);
        assertEquals(2.87999, data.getCoSubInhibRow().get(2), 0.0001);
        assertEquals(4.32, data.getCoSubInhibRow().get(3), 0.0001);
        assertEquals(5.75999, data.getCoSubInhibRow().get(4), 0.0001);

        //Checking substrate column
        assertEquals(1.0855, data.getSubstrateCol().get(0), 0.0001);
        assertEquals(3.0415, data.getSubstrateCol().get(1), 0.0001);
        assertEquals(4.9975, data.getSubstrateCol().get(2), 0.0001);
        assertEquals(6.9535, data.getSubstrateCol().get(3), 0.0001);
        assertEquals(8.9095, data.getSubstrateCol().get(4), 0.0001);
        assertEquals(10.8655, data.getSubstrateCol().get(5), 0.0001);

        //Checking data values (left to right diagonal)
        //The delta accounts for rounding to 3dp
        assertEquals(0.93528, data.getTableData()[0][0], 0.0001);
        assertEquals(1.8551, data.getTableData()[1][1], 0.0001);
        assertEquals(1.88567, data.getTableData()[2][2], 0.0001);
        assertEquals(1.6094, data.getTableData()[3][3], 0.0001);
        assertEquals(1.51679, data.getTableData()[4][4], 0.0001);
        assertEquals(1.52109, data.getTableData()[5][4], 0.0001);
    }

    /**
     * Testing the Saving class of sim1.ktn
     * by saving the data and then loading it back in
     * to make sure it is the same as the original
     * @throws Exception
     */
    @Test
    public void saveTest1() throws Exception {
    	Data data = Loading.load("src/test/resources/sim1.ktn");
    	Saving.save("src/test/resources/sim1save.ktn", data);
    	Data data1 = Loading.load("src/test/resources/sim1save.ktn");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveTest2() throws Exception {
    	Data data = Loading.load("src/test/resources/sim2.ktn");
    	Saving.save("src/test/resources/sim2save.ktn", data);
    	Data data1 = Loading.load("src/test/resources/sim2save.ktn");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveTest3() throws Exception {
    	Data data = Loading.load("src/test/resources/sim3.ktn");
    	Saving.save("src/test/resources/sim3save.ktn", data);
    	Data data1 = Loading.load("src/test/resources/sim3save.ktn");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveTest4() throws Exception {
    	Data data = Loading.load("src/test/resources/sim4.ktn");
    	Saving.save("src/test/resources/sim4save.ktn", data);
    	Data data1 = Loading.load("src/test/resources/sim4save.ktn");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveTest5() throws Exception {
    	Data data = Loading.load("src/test/resources/sim5.ktn");
    	Saving.save("src/test/resources/sim5save.ktn", data);
    	Data data1 = Loading.load("src/test/resources/sim5save.ktn");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveTest6() throws Exception {
    	Data data = Loading.load("src/test/resources/sim6.ktn");
    	Saving.save("src/test/resources/sim6save.ktn", data);
    	Data data1 = Loading.load("src/test/resources/sim6save.ktn");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveTest7() throws Exception {
    	Data data = Loading.load("src/test/resources/sim5.ktn");
    	List<Double> cosub = data.getCoSubInhibRow();
    	cosub.set(0, Double.NaN);
    	cosub.set(1, Double.NaN);
    	List<Double> subs = data.getSubstrateCol();
    	subs.set(0, Double.NaN);
    	double[][] tableD = data.getTableData();
    	tableD[0][0] = Double.NaN;
    	Saving.save("src/test/resources/sim7save.ktn", data);
    	Data data1 = Loading.load("src/test/resources/sim7save.ktn");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveTest8() throws Exception {
    	Data data = Loading.load("src/test/resources/sim5.ktn");
    	List<Double> cosub = data.getCoSubInhibRow();
    	cosub.set(0, Double.NaN);
    	cosub.set(1, Double.NaN);
    	List<Double> subs = data.getSubstrateCol();
    	subs.set(0, Double.NaN);
    	double[][] tableD = data.getTableData();
    	tableD[0][0] = Double.NaN;
    	tableD[0][data.getCols()-1] = Double.NaN;
    	tableD[data.getRows()-1][data.getCols()-1] = Double.NaN;
    	Saving.save("src/test/resources/sim8saveend.ktn", data);
    	Data data1 = Loading.load("src/test/resources/sim8saveend.ktn");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
    @Test
    public void saveCsvTest8() throws Exception {
    	Data data = Loading.load("src/test/resources/csvFiles/sim5csv.csv");
    	List<Double> cosub = data.getCoSubInhibRow();
    	cosub.set(0, Double.NaN);
    	cosub.set(1, Double.NaN);
    	List<Double> subs = data.getSubstrateCol();
    	subs.set(0, Double.NaN);
    	double[][] tableD = data.getTableData();
    	tableD[0][0] = Double.NaN;
    	tableD[0][data.getCols()-1] = Double.NaN;
    	tableD[data.getRows()-1][data.getCols()-1] = Double.NaN;
    	Saving.save("src/test/resources/csvFiles/sim8saveendcsv.csv", data);
    	Data data1 = Loading.load("src/test/resources/csvFiles/sim8saveendcsv.csv");
    	
    	//Checks the number of is correct and model is correct
    	assertEquals(data.getRows(), data1.getRows());
    	assertEquals(data.getCols(), data1.getCols());
    	assertEquals(data.getModelType(), data1.getModelType());
    	
    	//Checks the cosub row is the same
    	List<Double> extra1 = data.getCoSubInhibRow();
    	List<Double> extra2 = data1.getCoSubInhibRow();
    	for(int i = 0; i<data.getCols(); i++) {
    		assertEquals(extra1.get(i), extra2.get(i));
    	}
    	
    	//Checks the substrate column
    	List<Double> substrate1 = data.getSubstrateCol();
    	List<Double> substrate2 = data1.getSubstrateCol();
    	for(int i = 0; i<data.getRows(); i++) {
    		assertEquals(substrate1.get(i), substrate2.get(i));
    	}
    	
    	//Checks the table
    	double[][] table1 = data.getTableData();
    	double[][] table2 = data1.getTableData();
    	for(int row = 0; row<data.getRows(); row++) {
    		for(int col = 0; col<data.getCols(); col++) {
    			assertEquals((Object)table1[row][col], (Object)table2[row][col]);
    		}
    	}
    }
    
}
