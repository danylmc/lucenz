package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import gui.DisplayGui;
import gui.Gui;
import persistency.Loading;
import persistency.Saving;

/**
 * Is in charge of managing all the logic for the program. This managing entails
 * storing data about the program, like input data and graph type, then
 * processing it into graph data that is ready to be displayed. It also manages
 * the load/saving processes of turning data into files and vice versa.
 * 
 */
public class Main {

	/**
	 * Gui object that draws this program.
	 */
	private Gui gui;

	/**
	 * Object for holding data values from input panes and calculations.
	 */
	private Data data;

	/**
	 * Name of the substrate, default is S.
	 */
	private String substrateName;

	/**
	 * Runs on click of go method to calculate off data.
	 */
	public void go(){
		Calculations.fit(data);
		gui.displayOutputData(data);
	}


	/**
	 * Starts running the program by setting up Main and the GUI.
	 *
	 * @param args should be empty.
	 */
	public static void main(String[] args) {
		Main main = new Main();
		Gui gui = new DisplayGui(main);
		main.setGui(gui);
	}


	/**
	 * Loads data from a given file into the data object.
	 * Handles exceptions and prints error trace.
	 *
	 * @param fileName the name of the file to get the data from.
	 */
	public void loadData(String fileName) {
		try {
			data = Loading.load(fileName);
			gui.setInputData(data);
		} catch (Exception e) {
			System.out.println("Loading failed.");
			e.printStackTrace();
		}
	}

	/**
	 * Saves data to files.
	 *
	 * @param fileName the name of the file to save the data to.
	 */
	public void saveData(String fileName) {
		Saving.save(fileName, data);
	}

	/**
	 * Gets the GUI
	 *
	 * @return Gui
	 */
	public Gui getGui() {
		return gui;
	}

	/**
	 * Gets the Data object
	 *
	 * @return Data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * Gets the SubstrateName
	 *
	 * @return substrateName
	 */
	public String getSubstrateName() {
		return substrateName;
	}

	/**
	 * Sets the GUI
	 *
	 * @param gui Gui to be set as to be used by main.
	 */
	public void setGui(Gui gui) {
		this.gui = gui;
	}

	/**
	 * Sets the data object
	 *
	 * @param data to be set as to be used by main.
	 */
	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * Sets the substrate name
	 *
	 * @param substrateName to be set as to be used by main.
	 */
	public void setSubstrateName(String substrateName) {
		this.substrateName = substrateName;
	}
}
