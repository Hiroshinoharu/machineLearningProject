package cnn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JTextArea;

public class Logger {
	
    private static final String LOG_FILE = "Logger.txt";
	
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
	
	public static PrintWriter getLogger() {
        try {
            FileWriter fileWriter = new FileWriter(LOG_FILE, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            return printWriter;
        } catch (IOException e) {
            System.out.println("An error occurred while getting the logger.");
            e.printStackTrace();
            return null;
        }
    }
	
	public static void writeLogger(String text) {
		try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
	             PrintWriter printWriter = new PrintWriter(fileWriter)) {
	             printWriter.println(text);
	        } catch (IOException e) {
	             System.out.println("An error occurred while writing to the logger.");
	             e.printStackTrace();
	        }
	}
	
	public static void updateTextField(PrintWriter logger, JTextArea outputField) {
		// TODO Auto-generated method stub
		outputField.setText("");
		
		if(logger != null) {
			logger.flush();
			
			try(BufferedReader reader = new BufferedReader(new FileReader("Logger.txt"))){
				String line;
				while((line = reader.readLine()) != null) {
	                outputField.append(line + "\n"); // Append each line to the text area
				}
			}catch (IOException e) {
	            outputField.append("Error reading log file.\n");
			}
		}
		else {
			outputField.append("Logger is null\n");
		}
		
	}
}
