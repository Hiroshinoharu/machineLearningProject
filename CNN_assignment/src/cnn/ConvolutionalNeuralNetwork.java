/*
 * ConvolutionalNeuralNetwork : This class serves as the entry point for the program. It loads image data, constructs a neural network,
 *       trains the network, and evaluates its performance.
 *
 * Author: Max Ceban
 * Date: 26/03/2024
 */
package cnn;

import static java.util.Collections.shuffle;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import data.DataReader;
import data.Image;
import network.NetworkBuilder;
import network.NeuralNetwork;

public class ConvolutionalNeuralNetwork {

	public static void run() {

		Logger.createLogger();

		// Seed for random number generation
		long SEED = 123;

        // Starting message for data loading
		System.out.println("\nStarting data loading...");
		Logger.writeLogger("Data loaded");

        // Loading image data from files using DataReader
		List<Image> imageTest = new DataReader().readData("data/mnist_test.csv");
		List<Image> imageTrain = new DataReader().readData("data/mnist_train.csv");

        // Displaying the size of loaded image datasets
		System.out.println("Image Train size: "+ imageTrain.size());
		Logger.writeLogger("Image Train size: "+ imageTrain.size());

		System.out.println("Image test size: "+ imageTest.size());
		Logger.writeLogger("Image test size: "+ imageTest.size());


		Logger.writeLogger("Creating Neural Network architecture....");

        // Creating a Neural Network architecture using NetworkBuilder
		NetworkBuilder builder = new NetworkBuilder(28, 28, 256 * 100);
		builder.addConvolutionLayer(8, 8, 1, 0.1, SEED);
		builder.addMaxPoolLayer(3, 2);
		builder.addFullyConnectedLayer(10, 0.1, SEED);


        // Building the Neural Network
		NeuralNetwork net = builder.build();

		Logger.writeLogger("Neural Network bulit");

		// Create chart panel
		JPanel chartPanel = createChartPanel();
		
		// Create and configure the frame
		JFrame frame = new JFrame("Accuracy Over Epochs");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        
        XYSeries validationAccuracySeries = new XYSeries("Validation Accuracy");
        XYSeries epochSeries = new XYSeries("Epochs");
		
        // Testing the network's performance before training
		float rate = net.test(imageTest);
		Logger.writeLogger("Test data tested");
		System.out.println("Pre training success rate: "+ Math.round(rate) + "%");
		Logger.writeLogger("Pre training sucess rate: "+ Math.round(rate) + "%");


        // Number of training epochs
		int epochs = 5;
		Logger.writeLogger("Number of epochs " + epochs);
		
        validationAccuracySeries.add(0, 0);
        epochSeries.add(0, 0);
		
		for(int i = 0; i < epochs; i++) {

			// Shuffling the training data for each epoch
			shuffle(imageTrain);
			Logger.writeLogger("Training data shuffled");

			// Training the network with shuffled data
			net.train(imageTrain);
			Logger.writeLogger("Training data being trained");

			// Testing the network's performance after each training round
            float validationAccuracy = net.test(imageTest);
			Logger.writeLogger("Test data being tested");
            
			
            validationAccuracySeries.add(i + 1, validationAccuracy);
            epochSeries.add(i + 1, i + 1);

            System.out.println("Epoch: " + (i + 1) + ", Validation Accuracy: " + Math.round(validationAccuracy) + "%");
            Logger.writeLogger("Epoch: " + (i + 1) + ", Validation Accuracy: " + Math.round(validationAccuracy) + "%");

            updateChart(chartPanel, validationAccuracySeries, epochSeries);	
		}
	}

	// Method to create the chart panel
	 private static JPanel createChartPanel() {
	        XYSeriesCollection dataset = new XYSeriesCollection();
	        JFreeChart chart = ChartFactory.createXYLineChart("Accuracy Over Epochs", "Epochs", "Accuracy (%)", dataset);
	        ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        return chartPanel;
	    }
	
	// Method to update the chart with new data
	 private static void updateChart(JPanel chartPanel, XYSeries validationAccuracySeries, XYSeries epochSeries) {
	        XYSeriesCollection dataset = new XYSeriesCollection();
	        dataset.addSeries(validationAccuracySeries);
	        JFreeChart chart = ChartFactory.createXYLineChart("Accuracy Over Epochs", "Epochs", "Accuracy (%)", dataset);
	        chart.getXYPlot().setDataset(0, dataset);
	        ChartPanel cp = (ChartPanel) chartPanel;
	        cp.setChart(chart);
	    }
}