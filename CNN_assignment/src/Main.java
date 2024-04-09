/*
 * Main: This class represents a GUI application for training a Convolutional Neural Network (CNN).
 * It initializes the user interface, handles user interactions, and triggers CNN training.
 *
 * Author: Max Ceban
 * Date: 31/03/2024
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

//Main class representing the GUI application for CNN training
public class Main extends JFrame implements ActionListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//Attributes
	private JButton runButton;
	private JTextArea outputField;
	private JLabel trademark;

    // Flag to keep track of whether CNN is trained or not
	private boolean cnnTrained = false;

    // Constructor to initialize the GUI
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
		
        // Create main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

        // Create panel for 'Run' button
		JPanel runPanel = new JPanel();
		runButton = new JButton("Run Convolutional Neural Network training");
		runButton.addActionListener(this);
		runPanel.add(runButton);
		mainPanel.add(runPanel,BorderLayout.NORTH);

        // Create text area for output
		outputField = new JTextArea(15, 30);
		outputField.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(outputField);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create panel for trademark label
		JPanel bottomPanel = new JPanel();
		trademark = new JLabel("C22380206 Max Ceban @2024");
		bottomPanel.add(trademark,BorderLayout.CENTER);
		mainPanel.add(bottomPanel,BorderLayout.SOUTH);
        
		// Add main panel to the frame
		add(mainPanel);
		pack();
	}

    // Action performed when buttons are clicked
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == runButton) {
			if(!cnnTrained) {
				// Show a confirmation dialog before training
	            int choice = JOptionPane.showOptionDialog(
	                this,
	                "Are you sure you want to run the Convolutional Neural Network training process?",
	                "Confirm Training",
	                JOptionPane.YES_NO_OPTION,
	                JOptionPane.QUESTION_MESSAGE,
	                null,
	                new String[]{"Yes, run training", "No, cancel"},
	                "No, cancel"
	            );

	            if (choice == JOptionPane.YES_OPTION) {
	                trainCNN(); // Train CNN if "Yes" is selected
	            }
			}
			else {
				JOptionPane.showMessageDialog(this, "Convolutional Neural Network has been trained.","Training Already Completed", JOptionPane.INFORMATION_MESSAGE);
			}
		}


	}

    // Method to train the Convolutional Neural Network
	private void trainCNN() {
        
		// Creating a thread pool with 12 threads
		ExecutorService executorService = Executors.newFixedThreadPool(12);
		
		// Execute a task in the executor service, which runs the Convolutional Neural Network (CNN) training process.
		// Upon completion, update the output field with logs using the Logger class.
		// Set the flag 'cnnTrained' to true to indicate that the CNN has been trained.
		// Finally, shutdown the executor service to release its resources.
		executorService.execute(() -> {
			ConvolutionalNeuralNetwork.run();  // Run CNN training
			Logger.updateTextArea(Logger.getLogger(), outputField); // Update output field with logs
			cnnTrained = true; // Set flag to true indicating CNN is trained
			executorService.shutdown(); // Shutdown the executor service
		});
	}

    // Main method to start the application
	public static void main(String[] args) {
        // Run the GUI in the event dispatch thread
		// InvokeLater ensures that the GUI creation and interaction occur on the Event Dispatch Thread (EDT),
		// which is the recommended approach for Swing applications to ensure thread safety and proper UI behavior.
		// A lambda expression is used to create a new instance of the Main class and make the frame visible.
		SwingUtilities.invokeLater(() -> {
			Main app = new Main();
			app.setVisible(true); // Set the frame visible
		});
	}
}
