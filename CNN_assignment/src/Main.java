/*
 * Main: This class serves as the entry point for the program. It loads image data, constructs a neural network,
 *       trains the network, and evaluates its performance.
 * 
 * Author: Max Ceban
 * Date: 26/03/2024
 */

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import CNN.ConvolutionalNeuralNetwork;

public class Main extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Attributes
	private JTextField filePathField;
	private JButton browseButton;
	private JButton classifyButton;
	private JLabel imageView;
	private JLabel resultLabel;
	private JButton runCNNButton;
	
	public Main() {
		setTitle("Image Classifier App");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,400);
		setLocationRelativeTo(null); // Center the window
		
		//Components
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		filePathField = new JTextField(20);
		filePathField.setEditable(false);
		
		browseButton = new JButton("Browse");
		browseButton.addActionListener(this);
		
		JPanel filePanel = new JPanel();
		
		filePanel.add(filePathField);
		filePanel.add(browseButton);
		
		imageView = new JLabel();
		imageView.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel buttonPanel = new JPanel(); // Panel to hold classifyButton and resultLabel
		
		classifyButton = new JButton("Classify");
        classifyButton.addActionListener(this);
		buttonPanel.add(classifyButton);
		
		runCNNButton = new JButton("Run Convultional Neural Network");
        runCNNButton.addActionListener(this);
        buttonPanel.add(runCNNButton);
		
		resultLabel = new JLabel();
		buttonPanel.add(resultLabel);
	
        mainPanel.add(filePanel, BorderLayout.NORTH);
        mainPanel.add(imageView, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == browseButton) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif");
			fileChooser.setFileFilter(filter);
			int returnValue = fileChooser.showOpenDialog(this);
			if(returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				filePathField.setText(selectedFile.getAbsolutePath());
	            
				// Load and display the image
				ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
				imageView.setIcon(icon);
				imageView.setText(null); // Clear text if any
			}
		}
		else if (e.getSource() == classifyButton) {
			//Gets the selected Path
			String imagePath = filePathField.getText();
			if(imagePath.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please select an image first.");
				return;
			}
			
			//Preprocessing the Image
			BufferedImage image = loadImage(imagePath);
			if(image == null) {
				JOptionPane.showMessageDialog(this, "Failed to load the image.");
				return;
			}
			
			//Apply preprocessing steps (e.g., resize, normalize)
	        image = preprocessImage(image, 28, 28);
	        
	        //Classify the preprocessed image
	        String classificationResult = classifyImage(image);
	        
	        // Display the classification result
	        resultLabel.setText(classificationResult);
		}
	 else if(e.getSource() == runCNNButton) {
		ConvolutionalNeuralNetwork.run(); 
	 }
	}

	private String classifyImage(BufferedImage image) {
		// TODO Auto-generated method stub
		return null;
	}

	private BufferedImage loadImage(String imagePath) {
		// TODO Auto-generated method stub
		try {
			return ImageIO.read(new File(imagePath));
		}catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	// Preprocess the image (resize, convert to grayscale, etc.)
	private BufferedImage preprocessImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
	    BufferedImage resizedImage = resizeImage(originalImage, targetWidth, targetHeight);
	    BufferedImage grayscaleImage = convertToGrayScale(resizedImage);
	    return grayscaleImage;
	}
	
	private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	    g.dispose();
	    return resizedImage;
	}
	
	private BufferedImage convertToGrayScale(BufferedImage originalImage) {
		
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		
		BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int rgb = originalImage.getRGB(x, y);
	            int r = (rgb >> 16) & 0xFF;
	            int g = (rgb >> 8) & 0xFF;
	            int b = rgb & 0xFF;
	            int gray = (int) (0.2989 * r + 0.5870 * g + 0.1140 * b);
	            int grayValue = (gray << 16) + (gray << 8) + gray; // Grayscale value for all three channels
	            grayscaleImage.setRGB(x, y, grayValue);
			}
		}
		return grayscaleImage;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Main app = new Main();
			app.setVisible(true);
		});
	}
}
