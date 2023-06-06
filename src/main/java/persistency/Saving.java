package persistency;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import domain.Data;

/**
 * This class is responsible for saving data objects to a file.
 *
 */
public abstract class Saving {
	
	/**
	 * Checks the end of the filepath and then calls the
	 * method which corresponds to the correct saving
	 * method
	 * 
	 * @param filePath - the place and name of the file 
	 * @param data - the object to be saved to the file
	 */
	public static void save(String filePath, Data data) {
		if(filePath.endsWith(".csv")) {
			saveCsv(filePath, data);
		}
		else if (filePath.endsWith(".ktn")) {
			savektn(filePath, data);
		}
	}
	
	/**
	 * This takes the given filepath string, makes a file and
	 * then saves all of the information from the data object
	 * to the file
	 * 
	 * @param filePath - the place and name of the file
	 * @param data - the object to be saved to the file
	 */
	public static void savektn(String filePath, Data data) {
		File file = new File(filePath);
		try {
			//FileWriter writer = new FileWriter(file);
			PrintStream writer = new PrintStream(file);
			//First line
			//writer.write(" "+data.getCols()+"             "+data.getRows()+"             "+data.getModelType().getValue()+" "+'\n');
			writer.println(" "+data.getCols()+"             "+data.getRows()+"             "+data.getModelType().getValue()+" ");
			//Second line
			List<Double> cosubInhibList = data.getCoSubInhibRow();
			String rowS = "";
			for(int i = 0; i<cosubInhibList.size(); i++) {
				if(!Double.isNaN(cosubInhibList.get(i))) {
					rowS = rowS+cosubInhibList.get(i)+"   ";
				}
				else {
					rowS = rowS+"   ";
				}
			}
			//rowS = rowS+'\n';
			//writer.write(rowS);
			writer.println(rowS);
			//Main table data
			List<Double> substrate = data.getSubstrateCol();
			double[][] table = data.getTableData();
			for(int row = 0; row<data.getRows(); row++) {
				String s = "";
				if(!Double.isNaN(substrate.get(row))) {
					s = s+substrate.get(row)+"   ";
				}
				else {
					s = s+"   ";
				}
				for(int col = 0; col<data.getCols(); col++) {
					if(!Double.isNaN(table[row][col])) {
						s = s+table[row][col]+"   ";
					}
					else {
						s = s+"   ";
					}
				}
				//s=s+'\n';
				//writer.write(s);
				writer.println(s);
			}
			
			writer.close();
		} catch (IOException e) {
			System.out.println("Saving failed");
			e.printStackTrace();
		}
		
	}

	public static void saveCsv(String filePath, Data data) {
		String seperator = ",";
		File file = new File(filePath);
		try {
			PrintStream writer = new PrintStream(file);
			//First line
			writer.println(data.getCols()+seperator+data.getRows()+seperator+data.getModelType().getValue());
			//Second line
			List<Double> cosubInhibList = data.getCoSubInhibRow();
			String rowS = "";
			for(int i = 0; i<cosubInhibList.size(); i++) {
				if(!Double.isNaN(cosubInhibList.get(i))) {
					if(i==cosubInhibList.size()-1){
						rowS = rowS+cosubInhibList.get(i);
					}else{
						rowS = rowS+cosubInhibList.get(i) + seperator;
					}
				}
				else {
					rowS = rowS+seperator;
				}
			}
			//rowS = rowS+'\n';
			//writer.write(rowS);
			writer.println(rowS);
			//Main table data
			List<Double> substrate = data.getSubstrateCol();
			double[][] table = data.getTableData();
			for(int row = 0; row<data.getRows(); row++) {
				String s = "";
				if(!Double.isNaN(substrate.get(row))) {
					s = s+substrate.get(row)+seperator;
				}
				else {
					s = s+seperator;
				}
				for(int col = 0; col<data.getCols(); col++) {
					if(!Double.isNaN(table[row][col])) {
						s = s+table[row][col]+seperator;
					}
					else {
						s = s+seperator;
					}
				}
				writer.println(s);
			}

			writer.close();
		} catch (IOException e) {
			System.out.println("Saving failed");
			e.printStackTrace();
		}

	}

}
