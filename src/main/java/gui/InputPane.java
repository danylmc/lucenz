package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import domain.KineticModel;

/**
 * Input pane right side of GUI that contains the input table and various
 * program controls.
 */
@SuppressWarnings("serial")
public class InputPane extends JPanel {

	/** Table where user enters substrate concentrate data */
	private JTable substrateTable;
	/**
	 * Table where user enters co-substrate or inhibitor concentrates table may also
	 * be hidden
	 */
	private JTable extraRowTable;
	/** Table where user enters the reaction rate data */
	private JTable dataTable;

	/** Title for the substrate concentration table */
	private JLabel substrateLabel;
	/** Title of the main reaction rate table */
	private JLabel dataLabel;
	/** Title of the extra row table above table */
	private JLabel extraLabel;

	/** Go button that will do the calculations when pressed */
	private JButton goButton;

	/**Transpose button that will swap the rows and columns when pressed */
	private JButton transposeButton;

	/** Max width a column can have */
	private int maxColWidth = 48;

	// Panels for each object on screen
	/** Top of input pane with all buttons and sliders (Maybe split further) */
	private ModelSelection modelControlPanel;
	/** Panel containing slider and GO button */
	private JPanel sliderControlPanel;
	/** Top left corner of table area that does not contain any table */
	private JPanel tableCornerPanel;
	/** Contains single column table for co-substrate concentration */
	private JPanel extraRowPanel;
	/** Contains single optional single row table on top of main table */
	private JPanel substratePanel;
	/** Contains main body of the table for reaction rate data */
	private JPanel dataTablePanel;
	/** Bottom of input pane, below table, containing entry for substrate name */
	private JPanel bottomPanel;

	/** Listener for setting various update events to DisplayGui */
	private InputEventListener listener;

	/** Slider for setting number of table rows */
	private JSlider rowNumSlider;
	/** Slider for setting number of table columns */
	private JSlider colNumSlider;
	/** Text label to the extra row slider, text in title can and will change. */
	private JLabel extraSliderLabel;
	/** The text field entry form the substrate name. */
	private JTextField substrateNameFeild;

	/**
	 * Setup a new input pane.
	 * 
	 * @param inputEventListener
	 */
	InputPane(InputEventListener inputEventListener) {
		listener = inputEventListener;

		setupPanels();

		// this.substrateTable = new JTable(subsConcData, new Object[] { "[S]" });
		this.extraRowTable = new JTable(1, 10);
		extraRowTable.setModel(new InputTableModel(1, 2, 1, 10));
		// extraRowTable.setVisible(false);

		this.substrateTable = new JTable(10, 1);
		substrateTable.setModel(new InputTableModel(2, 1, 10, 1));
		// this.dataTable = new JTable(reactRateData, new Object[] { "" });

		this.dataTable = new JTable(10, 10);
		dataTable.setModel(new InputTableModel(2, 2, 10, 10));

		// Add substrate table to its panels
		JScrollPane subScroll = new JScrollPane(substrateTable);
		subScroll.setBorder(BorderFactory.createEmptyBorder());
		substrateTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		substrateTable.setTableHeader(null); // Remove header
		substratePanel.add(subScroll);
		
		// Add data table to its panels
		JScrollPane dataScroll = new JScrollPane(dataTable);
		dataScroll.setBorder(BorderFactory.createEmptyBorder());
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		dataTable.setTableHeader(null); // Remove header
		dataTablePanel.add(dataScroll, BorderLayout.CENTER);
		
		// Add extra table to its panels
		JScrollPane extraScroll = new JScrollPane(extraRowTable);
		extraScroll.setBorder(BorderFactory.createEmptyBorder());
		// Make extra table scroll horizontally with the data table
		extraScroll.getHorizontalScrollBar().setModel(dataScroll.getHorizontalScrollBar().getModel());
		extraScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		extraRowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		extraRowTable.setTableHeader(null); // Remove header
		extraRowPanel.add(extraScroll, BorderLayout.CENTER);

		// Add table titles
		substrateLabel = new JLabel("[S]");
		substratePanel.add(substrateLabel, BorderLayout.PAGE_START);
		dataLabel = new JLabel("Reactions Rates");
		dataTablePanel.add(dataLabel, BorderLayout.PAGE_START);
		extraLabel = new JLabel();
		extraRowPanel.add(extraLabel, BorderLayout.PAGE_START);
		
		// Set colour between cells, only effects Mac version
		dataTable.setGridColor(Color.GRAY);
		substrateTable.setGridColor(Color.GRAY);
		extraRowTable.setGridColor(Color.GRAY);

		// Set table to use custom renderers
		substrateTable.setDefaultRenderer(Object.class, new CellRenderer());
		dataTable.setDefaultRenderer(Object.class, new CellRenderer());
		extraRowTable.setDefaultRenderer(Object.class, new CellRenderer());

		// Setup a listener that prints does an even every time a number is updated
		substrateTable.getModel().addTableModelListener(new NumberTableModelListener());
		extraRowTable.getModel().addTableModelListener(new NumberTableModelListener());
		dataTable.getModel().addTableModelListener(new NumberTableModelListener());
		
		// Remove cell selection as it's not needed
		dataTable.setCellSelectionEnabled(false);
		substrateTable.setCellSelectionEnabled(false);
		extraRowTable.setCellSelectionEnabled(false);

		// Set max column width
		dataTable.getColumnModel().getColumns().asIterator().forEachRemaining((col) -> col.setMaxWidth(maxColWidth));
		extraRowTable.getColumnModel().getColumns().asIterator()
				.forEachRemaining((col) -> col.setMaxWidth(maxColWidth));
		substrateTable.getColumnModel().getColumns().asIterator()
				.forEachRemaining((col) -> col.setMaxWidth(maxColWidth));

		// Editing stops when user clicks off the table
		dataTable.putClientProperty("terminateEditOnFocusLost", true);
		extraRowTable.putClientProperty("terminateEditOnFocusLost", true);
		substrateTable.putClientProperty("terminateEditOnFocusLost", true);
		
		addSliders();

		setupBottomPanel();
		
		// Make sure that everything in is Uninhibited by default.
		setModel(KineticModel.UninhibitedOneSub);
	}

