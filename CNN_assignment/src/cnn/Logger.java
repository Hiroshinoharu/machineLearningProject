/*
 * Logger: This class provides logging functionalities for the ConvolutionalNeuralNetwork program.
 *         It allows creation of log files, writing log messages, and updating a JTextArea with log contents.
 *
 * Author: Max Ceban
 * Date: 31/03/2024
 */
package cnn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JTextArea;

public class Logger {

    // Define the log file name
    private static final String LOG_FILE = "Logger.txt";

    // Method to create a new logger file
	public static void createLogger() {
		try {
            PrintWriter myLogger = new PrintWriter(LOG_FILE);
            myLogger.close();
            System.out.println("Logger created: " + LOG_FILE);
        } catch (IOException e) {
            System.out.println("An error occurred while creating the logger.");
            e.printStackTrace();
        }
	}

    // Method to retrieve the logger for writing logs
	public static PrintWriter getLogger() {
        try {
            FileWriter fileWriter = new FileWriter(LOG_FILE, true); // Appending mode set to true
            PrintWriter printWriter = new PrintWriter(fileWriter);
            return printWriter;
        } catch (IOException e) {
            System.out.println("An error occurred while getting the logger.");
            e.printStackTrace();
            return null;
        }
    }

    // Method to write logs to the logger file
	public static void writeLogger(String text) {
		try (FileWriter fileWriter = new FileWriter(LOG_FILE, true); // Appending mode set to true
	             PrintWriter printWriter = new PrintWriter(fileWriter)) {
	             printWriter.println(text); // Write the provided text to the logger file
	        } catch (IOException e) {
	             System.out.println("An error occurred while writing to the logger.");
	             e.printStackTrace();
	        }
	}

	
	public static void updateTextArea(PrintWriter logger, JTextArea outputField) {
		// TODO Method to update a JTextArea with the contents of the logger file
		outputField.setText(""); // Clear the JTextArea

		if(logger != null) {
			logger.flush(); // Flush the logger to ensure all contents are written

			try(BufferedReader reader = new BufferedReader(new FileReader("Logger.txt"))){
				String line;
				// Read each line from the logger file and append it to the JTextArea
				while((line = reader.readLine()) != null) {
	                outputField.append(line + "\n"); // Append each line to the text area
				}
			}catch (IOException e) {
	            outputField.append("Error reading log file.\n");
			}
		}
		else {
			outputField.append("Logger is null\n"); // Indicate that the logger is null
		}

	}
}
