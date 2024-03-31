package layers;

import static data.MatrixUtility.add;
import static data.MatrixUtility.multiply;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ConvolutionLayer extends Layer{

 // Attributes

    // Seed for random filter initialization
	private long SEED;

    // List of filters (kernels) for convolution
	private List<double[][]> _filters;

    // Size of each filter
	private int _filterSize;

    // Stride size for convolution
	private int _stepSize;

    // Number of input channels
	private int _inLength;

    // Height of input feature maps
	private int _inRows;

    // Width of input feature maps
	private int _inCols;

    // Learning rate for weight updates
	private double _learningRate;

    // Last input feature maps used in forward pass
	private List<double[][]> _lastInput;

	/**
     * Constructor for ConvolutionLayer
     * @param _filterSize Size of each filter
     * @param _stepSize Stride size for convolution
     * @param _inLength Number of input channels
     * @param _inRows Height of input feature maps
     * @param _inCols Width of input feature maps
     * @param SEED Seed for random filter initialization
     * @param numFilters Number of filters to be generated
     * @param learningRate Learning rate for weight updates
     */
	public ConvolutionLayer(int _filterSize, int _stepSize, int _inLength, int _inRows, int _inCols,long SEED,int numFilters,double learningRate) {
		super();
		this._filterSize = _filterSize;
		this._stepSize = _stepSize;
		this._inLength = _inLength;
		this._inRows = _inRows;
		this._inCols = _inCols;
		this.SEED = SEED;
		_learningRate = learningRate;

		generateRandomFilters(numFilters);
	}

	/**
     * Generates random filters (kernels) for convolution
     * @param numFilters Number of filters to be generated
     */
	private void generateRandomFilters(int numFilters) {
		List<double[][]> filters = new ArrayList<>();
		Random random = new Random(SEED);

		for(int n = 0; n < numFilters; n++) {
			double[][] newFilter = new double[_filterSize][_filterSize];

			for(int i = 0; i < _filterSize; i++) {
				for(int j = 0; j < _filterSize; j++) {
                    // Initialize filter weights with random Gaussian values
					double value = random.nextGaussian();
					newFilter[i][j] = value;
				}
			}

			filters.add(newFilter);

		}

		_filters = filters;

	}

	/**
     * Performs forward pass through the convolutional layer
     * @param list Input feature maps
     * @return Output feature maps after convolution
     */
	public List<double[][]> convolutionForwardPass(List<double[][]> list){

		_lastInput = list;

		List<double[][]> output = new ArrayList<>();

        // Iterate over each input feature map and each filter
		for(int m=0; m < list.size(); m++) {
			for(double[][] filter: _filters) {
                // Apply convolution operation for each filter
				output.add(convolve(list.get(m),filter,_stepSize));
			}
		}

		return output;
	}

	/**
     * Performs 2D convolution operation
     * @param input Input feature map
     * @param filter Convolution filter
     * @param stepSize Stride size
     * @return Output feature map after convolution
     */
	private double[][] convolve(double[][] input, double[][] filter, int stepSize) {

        // Calculate dimensions of output feature map
		int outRows = (input.length - filter.length)/stepSize + 1;
		int outCols = (input[0].length - filter[0].length)/stepSize + 1;

		int inRows = input.length;
		int inCols = input[0].length;

		int fRows = filter.length;
		int fCols = filter[0].length;

		double[][] output = new double[outRows][outCols];

		int outRow = 0;
		int outCol;

        // Convolution operation
		for(int i = 0; i <= inRows - fRows; i += stepSize) {
			outCol = 0;
			for(int j = 0; j <= inCols - fCols; j += stepSize) {
				double sum = 0.0;
				//Apply filter around this position
				for(int x = 0; x < fRows; x++) {
					for(int y = 0; y < fCols; y++) {
						int inputRowIndex = i+x;
						int inputColIndex = j+y;
                        // Compute element-wise multiplication and accumulate sum
						double value = filter[x][y] * input[inputRowIndex][inputColIndex];
						sum += value;
					}
				}

				output[outRow][outCol] = sum;
				outCol++;

			}

			outRow++;

		}

		return output;

	}

	/**
     * Performs space-to-array operation to expand feature maps
     * @param input Input feature map
     * @return Expanded feature map
     */
	public double[][] spaceArray(double[][] input){

	    // If the step size is 1, return the input array as it is
		if(_stepSize == 1) {
			return input;
		}

	    // Calculate the dimensions of the output array after upsampling
		int outRows = (input.length - 1)*_stepSize+1;
		int outCols = (input[0].length - 1)*_stepSize+1;

	    // Initialize the output array
		double[][] output = new double[outRows][outCols];

	    // Fill the output array by inserting zeros between elements of the input array based on the step size
		for(int i = 0; i < input.length; i++) {
			for(int j = 0; j < input[0].length; j++) {
				output[i*_stepSize][j*_stepSize] = input[i][j];
			}
		}

	    // Return the upsampled array with spaced-out elements
		return output;
	}

	@Override
	public double[] getOutput(List<double[][]> input) {
		List<double[][]> output = convolutionForwardPass(input);
		return _nextLayer.getOutput(output);
	}

	@Override
	public void backPropagation(List<double[][]> dLdO) {

	    // Initialize lists to store the gradients of filters and errors for the previous layer
		List<double[][]> filtersDelta = new ArrayList<>();
		List<double[][]> dLdOPreviousLayer = new ArrayList<>();

	    // Initialize the filtersDelta list with zeros
		for(int f = 0; f < _filters.size();f++) {
			filtersDelta.add(new double[_filterSize][_filterSize]);
		}

	    // Iterate over each input feature map and its corresponding error
		for(int i =0; i<_lastInput.size();i++) {

	        // Initialize an error matrix for the input feature map
			double[][] errorForInput = new double[_inRows][_inCols];

	        // Iterate over each filter in the layer
			for(int f = 0; f < _filters.size();f++) {

	            // Get the current filter and its corresponding error
				double[][] currFilter = _filters.get(f);
				double[][] error = dLdO.get(i*_filters.size()+f);

	            // Perform full convolution between the error and the transposed filter
				double[][] spacedError = spaceArray(error);
				double[][] dLdF = convolve(_lastInput.get(i), spacedError, 1);

	            // Update the filter gradients by multiplying the convolution result with the learning rate
				double[][] delta = multiply(dLdF,_learningRate*-1);
				double[][] newTotalDelta = add(filtersDelta.get(f),delta);
				filtersDelta.set(f, newTotalDelta);

	            // Calculate the error contribution for the input feature map by performing full convolution
				double[][] flippedError = flipArrayHorizontal(flipArrayVertical(spacedError));
				errorForInput = add(errorForInput, fullConvolve(currFilter, flippedError));

			}

	        // Add the error contribution for the input feature map to the list
			dLdOPreviousLayer.add(errorForInput);

		}

	    // Update the filters by adding the accumulated gradients
		for(int f =0; f< _filters.size(); f++) {
			double[][] modified = add(filtersDelta.get(f),_filters.get(f));
			_filters.set(f, modified);
		}

	    // Propagate the errors to the previous layer if it exists
		if(_previousLayer != null) {
			_previousLayer.backPropagation(dLdOPreviousLayer);
		}

	}


	/**
     * Flips the 2D array horizontally
     * @param array Input 2D array
     * @return Flipped array
     */
	public double[][] flipArrayHorizontal(double[][] array){

	    // Get the number of rows and columns in the input array
		int rows = array.length;
		int cols = array[0].length;

	    // Create a new array to store the horizontally flipped version
		double[][] output = new double[rows][cols];

	    // Iterate through each element in the input array
		for(int i = 0;i < rows; i++) {
			for(int j = 0;j < rows; j++) {
	            // Copy elements from the input array to the output array, flipping horizontally
				output[rows-i-1][j] = array[i][j];
			}
		}

	    // Return the horizontally flipped array
		return output;
	}


	/**
     * Flips the 2D array vertically
     * @param array Input 2D array
     * @return Flipped array
     */
	public double[][] flipArrayVertical(double[][] array){

		int rows = array.length;
		int cols = array[0].length;

		double[][] output = new double[rows][cols];

		for(int i = 0;i < rows; i++) {
			for(int j = 0;j < rows; j++) {
				output[i][cols-j-1] = array[i][j];
			}
		}

		return output;
	}


	/**
     * Performs full convolution operation
     * @param input Input feature map
     * @param filter Convolution filter
     * @return Output feature map after convolution
     */
	private double[][] fullConvolve(double[][] input, double[][] filter) {

	    // Calculate the dimensions of the output array
		int outRows = (input.length + filter.length) + 1;
		int outCols = (input[0].length + filter[0].length) + 1;

		int inRows = input.length;
		int inCols = input[0].length;

		int fRows = filter.length;
		int fCols = filter[0].length;


	    // Initialize the output array
		double[][] output = new double[outRows][outCols];

		int outRow = 0;
		int outCol;

	    // Iterate over each position in the output array
		for(int i = -fRows + 1; i < inRows; i++) {

			outCol = 0;

			for(int j = -fCols; j < inCols; j++) {

				double sum = 0.0;

				//Apply filter around this position
				for(int x = 0; x < fRows; x++) {
					for(int y = 0; y < fCols; y++) {
						int inputRowIndex = i+x;
						int inputColIndex = j+y;
	                    // Check if the current position is within the bounds of the input matrix
						if(inputRowIndex >= 0 && inputColIndex >= 0 && inputRowIndex < inRows && inputColIndex < inCols) {
							double value = filter[x][y] * input[inputRowIndex][inputColIndex];
							sum += value;
						}
					}
				}

	            // Store the result of the convolution in the output array
				output[outRow][outCol] = sum;
				outCol++;

			}

			outRow++;

		}

	    // Return the result of the full convolution operation
		return output;

	}

	/**
	 * Computes the output of the layer given a 1D input array by first converting it into a list of 2D matrices,
	 * then passing it to the getOutput method that takes a list of matrices as input.
	 *
	 * @param input The 1D input array to be processed.
	 * @return The output of the layer computed from the input array.
	 */
	@Override
	public double[] getOutput(double[] input) {
	    // Convert the 1D input array into a list of 2D matrices
		List<double[][]> matrixInput = vectorToMatrix(input, _inLength, _inRows, _inCols);
	    // Compute the output using the list of matrices and return it
		return getOutput(matrixInput);
	}

	/**
	 * Performs backpropagation for the layer given a 1D array representing the gradients of the loss function
	 * with respect to the layer's output. Converts the 1D array into a list of 2D matrices and passes it to
	 * the backPropagation method that handles matrices as input.
	 *
	 * @param dLdO The 1D array representing the gradients of the loss function with respect to the layer's output.
	 */
	@Override
	public void backPropagation(double[] dLdO) {
	    // Convert the 1D array of gradients into a list of 2D matrices
		List<double[][]> matrixInput = vectorToMatrix(dLdO, _inLength, _inRows, _inCols);
	    // Perform backpropagation using the list of matrices
		backPropagation(matrixInput);
	}

	/**
	 * Computes the length of the output vector produced by the layer.
	 *
	 * @return The length of the output vector.
	 */
	@Override
	public int getOutputLength() {
	    // Compute the output length as the product of the number of filters and the input length
		return _filters.size()*_inLength;
	}

	/**
	 * Computes the number of rows in the output produced by the layer.
	 *
	 * @return The number of rows in the output.
	 */
	@Override
	public int getOutputRows() {
	    // Compute the number of rows in the output based on the input size, filter size, and step size
		return(_inRows-_filterSize)/_stepSize + 1;
	}

	/**
	 * Computes the number of columns in the output produced by the layer.
	 *
	 * @return The number of columns in the output.
	 */
	@Override
	public int getOutputCols() {
	    // Compute the number of rows in the output based on the input size, filter size, and step size
		return(_inCols-_filterSize)/_stepSize + 1;
	}

	/**
	 * Computes the total number of elements in the output produced by the layer.
	 *
	 * @return The total number of elements in the output.
	 */
	@Override
	public int getOutputElements() {
	    // Compute the total number of elements in the output as the product of the number of rows, columns, and length
		return getOutputCols()*getOutputRows()*getOutputLength();
	}

}