	/**
	 * Sets up everything in the bottom panel, e.g. the substrate name.
	 */
	private void setupBottomPanel() {
		bottomPanel.add(new JLabel("Varied Substrate Name ="));

		// Box for player to enter the substrate name
		substrateNameFeild = new JTextField("S");
		substrateNameFeild.setPreferredSize(new Dimension(200, 25));
		bottomPanel.add(substrateNameFeild);

		substrateNameFeild.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				substrateLabel.setText("[" + substrateNameFeild.getText() + "]");
				listener.onInputEvent(InputEvent.SUBSTRATE_NAME_CHANGED);

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				substrateLabel.setText("[" + substrateNameFeild.getText() + "]");
				listener.onInputEvent(InputEvent.SUBSTRATE_NAME_CHANGED);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				substrateLabel.setText("[" + substrateNameFeild.getText() + "]");
				listener.onInputEvent(InputEvent.SUBSTRATE_NAME_CHANGED);
			}

		});

		//Makes the transpose button
		this.transposeButton = new  JButton("Transpose");
		transposeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.onInputEvent(InputEvent.TRANSPOSE_PRESSED);
			}
		});
		bottomPanel.add(transposeButton);
		transposeButton.setVisible(false);
	}

	/**
	 * Create and setup both the slider objects.
	 */
	private void addSliders() {
		// Slider number of table rows
		this.rowNumSlider = new JSlider(JSlider.HORIZONTAL, 2, 10, 2);
		rowNumSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int newValue = source.getValue();
					// Update data table
					InputTableModel model = (InputTableModel) dataTable.getModel();
					model.setRows(newValue);
					dataTable.repaint();

					// Update extra row table
					model = (InputTableModel) substrateTable.getModel();
					model.setRows(newValue);
					substrateTable.repaint();
				}
			}
		});

		// Turn on labels at major tick marks.
		rowNumSlider.setMajorTickSpacing(1);
		rowNumSlider.setPaintTicks(true);
		rowNumSlider.setPaintLabels(true);

		// Slider for table columns
		this.colNumSlider = new JSlider(JSlider.HORIZONTAL, 2, 10, 2);
		this.colNumSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {

					// Get new value of the slider
					int newValue = source.getValue();

					// Update data table
					InputTableModel model = (InputTableModel) dataTable.getModel();
					model.setCols(newValue);
					dataTable.repaint();

					// Update extra row table
					model = (InputTableModel) extraRowTable.getModel();
					model.setCols(newValue);
					extraRowTable.repaint();
				}
			}
		});

		// Turn on labels at major tick marks.
		colNumSlider.setMajorTickSpacing(1);
		colNumSlider.setPaintTicks(true);
		colNumSlider.setPaintLabels(true);

		goButton = new JButton("Go");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.onInputEvent(InputEvent.GO_PRESSED);
			}
		});

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		sliderControlPanel.setLayout(layout);

		// Make objects fill the space
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 11, 11, 11); // Padding

		// Label for row number slider
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel substrateSliderLabel = new JLabel("<html>No. of Substrate concentrations</html>");
		substrateSliderLabel.setHorizontalAlignment(JLabel.CENTER);
		sliderControlPanel.add(substrateSliderLabel, gbc);

		// Label for column number slider
		gbc.gridx = 1;
		gbc.gridy = 0;
		extraSliderLabel = new JLabel();
		extraSliderLabel.setHorizontalAlignment(JLabel.CENTER);
		sliderControlPanel.add(extraSliderLabel, gbc);

		// Reduce padding a little, stops component moving for some reason...
		gbc.insets = new Insets(0, 10, 10, 10);

		// Row number slider
		gbc.gridx = 0;
		gbc.gridy = 1;
		sliderControlPanel.add(rowNumSlider, gbc);

		// Column number slider
		gbc.gridx = 1;
		gbc.gridy = 1;
		sliderControlPanel.add(colNumSlider, gbc);

		// Go button to run model
		gbc.gridx = 2;
		gbc.gridy = 1;
		sliderControlPanel.add(goButton, gbc);

	}

	/**
	 * Set the InputPane to the correct settings for the given model.
	 * 
	 * @param newModel The model to setup to InputPane for
	 */
	private void setModel(KineticModel newModel) {
		// Extract table models so their column number can be changed
		InputTableModel modelE = (InputTableModel) this.extraRowTable.getModel();
		InputTableModel modelD = (InputTableModel) this.dataTable.getModel();

		// Hide and show objects based off selected model
		switch (newModel) {
		case UninhibitedOneSub:
			// Hide extra row related objects
			extraRowPanel.setVisible(false);
			colNumSlider.setVisible(false);
			extraSliderLabel.setForeground(getBackground()); // This is a cheat

			// User should only be able to edit first column
			modelD.setCols(1);
			dataTable.repaint();
			transposeButton.setVisible(false);
			break;

		case InhibitedCompetitive:
		case InhibitedNonCompetitive:
		case InhibitedUnCompetitive:
			// Show objects to inhibitor concentration table
			extraRowPanel.setVisible(true);
			colNumSlider.setVisible(true);
			extraSliderLabel.setForeground(Color.black);
			extraLabel.setText("Inhibitor concentration");
			extraSliderLabel.setText("<html>No. of Inhibitor concentrations</html>");

			// Set number of columns to the slider value
			modelE.setCols(colNumSlider.getValue());
			modelD.setCols(colNumSlider.getValue());
			dataTable.repaint();
			extraRowTable.repaint();
			transposeButton.setVisible(false);
			break;

		case UninhibitedTwoSubOrderedBiBi:
		case UninhibitedTwoSubPingPong:
			// Show objects for Co-substrate concentration table
			extraRowPanel.setVisible(true);
			colNumSlider.setVisible(true);
			extraSliderLabel.setForeground(Color.black);
			extraLabel.setText("Co-substrate concentration");
			extraSliderLabel.setText("<html>No. of Co-substrate concentrations</html");

			// Set number of columns to the slider value
			modelE.setCols(colNumSlider.getValue());
			modelD.setCols(colNumSlider.getValue());
			dataTable.repaint();
			extraRowTable.repaint();
			transposeButton.setVisible(true);
			break;
		}

		listener.onInputEvent(InputEvent.MODEL_CHANGED);

		modelControlPanel.setModel(newModel);
	}

	/**
	 * Sets the layout of the whole pane and define all the individual areas of it.
	 */
	private void setupPanels() {
		setLayout(new GridBagLayout());

		// The layout
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		// Model control panel at the top
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.gridwidth = 2;
		modelControlPanel = new ModelSelection(m -> setModel(m));
		add(modelControlPanel, gbc);

		// Rest of control control panel below model controls
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.weightx = 0;
		sliderControlPanel = new JPanel();
		add(sliderControlPanel, gbc);

		// Emtpy table corner
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		tableCornerPanel = new JPanel();
		// Stop table moving up if extra row is removed
		tableCornerPanel.setMinimumSize(new Dimension(this.maxColWidth + 20, 45));
		add(tableCornerPanel, gbc);

		// Co-substrate table
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weighty = 0;
		gbc.weightx = 0.6;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		extraRowPanel = new JPanel();
		extraRowPanel.setLayout(new BorderLayout());
		extraRowPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		add(extraRowPanel, gbc);

		// Substrate data
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.weighty = 0;
		gbc.weightx = 0;
		substratePanel = new JPanel();
		substratePanel.setLayout(new BorderLayout());
		substratePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(substratePanel, gbc);
		
		// Main data
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 0.9;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		dataTablePanel = new JPanel();
		dataTablePanel.setLayout(new BorderLayout());
		dataTablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		dataTablePanel.setMinimumSize(new Dimension(0, 213));
		add(dataTablePanel, gbc);

		// Space at bottom
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weighty = 0.5;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		bottomPanel = new JPanel();
		add(bottomPanel, gbc);
	}

	/**
	 * Updates table with the data passed to it to display
	 * 
	 * @param data - data which should be displayed in the table
	 */
	public void updateData(domain.Data data) {
		// Sets the number of rows for the substrate and data tables
		InputTableModel modelS = (InputTableModel) this.substrateTable.getModel();
		InputTableModel modelD = (InputTableModel) this.dataTable.getModel();
		modelS.setRows(data.getRows());
		modelD.setRows(data.getRows());
		// Updates the substrate column data
		List<Double> substrateCol = data.getSubstrateCol();
		for (int i = 0; i < data.getRows(); i++) {
			if (!Double.isNaN(substrateCol.get(i))) {
				modelS.setValueAt(substrateCol.get(i).toString(), i, 0);
			} else {
				modelS.setValueAt("", i, 0);
			}
		}
		// Updates the main table data
		modelD.setCols(data.getCols());
		double[][] array = data.getTableData();
		for (int i = 0; i < data.getRows(); i++) {
			for (int j = 0; j < data.getCols(); j++) {
				if (!Double.isNaN(array[i][j])) {
					modelD.setValueAt("" + array[i][j], i, j);
				} else {
					modelD.setValueAt("", i, j);
				}
			}
		}

		// Updates the extra table model
		InputTableModel modelE = (InputTableModel) this.extraRowTable.getModel();
		List<Double> extra = data.getCoSubInhibRow();
		if (extra.isEmpty() == true || extra == null || data.getCols() == 1) {
			// No values need to be displayed if the data only has one column
			modelE.setCols(0);
		} else {
			// Sets the number of columns and inputs the data
			modelE.setCols(data.getCols());
			for (int i = 0; i < data.getCols(); i++) {
				if (!Double.isNaN(extra.get(i))) {
					modelE.setValueAt(extra.get(i).toString(), 0, i);
				} else {
					modelE.setValueAt("", 0, i);
				}
			}
		}
		//Checks version and what the model is
		boolean v3 = this.modelControlPanel.getVersion();
		if(v3==false && (data.getModelType()==KineticModel.UninhibitedTwoSubOrderedBiBi ||data.getModelType()==KineticModel.UninhibitedTwoSubPingPong)) {
			modelE.setCols(0);
			this.setModel(KineticModel.UninhibitedOneSub);
			this.rowNumSlider.setValue(data.getRows());
		}
		else {
			this.colNumSlider.setValue(data.getCols());
			this.rowNumSlider.setValue(data.getRows());
			this.setModel(data.getModelType());
		}
	}

	/**
	 * Gets the current name of the substrate.
	 * 
	 * @return Substrates name
	 */
	public String getSubstrateName() {
		return substrateNameFeild.getText();
	}

	/**
	 * Returns data object from data entered into this table
	 * 
	 * @return data object
	 */
	public domain.Data getData() {
		domain.Data data = new domain.Data();
		// Sets number of rows and columns to the number of editable rows and
		// columns in the data table
		InputTableModel modelD = (InputTableModel) this.dataTable.getModel();
		int rows = modelD.getRows();
		int cols = modelD.getCols();
		data.setRows(rows);
		data.setCols(cols);
		// Takes the data from the table and reads into the 2d array which is added to
		// the data object
		double[][] dataA = new double[rows][cols];
		for (int i = 0; i < data.getRows(); i++) {
			for (int j = 0; j < data.getCols(); j++) {
				try {
					dataA[i][j] = Double.parseDouble((String) modelD.getValueAt(i, j));
				} catch (NumberFormatException | NullPointerException e) {
					dataA[i][j] = Double.NaN;
				}
			}
		}
		data.setTableData(dataA);
		// Takes the data from the substrate column and adds it to the data object
		InputTableModel modelS = (InputTableModel) this.substrateTable.getModel();
		List<Double> substrate = new ArrayList<Double>();

		for (int i = 0; i < data.getRows(); i++) {
			try {
				substrate.add(Double.parseDouble((String) modelS.getValueAt(i, 0)));
			} catch (NumberFormatException | NullPointerException e) {
				substrate.add(Double.NaN);
			}
		}
		data.setSubstrateCol(substrate);
		// Takes the data from the extra row and adds it to the data object
		InputTableModel modelE = (InputTableModel) this.extraRowTable.getModel();
		if (modelE.getCols() >= 2 && this.extraRowPanel.isVisible()) {
			List<Double> extraList = new ArrayList<Double>();
			for (int i = 0; i < data.getCols(); i++) {
				try {
					extraList.add(Double.parseDouble((String) modelE.getValueAt(0, i)));
				} catch (NumberFormatException | NullPointerException e) {
					extraList.add(Double.NaN);
				}
			}
			data.setCoSubInhibRow(extraList);
		}
		data.setModelType(this.modelControlPanel.readModel());
		return data;
	}
	
	/**
	 * Creates and returned the menu bar to be used as
	 * the input listener needs to be used
	 * @return the menu bar to be used
	 */
	public JMenuBar createMenuBar() {
		//Set up the menu
		JMenuBar bar = new JMenuBar();
		
		//Sets up the file menu
		JMenu fileMenu = new JMenu("File");
		JMenuItem clearItem = new JMenuItem("Clear");
		JMenuItem openItem = new JMenuItem("Open");
		JMenuItem saveItem = new JMenuItem("Save");
		clearItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.onInputEvent(InputEvent.CLEAR_PRESSED);	
			}
		});
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.onInputEvent(InputEvent.LOAD_PRESSED);	
			}
		});
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.onInputEvent(InputEvent.SAVE_PRESSED);	
			}
		});
		fileMenu.add(clearItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		bar.add(fileMenu);
		
		// Add the info menu
		JMenu infoMenu = new JMenu("Info");
		JMenuItem openAbout = new JMenuItem("About");
		openAbout.addActionListener(new ActionListener() {
			// When press open about pane
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, new AboutPane(), "About LUCENZ", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		JMenuItem openHelp = new JMenuItem("Help");
		openHelp.addActionListener(new ActionListener() {
			// When pressed open instruction pane
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, new InstructionPane(), "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});


		// Add the Version menu
		JMenu versionMenu = new JMenu("Version");
		JMenuItem level200 = new JMenuItem("200 Level");
		level200.addActionListener(new ActionListener() {
			// When pressed open 200 pane
			public void actionPerformed(ActionEvent e) {
				listener.onInputEvent(InputEvent.LUCENZ_2_SELECTED);
				//modelControlPanel.setVersion(false);
			}
		});
		JMenuItem level300 = new JMenuItem("300 Level");
		level300.addActionListener(new ActionListener() {
			// When pressed open 300 pane
			public void actionPerformed(ActionEvent e) {
				listener.onInputEvent(InputEvent.LUCENZ_3_SELECTED);
				//modelControlPanel.setVersion(true);
			}
		});

		infoMenu.add(openAbout);
		infoMenu.add(openHelp);

		//Makes submenu so help pdfs can be opened
		JMenu helpDocs = new JMenu("Documents");
		JMenuItem pdf2 = new JMenuItem("BIOL209W.pdf");
		pdf2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
				    try {
				    	Path path = Paths.get("Resources"+File.separator+"BIOL209W.pdf");
				        File myFile = path.toFile();
				        Desktop.getDesktop().open(myFile);
				    } catch (Exception ex) {
				        // no application registered for PDFs
				    }
				}
			}
		});
		JMenuItem pdf3 = new JMenuItem("BIOL309W.pdf");
		pdf3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
				    try {
				    	Path path = Paths.get("Resources"+File.separator+"BIOL309W.pdf");
				        File myFile = path.toFile();
				        Desktop.getDesktop().open(myFile);
				    } catch (Exception ex) {
				        // no application registered for PDFs
				    }
				}
			}
		});
		helpDocs.add(pdf2);
		helpDocs.add(pdf3);
		infoMenu.add(helpDocs);
		bar.add(infoMenu);
		versionMenu.add(level200);
		versionMenu.add(level300);
		bar.add(versionMenu);
		return bar;
	}
	
	/**
	 * Clears the data in all the tables
	 */
	public void clearTables() {
		((InputTableModel)this.dataTable.getModel()).clearData();
		((InputTableModel)this.substrateTable.getModel()).clearData();
		((InputTableModel)this.extraRowTable.getModel()).clearData();
	}

	/**
	 * Sets whether the transpose button was visible
	 * @param visible - is visible or not
	 */
	public void setTransposeVisible(boolean visible) {
		transposeButton.setVisible(visible);

	}

	/**
	 * Transposes the current data. So swaps the rows and columns.
	 */
	public void transposeData() {
		//GET DATA

		InputTableModel modelD = (InputTableModel) this.dataTable.getModel();
		int rows = modelD.getRows();
		int cols = modelD.getCols();
		int maxRows = modelD.getRowCount();
		int maxCols = modelD.getColumnCount();

		double[][] currentData = new double[maxRows][maxCols];
		for (int i = 0; i < maxRows; i++) {
			for (int j = 0; j < maxCols; j++) {
				try {
					currentData[i][j] = Double.parseDouble((String) modelD.getValueAt(i, j));
				} catch (NumberFormatException | NullPointerException e) {
					currentData[i][j] = Double.NaN;
				}
			}
		}

		InputTableModel modelS = (InputTableModel) this.substrateTable.getModel();
		List<Double> currentSubstrate = new ArrayList<Double>();

		for (int i = 0; i < maxRows; i++) {
			try {
				currentSubstrate.add(Double.parseDouble((String) modelS.getValueAt(i, 0)));
			} catch (NumberFormatException | NullPointerException e) {
				currentSubstrate.add(Double.NaN);
			}
		}

		InputTableModel modelE = (InputTableModel) this.extraRowTable.getModel();
		List<Double> extraList = new ArrayList<Double>();
		for (int i = 0; i < maxCols; i++) {
			try {
				extraList.add(Double.parseDouble((String) modelE.getValueAt(0, i)));
			} catch (NumberFormatException | NullPointerException e) {
				extraList.add(Double.NaN);
			}
		}

		//UPDATE

		// Sets the number of rows for the substrate and data tables
		modelS.setRows(cols);
		modelD.setRows(cols);
		// Updates the substrate column data
		List<Double> substrateCol = extraList;
		for (int i = 0; i < maxCols; i++) {
			if (!Double.isNaN(substrateCol.get(i))) {
				modelS.setValueAt(substrateCol.get(i).toString(), i, 0);
			} else {
				modelS.setValueAt("", i, 0);
			}
		}
		// Updates the main table data
		modelD.setCols(rows);
		double[][] array = currentData;
		for (int i = 0; i < maxRows; i++) {
			for (int j = 0; j < maxCols; j++) {
				if (!Double.isNaN(array[i][j])) {
					modelD.setValueAt("" + array[i][j], j, i);
				} else {
					modelD.setValueAt("", j, i);
				}
			}
		}

		// Updates the extra table model
		List<Double> extra = currentSubstrate;
		// Sets the number of columns and inputs the data
		modelE.setCols(rows);
		this.colNumSlider.setValue(rows);
		for (int i = 0; i < maxRows; i++) {
			if (!Double.isNaN(extra.get(i))) {
				modelE.setValueAt(extra.get(i).toString(), 0, i);
			} else {
				modelE.setValueAt("", 0, i);
			}
		}
		this.rowNumSlider.setValue(cols);
	}

	/**
	 * Sets the LUCENZ version for the modelControlPanel, updating the available models.
	 *
	 * @param lucenz3 true if LUCENZ3, false if LUCENZ2
	 */
	public void setVersion(boolean lucenz3){
		modelControlPanel.setVersion(lucenz3);
		this.updateData(getData());
	}

	/**
	 * Sets the current cells which should be highlighted
	 * @param row - the current rows
	 * @param col - the current cols
	 */
	public void highlightCells(List<Integer> row, List<Integer> col) {
		((InputTableModel)dataTable.getModel()).setClickedCells(row, col);
		dataTable.repaint();
	}
}
