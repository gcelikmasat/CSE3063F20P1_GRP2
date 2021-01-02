/*
 * WriteOutputFile.java
 * 
 * Gives an output about label assignments and metrics
 * 
 * 
 * */



package CSE3063F20P1_GRP2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings({ "unchecked", "serial" })
public class WriteOutputFile implements Serializable {

	private ArrayList<Dataset> data_list = new ArrayList<Dataset>();
	
	// Constructor
	public WriteOutputFile(ArrayList<Dataset> data_list) {
		this.data_list = data_list;
	}

	//metrics
	public void writeMetrics(Dataset data, ArrayList<User> user_list) throws FileNotFoundException {
		File file = new File("metrics.txt");
		// Instantiating the PrintStream class
		PrintStream stream = new PrintStream(file);
		PrintStream originalPrintStream = System.out;
		System.setOut(stream);

		for (int x = 0; x < data_list.size(); x++) {
			if (data_list.get(x).datasetpercentage() != 0) {
				Metric metric = new Metric();
				metric.setData(data_list.get(x));

				System.out.println("********Dataset" + data_list.get(x).getId() + " "
						+ data_list.get(x).getName() + " " + "Metrics********");
				// data_list.get(x).datasetMetrics();
				metric.datasetMetrics();
				System.out.println();

				System.out.println("*************** Instance Metrics ***************");
				for (int i2 = 0; i2 < data_list.get(x).getInstances().size(); i2++) {
					if (!data_list.get(x).getInstances().get(i2).getClassLabel().isEmpty()) {
						// data_list.get(x).getInstances().get(i).instanceMetrics(data_list.get(x),
						// data_list.get(x).getAssignments());

						metric.instanceMetrics(data_list.get(x).getInstances().get(i2));
						System.out.println();
					}
				}

			}

		}
		System.out.println("*************** User Metrics ***************");
		for (int m = 0; m < user_list.size(); ++m) {
			Metric metric = new Metric();
			// user_list.get(m).userMetrics(data_list, data);
			metric.userMetrics(user_list.get(m));
			System.out.println();
		}
		System.setOut(originalPrintStream);
	}

	
	// Print to JSon file
	public void printToFile(String fileName) {
		// "last" holds the last index so that we can distinguish whether to put comma
		// or not.
		// "i" holds the index of current element we are printing in the loops
		for (Dataset data : data_list) {
			int i, last;
			// We will use this string to build an output file with the expected format
			String output = "{%n";
			// Entering key values related to dataset
			output += "\"dataset id\":" + data.getId() + ",%n";
			output += "\"dataset name\": \"" + data.getName() + "\",%n";
			output += "\"maximum number of labels per instance\":" + data.getMaxNoLabels() + ",%n";

			i = 0;
			output += "\"class labels\":[%n";
			last = data.getLabels().size() - 1;
			// Add each of the class labels to JSON array
			for (Label label : data.getLabels()) {
				JSONObject label_object = new JSONObject();
				label_object.put("label id", label.getId());
				label_object.put("label text", label.getText());
				// If it is the last element, do not put comma
				output += label_object.toString() + (i != last ? ",%n" : "%n");
				i++;
			}

			output += "],%n";

			i = 0;
			last = data.getInstances().size() - 1;
			output += "\"instances\":[%n";
			// Add each of instances to JSON array
			for (Instance instance : data.getInstances()) {
				String temp = "{";
				temp += "\"id\":" + instance.getId() + ", ";
				temp += "\"instance\": \"" + instance.getDocument() + "\"";
				// If it is the last element, do not put comma
				output += temp + (i != last ? "},%n" : "}%n");
				i++;
			}
			output += "],%n";

			i = 0;
			last = data.getAssignments().size() - 1;
			output += "\"class label assignments\":[%n";
			// Add each of the class label assignments to JSON array
			for (LabelAssignment labelAssignments : data.getAssignments()) {
				String temp = "{";
				if (labelAssignments.getInstance() != null) {

					temp += "\"instance id\":" + labelAssignments.getInstance().getId() + ", ";
					JSONArray class_label_ids = new JSONArray();
					// Construct class label ids as a JSON array
					for (Label label : labelAssignments.getClassLabel()) {
						class_label_ids.add(label.getId());
					}
					temp += "\"class label ids\":" + class_label_ids.toString() + ", ";
					temp += "\"user id\":" + labelAssignments.getUser().getId() + ", ";
					// Format date according to expected and given format
					SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss");
					temp += "\"datetime\": \"" + ft.format(labelAssignments.getDate()) + "\"";
					// If it is the last element, do not put comma

				} else {
					temp += "\"user id\":" + labelAssignments.getUser().getId() + ", ";
					String name = labelAssignments.getUser().getName();
					temp += "\"user name\": \"" + name + "\"";
				}
				output += temp + (i != last ? "},%n" : "}%n");
				i++;
			}
			output += "],%n";

			i = 0;
			last = data.getUsers().size()-1;
			output += "\"users\":[%n";
			// Add each of the users to JSON array
			for (User user : data.getUsers()) {
				String temp = "{";
				temp += "\"user id\":" + user.getId() + ",";
				temp += "\"user name\": \"" + user.getName() + "\",";
				temp += "\"user type\": \"" + user.getUser_type() + "\"";
				// If it is the last element, do not put comma
				output += temp + (i != last ? "},%n" : "}%n");
				i++;
			}
			output += "]%n%n}";

			if (!data.getAssignments().isEmpty()) {
				try (FileWriter file = new FileWriter(fileName + data.getId() + ".json")) {
					// write output string to file
					file.write(String.format(output));
					file.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}