package layers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConvolutionLayer extends Layer{
	
	private long SEED;
	
	private List<double[][]> _filters;
	
	private int _filterSize;
	private int _stepSize;
	
	private int _inLength;
	private int _inRows;
	private int _inCols;

	/**
	 * @param _filterSize
	 * @param _stepSize
	 * @param _inLength
	 * @param _inRows
	 * @param _inCols
	 */
	public ConvolutionLayer(int _filterSize, int _stepSize, int _inLength, int _inRows, int _inCols,long SEED,int numFilters) {
		super();
		this._filterSize = _filterSize;
		this._stepSize = _stepSize;
		this._inLength = _inLength;
		this._inRows = _inRows;
		this._inCols = _inCols;
		this.SEED = SEED;
		
		generateRandomFilters(numFilters);
	}
	
	private void generateRandomFilters(int numFilters) {
		List<double[][]> filters = new ArrayList<>();
		Random random = new Random(SEED);
		
		for(int n = 0; n < numFilters; n++) {
			double[][] newFilter = new double[_filterSize][_filterSize];
			
			for(int i = 0; i < _filterSize; i++) {
				for(int j = 0; j < _filterSize; j++) {
					
					double value = random.nextGaussian();
					newFilter[i][j] = value;
				}
			}
			
			filters.add(newFilter);
			
		}
		
		_filters = filters;
		
	}

	public List<double[][]> convolutionForwardPass(List<double[][]> list){
		
		List<double[][]> output = new ArrayList<>();
		
		for(int m=0; m < list.size(); m++) {
			for(double[][] filter: _filters) {
				output.add(convolve(list.get(m),filter,_stepSize));
			}
		}
		
		return output;
	}

	private double[][] convolve(double[][] input, double[][] filter, int stepSize) {
		
		int outRows = (input.length - filter.length)/stepSize + 1;
		int outCols = (input[0].length - filter[0].length)/stepSize + 1;
		
		int inRows = input.length;
		int inCols = input[0].length;
		
		int fRows = filter.length;
		int fCols = filter[0].length;
		
		double[][] output = new double[outRows][outCols];
		
		int outRow = 0;
		int outCol;
		
		for(int i = 0; i <= inRows - fRows; i += stepSize) {
			
			outCol = 0;
			
			for(int j = 0; j <= inCols - fCols; j += stepSize) {
				
				double sum = 0.0;
				
				//Apply filter around this position
				for(int x = 0; x < fRows; x++) {
					for(int y = 0; y < fCols; y++) {
						int inputRowIndex = i+x;
						int inputColIndex = j+y;
						
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

	@Override
	public double[] getOutput(List<double[][]> input) {
		List<double[][]> output = convolutionForwardPass(input);
		return _nextLayer.getOutput(output);
	}

	@Override
	public void backPropagation(List<double[][]> dLdO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] getOutput(double[] input) {
		List<double[][]> matrixInput = vectorToMatrix(input, _inLength, _inRows, _inCols);
		return getOutput(matrixInput);
	}

	@Override
	public void backPropagation(double[] dLdO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getOutputLength() {
		return _filters.size()*_inLength;
	}

	@Override
	public int getOutputRows() {
		return(_inRows-_filterSize)/_stepSize + 1;
	}

	@Override
	public int getOutputCols() {
		return(_inCols-_filterSize)/_stepSize + 1;
	}

	@Override
	public int getOutputElements() {
		return getOutputCols()*getOutputRows()*getOutputLength();
	}

}
