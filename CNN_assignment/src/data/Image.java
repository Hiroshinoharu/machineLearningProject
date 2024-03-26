/*
 * Image: This class represents an image along with its associated label.
 *        It is used to store image data and corresponding labels for training or testing.
 * 
 * Author: Max Ceban
 * Date: 26/03/2024
 */
package data;

public class Image {
	
	//Attributes
	
    /** The pixel data of the image. */
	private double[][] data;

    /** The label associated with the image. */
	private int label;

	/**
     * Gets the pixel data of the image.
     * 
     * @return The pixel data.
     */
	public double[][] getData() {
		return data;
	}

	/**
     * Gets the label associated with the image.
     * 
     * @return The image label.
     */
	public int getLabel() {
		return label;
	}
	
	/**
     * Constructs an Image object with the given pixel data and label.
     * 
     * @param data The pixel data of the image.
     * @param label The label associated with the image.
     */
	public Image(double[][] data, int label) {
		this.data = data;
		this.label = label;
	}
	
	/**
     * Returns a string representation of the Image object, including its label and pixel data.
     * 
     * @return A string representation of the Image.
     */
	@Override
	public String toString() {
		String s  = label + ", \n";
		
		for(int i = 0;i < data.length; i++) {
			for(int j=0; j < data[0].length;j++) {
				s += data[i][j] + ", ";
			}
			s+= "\n";
		}
		
		return s;
	}

}
