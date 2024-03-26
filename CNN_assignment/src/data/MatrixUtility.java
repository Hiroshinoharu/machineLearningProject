/**
 * MatrixUtility: This class provides utility methods for matrix operations such as addition and multiplication.
 * Author: Max Ceban
 * Date: 26/03/2024
 */
package data;

public class MatrixUtility {

	/**
     * Performs element-wise addition of two 2D arrays.
     * 
     * @param a The first input 2D array.
     * @param b The second input 2D array.
     * @return The result of element-wise addition as a new 2D array.
     */
	public static double[][] add(double[][] a, double[][] b){
		
        // Initialize the output array
		double[][] out = new double[a.length][a[0].length];
		
        // Perform element-wise addition
		for(int i =0; i < a.length; i++) {
			for(int j = 0; j < a[0].length; j++) {
				out[i][j] = a[i][j] + b[i][j];
			}
		}
		
		return out;
		
	}
	
	/**
     * Performs element-wise addition of two 1D arrays.
     * 
     * @param a The first input 1D array.
     * @param b The second input 1D array.
     * @return The result of element-wise addition as a new 1D array.
     */
	public static double[] add(double[] a, double[] b){
        // Initialize the output array
		double[] out = new double[a.length];
		
        // Perform element-wise addition
		for(int i =0; i < a.length; i++) {
			out[i] = a[i] + b[i];
		}
		
		return out;
	}
	
	/**
     * Performs scalar multiplication of a 2D array.
     * 
     * @param a The input 2D array.
     * @param scalar The scalar value to multiply each element by.
     * @return The result of scalar multiplication as a new 2D array.
     */
	public static double[][] multiply(double[][] a, double scalar){
        // Initialize the output array
		double[][] out = new double[a.length][a[0].length];
		
        // Perform scalar multiplication
		for(int i =0; i < a.length; i++) {
			for(int j = 0; j < a[0].length; j++) {
				out[i][j] = a[i][j] * scalar;
			}
		}
		
		return out;
		
	}
	
	/**
     * Performs scalar multiplication of a 1D array.
     * 
     * @param a The input 1D array.
     * @param scalar The scalar value to multiply each element by.
     * @return The result of scalar multiplication as a new 1D array.
     */
	public static double[] multiply(double[] a, double scalar){
        // Initialize the output array
		double[] out = new double[a.length];
		
        // Perform scalar multiplication
		for(int i =0; i < a.length; i++) {
			out[i] = a[i]*scalar;
		}
		
		return out;
		
	}
}
