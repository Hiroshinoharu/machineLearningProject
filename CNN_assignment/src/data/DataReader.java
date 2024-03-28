/*
 * DataReader: This class is responsible for reading image data from a file and creating Image objects.
 *              It parses the file containing image data and labels, then constructs Image objects for each entry.
 * 
 * Author: Max Ceban
 * Date: 26/03/2024
 */
package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataReader {
	
	//Attributes 
	private final int rows = 28; // Number of rows in the image data
	private final int cols = 28; // Number of columns in the image data

	/**
	 * Reads image data from a file and constructs Image objects.
	 * 
	 * @param path The path to the file containing image data.
	 * @return A list of Image objects constructed from the data.
	 * @throws IllegalArgumentException if the file is not found.
	 */
	public List<Image> readData(String path){
		
		List<Image> images = new ArrayList<>(); // List to store the Image objects
		
		try(BufferedReader dataReader = new BufferedReader(new FileReader(path))){
			
			String line; // Variable to hold each line read from the file
			
			// Loop through each line in the file
			while((line = dataReader.readLine()) != null) {
				
				String[] lineItems = line.split(","); // Split the line by comma
				
				double[][] data = new double[rows][cols]; // 2D array to hold image data
				int label = Integer.parseInt(lineItems[0]); // Extract label from the first item
				
				int i = 1; // Counter to keep track of the current position in the line
				
				// Loop through each row and column of the image data
				for(int row = 0; row < rows; row++) {
					for(int col = 0; col < cols; col++) {
						data[row][col] = (double) Integer.parseInt(lineItems[i]); // Parse data and store in the array
						i++; // Move to the next item in the line
					}
				}
				// Create a new Image object using the extracted data and label, then add it to the list
                images.add(new Image(data, label));
			}
			
			
		}catch(Exception e) {
			throw new IllegalArgumentException("File not found " + path); // Throw exception if file not found or there's an issue reading it
		}
		return images; // Return the list of Image objects
	}
	
	
}
