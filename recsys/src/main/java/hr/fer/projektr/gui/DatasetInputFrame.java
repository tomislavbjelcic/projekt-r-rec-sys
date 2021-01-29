package hr.fer.projektr.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.projektr.gui.InputDataPanel.InputData;

public class DatasetInputFrame extends JFrame {
	
	private static final int WHOLE_DATASET_ITEM_COUNT = 17770;
	private static final int WHOLE_DATASET_USER_COUNT = 480189;
	private InputDataPanel idp;

	public DatasetInputFrame() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Učitaj dataset");
		this.setSize(500, 150);
		this.setLocationRelativeTo(null);
		this.initGUI();
	}
	
	private void initGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		
		idp = new InputDataPanel();
		cp.add(idp, BorderLayout.CENTER);
		
		JPanel loadPnl = new JPanel(new FlowLayout());
		JButton loadBtn = new JButton("Učitaj");
		loadBtn.addActionListener(this::loadButtonPressed);
		loadPnl.add(loadBtn);
		cp.add(loadPnl, BorderLayout.PAGE_END);
		
		
	}
	
	private void loadButtonPressed(ActionEvent e) {
		InputData data = idp.getInputData();
		String err = checkAndParseData(data);
		if (err != null) {
			JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	private static String checkAndParseData(InputData data) {
		String userErr = "Broj korisnika mora biti prirodan broj između 1 i " + WHOLE_DATASET_USER_COUNT;
		String itemErr = "Broj filmova mora biti između 1 i " + WHOLE_DATASET_ITEM_COUNT;
		int userCount = -1;
		try {
			userCount = Integer.parseInt(data.userCountStr);
		} catch (NumberFormatException ex) {
			return userErr;
		}
		if (userCount < 1 || userCount > WHOLE_DATASET_USER_COUNT)
			return userErr;
		data.userCount = userCount;
		
		int itemCount = -1;
		try {
			itemCount = Integer.parseInt(data.itemCountStr);
		} catch (NumberFormatException ex) {
			return itemErr;
		}
		if (itemCount < 1 || itemCount > WHOLE_DATASET_ITEM_COUNT)
			return itemErr;
		data.itemCount = itemCount;
		
		if (data.trainingSetPath == null)
			return "Putanja nije postavljena";
		
		return null;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame f = new DatasetInputFrame();
			f.setVisible(true);
		});
	}
	
	

}
