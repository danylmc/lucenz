package domain;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The data class stores all the data points along with
 * all other information connected to the data.
 */
public class Data {

	/**
	 * Stores file path (if a file has been loaded)
	 */
	private String filepath;
	
	/**
	 * Stores the substrate concentration data
	 */
	private List<Double> substrateCol = new ArrayList<Double>();
	
	/**
	 * Stores the co-substrate/inhibitor concentration data
	 */
	private List<Double> coSubInhibRow = new ArrayList<Double>();
	
	/**
	 * Stores the data input by the user or file in the main table.
	 * This is set to initially have 2 rows and 1 column.
	 * The first [] is row and the second [] is column
	 */
	private double[][] tableData = new double[2][1];
	
	/**
	 * Stores the how the raw data would be displayed
	 * in a graph
	 */
	private List<List<Point>> graphPoints = new ArrayList<List<Point>>();
	
	/**
	 * Stores points for the line of best fit for the graph
	 */
	private List<List<Point>> graphLine = new ArrayList<List<Point>>();
	
	/**
	 * Stores the results of the required calculations
	 */
	private Map<String, Double> textData = new HashMap<String, Double>();
	
	/**
	 * Stores the kinetic model for the data
	 */
	private KineticModel modelType = KineticModel.UninhibitedOneSub;
	
	/**
	 * Stores the graph type. This is initially set to
	 * Velocity vs [S]
	 */
	private GraphType graphType = GraphType.VelocityVS;
	
	/**
	 * Stores the number of rows that can have data entered.
	 * This excludes the row for the inhibitor/co-substrate concentration.
	 * This is initially set to the minimum possible value of 2.
	 */
	private int rows = 2;
	
	/**
	 * Stores the number of columns that can have data entered.
	 * This excludes the columns for the substrate concentration.
	 * This is initially set to the minimum possible value of 2.
	 */
	private int cols = 1;
	
	/**
	 * Stores a string of all the current data errors.
	 */
	private String errorMessage = "";
	
	/**
	 * Default constructor
	 */
	public Data() {}
	
	/**
	 * Constructor that takes initial data with only one substrate
	 * @param substrateCol - concentrations of the substrates
	 * @param data - initial data to be used
	 */
	public Data(List<Double> substrateCol, double[][] data) {
		this.setSubstrateCol(substrateCol);
		this.tableData = data;
	}
	
	/**
	 * Constructor that takes initial data for substrate with co-substrate/inhibitor
	 * @param substrateCol - concentrations of the substrates
	 * @param cosubinhibRow - concentrations of the co-substrate/inhibitors
	 * @param data - initial data to be used
	 */
	public Data(List<Double> substrateCol, List<Double> cosubinhibRow, double[][] data) {
		this.setSubstrateCol(substrateCol);
		this.setCoSubInhibRow(cosubinhibRow);
		this.tableData = data;
	}

	/**
	 * Gets the list of substrate concentrations
	 * @return the list of substrate concentrations
	 */
	public List<Double> getSubstrateCol() {
		return substrateCol;
	}

	/**
	 * Sets the list of substrate concentrations
	 * @param substrateCol - the list of substrate concentrations
	 */
	public void setSubstrateCol(List<Double> substrateCol) {
		this.substrateCol = substrateCol;
	}

	/**
	 * Gets the list of co-substrate/inhibitor concentrations
	 * @return the co-substrate/inhibitor concentration row
	 */
	public List<Double> getCoSubInhibRow() {
		return coSubInhibRow;
	}

	/**
	 * Sets the list of co-substrate/inhibitor concentrations
	 * @param coSubInhibRow - the co-substrate/inhibitor concentrations
	 */
	public void setCoSubInhibRow(List<Double> coSubInhibRow) {
		this.coSubInhibRow = coSubInhibRow;
	}

	/**
	 * Gets the raw data from the data object
	 * @return the raw data
	 */
	public double[][] getTableData() {
		return tableData;
	}

	/**
	 * Sets the raw data for the data object
	 * @param rawData - data that user has provided
	 */
	public void setTableData(double[][] rawData) {
		this.tableData = rawData;
	}

