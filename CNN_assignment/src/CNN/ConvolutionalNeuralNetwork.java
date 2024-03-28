/*
 * CNN: This class serves as the entry point for the program. It loads image data, constructs a neural network,
 *       trains the network, and evaluates its performance.
 * 
 * Author: Max Ceban
 * Date: 26/03/2024
 */

package CNN;

import static java.util.Collections.shuffle;

import java.util.List;

import data.DataReader;
import data.Image;
import network.NetworkBuilder;
import network.NeuralNetwork;

public class ConvolutionalNeuralNetwork {
	
	public static void run() {
		
		// Seed for random number generation
		long SEED = 123;
		
        // Starting message for data loading
		System.out.println("Starting data loading...");
		
        // Loading image data from files using DataReader
		List<Image> imageTest = new DataReader().readData("data/mnist_test.csv");
		List<Image> imageTrain = new DataReader().readData("data/mnist_train.csv");

        // Displaying the size of loaded image datasets
		System.out.println("Image Train size: "+ imageTrain.size());
		System.out.println("Image test size: "+ imageTest.size());
		
        // Creating a Neural Network architecture using NetworkBuilder
		NetworkBuilder builder = new NetworkBuilder(28, 28, 256*100);
		builder.addConvolutionLayer(8, 8, 1, 0.1, SEED);
		builder.addMaxPoolLayer(3, 2);
		builder.addFullyConnectedLayer(10, 0.1, SEED);
		builder.addConvolutionLayer(8, 8, 1, 0.1, SEED);
		builder.addMaxPoolLayer(3, 2);
		builder.addFullyConnectedLayer(10, 0.1, SEED);
		
        // Building the Neural Network
		NeuralNetwork net = builder.build();
		
        // Testing the network's performance before training
		float rate = net.test(imageTest);
		System.out.println("Pre training sucess rate: "+ rate);
		
        // Number of training epochs
		int epochs = 3;
		
        // Training loop
		for(int i = 0; i < epochs; i++) {
            // Shuffling the training data for each epoch
			shuffle(imageTrain);
            // Training the network with shuffled data
			net.train(imageTrain);
            // Testing the network's performance after each training round
			rate = net.test(imageTest);
			System.out.println("Sucess rate after round " + i + ": " + rate);
		}
	}
	
}
