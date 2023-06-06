package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;

import domain.KineticModel;

/**
 * A JPanel that sets up a set of radio buttons in itself for the purpose of
 * model selection. You can get the and set the selected model.
 */
@SuppressWarnings("serial")
public class ModelSelection extends JPanel {
	/** The panel with inhibition options. */
	private JPanel inhibitionPanel;
	/** The panel with substrate number options. */
	private JPanel SubstratesPanel;
	/** The panel with kinetic model options for uninhibited, two substrates. */
	private JPanel kineticModelPanel1;
	/** The panel with kinetic model options for inhibited. */
	private JPanel kineticModelPanel2;

	/** Button for setting the model to uninhibited. */
	private JRadioButton uninhibited;
	/** Button for setting the model to inhibited. */
	private JRadioButton inhibited;
	/** Button for setting the uninhibited model to one substrate. */
	private JRadioButton oneSub;
	/** Button for setting the uninhibited model to two substrates. */
	private JRadioButton twoSubs;
	/** Button for setting the uninhibited two substrate model to Bi-Bi. */
	private JRadioButton biBi;
	/** Button for setting the uninhibited two substrate model to Ping-Pong. */
	private JRadioButton pingPong;
	/** Button for setting the inhibited model to competitive. */
	private JRadioButton competitive;
	/** Button for setting the inhibited model to non-competitive. */
	private JRadioButton nonCompetitive;
	/** Button for setting the inhibited model to un-competitive. */
	private JRadioButton unCompetitive;

	/** Group of radio buttons for inhibition selection. */
	private ButtonGroup inhibitionButts;
	/** Group of radio buttons for substrate number selection. */
	private ButtonGroup substrateButts;
	/** Group of buttons for kinetic model selection for uninhibited two subs. */
	private ButtonGroup kineticModelButts1;
	/** Group of radio buttons for kinetic model selection for inhibited. */
	private ButtonGroup kineticModelButts2;

	/** If the Model selector is currently in LUCENZ 3 (or 2) */
	private boolean version3 = true;