	/**
	 * Gets the positions of the raw data in a graph
	 * @return the graph points of the raw data
	 */
	public List<List<Point>> getGraphPoints() {
		return graphPoints;
	}

	/**
	 * Sets the graph points for the raw data
	 * @param graphPoints
	 */
	public void setGraphPoints(List<List<Point>> graphPoints) {
		this.graphPoints = graphPoints;
	}

	/**
	 * Gets the points for the line of best fit for the graph
	 * @return points for line of best fit
	 */
	public List<List<Point>> getGraphLine() {
		return graphLine;
	}

	/**
	 * Sets the points for the line of best fit for the graph
	 * @param graphLine
	 */
	public void setGraphLine(List<List<Point>> graphLine) {
		this.graphLine = graphLine;
	}

	/**
	 * Gets the text information about the data
	 * @return the text data
	 */
	public Map<String, Double> getTextData() {
		return textData;
	}

	/**
	 * Sets the text data for the raw data
	 * @param textData
	 */
	public void setTextData(Map<String, Double> textData) {
		this.textData = textData;
	}

	/**
	 * Gets the current kinetic model for the data
	 * @return the current kinetic model
	 */
	public KineticModel getModelType() {
		return modelType;
	}

	/**
	 * Sets the model type for the data
	 * @param modelType - the new model type
	 */
	public void setModelType(KineticModel modelType) {
		this.modelType = modelType;
	}

	/**
	 * Gets the current graph type for the data
	 * @return the graph types
	 */
	public GraphType getGraphType() {
		return graphType;
	}

	/**
	 * Sets the current graph type for the data
	 * @param graphType
	 */
	public void setGraphType(GraphType graphType) {
		this.graphType = graphType;
	}

	/**
	 * Gets the current number of rows where data, excluding 
	 * the inhibitor/co-substrate concentration row, can
	 * be entered.
	 * @return the number of rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Sets the number of rows where data can be entered.
	 * This excludes the row for the inhibitor/co-substrate concentration.
	 * @param rows - the number of rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Gets the current number of columns where data, excluding 
	 * the substrate concentration column, can be entered
	 * @return the number of columns
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Sets the number of columns where data can be entered.
	 * This excludes the column for the substrate concentration.
	 * @param cols - the number of columns
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}
	
	/**
	 * Calculates the total number of cells based on the current
	 * number of rows and colums.
	 * This exculdes the number of substrate concentrations
	 * and the number of co-substrate/inhibitor concentrations.
	 * @return the total number of cells in the table
	 */
	public int getNumCells() {
		return this.rows*this.cols;
	}
	
	/**
	 * Gets the error message string
	 * @return current error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message string
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Sets the filepath string
	 * @param input
	 */
	public void setFilePath(String input){
		this.filepath = input;
	}

	/**
	 *
	 * @return
	 */
	public String getFilePath(){
		return filepath;
	}



	/**
	 * Checks all of the data to check that all the values are valid
	 * and that all the cells have values entered.
	 * @return whether all cells have valid values entered
	 */
	public boolean isValid() {
		errorMessage = "";
		boolean valid = true;
		//Checks that the substrate col doesn't have 0
		if(substrateCol.contains(0.0)) {
			valid = false;
			errorMessage = errorMessage+"A substrate concentration is zero."+'\n';
		}
		//Checks that the substrate col and co-sub inhib row are full
		if(substrateCol.contains(Double.NaN) || coSubInhibRow.contains(Double.NaN)) {
			valid = false;
			errorMessage = errorMessage+"There are blank cells."+'\n';
		}
		//Checks all the table data for an empty cell (NaN)
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				if(((Double)tableData[i][j]).isNaN()) {
					valid = false;
					if(!errorMessage.contains("There are blank cells.")) {
						errorMessage = errorMessage+"There are blank cells."+'\n';
					}
					break;
				}
			}
		}
		//Checks to see if the co-substrate is zero
		if(modelType==KineticModel.UninhibitedTwoSubOrderedBiBi || modelType==KineticModel.UninhibitedTwoSubPingPong) {
			if(this.coSubInhibRow.contains(0.0)) {
				valid = false;
				errorMessage = errorMessage+"A co-substrate concentration is zero."+'\n';
			}
		}
		return valid;
	}
}
