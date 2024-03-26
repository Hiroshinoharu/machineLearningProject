/*
 * Layer: This abstract class represents a generic layer in a neural network.
 *        It defines common properties and behaviors for all types of layers.
 * Author: Max Ceban
 * Date: 26/03/2024
 */
package layers;

import java.util.ArrayList;
import java.util.List;

//Abstract class representing a layer in a neural network
public abstract class Layer {
	
    // Encapsulation: Private fields with public getters and setters
	protected Layer _nextLayer;
	protected Layer _previousLayer;
	
    // Getter for the next layer
	public Layer get_nextLayer() {
		return _nextLayer;
	}
	
    // Setter for the next layer
	public void set_nextLayer(Layer _nextLayer) {
		this._nextLayer = _nextLayer;
	}
	
    // Getter for the previous layer
	public Layer get_previousLayer() {
		return _previousLayer;
	}
	
    // Setter for the previous layer
	public void set_previousLayer(Layer _previousLayer) {
		this._previousLayer = _previousLayer;
	}
	
	// Abstraction: Abstract methods defining behavior to be implemented by subclasses
    // Polymorphism: Method overloading based on input type
	public abstract double[] getOutput(List<double[][]> input);
	public abstract void backPropagation(List<double[][]> dLdO );
	
	// Abstraction: Abstract methods defining behavior to be implemented by subclasses
    // Polymorphism: Method overloading based on input type
	public abstract double[] getOutput(double[] input);
	public abstract void backPropagation(double[] dLdO );
	
    // Abstraction: Abstract methods defining behavior to be implemented by subclasses
	public abstract int getOutputLength();
	public abstract int getOutputRows();
	public abstract int getOutputCols();
	public abstract int getOutputElements();

	
    // Helper method to convert matrix representation to vector
	public double[] matrixToVector(List<double[][]> input) {
		
		int length = input.size();
		int rows = input.get(0).length;
		int cols = input.get(0)[0].length;
		
		double[] vector = new double[length*rows*cols];
		
		int i = 0;
		for(int l = 0; l < length; l++) {
			for(int r = 0; r < rows; r++) {
				for(int c = 0; c < cols; c++) {
					vector[i] = input.get(l)[r][c];
					i++;
				}
			}
		}
		
		return vector;
	}
	
    // Helper method to convert vector representation to matrix
	public List<double[][]> vectorToMatrix(double[] input, int length, int rows, int cols){
		
		List<double[][]> out = new ArrayList<>();
		
		int i = 0;
		
		for(int l = 0; l < length; l++) {
			
			double[][] matrix = new double[rows][cols];
			
			for(int r = 0; r < rows; r++) {
				for(int c = 0; c < cols; c++) {
					matrix[r][c] = input[i];
					i++;
				}
			}
			
			out.add(matrix);
		}
		
		return out;
	}
}