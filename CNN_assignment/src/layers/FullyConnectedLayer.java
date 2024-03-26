/*
 * FullyConnectedLayer: This class represents a fully connected layer in a neural network.
 *                      It connects every neuron in the input to every neuron in the output.
 * Author: Max Ceban
 * Date: 26/03/2024
 */
package layers;

import java.util.List;
import java.util.Random;

public class FullyConnectedLayer extends Layer{
	
	private long SEED; // Seed for random weight initialization
	private final double leak = 0.01; // Leakage parameter for ReLU activation function

	private double[][] _weights; // Weight matrix connecting input neurons to output neurons
	private int _inLength; // Number of input neurons
	private int _outLength; // Number of output neurons
	private double _learningRate; // Learning rate for weight updates
	
	private double[] lastZ; // Last computed weighted sum of inputs
	private double[] lastX; // Last input vector

	/**
	 * Constructor for FullyConnectedLayer
	 * @param _inLength Number of input neurons
     * @param _outLength Number of output neurons
     * @param SEED Seed for random weight initialization
     * @param learningRate Learning rate for weight updates
	 */
	public FullyConnectedLayer(int _intLength, int _outLength,long SEED,double learningRate) {
		
		this._inLength = _intLength;
		this._outLength = _outLength;
		this.SEED = SEED;
		this._learningRate = learningRate;
		
		_weights = new double[_intLength][_outLength];
		setRandomWeights();
	}
	
    // Forward pass through the fully connected layer
	public double[] fullyConnectedForwardPass(double[] input){
		
		lastX = input;
		
		double[] z = new double[_outLength];
		double[] out = new double[_outLength];
		
        // Calculate the weighted sum of inputs
		for(int i = 0; i < _inLength; i++) {
			for(int j = 0; j < _outLength; j++) {
				z[j] += input[i]*_weights[i][j];
			}
		}
		
		lastZ = z;
		
        // Apply ReLU activation function
		for(int i = 0; i < _inLength; i++) {
			for(int j = 0; j < _outLength; j++) {
				out[j] = relu(z[j]);
			}
		}
		
		return out;
	}
	
    // Override method: Get output using matrix input
	@Override
	public double[] getOutput(List<double[][]> input) {
		double[] vector = matrixToVector(input);
		return getOutput(vector);
	}

    // Override method: Perform backpropagation using matrix input
	@Override
	public void backPropagation(List<double[][]> dLdO) {
		double[] vector = matrixToVector(dLdO);
		backPropagation(vector);
	}

    // Override method: Get output using vector input
	@Override
	public double[] getOutput(double[] input) {
		double[] forwardPass = fullyConnectedForwardPass(input);
		
		if(_nextLayer != null) {
			return _nextLayer.getOutput(forwardPass);
		}
		else {
			return forwardPass;
		}
	}

    // Override method: Perform backpropagation using vector input
	@Override
	public void backPropagation(double[] dLdO) {
		
        // Chain Rule: Compute the gradients of the loss with respect to the inputs
		
		double[] dLdX = new double[_inLength];
		
		double dOdZ;
		double dZdW;
		double dLdW;
		double dZdX;
		
        // Calculate gradients for each input neuron
		for(int k = 0; k < _inLength;k++) {
			
			double dLdX_sum = 0;
			
            // Compute gradients for each output neuron
			for(int j = 0; j < _outLength; j++) {
				
                // Compute derivatives of activation function
				dOdZ = derivativeRelu(lastZ[j]);
				dZdW = lastX[k];
				dZdX = _weights[k][j];
				
                // Compute gradient of loss with respect to weights
				dLdW = dLdO[j]*dOdZ*dZdW;
				
                // Update weights using gradient descent
				_weights[k][j] -= dLdW*_learningRate;
				
                // Accumulate gradient of loss with respect to inputs
				dLdX_sum += dLdO[j]*dOdZ*dZdX;
			}
			
            // Assign accumulated gradient to the corresponding input neuron
			dLdX[k] = dLdX_sum;
		}
		
        // Propagate gradients to the previous layer
		if(_previousLayer != null) {
			_previousLayer.backPropagation(dLdX);
		}
	}

    // Override method: Get length of output
	@Override
	public int getOutputLength() {
		return 0;
	}

    // Override method: Get number of rows in output
	@Override
	public int getOutputRows() {
		return 0;
	}

    // Override method: Get number of columns in output
	@Override
	public int getOutputCols() {
		return 0;
	}

    // Override method: Get total number of elements in output
	@Override
	public int getOutputElements() {
		return _outLength;
	}
	
    // Method to initialize weights randomly
	public void setRandomWeights() {
		
		Random random = new Random(SEED);
		
		for(int i = 0; i < _inLength; i++) {
			for(int j = 0; j < _outLength; j++) {
				_weights[i][j] = random.nextGaussian();
			}
		}
		
	}
	
    // ReLU activation function
	public double relu(double input) {
		if(input <= 0) {
			return 0;
		}
		else {
			return input;
		}
	}
	
    // Derivative of ReLU activation function
	public double derivativeRelu(double input) {
		if(input <= 0) {
			return leak;
		}
		else {
			return 1;
		}
	}

}
