import java.util.List;

import static java.util.Collections.shuffle;

import data.DataReader;
import data.Image;
import network.NetworkBuilder;
import network.NeuralNetwork;

public class Main {

	public static void main(String[] args) {
		
		long SEED = 123;
		
		System.out.println("Starting data loading...");
		
		List<Image> imageTest = new DataReader().readData("data/mnist_test.csv");
		List<Image> imageTrain = new DataReader().readData("data/mnist_train.csv");

		System.out.println("Image Train size: "+ imageTrain.size());
		System.out.println("Image test size: "+ imageTest.size());
		
		NetworkBuilder builder = new NetworkBuilder(28, 28, 256*100);
		builder.addConvolutionLayer(8, 8, 1, 0.1, SEED);
		builder.addMaxPoolLayer(3, 2);
		builder.addFullyConnectedLayer(10, 0.1, SEED);
		
		NeuralNetwork net = builder.build();
		
		float rate = net.test(imageTest);
		System.out.println("Pre training sucess rate: "+ rate);
		
		int epochs = 3;
		
		for(int i = 0; i < epochs; i++) {
			shuffle(imageTrain);
			net.train(imageTrain);
			rate = net.test(imageTest);
			System.out.println("Sucess rate after round " + i + ": " + rate);
		}
	}

}
