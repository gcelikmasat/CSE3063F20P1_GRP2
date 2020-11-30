package CSE3063F20P1_GRP2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class WriteJsonFile {
	private int datasetID;
	private String datasetName;
	private int maxNoLabels;
	private ArrayList<Label> label_list;
	private ArrayList<Instance> instance_list;
	private ArrayList<LabelAssignments> assignment_list;
	private ArrayList<User> user_list;

	@SuppressWarnings("unchecked")

	public WriteJsonFile(int datasetID, String datasetName, int maxNoLabels, ArrayList<Label> label_list,
			ArrayList<Instance> instance_list, ArrayList<LabelAssignments> assignment_list, ArrayList<User> user_list) {
		this.datasetID = datasetID;
		this.datasetName = datasetName;
		this.maxNoLabels = maxNoLabels;
		this.label_list = label_list;
		this.instance_list = instance_list;
		this.assignment_list = assignment_list;
		this.user_list = user_list;
	}

	public void printToFile(String fileName) {


		JSONObject dataset = new JSONObject();
		dataset.put("dataset id ", String.valueOf(datasetID));
		dataset.put("dataset name ", datasetName);
		dataset.put("maximum number of labels per instance ", String.valueOf(maxNoLabels));

		JSONArray class_labels = new JSONArray();
		for (Label label : label_list) {
			JSONObject label_object = new JSONObject();
			label_object.put("label id", label.getId());
			label_object.put("label text", label.getText());
			class_labels.add(label_object);
		}
		dataset.put("class labels", class_labels);

		JSONArray instances = new JSONArray();
		for (Instance instance : instance_list) {
			JSONObject instance_object = new JSONObject();
			instance_object.put("id", instance.getId());
			instance_object.put("instance", instance.getDocument());
			class_labels.add(instance_object);
		}
		dataset.put("instances", instances);

		JSONArray class_label_assignments = new JSONArray();
		for (LabelAssignments labelAssignments : assignment_list) {
			JSONObject assignment_object = new JSONObject();
			assignment_object.put("instance id", labelAssignments.getInstance().getId());
			JSONArray class_label_ids = new JSONArray();
			for (Label label : labelAssignments.getClassLabel()) {
				class_label_ids.add(label.getId());
			}
			assignment_object.put("class label ids", class_label_ids);
			assignment_object.put("user id", labelAssignments.getUser().getId());
			assignment_object.put("datetime", labelAssignments.getDate().toString());
			class_label_assignments.add(assignment_object);
		}
		dataset.put("class label assignments", class_label_assignments);

		JSONArray users = new JSONArray();
		for (User user : user_list) {
			JSONObject user_object = new JSONObject();
			user_object.put("user id", user.getId());
			user_object.put("user name", user.getName());
			users.add(user_object);
		}
		dataset.put("users", users);

    
		try {
			FileWriter file = new FileWriter("output.json");
			file.write(dataset.toJSONString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
