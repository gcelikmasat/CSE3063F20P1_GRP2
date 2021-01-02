package CSE3063F20P1_GRP2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.List;
import java.util.ArrayList;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.converters.ArffSaver;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.tokenizers.NGramTokenizer;

import weka.filters.unsupervised.attribute.StringToWordVector;

/*
 * This class implements a Multinomial NaiveBayes text classifier using WEKA.
 */
public class Train {

	private static Logger LOGGER = Logger.getLogger("Train");
	private FilteredClassifier classifier;

	// Declare train and test data Instances
	private Instances trainData;

	// Declare attributes of Instance
	private ArrayList<Attribute> wekaAttributes;

	// Declare and initialize file names
	private final String TRAIN_DATA = "train.txt";
	private final String TRAIN_ARFF_ARFF = "train.arff";

	public Train() {

		// Change the red output
		LOGGER.setUseParentHandlers(false);

		SimpleFormatter fmt = new SimpleFormatter();
		StreamHandler sh = new StreamHandler(System.out, fmt);
		LOGGER.addHandler(sh);

		classifier = new FilteredClassifier();

		// Set Multinomial NaiveBayes as arbitrary classifier
		classifier.setClassifier(new NaiveBayesMultinomial());

		// Declare text attribute to hold the message
		Attribute attributeText = new Attribute("text", (List<String>) null);

		// Declare the label attribute along with its values
		ArrayList<String> classAttributeValues = new ArrayList<>();
		classAttributeValues.add("Positive");
		classAttributeValues.add("Negative");
		classAttributeValues.add("Notr");
		Attribute classAttribute = new Attribute("label", classAttributeValues);

		// Declare the feature vector
		wekaAttributes = new ArrayList<>();
		wekaAttributes.add(classAttribute);
		wekaAttributes.add(attributeText);
	}

	// Load training data and set feature generators
	public void transform() {
		try {
			trainData = loadRawDataset(TRAIN_DATA);
			saveArff(trainData, TRAIN_ARFF_ARFF);

			// Create the filter and set the attribute to be transformed from text into a
			// feature vector (the last one)
			StringToWordVector filter = new StringToWordVector();
			filter.setAttributeIndices("last");

			// Add ngram tokenizer to filter with min and max length set to 1
			NGramTokenizer tokenizer = new NGramTokenizer();
			tokenizer.setNGramMinSize(1);
			tokenizer.setNGramMaxSize(1);

			// Use word delimiter
			tokenizer.setDelimiters("\\W");
			filter.setTokenizer(tokenizer);

			// Convert tokens to lower case
			filter.setLowerCaseTokens(true);

			// Add filter to classifier
			classifier.setFilter(filter);
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}

	}

	// Build the classifier with the training data
	public void fit() {
		try {
			classifier.buildClassifier(trainData);
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}
	}

	// Classify a review into positive, negative or notr.
	public String predict(String text) {
		try {
			// Create new Instance for prediction.
			DenseInstance newinstance = new DenseInstance(2);

			// Create a dataset to be set to new Instance
			Instances newDataset = new Instances("predictiondata", wekaAttributes, 1);
			newDataset.setClassIndex(0);

			newinstance.setDataset(newDataset);

			// Text attribute value set to value to be predicted
			newinstance.setValue(wekaAttributes.get(1), text);

			// Predict most likely class for the instance
			double pred = classifier.classifyInstance(newinstance);

			// Return predicted label
			return newDataset.classAttribute().value((int) pred);
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			return null;
		}
	}

	// Load the model to be used as classifier.
	public void loadModel(String fileName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
			Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
			in.close();
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
		} catch (ClassNotFoundException e) {
			LOGGER.warning(e.getMessage());
		}
	}

	// Save the trained model into a file
	public void saveModel(String fileName) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(classifier);
			out.close();
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
		}
	}

	// Load a dataset in space seperated text file and convert it to Arff format.
	public Instances loadRawDataset(String filename) {

		Instances dataset = new Instances("review", wekaAttributes, 10);

		// Set class index
		dataset.setClassIndex(0);

		// Read text file, parse data and add to instance
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			for (String line; (line = br.readLine()) != null;) {
				// Split at first occurrence of n no. of words
				String[] parts = line.split("\\s+", 2);

				// Basic validation
				if (!parts[0].isEmpty() && !parts[1].isEmpty()) {

					DenseInstance row = new DenseInstance(2);
					row.setValue(wekaAttributes.get(0), parts[0]);
					row.setValue(wekaAttributes.get(1), parts[1]);

					// Add row to instances
					dataset.add(row);
				}

			}

		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			LOGGER.info("invalid row.");
		}
		return dataset;

	}

	// Load a dataset in ARFF format.
	public Instances loadArff(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(reader);
			Instances dataset = arff.getData();
			reader.close();
			return dataset;
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
			return null;
		}
	}

	// Save a dataset in ARFF format.
	public void saveArff(Instances dataset, String filename) {
		try {
			// initialize
			ArffSaver arffSaverInstance = new ArffSaver();
			arffSaverInstance.setInstances(dataset);
			arffSaverInstance.setFile(new File(filename));
			arffSaverInstance.writeBatch();
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
		}
	}

}