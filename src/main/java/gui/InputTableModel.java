package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * This is the Table Model used by the input table.
 * It allows the specific number of rows and columns to be entered
 * which indicate that cells within the range of rows and columns
 * should be able to be edited and cells outside of that shouldn't
 * be edited.
 *
 */
public class InputTableModel extends DefaultTableModel {

	/** Stores the number of rows that the user can currently enter data into	 */
	int rows;
	/** Stores the number of columns that the user can currently enter data into	*/
	int cols;
	
	/**
	 * The row of the point clicked in the graph.
	 * If there isn't one it is set to -1
	 */
	private List<Integer> clickedRow = new ArrayList<Integer>();
	
	
	/**
	 * The col of the point clicked in the graph.
	 * If there isn't one it is set to -1
	 */
	private List<Integer> clickedCol = new ArrayList<Integer>();
	
	/**
	 * Constructor for the input table model
	 * @param rows - the current number of rows where data can be entered in
	 * @param cols - the current number of columns where the data can be entered in
	 * @param maxRows - the maximum number of rows in the table
	 * @param maxCols - the maximum number of columns in the table
	 */
	public InputTableModel(int rows, int cols, int maxRows, int maxCols) {
		super(maxRows, maxCols);
		this.rows = rows;
		this.cols = cols;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		if(row<rows && col<cols) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the number of rows that can currently be edited
	 * @return the number of editable rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Sets the number of rows that can currently be edited
	 * @param rows - number of editable rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Gets the number of columns that can currently be edited
	 * @return the number of editable columns
	 */
	public int getCols() {
		return cols;
	}
	
	/**
	 * Sets the number of columns that can currently be edited
	 * @param cols - number of editable columnss
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}
	
	/**
	 * Clears the data in all rows and columns
	 */
	public void clearData() {
		for(int r = 0; r<this.getRowCount(); r++) {
			for(int c = 0; c<this.getColumnCount(); c++) {
				this.setValueAt("", r, c);
			}
		}
	}
	
	/**
	 * Checks to see if the current cell is the one that
	 * should be highlighted
	 * @param row - the row of the current cell
	 * @param col - the col of the current cell
	 * @return whether this cell should be highlighted
	 */
	public boolean isCellClicked(int row, int col) {
		for(int i = 0; i<clickedRow.size(); i++) {
			if(clickedRow.get(i)==row && clickedCol.get(i)==col) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the rows and columns for the cells that has
	 * had its point on the graph clicked
	 * @param row - the current rows of the clicked point
	 * @param col - the current cols of the clicked point
	 */
	public void setClickedCells(List<Integer> row, List<Integer> col) {
		clickedRow = row;
		clickedCol = col;
	}

}
