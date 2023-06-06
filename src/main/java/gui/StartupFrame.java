package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * StartupFrame is a JFrame shown upon starting the program.
 * It allows the user to select a LUCENZ version before entering the main program.
 */
public class StartupFrame extends JFrame {

    /** Foreground colour. */
    private final static Color COLOR_FOREGROUND = Color.GREEN.darker().darker();
    /** Background colour. */
    private final static Color COLOR_BACKGROUND = Color.LIGHT_GRAY;
    /** Listener for handling input events. */
    private final InputEventListener listener;

    /**
     * Create new StartupPane.
     */
    public StartupFrame(InputEventListener listener) {
        super("LUCENZ Java Edition");
        this.listener = listener;

        // Set size and centre on screen:
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        //getContentPane().setBackground(COLOR_BACKGROUND);

        // Create panels:
        JPanel titlePanel = createTitlePanel();
        JPanel optionPanel = createOptionPanel();
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(COLOR_FOREGROUND);
        footerPanel.add(Box.createVerticalStrut(10));

        // Add panels vertically:
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        add(Box.createVerticalStrut(20), c);
        c.gridy = 1;
        add(titlePanel, c);
        c.gridy = 2;
        c.weighty = 1.0;
        add(optionPanel, c);
        c.gridy = 3;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(footerPanel, c);
    }

    /**
     * Creates a panel showing the title.
     *
     * @return titlePanel
     */
    private JPanel createTitlePanel() {
        // Creates a panel with a custom shape of RoundRect; a rectangle with rounded corners:
        JPanel panel = new JPanel(){
            private Shape shape;
            private final int arcDiameter = 30;
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, arcDiameter, arcDiameter);
                super.paintComponent(g);
            }
            public boolean contains(int x, int y) {
                if (shape == null || !shape.getBounds().equals(getBounds())) {
                    shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, arcDiameter, arcDiameter);
                }
                return shape.contains(x, y);
            }
        };
        panel.setOpaque(false);
        panel.setBackground(COLOR_FOREGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Border border = BorderFactory.createEmptyBorder(20, 50, 20, 50);
        //border = BorderFactory.createCompoundBorder(BorderFactory.createLoweredSoftBevelBorder(), border);
        //border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), border);
        //border = BorderFactory.createCompoundBorder(BorderFactory.createRaisedSoftBevelBorder(), border);
        panel.setBorder(border);

        JLabel lucenzLabel = new JLabel("LUCENZ");
        lucenzLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lucenzLabel.setFont(createFont(60));
        lucenzLabel.setForeground(Color.WHITE);
        JLabel editionLabel = new JLabel("Java Edition (2022)");
        editionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        editionLabel.setFont(createFont(20));
        editionLabel.setForeground(Color.WHITE);

        panel.add(lucenzLabel);
        panel.add(editionLabel);

        return panel;
    }

    /**
     * Creates a panel for holding buttons representing the LUCENZ version options.
     *
     * @return optionPanel
     */
    private JPanel createOptionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel selectVersionLabel = new JLabel("Select a version:");
        selectVersionLabel.setFont(createFont(14));
        JButton lucenzTwoButton = new JButton("LUCENZ 2 (200 Level)");
        lucenzTwoButton.addActionListener(e -> listener.onInputEvent(InputEvent.LUCENZ_2_SELECTED));
        JButton lucenzThreeButton = new JButton("LUCENZ 3 (300 Level)");
        lucenzThreeButton.addActionListener(e -> listener.onInputEvent(InputEvent.LUCENZ_3_SELECTED));

        panel.add(selectVersionLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lucenzTwoButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lucenzThreeButton);

        return panel;
    }

    /**
     * Creates a new font, matching the default font but with specified size.
     *
     * @param size font size
     * @return new font
     */
    private Font createFont(int size){
        Font font = new JLabel().getFont();
        return new Font(font.getName(), font.getStyle(), size);
    }
}
