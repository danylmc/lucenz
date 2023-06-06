package gui;

import domain.Data;

/**
 * Gui is an interface representing any sort of graphical user interface the user could use to interact with
 * the program. It contains method headers for standardising the flow of information from Main.
 */
public interface Gui {

    /**
     * Programmatically sets the input data.
     * Used when loading a data file.
     *
     * @param data new input data
     */
    void setInputData(Data data);

    /**
     * Sets the output data to be displayed.
     * Used when the "GO" operation occurs, or after the GraphType has been changed.
     *
     * @param data output data
     */
    void displayOutputData(Data data);
}
