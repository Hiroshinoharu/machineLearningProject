/*
 * MaxPoolLayer: This class represents a max pooling layer used in convolutional neural networks.
 *               It downsamples the input by taking the maximum value within a sliding window.
 * Author: Max Ceban
 * Date: 26/03/2024
 */

package layers;

import java.util.ArrayList;
import java.util.List;

public class MaxPoolLayer extends Layer {
	
	//Attributes
	private int _stepSize; // Step size for the sliding window
	private int _windowSize; // Size of the pooling window
	
	private int _inLength; // Number of input channels
	private int _inRows; // Number of input rows 
	private int _inCols; // Number of input columns
	
	List<int[][]> _lastMaxRow; // Store indices of max elements along rows
	List<int[][]> _lastMaxCol; // Store indices of max elements along columns

	/**
	 * Constructor for MaxPoolLayer
	 * @param _stepSize The step size for the sliding window
	 * @param _windowSize the size of the pooling window
	 * @param _inLength Number of input channels
	 * @param _inRows Number of input rows
	 * @param _inCols Number of input columns
	 */
	public MaxPoolLayer(int _stepSize, int _windowSize, int _inLength, int _inRows, int _inCols) {
		super();
		this._stepSize = _stepSize;
		this._windowSize = _windowSize;
		this._inLength = _inLength;
		this._inRows = _inRows;
		this._inCols = _inCols;
	}
	
	/**
     * Performs forward pass of max pooling on the input
     * @param input List of input matrices (channels)
     * @return List of pooled matrices (channels)
     */
	public List<double[][]> maxPoolForwardPass(List<double[][]> input){
		
		List<double[][]> output = new ArrayList<>();
		_lastMaxRow = new ArrayList<>();
		_lastMaxCol = new ArrayList<>();
		
		// Loop through each input matrix and perform max pooling
		for(int l = 0; l<input.size(); l++) {
			output.add(pool(input.get(l)));
		}
		return output;
		
	}
	
	/**
     * Performs max pooling on a single input matrix
     * @param input Input matrix
     * @return Pooled matrix
     */
	public double[][] pool(double[][] input){
		
		double[][] output = new double[getOutputRows()][getOutputCols()];
		
		int[][] maxRows = new int[getOutputRows()][getOutputCols()];
		int[][] maxCols = new int[getOutputRows()][getOutputCols()];

		// Loop through the input matrix with the sliding window
		for(int r = 0; r < getOutputRows(); r += _stepSize) {
			for(int c = 0; c < getOutputCols(); c+= _stepSize) {
				
				double max = 0.0;
				maxRows[r][c] = -1;
				maxCols[r][c] = -1;
				
				// Find the maxium value within the window
				for(int x = 0; x < _windowSize; x++) {
					for(int y =0; y < _windowSize; y++) {
						if(max < input[r+x][c+y]) {
							max = input[r+x][c+y];
							maxRows[r][c] = r+x;
							maxCols[r][c] = c+y;
						}
					}
				}
				
				output[r][c] = max;
			}
		}
		
		// Store indices of max elements for backpropagation 
		_lastMaxRow.add(maxRows);
		_lastMaxCol.add(maxCols);
		
		return output;
	}

	@Override
	public void backPropagation(List<double[][]> dLdO) {
		
		List<double[][]> dXdL = new ArrayList<>();
		
		int l = 0;
		
		// Backpropagate errors to previous layer
		for(double[][] array : dLdO) {
			
			double[][] error = new double[_inRows][_inCols];
			
			for(int r =0; r < getOutputRows(); r++) {
				for(int c =0; c < getOutputCols(); c++) {
					int max_i = _lastMaxRow.get(l)[r][c];
					int max_j = _lastMaxCol.get(l)[r][c];
					
					if(max_i != -1) {
						error[max_i][max_j] += array[r][c];
					}
					
				}
			}
			
			dXdL.add(error);
			l++;		
		}
		
		// Propagate errors to previous layer
		if(_previousLayer != null) {
		   _previousLayer.backPropagation(dXdL);
		}
	}

	@Override
	public double[] getOutput(double[] input) {
		List<double[][]> matrixList = vectorToMatrix(input, _inLength, _inRows, _inCols);
		return getOutput(matrixList);
	}
	
	@Override
	public double[] getOutput(List<double[][]> input) {
		List<double[][]> outputPool = maxPoolForwardPass(input);
		return _nextLayer.getOutput(outputPool);
	}

	@Override
	public void backPropagation(double[] dLdO) {
		List<double[][]> matrixList = vectorToMatrix(dLdO, getOutputLength(), getOutputRows(), getOutputCols());
		backPropagation(matrixList);
	}

	@Override
	public int getOutputLength() {
		return _inLength;
	}

	@Override
	public int getOutputRows() {
		return (_inRows - _windowSize)/_stepSize + 1;
	}

	@Override
	public int getOutputCols() {
		return (_inCols - _windowSize)/_stepSize + 1;
	}

	@Override
	public int getOutputElements() {
		return _inLength*getOutputCols()*getOutputRows();
	}

}
