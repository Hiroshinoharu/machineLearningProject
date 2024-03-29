/*
 * NetworkBuilder: This class is responsible for building neural networks by adding different types of layers.
 *                 It allows the construction of convolutional neural networks (CNNs) for image classification tasks.
 * Author: Max Ceban
 * Date: 26/03/2024
 */

package network;

import java.util.ArrayList;
import java.util.List;

import layers.ConvolutionLayer;
import layers.FullyConnectedLayer;
import layers.Layer;
import layers.MaxPoolLayer;

public class NetworkBuilder {
	
	private NeuralNetwork net; // The neural network being built
	private int _inputRows; // Number of rows in the input range 
	private int _inputCols; // Number of columns in the input image
	private double _scaleFactor; // Factor used for scaling input data
	List<Layer> _layers; // List to store the layers of the network
	
	/**
	 * Constructor to initialize the NetworkBuilder object with input dimensions and scale factor.
	 * @param _inputRows Number of rows in the input range
	 * @param _inputCols Number of columns in the input image
	 * @param _scaleFactor Factor used for scaling input data
	 */
	public NetworkBuilder(int _inputRows, int _inputCols, double _scaleFactor) {
		super();
		this._inputRows = _inputRows;
		this._inputCols = _inputCols;
		this._scaleFactor = _scaleFactor;
		_layers = new ArrayList<>(); // Initialize thee list of layers
	}
	
	// Method to add a convolutional layer to the network
	public void addConvolutionLayer(int numFilters, int filterSize, int stepSize, double learningRate, long SEED) {
		
		if(_layers.isEmpty()) {
			_layers.add(new ConvolutionLayer(filterSize, stepSize, 1, _inputRows, _inputCols, SEED, numFilters, learningRate));
		} else {
			Layer prev = _layers.get(_layers.size() - 1);
			_layers.add(new ConvolutionLayer(filterSize, stepSize, prev.getOutputLength(), prev.getOutputRows(), prev.getOutputCols(), SEED, numFilters, learningRate));
		}
	}
	
	// Method to add a max pooling layer to the network
	public void addMaxPoolLayer(int windowSize, int stepSize) {
		
		if(_layers.isEmpty()) {
			_layers.add(new MaxPoolLayer(stepSize, windowSize, 1, _inputRows, _inputCols));
		} else {
			Layer prev = _layers.get(_layers.size() - 1);
			_layers.add(new MaxPoolLayer(stepSize, windowSize, prev.getOutputLength(), prev.getOutputRows(), prev.getOutputCols()));
		}
	}
	
	// Method to add a fully connected layer to the network
	public void addFullyConnectedLayer(int outLength, double learningRate, long SEED) {
		if(_layers.isEmpty()) {
			_layers.add(new FullyConnectedLayer(_inputCols*_inputRows, outLength, SEED, learningRate));
		} else {
			Layer prev = _layers.get(_layers.size() - 1);
			_layers.add(new FullyConnectedLayer(prev.getOutputElements(), outLength, SEED, learningRate));
		}
	}
	
	// Method to build the neural network using the constructed layers
	public NeuralNetwork build() {
		net = new NeuralNetwork(_layers,_scaleFactor);
		return net;
	}

}
