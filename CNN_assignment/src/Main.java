import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import cnn.ConvolutionalNeuralNetwork;
import cnn.Logger;

public class Main extends JFrame implements ActionListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//Attributes
	private JButton runButton;
	private JTextArea outputField;
	private JLabel trademark;

	private boolean cnnTrained = false;

	public Main() {
		initializeUI();
	}

	private void initializeUI() {
		
		setTitle("CNN app");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(600,600);
		setResizable(false);
		setLocationRelativeTo(null); //Center the window

		//Components
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel runPanel = new JPanel();
		runButton = new JButton("Run Convolutional Neural Network training");
		runButton.addActionListener(this);
		runPanel.add(runButton);
		mainPanel.add(runPanel,BorderLayout.NORTH);


		outputField = new JTextArea(15, 30);
		outputField.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(outputField);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel resultPanel = new JPanel();
		trademark = new JLabel("C22380206 Max Ceban @2024");
		resultPanel.add(trademark,BorderLayout.CENTER);
		mainPanel.add(resultPanel,BorderLayout.SOUTH);

		add(mainPanel);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == runButton) {
			if(!cnnTrained) {
				trainCNN();
			}
			else {
				JOptionPane.showMessageDialog(this, "Convolutional Neural Network has been trained already");
			}
		}


	}

	private void trainCNN() {
		ConvolutionalNeuralNetwork.run();
		Logger.updateTextArea(Logger.getLogger(), outputField);
		cnnTrained = true;
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Main app = new Main();
			app.setVisible(true);
		});
	}
}
