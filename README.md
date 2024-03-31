## Assignment: OO Programming Sem 2 2023 - Java

## Project: Convolutional Neural Network from scratch using JAVA and OOP concepts using the MNIST dataset

# Overview
The project's goal is to create a Convolutional Neural Network (CNN) from scratch in Java, following Object-Oriented Programming (OOP) concepts. The implementation will concentrate on processing the MNIST dataset, which is a prominent benchmark for handwritten digit recognition tasks.

# Project Structure:
To ensure code organisation and modularity, the project is divided into five packages, each responsible for a certain functionality.

** 1. Default Package: ** This default package is responsible for handling the front end interface for the user when the program runs in a GUI where all the information passed will display to the user on top of that it handles all user interaction and triggers the CNN training

** 2. CNN package: ** this package's function is to serve as a entry point for the CNN training process in which it loads image data, constructs a neural network, trains with the data and evalutates the performance and displays a line graph giving a visual representation of how it was trained for each epoch. This package also contains a Logger in which it logs every action done and writes it to a file in which is displayed to the main GUI.

**3. Data package: ** this package helps process the data processed which in case is a CSV value and reads in the data and then turns into data (2D array) in which can be accessed by the CNN for training and testing purposes. This package contains a utility class in which helps perform matrix operations such as adding and multiplying. 

**4. Layer package : ** this package contains all the layers needed to operate a CNN, it contains a Convolution Layer, Fully Connected Layer and a Max Pool Layer abstracted from a general Layer class.

** 5. Network Package: ** this package is responsible for buliding all the layers to a CNN for image classification tasks. This package contains method to train the network and testing the accuracy and for making predictions

## Object-Oriented Principles
Encapsulation, inheritance, polymorphism, and abstraction are examples of OOP principles employed throughout the implementation. 

The classes and objects are intended to reflect real-world entities and encapsulate related functionality, hence encouraging code reuse, maintainability, and scalability.

## Project Scopes 
The goal of this project is to develop a functional CNN implementation that can perform well on the MNIST dataset in terms of digit classification accuracy.

Furthermore, it aims to demonstrate competency in applying OOP ideas to complicated machine learning problems, laying the groundwork for future research and development in the field of deep learning with Java.

## List of classes
* Main: This class represents a GUI application for training a Convolutional Neural Network (CNN). It initializes the user interface, handles user interactions, and triggers CNN training.
* ConvolutionalNeuralNetwork:  This class serves as the entry point for the program. It loads image data, constructs a neural network, trains the network, and evaluates its performance with a graph displaying its performance.
* Logger: This class provides logging functionalities for the ConvolutionalNeuralNetwork program. It allows creation of log files, writing log messages, and updating a JTextArea with log contents.
* DataReader: This class is responsible for reading image data from a file and creating Image objects. It parses the file containing image data and labels, then constructs Image objects for each entry.
* Image: This class represents an image along with its associated label. It is used to store image data and corresponding labels for training or testing.
* MatrixUtility: This class provides utility methods for matrix operations such as addition and multiplication.
* ConvolutionLayer: This class represents a convolutional layer in a convolutional neural network (CNN). It applies convolution operation to input feature maps using randomly initialized filters. The class also handles forward pass, backpropagation, and weight updates.
* FullyConnectedLayer: This class represents a fully connected layer in a neural network. It connects every neuron in the input to every neuron in the output.
* Layer: This abstract class represents a generic layer in a neural network. It defines common properties and behaviors for all types of layers.
* MaxPoolLayer: This class represents a max pooling layer used in convolutional neural networks. It downsamples the input by taking the maximum value within a sliding window.
* NetworkBuilder: This class is responsible for building neural networks by adding different types of layers. It allows the construction of convolutional neural networks (CNNs) for image classification tasks.
* NeuralNetwork: This class represents a neural network used for image classification. It includes methods for training the network, making predictions, and testing accuracy.

## Core Functionality
The project includes functionalities for preprocessing data, loading CSV datasets, constructing the neural network architecture, training the network, shuffling data before evaluation.

## Optional Functionality
Optional functionalities include a logger for logging CNN actions and a graph visualization for live training visualization.

## Future Improvements
Given more time, additional layers such as Softmax and output layers could be implemented for image classification. Integration with an image classifier app could be explored to extend functionality. 
