package gui;

import domain.Data;
import domain.GraphType;
import domain.KineticModel;
import domain.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * GraphOutputPane represents the pane on the bottom right of the window used for displaying graph data.
 */
public class GraphOutputPane extends JPanel {

	/** The full names of all the GraphTypes in order. */
	private static final String[] GRAPH_TYPES = {"Velocity vs [S]", "Lineweaver-Burke", "Hanes", "Eadie-Hofstee", "Dixon", "Hunter-Downs"};
	/** The colours to use for lines/points of different columns. */
	private static final Color[] GRAPH_COLORS = {
			Color.BLACK, Color.RED.darker(), Color.getHSBColor(0.11f, 1, 0.85f), Color.getHSBColor(0.167f, 1, 0.75f), Color.GREEN.darker(),
			Color.CYAN.darker(), Color.BLUE, Color.MAGENTA, Color.PINK.darker(), Color.GRAY
	};
	/** The fraction of how much horizontal space the graph should take on the panel. */
	private static final double GRAPH_WIDTH_SIZE_FACTOR = 0.75;
	/** The fraction of how much vertical space the graph should take on the panel. */
	private static final double GRAPH_HEIGHT_SIZE_FACTOR = 0.75;
	/** The maximum pixel distance a point can be from the mouse to be clicked. */
	private static final double MAX_CLICKED_POINT_DIST = 6.0;
	/** The maximum squared pixel distance a point can be from the mouse to be clicked. */
	private static final double MAX_CLICKED_POINT_DIST_SQUARED = Math.pow(MAX_CLICKED_POINT_DIST, 2);

	/** Event listener for input events. */
	private final InputEventListener listener;
	/** Panel for storing and handling menu options. */
	private final JPanel menuPanel;
	/** Panel for storing and handling graph text information. */
	private final JPanel infoPanel;
	/** Panel for drawing the graph output. */
	private final JPanel drawingPanel;

	/** The combo box for selecting a GraphType. */
	private JComboBox<String> graphTypesComboBox;
	/** The button for copying the graph to clipboard. */
	private JButton graphCopyButton;
	/** The text field showing the x value of the hovered position. */
	private JTextField xValueTextField;
	/** The text field showing the y value of the hovered position. */
	private JTextField yValueTextField;

	/** The currently selected GraphType. */
	private GraphType selectedGraphType = GraphType.VelocityVS;
	/** The current name of the substrate. */
	private String substrateName = "S";
	/** The measurement unit on the x axis for the selected GraphType. */
	private String xUnit = "[" + substrateName + "]";
	/** The measurement unit on the y axis for the selected GraphType. */
	private String yUnit = "V";

	/** Indicates if a graph is currently displayed. */
	private boolean graphDisplayed;
	/** The theoretical points (line of best fit points) from the latest update. */
	private List<List<Point>> lines;
	/** The experiment points (raw data points) from the latest update. */
	private List<List<Point>> points;
	/** The function for converting a point on the screen to a point within the data (model). */
	private Function<Point, Point> screenToDataPoint;
	/** A map from each current experiment point (in screen coordinates) to its position in the table. */
	private final Map<Point, Point> screenPointToTablePosition = new HashMap<>();
	/** The table positions of the experiment points that are currently clicked. */
	private final List<Point> clickedTablePositions = new ArrayList<>();


	/**
	 * Creates a new GraphOutputPane for displaying graph data.
	 *
	 * @param listener InputEventListener used to notify the GUI of any user input events
	 */
	public GraphOutputPane(InputEventListener listener) {
		this.setBackground(Color.LIGHT_GRAY);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.listener = listener;

		// Create the panels within this pane:
		this.menuPanel = createMenuPanel();
		this.infoPanel = createInfoPanel();
		this.drawingPanel = createDrawingPanel();

		// Add the panels to this pane:
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		this.add(menuPanel, c); // bar at top
		c.gridy = 1;
		this.add(infoPanel, c); // bar also at top but under the first
		c.gridy = 2;
		c.weighty = 1;
		this.add(drawingPanel, c); // big panel at bottom
	}

