package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Pane that contains all the information from the about menu of the program.
 * Make pane pop up by calling: JOptionPane.showMessageDialog(frame, new
 * AboutPane(), "About LUCENZ", JOptionPane.INFORMATION_MESSAGE);
 * 
 */
@SuppressWarnings("serial")
public class AboutPane extends JPanel {
	/** Larger title line at start of text. */
	private final String TITLE = "LUCENZ: JAVA edition";
	/** Text body of the text. */
	private final String TEXT = "Help and detailed information in associated files:"
			+ "\nBIOL209W.pdf (200 level) or BIOL309W.pdf (300 level). "
			+ "You can access these files at Info > Documents."
			+ "\n\nThis version of LUCENZ created by Group 9 from ENGR302 in 2022."
			+ " It is a recreation of the original program in the JAVA programming language."
			+ "\n\nQuestions? Contact:" + "\nLifeng Peng: lifeng.peng@vuw.ac.nz (Lecturer in charge)"
			+ "\nAlan Clark: alan.clark69@yahoo.com (Original program creator)";

	/**
	 * Create new about pane.
	 */
	public AboutPane() {
		// Window size
		setPreferredSize(new Dimension(350, 250));

		// Title
		JLabel messageTitle = new JLabel(TITLE);
		messageTitle.setFont(new Font("TitleFont", Font.BOLD, 25));

		// Body
		JTextArea messageBody = new JTextArea(TEXT);
		messageBody.setAlignmentX(Component.LEFT_ALIGNMENT);
		messageBody.setEditable(false);
		messageBody.setOpaque(false);
		// Make lines warp around
		messageBody.setLineWrap(true);
		messageBody.setWrapStyleWord(true);

		// Add object down pane
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(messageTitle);
		add(messageBody);
	}
}
