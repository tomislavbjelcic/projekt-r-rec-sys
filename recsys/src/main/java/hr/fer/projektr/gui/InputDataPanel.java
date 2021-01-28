package hr.fer.projektr.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class InputDataPanel extends JPanel {
	
	private JLabel pathLabel;
	private JTextField tfUserCount;
	private JTextField tfItemCount;

	public InputDataPanel() {
		setLayout(new SpringLayout());

		// row 0
		add(new JLabel("Putanja:", SwingConstants.RIGHT));
		
		JPanel pathPnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		JButton choosePathBtn = new JButton();
		tfFirstName = new JTextField();
		tfFirstName.setColumns(10);
		add(tfFirstName);

		// row 1
		add(new JLabel("Prezime:", SwingConstants.RIGHT));

		tfLastName = new JTextField();
		//tfLastName.setColumns(10);
		add(tfLastName);

		// row 2
		add(new JPanel());

		cbEmail = new JCheckBox("Želite li primati e-mailove?");
		add(cbEmail);

		// rox 3
		add(new JPanel());

		JPanel radioPanel = new JPanel();
		radioPanel
		.setBorder(BorderFactory.createTitledBorder(null,
				"Kako želite primati e-mailove?", TitledBorder.LEADING,
				TitledBorder.TOP));
		add(radioPanel);

		radioPanel.setLayout(new GridLayout(0, 1, 0, 0));
		emailGroup = new ButtonGroup();
		rbWeekly = new JRadioButton("Tjedno");
		radioPanel.add(rbWeekly);
		emailGroup.add(rbWeekly);
		rbDaily = new JRadioButton("Dnevno");
		radioPanel.add(rbDaily);
		emailGroup.add(rbDaily);
		rbMonthly = new JRadioButton("Mjesečno");
		radioPanel.add(rbMonthly);
		emailGroup.add(rbMonthly);

		// row 4
		add(new JLabel("Ulica i broj:", SwingConstants.RIGHT));

		tfStreet = new JTextField();
		tfStreet.setColumns(10);
		add(tfStreet);

		// row 5
		add(new JLabel("Grad:", SwingConstants.RIGHT));

		comboCity = new JComboBox<String>(cityArray);
		add(comboCity);

		// row 6
		add(new JLabel("Poštanski broj:", SwingConstants.RIGHT));

		tfPostalCode = new JFormattedTextField(new DecimalFormat("#####"));
		add(tfPostalCode);

		SpringUtilities.makeCompactGrid(this, 7, 2, 0, 0, 5, 5);
	}

}
