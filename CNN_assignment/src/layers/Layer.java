package layers;

import java.util.ArrayList;
import java.util.List;

public abstract class Layer {
	
	protected Layer _nextLayer;
	protected Layer _previousLayer;
	
	public Layer get_nextLayer() {
		return _nextLayer;
	}
	public void set_nextLayer(Layer _nextLayer) {
		this._nextLayer = _nextLayer;
	}
	public Layer get_previousLayer() {
		return _previousLayer;
	}
	public void set_previousLayer(Layer _previousLayer) {
		this._previousLayer = _previousLayer;
	}
	
	//Polymorphism - calls this getOutput and backPropagation as a matrix
	public abstract double[] getOutput(List<double[][]> input);
	public abstract void backPropagation(List<double[][]> dLdO );
	
	//Polymorphism - calls this getOutput and backPropagation as a vector
	public abstract double[] getOutput(double[] input);
	public abstract void backPropagation(double[] dLdO );
	
	public abstract int getOutputLength();
	public abstract int getOutputRows();
	public abstract int getOutputCols();
	public abstract int getOutputElements();

	
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