	/**
	 * Create a completely new model selection panel.
	 * 
	 * @param updateModel Method to call that updates the selected of the InputPane
	 */
	public ModelSelection(Consumer<KineticModel> updateModel) {
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Set window size
		setMinimumSize(new Dimension(300, 110));
		setPreferredSize(new Dimension(300, 110));
		// this.getMinimumSize();
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		// Setup panels. All panels use box layout to make them into a nice list.

		inhibitionPanel = new JPanel();
		inhibitionPanel.setLayout(new BoxLayout(inhibitionPanel, BoxLayout.PAGE_AXIS));
		add(inhibitionPanel);

		SubstratesPanel = new JPanel();
		SubstratesPanel.setLayout(new BoxLayout(SubstratesPanel, BoxLayout.PAGE_AXIS));
		add(SubstratesPanel);

		kineticModelPanel1 = new JPanel();
		kineticModelPanel1.setLayout(new BoxLayout(kineticModelPanel1, BoxLayout.PAGE_AXIS));
		add(kineticModelPanel1);

		kineticModelPanel2 = new JPanel();
		kineticModelPanel2.setLayout(new BoxLayout(kineticModelPanel2, BoxLayout.PAGE_AXIS));
		add(kineticModelPanel2);

		// Make SubstratesPanel sit to the right of inhibitionPanel
		layout.putConstraint(SpringLayout.WEST, SubstratesPanel, 0, SpringLayout.EAST, inhibitionPanel);
		// Make kineticModelPanel1 sit to the right of SubstratesPanel
		layout.putConstraint(SpringLayout.WEST, kineticModelPanel1, 0, SpringLayout.EAST, SubstratesPanel);
		// Make kineticModelPanel2 sit to the right of inhibitionPanel
		layout.putConstraint(SpringLayout.WEST, kineticModelPanel2, 0, SpringLayout.EAST, inhibitionPanel);

		ActionListener buttonPressed = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KineticModel model = readModel();
				updateModel.accept(model);
			}
		};

		// Create the radio buttons.

		// Inhibition options
		uninhibited = new JRadioButton("Uninhibited");
		inhibited = new JRadioButton("Inhibited");
		// Add the buttons into a group together
		inhibitionButts = new ButtonGroup();
		inhibitionButts.add(uninhibited);
		inhibitionButts.add(inhibited);
		// Set default
		uninhibited.setSelected(true);
		// Add listeners
		uninhibited.addActionListener(buttonPressed);
		inhibited.addActionListener(buttonPressed);
		// Add buttons to correct panel
		inhibitionPanel.add(new JLabel("Inhibition"));
		inhibitionPanel.add(uninhibited);
		inhibitionPanel.add(inhibited);

		// Substrate number options
		oneSub = new JRadioButton("One Substrate");
		twoSubs = new JRadioButton("Two Substrates");
		// Add the buttons into a group together
		substrateButts = new ButtonGroup();
		substrateButts.add(oneSub);
		substrateButts.add(twoSubs);
		// Set default
		oneSub.setSelected(true);
		// Add listeners
		oneSub.addActionListener(buttonPressed);
		twoSubs.addActionListener(buttonPressed);
		// Add buttons to correct panel
		SubstratesPanel.add(new JLabel("Substrates"));
		SubstratesPanel.add(oneSub);
		SubstratesPanel.add(twoSubs);

		// Kinetic model options for uninhibited, two substrates
		biBi = new JRadioButton("Ordered Bi-Bi");
		pingPong = new JRadioButton("Ping Pong");
		// Add the buttons into a group together
		kineticModelButts1 = new ButtonGroup();
		kineticModelButts1.add(biBi);
		kineticModelButts1.add(pingPong);
		// Set default
		biBi.setSelected(true);
		// Add buttons to correct panel
		kineticModelPanel1.add(new JLabel("Kinetic Model"));
		kineticModelPanel1.add(biBi);
		kineticModelPanel1.add(pingPong);

		// Kinetic model options for inhibited
		competitive = new JRadioButton("Competitive");
		nonCompetitive = new JRadioButton("Non-Competitive");
		unCompetitive = new JRadioButton("Un-Competitive");
		// Add the buttons into a group together
		kineticModelButts2 = new ButtonGroup();
		kineticModelButts2.add(competitive);
		kineticModelButts2.add(nonCompetitive);
		kineticModelButts2.add(unCompetitive);
		// Set default
		competitive.setSelected(true);
		// Add buttons to correct panel
		kineticModelPanel2.add(new JLabel("Kinetic Model"));
		kineticModelPanel2.add(competitive);
		kineticModelPanel2.add(nonCompetitive);
		kineticModelPanel2.add(unCompetitive);
		
		setModel(readModel());
	}

	/**
	 * Set the LUCENZ version of the input pane. True of 3, false for 2. Effects
	 * radio buttons that are showing.
	 * 
	 * @param version3 If version is being switch to version 3 (or 2)
	 */
	public void setVersion(boolean version3) {
		// Do nothing if the model is unchanged
		if (this.version3 == version3)
			return;

		// Set to the new version
		this.version3 = version3;
		// Reset radio buttons if uninhibited is already selected
		if (uninhibited.isSelected()) {
			setModel(KineticModel.UninhibitedOneSub);
		}
	}

	/**
	 * Get the current LUCENZ version being used.
	 * 
	 * @return true of LUCENZ 3, and false for 2
	 */
	public boolean getVersion() {
		return version3;
	}

	/**
	 * For getting the currently selected model.
	 * 
	 * @return The model currently selected by the radio buttons
	 */
	public KineticModel readModel() {
		if (uninhibited.isSelected()) {
			if (oneSub.isSelected()) {
				return KineticModel.UninhibitedOneSub;
			}
			if (twoSubs.isSelected()) {
				if (biBi.isSelected()) {
					return KineticModel.UninhibitedTwoSubOrderedBiBi;
				}
				if (pingPong.isSelected()) {
					return KineticModel.UninhibitedTwoSubPingPong;
				}
			}
		}
		if (inhibited.isSelected()) {
			if (competitive.isSelected()) {
				return KineticModel.InhibitedCompetitive;
			}
			if (nonCompetitive.isSelected()) {
				return KineticModel.InhibitedNonCompetitive;
			}
			if (unCompetitive.isSelected()) {
				return KineticModel.InhibitedUnCompetitive;
			}
		}

		// Should be unreachable
		throw new RuntimeException("Invalid model selected!");
	}

	/**
	 * Set the viable panels for the given model. Works on assumption is is not been
	 * given an invalid model for the current LUCENZ version.
	 * 
	 * @param currentModel The model to set visibilities for
	 */
	private void setPanels(KineticModel currentModel) {
		// Select what buttons to show for this model
		switch (currentModel) {
		case UninhibitedOneSub:
			SubstratesPanel.setVisible(version3);
			kineticModelPanel1.setVisible(false);
			kineticModelPanel2.setVisible(false);
			break;

		case InhibitedCompetitive:
		case InhibitedNonCompetitive:
		case InhibitedUnCompetitive:
			SubstratesPanel.setVisible(false);
			kineticModelPanel1.setVisible(false);
			kineticModelPanel2.setVisible(true);
			break;

		case UninhibitedTwoSubOrderedBiBi:
		case UninhibitedTwoSubPingPong:
			SubstratesPanel.setVisible(true);
			kineticModelPanel1.setVisible(true);
			kineticModelPanel2.setVisible(false);
			break;
		default:
			break;
		}
	}

	/**
	 * For setting the currently selected model.
	 * 
	 * @param newModel The model to set as selected
	 */
	public void setModel(KineticModel newModel) {
		// User cannot select LUCENZ 3 models in LUCENZ 2
		if (!version3 && (newModel.equals(KineticModel.UninhibitedTwoSubOrderedBiBi)
				|| newModel.equals(KineticModel.UninhibitedTwoSubPingPong))) {
			newModel = KineticModel.UninhibitedOneSub;
		}

		// Set the selected buttons for each viable panel for each model
		switch (newModel) {
		case UninhibitedOneSub:
			inhibitionButts.setSelected(uninhibited.getModel(), true);
			substrateButts.setSelected(oneSub.getModel(), true);
			break;

		case InhibitedCompetitive:
			inhibitionButts.setSelected(inhibited.getModel(), true);
			kineticModelButts2.setSelected(competitive.getModel(), true);
			break;

		case InhibitedNonCompetitive:
			inhibitionButts.setSelected(inhibited.getModel(), true);
			kineticModelButts2.setSelected(nonCompetitive.getModel(), true);
			break;

		case InhibitedUnCompetitive:
			inhibitionButts.setSelected(inhibited.getModel(), true);
			kineticModelButts2.setSelected(unCompetitive.getModel(), true);
			break;

		case UninhibitedTwoSubOrderedBiBi:
			inhibitionButts.setSelected(uninhibited.getModel(), true);
			substrateButts.setSelected(twoSubs.getModel(), true);
			kineticModelButts1.setSelected(biBi.getModel(), true);
			break;

		case UninhibitedTwoSubPingPong:
			inhibitionButts.setSelected(uninhibited.getModel(), true);
			substrateButts.setSelected(twoSubs.getModel(), true);
			kineticModelButts1.setSelected(pingPong.getModel(), true);
			break;

		default:
			break;
		}

		setPanels(newModel);
	}
}
