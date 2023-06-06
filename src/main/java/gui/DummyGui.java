package gui;

import domain.Data;
import domain.Main;

/**
 * This is a DummyGui class that doesn't
 * make a GUI pop-up so it can be used for testing.
 *
 */
public class DummyGui implements Gui{
	/** Main object that this GUI is connected to. */
	private Main main;
	
	/**
	 * Constructor for a DummyGui
	 * This doesn't generate anything visible
	 * @param main
	 */
	public DummyGui(Main main) {
		this.main = main;
	}

	@Override
	public void setInputData(Data data) {
		//Doesn't need to do anything		
	}

	@Override
	public void displayOutputData(Data data) {
		//Doesn't need to do anything		
	}

}
