package CSE3063F20P1_GRP2;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class WriteJsonFile {
	
	private Dataset data; 
	private ArrayList<LabelAssignment> assignment_list;
	private ArrayList<User> user_list;

	

	public WriteJsonFile(Dataset dataset, ArrayList<LabelAssignment> assignment_list, ArrayList<User> user_list) {
		
		this.data = dataset;
		this.assignment_list = assignment_list;
		this.user_list = user_list;
	}

	public void printToFile(String fileName) {


		JSONObject dataset = new JSONObject();
		dataset.put("dataset id ", String.valueOf(data.getId()));
		dataset.put("dataset name ", data.getName());
		dataset.put("maximum number of labels per instance ", String.valueOf(data.getMaxNoLabels()));

		JSONArray class_labels = new JSONArray();
		for (Label label : data.getLabels()) {
			JSONObject label_object = new JSONObject();
			label_object.put("label id", label.getId());
			label_object.put("label text", label.getText());
			class_labels.add(label_object);
		}
		dataset.put("class labels", class_labels);

		JSONArray instances = new JSONArray();
		for (Instance instance : data.getInstances()) {
			JSONObject instance_object = new JSONObject();
			instance_object.put("id", instance.getId());
			instance_object.put("instance", instance.getDocument());
			class_labels.add(instance_object);
		}
		dataset.put("instances", instances);

		JSONArray class_label_assignments = new JSONArray();
		for (LabelAssignment labelAssignment : assignment_list) {
			JSONObject assignment_object = new JSONObject();
			assignment_object.put("instance id", labelAssignment.getInstance().getId());
			JSONArray class_label_ids = new JSONArray();
			for (Label label : labelAssignment.getClassLabel()) {
				class_label_ids.add(label.getId());
			}
			assignment_object.put("class label ids", class_label_ids);
			assignment_object.put("user id", labelAssignment.getUser().getId());
			assignment_object.put("datetime", labelAssignment.getDate().toString());
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

	public Dataset getDataset() {
		return data;
	}

	public void setDataset(Dataset dataset) {
		this.data = dataset;
	}
}
