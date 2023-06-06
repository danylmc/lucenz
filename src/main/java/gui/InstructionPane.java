package gui;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

/**
 * InstructionPane is a JPanel that contains all the information from the help menu of the program.
 * Make the pane pop up by calling:
 * JOptionPane.showMessageDialog(frame, new InstructionPane(), "Help", JOptionPane.INFORMATION_MESSAGE);
 */
public class InstructionPane extends JPanel {

    /** Instruction text to be displayed. */
    private static final String INSTRUCTIONS = "To get started in this programme you first need to enter data into the " +
            "grid on the left. You can do this either by entering the data directly into the grid cells or by " +
            "retrieving stored data using \"Open\" in the \"Files\" menu. If editing the grid cells, make sure to hit " +
            "ENTER after editing each cell to properly update the value." +
            "\n\nSelect whether you want inhibited or uninhibited kinetics and then adjust the slider(s) to give the " +
            "number of substrate concentrations and inhibitor concentrations you want." +
            "\n\nIf you are studying inhibition kinetics, you will be offered a selection of three models of inhibition " +
            "to fit to your data. Do this by clicking on the option you want. Similarly, if you are working with " +
            "uninhibited rates, there are three possible models you can fit your data to." +
            "\n\nOnce you have set up your data and chosen the appropriate model, you are ready to analyse your data. " +
            "Do this simply by pressing the \"GO\" button. Miraculously, a summary of your results will appear in a " +
            "pane on the right and a graphical presentation of your data at the lower right. In the latter pane you " +
            "can select a variety of ways to plot your data, using options from the \"Plotting\" menu.\n\n"+
            "One thing to note is the superscript of values 1, 2, and 3 may be shorter and display lower than the other " +
            "superscript values. This does not affect proper copying and pasting of the data into a separate document.\n\n" +
            "For further help with a specific version of LUCENZ, you can access the program's documentation at Info > Documents.";

    /**
     * Creates a new InstructionPane for showing the help message.
     */
    public InstructionPane(){
        // Window size and layout:
        setPreferredSize(new Dimension(400, 450));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create message:
        JTextArea messageTextArea = new JTextArea(INSTRUCTIONS);
        messageTextArea.setEditable(false);
        messageTextArea.setOpaque(false);
        messageTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        add(messageTextArea);
    }
}
