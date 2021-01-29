package hr.fer.projektr.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class InputDataPanel extends JPanel {
	
	static class InputData {
		Path trainingSetPath;
		String userCountStr;
		String itemCountStr;
		
		int userCount;
		int itemCount;
		
		InputData(Path trainingSetPath, String userCountStr, String itemCountStr) {
			this.trainingSetPath = trainingSetPath;
			this.userCountStr = userCountStr;
			this.itemCountStr = itemCountStr;
		}
	} 
	
	private JLabel pathLabel;
	private Path trainingSetPath;
	private JTextField tfUserCount;
	private JTextField tfItemCount;

	public InputDataPanel() {
		setLayout(new SpringLayout());

		// row 0
		add(new JLabel("Putanja:", SwingConstants.RIGHT));
		
		JPanel pathPnl = new JPanel();
		pathPnl.setLayout(new BoxLayout(pathPnl, BoxLayout.X_AXIS));
		JButton choosePathBtn = new JButton("Odaberi");
		choosePathBtn.addActionListener(this::buttonClick);
		pathLabel = new JLabel();
		pathPnl.add(choosePathBtn);
		pathPnl.add(Box.createRigidArea(new Dimension(5, 0)));
		pathPnl.add(pathLabel);
		add(pathPnl);

		// row 1
		add(new JLabel("Broj korisnika:", SwingConstants.RIGHT));

		tfUserCount = new JTextField();
		add(tfUserCount);
		
		// row 2
		add(new JLabel("Broj filmova:", SwingConstants.RIGHT));

		tfItemCount = new JTextField();
		add(tfItemCount);
		

		SpringUtilities.makeCompactGrid(this, 3, 2, 0, 0, 5, 5);
	}
	
	private void buttonClick(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int jfcResult = jfc.showOpenDialog(this);
		if (jfcResult != JFileChooser.APPROVE_OPTION)
			return;
		
		trainingSetPath = jfc.getSelectedFile().toPath();
		pathLabel.setText(trainingSetPath.toString());
		
	}
	
	InputData getInputData() {
		String userCountStr = tfUserCount.getText();
		String itemCountStr = tfItemCount.getText();
		return new InputData(trainingSetPath, userCountStr, itemCountStr);
	}
	
	

}
