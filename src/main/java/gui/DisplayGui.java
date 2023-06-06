package gui;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import domain.Data;
import domain.GraphType;
import domain.KineticModel;
import domain.Main;
import domain.Point;

/**
 * In charge of the Graphical user interface. This class will contain all the
 * information about the graphical user interface we create. This means it
 * contains all the objects that form the graphical user interface, including
 * how they act, look and what state they are currently in. Thus, one DisplayGui
 * object will be created and used for each instance of the program running.
 */
public class DisplayGui implements Gui {
	/** Main object that this GUI is displaying. */
	private Main main;
	/** Frame that forms the window of GUI. */
	private JFrame frame;
	/** Pane on left side of window for table of input data and controls. */
	private InputPane inputPane;
	/** Pane on bottom right of window that draws the graph */
	private GraphOutputPane graphOutputPane;
	/** Pane on top right that print out text for graph (e.g. error values) */
	private TextOutputPane textOutputPane;
	/** Frame used for the startup popup. */
	private JFrame startupFrame;
	/** The current LUCENZ version of the program. */
	private boolean lucenz3 = false;

	/**
	 * Create a new GUI with a graphical display.
	 * 
	 * @param main The main object the GUI is to display.
	 */
	public DisplayGui(Main main) {
		this.main = main;
		initialise();
	}

	/**
	 * Setup all the components that make up this panel.
	 */
	private void initialise() {
		// Setup panes
		frame = new JFrame("LUCENZ Java Edition");
		inputPane = new InputPane(initInputPaneListener());
		graphOutputPane = new GraphOutputPane(initGraphOutputPaneListener());
		textOutputPane = new TextOutputPane();
		
		//Create and add menu bar to the frame
		frame.setJMenuBar(inputPane.createMenuBar());
		
		// Set the prefer width of of graphing area
		// The height is set to a unobtainable value as to not change anything
		graphOutputPane.setPreferredSize(new Dimension(500, 99999));
		
		// Full screen mode boys!
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(900, 600);

		// Layout objects
		frame.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		// Make the pane fill out the window
		gbc.fill = GridBagConstraints.BOTH;

		// Data input pane
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		frame.add(inputPane, gbc);

		// Text output pane
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.4;
		frame.add(textOutputPane, gbc);

		// Graph output pane
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.6;
		frame.add(graphOutputPane, gbc);

		// Decorative coloured stripe on left
		JPanel footerPanel = new JPanel();
		footerPanel.setBackground(Color.GREEN.darker().darker());
		footerPanel.add(Box.createHorizontalStrut(10));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		frame.add(footerPanel, gbc);

		// Make is shut when it's shut (might not be needed)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Show startup popup:
		startupFrame = new StartupFrame(initStartupFrameListener());
		startupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startupFrame.setVisible(true);
	}

