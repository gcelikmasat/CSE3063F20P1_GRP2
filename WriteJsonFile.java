package CSE3063F20P1_GRP2;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
@SuppressWarnings("unchecked")

public class WriteJsonFile {
	private  Dataset data;
	private ArrayList<Label> label_list;
	private ArrayList<Instance> instance_list;
	private ArrayList<LabelAssignment> assignment_list;
	private ArrayList<User> user_list;

	//Constructor
public WriteJsonFile(Dataset dataset, ArrayList<LabelAssignment> assignment_list, ArrayList<User> user_list ) {
	this.data=dataset;
	this.label_list = dataset.getLabels();
	this.instance_list = dataset.getInstances();
	this.assignment_list = assignment_list;
	this.user_list = user_list;
}
	//Print to JSon file 
public void printToFile(String fileName) {
	//"last" holds the last index so that we can distinguish whether to put comma or not.
	//"i" holds the index of current element we are printing in the loops
	int i, last;
	//We will use this string to build an output file with the expected format
	String output = "{%n";
	//Entering key values related to dataset
	output += "\"dataset id\":" + data.getId() + ",%n";
	output += "\"dataset name\":" + data.getName() + ",%n";
	output += "\"maximum number of labels per instance\":" + data.getMaxNoLabels() + ",%n";
	
	

	i = 0;
	output += "\"class labels\":[%n";
	last = label_list.size() - 1;
	//Add each of the class labels to JSON array 
	for(Label label: label_list) {
	    JSONObject label_object = new JSONObject();
	    label_object.put("label id", label.getId());
	    label_object.put("label text", label.getText());
	    //If it is the last element, do not put comma
		output += label_object.toString() + (i != last ? ",%n":"%n");
		i++;
	}
	output += "],%n";

	
	i = 0;
	last = instance_list.size() - 1;
	output += "\"instances\":[%n";
	//Add each of instances to JSON array 
	for(Instance instance: instance_list) {
		String temp = "{";
		temp += "\"id\":" + instance.getId() + ", ";
		temp += "\"instance\":" + instance.getDocument();
		//If it is the last element, do not put comma
	    output += temp + (i != last ? "},%n":"}%n");
		i++;
	}
	output += "],%n";
	
	i = 0;
	last = assignment_list.size() - 1;
	output += "\"class label assignments\":[%n";
	//Add each of the class label assignments to JSON array 
	for(LabelAssignment labelAssignments: assignment_list) {
		String temp = "{";
		temp += "\"instance id\":" + labelAssignments.getInstance().getId() + ", ";
	    JSONArray class_label_ids = new JSONArray();
	    //Construct class label ids as a JSON array
	    for(Label label: labelAssignments.getClassLabel()) {
	        class_label_ids.add(label.getId());
		}
		temp += "\"class label ids\":" + class_label_ids.toString() + ", ";
		temp += "\"user id\":" + labelAssignments.getUser().getId() + ", ";
		//Format date according to expected and given format
		SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy, hh:mm:ss");
		temp += "\"datetime\":" + ft.format(labelAssignments.getDate());
		//If it is the last element, do not put comma
		output += temp + (i != last ? "},%n":"}%n");
		i++;
	}
	output += "],%n";
	
	
	i = 0;
	last = user_list.size() - 1;
	output += "\"users\":[%n";
	//Add each of the users to JSON array 
	for(User user: user_list) {
		String temp = "{";
		temp += "\"user id\":" + user.getId() + ",";
		temp += "\"user name\":" + user.getName() + ",";
	    temp += "\"user type\":" + user.getUser_type();
	  //If it is the last element, do not put comma
	    output += temp + (i != last ? "},%n":"}%n");
		i++;
	}
	output += "]%n%n}";
	
	try (FileWriter file = new FileWriter(fileName)) {
		//write output string to file
		file.write(String.format(output));
	    file.flush();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
}
	
}