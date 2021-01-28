package hr.fer.projektr.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class DatasetInputFrame extends JFrame {
	
	private int c = 0;

	public DatasetInputFrame() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(500, 250);
		this.setLocationRelativeTo(null);
		this.initGUI();
	}
	
	private void initGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		
		JPanel inputDataPanel = new JPanel();
		int rows = 1;
		int cols = 2;
		inputDataPanel.setLayout(new BoxLayout(inputDataPanel, BoxLayout.Y_AXIS));
		
		JPanel firstRow = new JPanel(new FlowLayout());
		
		JLabel datasetPathLabel = new JLabel("Putanja:", SwingConstants.RIGHT);
		JPanel datasetPnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		JLabel pathLabel = new JLabel("lbn");
		JButton choosePathBtn = new JButton("Odaberi putanju");
		choosePathBtn.addActionListener(e -> {
			
		});
		datasetPnl.add(choosePathBtn);
		datasetPnl.add(pathLabel);
		firstRow.add(datasetPathLabel);
		firstRow.add(datasetPnl);
		
		inputDataPanel.add(firstRow);
		
		JPanel secondRow = new JPanel(new FlowLayout());
		JLabel userCountLabel = new JLabel("Broj korisnika: ", SwingConstants.RIGHT);
		JTextField userCountTf = new JTextField();
		secondRow.add(userCountLabel);
		secondRow.add(userCountTf);
		
		inputDataPanel.add(secondRow);
		
		cp.add(inputDataPanel);
		
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame f = new DatasetInputFrame();
			f.setVisible(true);
		});
	}
	
	

}
