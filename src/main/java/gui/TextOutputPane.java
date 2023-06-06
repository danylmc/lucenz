package gui;

import domain.Data;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * JPanel extension so users can view kinetic parameters/outputs for a simulation
 */
public class TextOutputPane extends JPanel {

	private String str;
	private JTextArea textoutput;
	private JPanel controlPanel;

	TextOutputPane() {
		str =  "No data entered.";
		this.setBackground(Color.LIGHT_GRAY);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setLayout(new BorderLayout());
		textoutput = new JTextArea(str);
		textoutput.setEditable(false);
		textoutput.setFont(createFont(15));
		textoutput.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0)); // add padding to left of text
		
		// Create control panel
		this.controlPanel = new JPanel();
		this.controlPanel.setBackground(null);
		this.controlPanel.setLayout(new BoxLayout(this.controlPanel, BoxLayout.LINE_AXIS));
		controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		this.add(this.controlPanel, BorderLayout.NORTH);
		
		controlPanel.add(new JLabel("Kinetic parameters:"), BorderLayout.NORTH);
		controlPanel.add(Box.createHorizontalStrut(10));
		
		addButton();
		JScrollPane scroll = new JScrollPane(textoutput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(Box.createVerticalStrut(10));
		this.add(scroll);

	}

	/**
	 * adds the copy button to the JPanel so users can copy data. called by constructor
	 */
	private void addButton() {
		JButton copyButton = new JButton("Copy Text");
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//https://stackoverflow.com/questions/24702434/copy-text-to-clipboard-from-a-jtextfield-with-press-of-a-button
				StringSelection stringSelection = new StringSelection (str);
				Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
				clpbrd.setContents (stringSelection, null);
			}
		});
		controlPanel.add(copyButton, BorderLayout.NORTH);
	}


	/**
	 * to be called when the text is to be changed.
	 * @param data the data to display in the text output pane.
	 */
	public void update(Data data) {
			String filepath = data.getFilePath();
			if(filepath == null) {
				filepath = "No file loaded";
			}
			str =
					"DATA SET... " + filepath + "\n" +
							"Mechanism..." + data.getModelType().toString() + "\n" +
							"\n" +
							String.format("Weighted Error Sum = %s\n", numFormat(data, "wes")) +
							String.format("RMS Fractional Residual = %s\n", numFormat(data, "chis")) +
							"\n" +
							"Parameter \tValue \t+/- \tCoefficient of variation \n" +
							String.format("1 Vm \t%s \t+/- \t%s\n", numFormat(data, "vm"), numFormat(data, "sevm"));

		//displays different parameters depending on the Kinetic model type.
		switch (data.getModelType()){
			case UninhibitedOneSub:
				str = str + String.format("2 Km \t%s \t+/- \t%s\n", numFormat(data, "km"), numFormat(data, "sekm"));
				break;
			case UninhibitedTwoSubOrderedBiBi:
				str = str +
						String.format("2 Ka \t%s \t+/- \t%s\n", numFormat(data, "ka"), numFormat(data, "seka")) +
						String.format("3 Kb \t%s \t+/- \t%s\n", numFormat(data, "kb"), numFormat(data, "sekb")) +
						String.format("4 Kia \t%s \t+/- \t%s\n", numFormat(data, "kia"), numFormat(data, "sekia"));
				break;
			case UninhibitedTwoSubPingPong:
				str = str +
						String.format("2 Ka \t%s \t+/- \t%s\n", numFormat(data, "ka"), numFormat(data, "seka")) +
						String.format("3 Kb \t%s \t+/- \t%s\n", numFormat(data, "kb"), numFormat(data, "sekb"));
				break;
			case InhibitedCompetitive:
				str = str +
						String.format("2 Km \t%s \t+/- \t%s\n", numFormat(data, "km"), numFormat(data, "sekm")) +
						String.format("3 Kis \t%s \t+/- \t%s\n", numFormat(data, "kis"), numFormat(data, "sekis"));
				break;
			case InhibitedNonCompetitive:
				str = str +
						String.format("2 Km \t%s \t+/- \t%s\n", numFormat(data, "km"), numFormat(data, "sekm")) +
						String.format("3 Kii \t%s \t+/- \t%s\n", numFormat(data, "kii"), numFormat(data, "sekii")) +
						String.format("4 Kis \t%s \t+/- \t%s\n", numFormat(data, "kis"), numFormat(data, "sekis"));
				break;
			case InhibitedUnCompetitive:
				str = str +
						String.format("2 Km \t%s \t+/- \t%s\n", numFormat(data, "km"), numFormat(data, "sekm")) +
						String.format("3 Kii \t%s \t+/- \t%s\n", numFormat(data, "kii"), numFormat(data, "sekii"));
				break;
			default:
				throw new IllegalArgumentException("Unrecognised kinetic model");
		}
		if(str.contains("null")) {
			str = "Error, incomplete data";
		}
		textoutput.setText(str);
	}

	/**
	 * Gets data for a given key and returns it in scientific notation.
	 * @param data
	 * @param key
	 * @return
	 */
	private String numFormat(Data data, String key){
		String s = String.format("%.2e", data.getTextData().get(key));
		String[] tokens = s.split("e");
		//following if statement catches certain NaN errors. We do not understand this error, but to the best of our knowledge it should never happen with valid experiment readings
		//errors thrown by all cell and substrate numbers are the same and on  of the following:
		// all numbers are even single digit numbers
		// all numbers contain a run of two or more of the same digit
		if(!s.contains("e")){
			return "Error";
		}

		if (tokens[1].endsWith("00")){
			//remove redundant + and make 00 superscript
			tokens[1] = "\u2070\u2070";
		} else {
			//replace normal characters with their superscript counterpart
			tokens[1] = tokens[1].replace("+", "\u207a");
			tokens[1] = tokens[1].replace("-", "\u207b");
			tokens[1] = tokens[1].replace("0", "\u2070");
			tokens[1] = tokens[1].replace("1", "\u00b9");
			tokens[1] = tokens[1].replace("2", "\u00b2");
			tokens[1] = tokens[1].replace("3", "\u00b3");
			tokens[1] = tokens[1].replace("4", "\u2074");
			tokens[1] = tokens[1].replace("5", "\u2075");
			tokens[1] = tokens[1].replace("6", "\u2076");
			tokens[1] = tokens[1].replace("7", "\u2077");
			tokens[1] = tokens[1].replace("8", "\u2078");
			tokens[1] = tokens[1].replace("9", "\u2079");
		}

		return tokens[0] + "e" + tokens[1];
	}

	private Font createFont(int size){
		Font font = new JLabel().getFont();
		return new Font(font.getName(), font.getStyle(), size);
	}

}