	/**
	 * Creates an InputEventListener for handling input events from the InputPane.
	 *
	 * @return InputEventListener for InputPane
	 */
	private InputEventListener initInputPaneListener() {
		return event -> {
			switch (event) {
			case GO_PRESSED:
				// Get data from InputPane:
				Data inputData = inputPane.getData();
				if(inputData.isValid()) {
					if(main.getData()!= null) {
						inputData.setFilePath(main.getData().getFilePath());
					}
					graphOutputPane.updateAvailableGraphTypes(inputData.getModelType(), lucenz3); // only allow valid GraphTypes for this model
					inputData.setGraphType(graphOutputPane.getSelectedGraphType());
					main.setData(inputData);
					main.go();
					if(inputData.getModelType()==KineticModel.UninhibitedTwoSubOrderedBiBi || inputData.getModelType()==KineticModel.UninhibitedTwoSubPingPong) {
						inputPane.setTransposeVisible(true);
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, inputData.getErrorMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
				break;
			case CLEAR_PRESSED:
				int input = JOptionPane.showConfirmDialog(frame, "All data currently in the tables will be cleared."+'\n'+"If the data hasn't been saved this will be lost."+'\n'+"Do you wish to continue?", null, JOptionPane.OK_CANCEL_OPTION);
				if(input==0) {
					inputPane.clearTables();
					main.getData().setFilePath(null);
				}
				break;
			case LOAD_PRESSED:
				//Gets file
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("KTN and CSV", "ktn", "csv"));
				File file = new File(System.getProperty("user.dir"));
				String path = file.getPath();
				String newPath = path.substring(0, path.lastIndexOf(System.getProperty("file.separator")));
				fc.setCurrentDirectory(new File(newPath));
				fc.showOpenDialog(frame);
				try {
					String fileName = fc.getSelectedFile().getPath();
					inputPane.clearTables();
					main.loadData(fileName);
				}
				catch(NullPointerException e) {
					System.out.println("File name is invalid / No file was selected");
				}
				break;
			case SAVE_PRESSED:
				//Gets file
				JFileChooser f = new JFileChooser();
				f.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
				f.addChoosableFileFilter(new FileNameExtensionFilter("KTN", "ktn"));
				File file1 = new File(System.getProperty("user.dir"));
				String path1 = file1.getPath();
				String newPath1 = path1.substring(0, path1.lastIndexOf(System.getProperty("file.separator")));
				f.setCurrentDirectory(new File(newPath1));
				f.showSaveDialog(frame);
				try {
					FileNameExtensionFilter filter = (FileNameExtensionFilter) f.getFileFilter();
					String fileName = ((File)f.getSelectedFile()).getPath();
					String extension = "."+filter.getExtensions()[0];
					if(fileName.endsWith(extension)==false) {
						fileName = fileName+extension;
					}
					main.setData(inputPane.getData());
					main.saveData(fileName);
					main.getData().setFilePath(fileName);
				}
				catch(NullPointerException e) {
					System.out.println("File name is invalid / No file was selected");
				}
				break;
			case MODEL_CHANGED:
				// TODO: do event for when model changes
				break;
			case SUBSTRATE_NAME_CHANGED:
				graphOutputPane.updateSubstrateName(inputPane.getSubstrateName());
				break;
			case TRANSPOSE_PRESSED:
				inputPane.transposeData();
				break;
			case LUCENZ_2_SELECTED:
				setVersion(false);
				break;
			case LUCENZ_3_SELECTED:
				setVersion(true);
				break;
			default:
				System.out.println("Invalid input event from InputPane: " + event);
			}
		};
	}

	/**
	 * Creates an InputEventListener for handling input events from the
	 * GraphOutputPane.
	 *
	 * @return InputEventListener for GraphOutputPane
	 */
	private InputEventListener initGraphOutputPaneListener() {
		return event -> {
			switch (event) {
				case GRAPH_TYPE_CHANGED:
					Data data = main.getData();
					// Get GraphType from GraphOutputPane:
					GraphType selectedGraphType = graphOutputPane.getSelectedGraphType();
					data.setGraphType(selectedGraphType);
					main.go();
					break;
				case CLICKED_POINTS_CHANGED:
					List<Point> points = graphOutputPane.getClickedTablePositions();
					List<Integer> rowVals = new ArrayList<Integer>();
					List<Integer> colVals = new ArrayList<Integer>();
					if(points.size()!=0) {
						//Sets the current point to be highlighted
						for(Point p: points) {
							rowVals.add((int) p.getY());
							colVals.add((int) p.getX());
						}
						inputPane.highlightCells(rowVals, colVals);
					}
					else if(points.size()==0) {
						//Makes the cells not highlighted
						rowVals.add(-1);
						colVals.add(-1);
						inputPane.highlightCells(rowVals, colVals);
					}
					break;
				default:
					System.out.println("Invalid input event from GraphOutputPane: " + event);
			}
		};
	}

	/**
	 * Creates an InputEventListener for handling input events from the
	 * StartupFrame.
	 *
	 * @return InputEventListener for StartupFrame
	 */
	private InputEventListener initStartupFrameListener() {
		return event -> {
			switch (event){
				case LUCENZ_2_SELECTED:
					// Hides the startup popup and reveals the main program:
					startupFrame.setVisible(false);
					frame.setVisible(true);
					setVersion(false);
					break;
				case LUCENZ_3_SELECTED:
					// Hides the startup popup and reveals the main program:
					startupFrame.setVisible(false);
					frame.setVisible(true);
					setVersion(true);
					break;
				default:
					System.out.println("Invalid input event from StartupFrame: " + event);
			}
		};
	}

	/**
	 * Sets the LUCENZ version of the program, determining the available models and graphs.
	 *
	 * @param lucenz3 LUCENZ3 if true, LUCENZ2 if false
	 */
	private void setVersion(boolean lucenz3){
		this.lucenz3 = lucenz3;
		inputPane.setVersion(lucenz3);
	}

	@Override
	public void setInputData(Data data) {
		// Set the values on InputPane:
		inputPane.updateData(data);
	}

	@Override
	public void displayOutputData(Data data) {
		// Set the values on TextOutputPane and GraphOutputPane:
		textOutputPane.update(data);
		graphOutputPane.update(data);
	}
}