	/**
	 * Creates the menu panel with the combo box for selecting a GraphType, and a copy button for copying the
	 * graph to the user's clipboard.
	 *
	 * @return menu panel
	 */
	private JPanel createMenuPanel(){
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		JLabel graphTypesLabel = new JLabel("Select Graph Type:");
		this.graphTypesComboBox = new JComboBox<>(GRAPH_TYPES);
		graphTypesComboBox.setMaximumSize(graphTypesComboBox.getPreferredSize()); // stop it from expanding
		graphTypesComboBox.addActionListener(e -> {
			updateSelectedGraphType(GraphType.values()[graphTypesComboBox.getSelectedIndex()]);
			listener.onInputEvent(InputEvent.GRAPH_TYPE_CHANGED);
		});
		graphTypesComboBox.setEnabled(false);
		this.graphCopyButton = new JButton("Copy Graph");
		// Add copy button functionality:
		graphCopyButton.addActionListener(e -> {
			if (drawingPanel == null) return;

			// Create Image - code from https://gist.github.com/AdoPi/11032315:
			BufferedImage image = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = image.createGraphics();
			drawGraph(g2);

			// Copy Image to clipboard - code from https://alvinalexander.com/java/java-copy-image-to-clipboard-example/:
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new Transferable() {
				@Override
				public DataFlavor[] getTransferDataFlavors() {
					return new DataFlavor[]{ DataFlavor.imageFlavor };
				}

				@Override
				public boolean isDataFlavorSupported(DataFlavor flavor) {
					return DataFlavor.imageFlavor.equals(flavor);
				}

				@Override
				public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
					if (!DataFlavor.imageFlavor.equals(flavor)) {
						throw new UnsupportedFlavorException(flavor);
					}
					return image;
				}
			}, null);
		});
		graphCopyButton.setEnabled(false);

		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(graphTypesLabel);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(graphTypesComboBox);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(graphCopyButton);
		panel.add(Box.createHorizontalGlue());

		return panel;
	}

	/**
	 * Creates the info panel that displays the current value under the mouse, and the prompt telling you to click
	 * on a point to identify it.
	 *
	 * @return info panel
	 */
	private JPanel createInfoPanel(){
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		JLabel hoverValueLabel = new JLabel("Value Under Mouse:");
		this.xValueTextField = new JTextField("", 10);
		this.yValueTextField = new JTextField("", 10);
		xValueTextField.setEditable(false);
		yValueTextField.setEditable(false);
		xValueTextField.setMaximumSize(xValueTextField.getPreferredSize()); // stop it from expanding
		yValueTextField.setMaximumSize(yValueTextField.getPreferredSize()); // stop it from expanding
		JLabel clickPointLabel = new JLabel("Click on a point to identify it.");

		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(hoverValueLabel);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(xValueTextField);
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(yValueTextField);
		panel.add(Box.createRigidArea(new Dimension(10, 0)));
		panel.add(clickPointLabel);
		panel.add(Box.createHorizontalGlue());

		return panel;
	}


	/**
	 * Creates the drawing panel that displays the graph.
	 *
	 * @return drawing panel
	 */
	private JPanel createDrawingPanel(){
		JPanel panel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawGraph(g);
			}
		};
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		// Add mouse listener/adapter for updating the hovered location as the mouse moves,
		// and updating which points are clicked as it is pressed and released:
		MouseAdapter mouseAdapter = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				setClickedTablePositions(new Point(e.getX(), e.getY()));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				clearClickedTablePositions();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				setHoverValue(null);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				setHoverValue(new Point(e.getX(), e.getY()));
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
				setHoverValue(new Point(e.getX(), e.getY()));
			}
		};
		panel.addMouseListener(mouseAdapter);
		panel.addMouseMotionListener(mouseAdapter);
		return panel;
	}

	/**
	 * Gets the current selected GraphType.
	 *
	 * @return selected GraphType
	 */
	public GraphType getSelectedGraphType(){
		return this.selectedGraphType;
	}

	/**
	 * Gets the table positions of the currently clicked points.
	 * Returns an empty list if nothing is clicked.
	 *
	 * @return table positions of clicked points
	 */
	public List<Point> getClickedTablePositions(){
		return new ArrayList<>(clickedTablePositions);
	}

	/**
	 * Updates the current graph display.
	 *
	 * @param data the data object containing the lines and points to show
	 */
	public void update(Data data){
		this.lines = data.getGraphLine();
		this.points = data.getGraphPoints();
		drawingPanel.repaint();
	}
	
	/**
	 * Update the name of the substrate.
	 * 
	 * @param substrateName the new substrate name
	 */
	public void updateSubstrateName(String substrateName) {
		this.substrateName = substrateName;
		updateSelectedGraphType(this.selectedGraphType);
	}

	/**
	 * Updates the available GraphTypes in the and the selected GraphType depending on the current KineticModel.
	 * If Uninhibited, the first 4 should be available.
	 * If Inhibited, all 6 should be available. However, if LUCENZ2, then only 5 should be available.
	 *
	 * @param model the current KineticModel
	 * @param lucenz3 the current LUCENZ version
	 */
	public void updateAvailableGraphTypes(KineticModel model, boolean lucenz3) {
		// Show only valid GraphTypes in the dropdown menu for the current model:
		switch (model){
			case UninhibitedOneSub:
			case UninhibitedTwoSubPingPong:
			case UninhibitedTwoSubOrderedBiBi:
				// If the selected GraphType is invalid for the current model, reset it to the default:
				if (selectedGraphType.ordinal() >= 4) {
					updateSelectedGraphType(GraphType.VelocityVS);
					graphTypesComboBox.setSelectedIndex(selectedGraphType.ordinal());
				}
				while (graphTypesComboBox.getItemCount() > 4) {
					graphTypesComboBox.removeItemAt(graphTypesComboBox.getItemCount() - 1);
				}
				break;
			case InhibitedCompetitive:
			case InhibitedUnCompetitive:
			case InhibitedNonCompetitive:
				while (graphTypesComboBox.getItemCount() < 6) {
					graphTypesComboBox.addItem(GRAPH_TYPES[graphTypesComboBox.getItemCount()]);
				}
				if (!lucenz3) {
					// If switching from LUCENZ3 to LUCENZ2 and Hunter-Downs is selected, change the selected
					// graph type to Dixon:
					if (selectedGraphType.ordinal() >= 5){
						updateSelectedGraphType(GraphType.Dixon);
					}
					while (graphTypesComboBox.getItemCount() > 5) {
						graphTypesComboBox.removeItemAt(graphTypesComboBox.getItemCount() - 1);
					}
				}
				break;
			default:
				throw new IllegalArgumentException("Unrecognised kinetic model: " + model);
		}
	}

	/**
	 * Sets the selected GraphType and updates the units of each axis.
	 *
	 * @param type selected GraphType
	 */
	private void updateSelectedGraphType(GraphType type){
		this.selectedGraphType = type;
		switch (type) {
			case VelocityVS:
				xUnit = "[" + substrateName + "]";
				yUnit = "V";
				break;
			case LineweaverBurke:
				xUnit = "1/[" + substrateName + "]";
				yUnit = "1/V";
				break;
			case Hanes:
				xUnit = "[" + substrateName + "]";
				yUnit = "[" + substrateName + "]/V";
				break;
			case EadieHofstee:
				xUnit = "V/[" + substrateName + "]";
				yUnit = "V";
				break;
			case Dixon:
				xUnit = "[I]";
				yUnit = "1/V";
				break;
			case HunterDowns:
				xUnit = "[" + substrateName + "]";
				yUnit = "i.à/1-à";
				break;
			default:
				throw new IllegalArgumentException("Unrecognised graph type: " + type);
		}
	}

	/**
	 * Draws the current graph data onto the pane that owns the given graphics object.
	 *
	 * @param g graphics object
	 */
	private void drawGraph(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		screenToDataPoint = point -> null; // default if no graph is shown

		// Make sure graph data exists:
		if (lines == null || points == null) return;

		// Init useful variables:
		int numLines = lines.size();
		assert numLines == points.size();
		List<List<Point>> linesCopy = new ArrayList<>(lines);
		linesCopy.addAll(points);
		List<Point> allPoints = linesCopy.stream().flatMap(Collection::stream).collect(Collectors.toList());

		// Make sure there is at least one point to draw:
		if (allPoints.size() == 0) return;

		// Find highest and lowest values:
		double xMin = 0.0, yMin = 0.0, xMax = 0.0, yMax = 0.0;
		for (Point p : allPoints){
			if (p == null) continue;
			double x = p.getX(), y = p.getY();
			if (x > xMax) xMax = x;
			//else if (x < xMin) xMin = x;
			if (y > yMax) yMax = y;
			//else if (y < yMin) yMin = y;
		}

		// Round each axis upward so the axis labels contain nice rounded numbers:
		double xMaxRounded = Math.ceil(xMax);
		xMax = xMaxRounded;
		double yMaxRounded = Math.ceil(yMax);
		yMax = yMaxRounded;

		// Determine graph dimensions:
		double width = drawingPanel.getWidth();
		double height = drawingPanel.getHeight();
		double graphWidth = GRAPH_WIDTH_SIZE_FACTOR * width;
		double graphHeight = GRAPH_HEIGHT_SIZE_FACTOR * height;
		double graphLeft = (1.0 - GRAPH_WIDTH_SIZE_FACTOR) / 2.0 * width;
		double graphTop = (1.0 - GRAPH_HEIGHT_SIZE_FACTOR) / 2.0 * height;
		double graphRight = graphLeft + graphWidth;
		double graphBottom = graphTop + graphHeight;

		// Extend each axis to give space on either side, and determine scaling factors:
		double xRange = xMax - xMin;
		double yRange = yMax - yMin;
		double xRangeIncrease = xRange * 0.05;
		double yRangeIncrease = yRange * 0.05;
		xRange += xRangeIncrease;
		yRange += yRangeIncrease;
		xMax += xRangeIncrease / 2.0;
		yMax += yRangeIncrease / 2.0;
		xMin -= xRangeIncrease / 2.0;
		yMin -= yRangeIncrease / 2.0;
		double xScaleFactor = graphWidth / xRange;
		double yScaleFactor = graphHeight / yRange;

		// Write function for translating between coordinate systems:
		double finalXMin = xMin, finalYMin = yMin;
		screenToDataPoint = (Point p) -> {
			if (p == null) return null;
			double x = (p.getX() - graphLeft) / xScaleFactor + finalXMin;
			double y = (graphBottom - p.getY()) / yScaleFactor + finalYMin;
			return new Point(x, y);
		};

		// Draw background:
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, (int)(width), (int)(height));

		// Draw axes:
		g2.setColor(Color.BLACK);
		int xOrigin = (int)(graphLeft - xMin * xScaleFactor);
		int yOrigin = (int)(graphBottom + yMin * yScaleFactor);
		g2.drawLine((int)(graphLeft), yOrigin, (int)(graphRight), yOrigin); // x-axis
		g2.drawLine(xOrigin, (int)(graphBottom), xOrigin, (int)(graphTop)); // y-axis

		// Draw axes labels:
		drawStringCentered(g2, xUnit, width / 2.0, graphBottom + (height - graphBottom) / 2.0);
		drawStringCentered(g2, yUnit, graphLeft / 2.0, height / 2.0);
		int numLabels = 5;
		double xLabelY = graphBottom + (height - graphBottom) / 4.0;
		double yLabelX = graphLeft - graphLeft / 4.0;
		double markerWidth = graphWidth / 25.0;
		double markerHeight = graphHeight / 25.0;
		for (int i = 0; i <= numLabels; i++){

			double xValue = xMaxRounded * i / numLabels; // x value from left to right
			double xScreenValue = graphLeft + (xValue - xMin) * xScaleFactor;
			drawStringCentered(g2, "" + xValue, xScreenValue, xLabelY);

			double yValue = yMaxRounded * i / numLabels; // y value from bottom to top
			double yScreenValue = graphBottom - (yValue - yMin) * yScaleFactor;
			drawStringCentered(g2, "" + yValue, yLabelX, yScreenValue);

			// Draw marker lines:
			if (i > 0) {
				g2.drawLine((int) xScreenValue, (int) (yOrigin - markerHeight / 2.0), (int) xScreenValue, (int) (yOrigin + markerHeight / 2.0));
				g2.drawLine((int) (xOrigin - markerWidth / 2.0), (int) yScreenValue, (int) (xOrigin + markerWidth / 2.0), (int) yScreenValue);
			}
		}

		// Draw theoretical lines (lines of best fit):
		for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++){

			// Set line colour:
			g2.setColor(Color.BLACK);
			if (lineIndex < GRAPH_COLORS.length) g2.setColor(GRAPH_COLORS[lineIndex]);

			List<Point> line = lines.get(lineIndex);
			for (int i = 0; i < line.size()-1; i++){
				Point p0 = line.get(i);
				Point p1 = line.get(i+1);
				int x0 = (int)(graphLeft + (p0.getX() - xMin) * xScaleFactor);
				int y0 = (int)(graphBottom - (p0.getY() - yMin) * yScaleFactor);
				int x1 = (int)(graphLeft + (p1.getX() - xMin) * xScaleFactor);
				int y1 = (int)(graphBottom - (p1.getY() - yMin) * yScaleFactor);
				g2.drawLine(x0, y0, x1, y1);
			}
		}

		// Draw experiment points (raw data points):
		screenPointToTablePosition.clear();
		for (int pointListIndex = 0; pointListIndex < points.size(); pointListIndex++){
			List<Point> pointList = points.get(pointListIndex);

			// Set point colour:
			g2.setColor(Color.BLACK);
			if (pointListIndex < GRAPH_COLORS.length) g2.setColor(GRAPH_COLORS[pointListIndex]);

			for (int pointIndex = 0; pointIndex < pointList.size(); pointIndex++){
				Point p = pointList.get(pointIndex);
				if (p == null) continue;
				int x = (int)(graphLeft + (p.getX() - xMin) * xScaleFactor);
				int y = (int)(graphBottom - (p.getY() - yMin) * yScaleFactor);
				drawShapeFromIndex(g2, pointListIndex, x, y);

				// Remember each point's position in the table:
				screenPointToTablePosition.put(new Point(x, y), new Point(pointListIndex, pointIndex));
			}
		}

		// Set graph as displayed:
		if (!graphDisplayed){
			this.graphDisplayed = true;
			this.graphTypesComboBox.setEnabled(true);
			this.graphCopyButton.setEnabled(true);
		}
	}

	/**
	 * Draw a String centered at (x, y).
	 * Help from: https://stackoverflow.com/a/27740330.
	 *
	 * @param g2 Graphics object
	 * @param text String to draw
	 * @param x x position
	 * @param y y position
	 */
	private void drawStringCentered(Graphics2D g2, String text, double x, double y){
		FontMetrics metrics = g2.getFontMetrics();
		int left = (int)(x - metrics.stringWidth(text) / 2.0);
		int top = (int)((y - metrics.getHeight() / 2.0) + metrics.getAscent());
		g2.drawString(text, left, top);
	}

	/**
	 * Draws a shape centered at (x, y), with the type of shape depending on the given index i.
	 *
	 * @param g2 the Graphics object
	 * @param i index
	 * @param x x position
	 * @param y y position
	 */
	private void drawShapeFromIndex(Graphics2D g2, int i, int x, int y){
		boolean shapeVariant = false;
		if (i >= 5) shapeVariant = true; // make shapes hollow or rotate them to be different
		i %= 5; // wrap index so it is from 0 to 5, as there are 5 available shapes
		int size = 12;
		int radius = size / 2;
		int strokeWidth = 2;
		Stroke originalStroke = g2.getStroke(); // store original stroke so it can be set back after
		g2.setStroke(new BasicStroke(strokeWidth));

		// Draw shape based on index i:
		switch (i) {
			case 1: // square
				drawPolygon(g2, x, y, 4, radius, -Math.PI / 4.0, !shapeVariant, false);
				break;
			case 2: // triangle
				drawPolygon(g2, x, y, 3, radius, -Math.PI / 2.0, !shapeVariant, false);
				break;
			case 3: // cross (+)
				if (shapeVariant){
					radius = (int) (radius * 1.0 / Math.sqrt(2.0));
					g2.drawLine(x - radius, y - radius, x + radius, y + radius);
					g2.drawLine(x - radius, y + radius, x + radius, y - radius);
				}
				else {
					g2.drawLine(x - radius, y, x + radius, y);
					g2.drawLine(x, y - radius, x, y + radius);
				}
				break;
			case 4: // star
				drawPolygon(g2, x, y, 10, radius, -Math.PI / 2.0, !shapeVariant, true);
				break;
			default: // circle
				if (shapeVariant) g2.drawOval(x - radius, y - radius, size, size);
				else g2.fillOval(x - radius, y - radius, size, size);
		}
		g2.setStroke(originalStroke);
	}

	/**
	 * Draws a polygon centered at (x, y) with the given number of points and radius.
	 *
	 * @param g2 the Graphics object
	 * @param x x position
	 * @param y y position
	 * @param numPoints number of points
	 * @param radius distance of points from center
	 * @param startAngle starting angle for drawing points
	 * @param fill if false, only draw outline
	 * @param star if true, every second point has half the radius
	 */
	private void drawPolygon(Graphics2D g2, double x, double y, int numPoints, double radius, double startAngle, boolean fill, boolean star) {
		double angleIncrement = (2 * Math.PI) / numPoints;
		int[] xPoints = new int[numPoints];
		int[] yPoints = new int[numPoints];

		// Calculate points:
		for (int p = 0; p < numPoints; p++) {
			double r = radius;
			if (star && p % 2 != 0) r /= 2.0;
			double angle = startAngle + angleIncrement * p;
			xPoints[p] = (int) (Math.cos(angle) * r + x);
			yPoints[p] = (int) (Math.sin(angle) * r + y);
		}

		if (fill) g2.fillPolygon(xPoints, yPoints, numPoints);
		else g2.drawPolygon(xPoints, yPoints, numPoints);
	}

	/** 
	 * Sets the hover location values shown in xValueTextField and yValueTextField to the values
	 * in the given mousePoint (or blank if null).
	 *
	 * @param mousePoint current mouse location (or null if not available)
	 */
	private void setHoverValue(Point mousePoint){
		if (!graphDisplayed) return;

		// Attempt to convert point on screen to point in data.
		// dataPoint is null if the mouse is outside the graph or a graph is not displayed.
		Point dataPoint = screenToDataPoint.apply(mousePoint);
		if (dataPoint == null){ // clear text
			xValueTextField.setText("");
			yValueTextField.setText("");
			return;
		}

		// Display value under mouse position:
		xValueTextField.setText(xUnit + " =    " + String.format("%.4f", dataPoint.getX()));
		yValueTextField.setText(yUnit + " =    " + String.format("%.4f", dataPoint.getY()));
	}

	/**
	 * Sets clickedTablePositions to the table positions of all data points currently within range of the mouse.
	 *
	 * @param mousePoint mouse position on the screen
	 */
	private void setClickedTablePositions(Point mousePoint){
		if (!graphDisplayed || points == null) return;
		if (!clickedTablePositions.isEmpty()) clickedTablePositions.clear();

		// Add the table positions of all raw data points within range of the mouse to a list:
		for (Point screenPoint : screenPointToTablePosition.keySet()){
			if (mousePoint.distanceSquaredTo(screenPoint) < MAX_CLICKED_POINT_DIST_SQUARED){
				clickedTablePositions.add(screenPointToTablePosition.get(screenPoint));
			}
		}
		listener.onInputEvent(InputEvent.CLICKED_POINTS_CHANGED);
	}

	/**
	 * Clears clickedTablePositions.
	 */
	private void clearClickedTablePositions(){
		if (clickedTablePositions.isEmpty()) return;
		clickedTablePositions.clear();
		listener.onInputEvent(InputEvent.CLICKED_POINTS_CHANGED);
	}
}
