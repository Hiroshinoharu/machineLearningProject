package layers;

import java.util.List;
import java.util.Random;

public class FullyConnectedLayer extends Layer{
	
	private long SEED;
	private final double leak = 0.01;

	private double[][] _weights;
	private int _inLength;
	private int _outLength;
	private double _learningRate;
	
	private double[] lastZ;
	private double[] lastX;

	/**
	 * @param _weights
	 * @param _intLength
	 * @param _outLength
	 */
	public FullyConnectedLayer(int _intLength, int _outLength,long SEED,double learningRate) {
		
		this._inLength = _intLength;
		this._outLength = _outLength;
		this.SEED = SEED;
		this._learningRate = learningRate;
		
		_weights = new double[_intLength][_outLength];
		setRandomWeights();
	}
	
	public double[] fullyConnectedForwardPass(double[] input){
		
		lastX = input;
		
		double[] z = new double[_outLength];
		double[] out = new double[_outLength];
		
		for(int i = 0; i < _inLength; i++) {
			for(int j = 0; j < _outLength; j++) {
				z[j] += input[i]*_weights[i][j];
			}
		}
		
		lastZ = z;
		
		for(int i = 0; i < _inLength; i++) {
			for(int j = 0; j < _outLength; j++) {
				out[j] = relu(z[j]);
			}
		}
		
		return out;
	}
	
	@Override
	public double[] getOutput(List<double[][]> input) {
		double[] vector = matrixToVector(input);
		return getOutput(vector);
	}

	@Override
	public void backPropagation(List<double[][]> dLdO) {
		double[] vector = matrixToVector(dLdO);
		backPropagation(vector);
	}

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

	@Override
	public void backPropagation(double[] dLdO) {
		
		//Chain Rule
		double[] dLdX = new double[_inLength];
		
		double dOdZ;
		double dZdW;
		double dLdW;
		double dZdX;
		
		for(int k = 0; k < _inLength;k++) {
			
			double dLdX_sum = 0;
			
			for(int j = 0; j < _outLength; j++) {
				
				dOdZ = derivativeRelu(lastZ[j]);
				dZdW = lastX[k];
				dZdX = _weights[k][j];
				
				dLdW = dLdO[j]*dOdZ*dZdW;
				
				_weights[k][j] -= dLdW*_learningRate;
				
				dLdX_sum += dLdO[j]*dOdZ*dZdX;
			}
			
			dLdX[k] = dLdX_sum;
		}
		
		if(_previousLayer != null) {
			_previousLayer.backPropagation(dLdX);
		}
	}

	@Override
	public int getOutputLength() {
		return 0;
	}

	@Override
	public int getOutputRows() {
		return 0;
	}

	@Override
	public int getOutputCols() {
		return 0;
	}

	@Override
	public int getOutputElements() {
		return _outLength;
	}
	
	public void setRandomWeights() {
		
		Random random = new Random(SEED);
		
		for(int i = 0; i < _inLength; i++) {
			for(int j = 0; j < _outLength; j++) {
				_weights[i][j] = random.nextGaussian();
			}
		}
		
	}
	
	public double relu(double input) {
		if(input <= 0) {
			return 0;
		}
		else {
			return input;
		}
	}
	
	public double derivativeRelu(double input) {
		if(input <= 0) {
			return leak;
		}
		else {
			return 1;
		}
	}

}
