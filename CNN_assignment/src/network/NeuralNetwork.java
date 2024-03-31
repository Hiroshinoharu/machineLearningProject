/*
 * NeuralNetwork: This class represents a neural network used for image classification.
 *                It includes methods for training the network, making predictions, and testing accuracy.
 * Author: Max Ceban
 * Date: 26/03/2024
 */

package network;

import static data.MatrixUtility.add;
import static data.MatrixUtility.multiply;

import java.util.ArrayList;
import java.util.List;

import data.Image;
import layers.Layer;

public class NeuralNetwork {

    List<Layer> _layers; // List of layers in the neural network
    double scaleFactor; //Factor used for scaling input data

    // Constructor
    public NeuralNetwork(List<Layer> _layers, double scaleFactor) {
        this._layers = _layers;
        this.scaleFactor = scaleFactor;
        linkLayers(); // Method to link layers together
    }

    // Method to link layers together in the network
    private void linkLayers(){

        if(_layers.size() <= 1){ // If there's only layer or less, return (nothing to link)
            return;
        }

        // Iterate through layers to set their previous and next layers
        for(int i = 0; i < _layers.size(); i++){
            if(i == 0){
                _layers.get(i).set_nextLayer(_layers.get(i+1)); // First layer's next layer is the second
            } else if (i == _layers.size()-1){
                _layers.get(i).set_previousLayer(_layers.get(i-1)); // Last layer's previous layer is the 2nd to last layer
            } else {
                _layers.get(i).set_previousLayer(_layers.get(i-1)); // Set previous layer
                _layers.get(i).set_nextLayer(_layers.get(i+1)); // Set next layer
            }
        }
    }

    //Method to calculate errors in the network output
    public double[] getErrors(double[] networkOutput, int correctAnswer){

    	int numClasses = networkOutput.length;

        double[] expected = new double[numClasses]; //Array to represent expected output

        expected[correctAnswer] = 1; // Set the correct answer index to 1, rest are 0

        // Calculate error by subtracting expected output from the actual output
        return add(networkOutput, multiply(expected, -1));
    }

    // Method to get the index of the maximum value in an array
    private int getMaxIndex(double[] in){

        double max = 0;
        int index = 0;

        // Iterate through array to find the maximum value and its index
        for(int i = 0; i < in.length; i++){
            if(in[i] >= max){
                max = in[i];
                index = i;
            }

        }

        return index; // Return index of maximum value
    }

    // Method to make a prediction (guess) based on an input image
    public int guess(Image image){

        List<double[][]> inList = new ArrayList<>();

        inList.add(multiply(image.getData(), (1.0/scaleFactor))); // Scale input data

        double[] out = _layers.get(0).getOutput(inList); // Get output from the 1st layer
        int guess = getMaxIndex(out); // Get the index of the maximum output value

        return guess; // Return the predicted class
    }

    //Method to test the accuracy of the neural network on a set of images
    public float test (List<Image> images){

    	int correct = 0;

    	// Iterate through images and check if the network's prediction matches the image's label
        for(Image img: images){
            int guess = guess(img);

            if(guess == img.getLabel()){
                correct++;
            }
        }

        // Return the accuracy as a percentage
        return((float)correct/images.size() * 100);
    }

    // Method to train the neural network using a set of images
    public void train (List<Image> images){

    	// Iterate through images and perform backpropagation to train the network
        for(Image img:images){
            List<double[][]> inList = new ArrayList<>();
            inList.add(multiply(img.getData(), (1.0/scaleFactor))); // Scale input data

            double[] out = _layers.get(0).getOutput(inList); // Get output from the 1st layer
            double[] dldO = getErrors(out, img.getLabel()); // Calculate error

            _layers.get((_layers.size()-1)).backPropagation(dldO); // Perform backpropagation
        }

    }

}