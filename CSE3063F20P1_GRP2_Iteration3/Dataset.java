/*
* Dataset.java
* This class keeps the neccessary informations (labels,instances..) for labelling operations.
*
*/

package CSE3063F20P1_GRP2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("serial")
public class Dataset implements Serializable {

	private int id;
	private String name;
	private int maxNoLabels;
	private ArrayList<Label> labels = new ArrayList<Label>();
	private ArrayList<Instance> instances = new ArrayList<Instance>();
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<LabelAssignment> assignments = new ArrayList<LabelAssignment>();

	// default constructor
	public Dataset() {

	}

	// constructor
	public Dataset(int id, String name, int maxNoLabels, ArrayList<Label> labels, ArrayList<Instance> instances) {
		this.id = id;
		this.name = name;
		this.maxNoLabels = maxNoLabels;
		this.labels = new ArrayList<Label>(labels);
		this.instances = new ArrayList<Instance>(instances);
		this.assignments = new ArrayList<LabelAssignment>();
	}

	// getter-setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxNoLabels() {
		return maxNoLabels;
	}

	public void setMaxNoLabels(int maxNoLabels) {
		this.maxNoLabels = maxNoLabels;
	}

	public ArrayList<Label> getLabels() {
		return labels;
	}

	public void setLabels(ArrayList<Label> labels) {
		this.labels = labels;
	}

	public ArrayList<Instance> getInstances() {
		return instances;
	}

	public void setInstances(ArrayList<Instance> instances) {
		this.instances = instances;
	}

	public ArrayList<LabelAssignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(ArrayList<LabelAssignment> assignments) {
		this.assignments = assignments;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	// prints labels
	public void printLabels() {
		System.out.println("Class Labels :");
		for (int i = 0; i < this.labels.size(); i++) {
			System.out.print("Label id: " + this.labels.get(i).getId() + "\n" + "Label text: "
					+ this.labels.get(i).getText() + "\n");
		}
	}

	// prints instances
	public void printInstances() {
		System.out.println("Instances :");
		for (int i = 0; i < this.instances.size(); i++) {
			System.out.print("id: " + this.instances.get(i).getId() + "\n" + "Instance: "
					+ this.instances.get(i).getDocument() + "\n");
		}
	}

	// print datasets
	public void printDataset() {
		System.out.print("Dataset id: " + getId() + "\n" + "Dataset Name: " + getName() + "\n"
				+ "Maximum number of labels per instance: " + getMaxNoLabels() + "\n");

		printLabels();
		printInstances();
	}

	// reads given json file and sets the dataset object
	public void readFileDataset(String fileName) throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(fileName));
		JSONObject jsonObject = (JSONObject) obj;

		int datasetID = (int) (long) jsonObject.get("dataset id");
		String datasetName = (String) jsonObject.get("dataset name");
		int maxNoLabels = (int) (long) jsonObject.get("maximum number of labels per instance");

		ArrayList<Label> label_list = new ArrayList<Label>();
		JSONArray labels = (JSONArray) jsonObject.get("class labels");
		for (int i = 0; i < labels.size(); ++i) {

			JSONObject labelObject = (JSONObject) labels.get(i);

			int labelId = (int) (long) labelObject.get("label id");
			String labelText = (String) labelObject.get("label text");

			Label label = new Label(labelId, labelText);
			label_list.add(label);
		}

		ArrayList<Instance> instance_list = new ArrayList<Instance>();
		JSONArray instances = (JSONArray) jsonObject.get("instances");
		for (int i = 0; i < instances.size(); ++i) {

			JSONObject instanceObject = (JSONObject) instances.get(i);

			int instanceId = (int) (long) instanceObject.get("id");
			String instanceText = (String) instanceObject.get("instance");

			Instance instance = new Instance(instanceId, instanceText);
			instance_list.add(instance);
		}

		this.id = datasetID;
		this.name = datasetName;
		this.maxNoLabels = maxNoLabels;
		this.labels = new ArrayList<Label>(label_list);
		this.instances = new ArrayList<Instance>(instance_list);

	}



	@Override
	public String toString() {
		return "Dataset [id=" + id + ", name=" + name + ", maxNoLabels=" + maxNoLabels + ", labels=" + labels
				+ ", instances=" + instances + ", users=" + users + ", assignments=" + assignments + "]";
	}

	// Finds how many times an instance labeled
	public int findLabelledInstances() {
		int total = 0;
		for (int j = 0; j < getInstances().size(); ++j) {
			if (!instances.get(j).getClassLabel().isEmpty()) {
				total++;
			}
		}
		return total;
	}

	// Find the number of unique instances
	public ArrayList<Integer> uniqueInstancesOfLabels(Label lab) {

		ArrayList<Integer> index = new ArrayList<>();

		for (int j = 0; j < getInstances().size(); ++j) {
			if (instances.get(j).getClassLabel().contains(lab))
				if (!index.contains(instances.get(j).getId()))
					index.add(instances.get(j).getId());
		}

		return index;
	}

	// Find the class distribution based on final instance labels
	public void classDistribution() {

		Integer[] numbers = new Integer[this.getLabels().size()];
		int length = numbers.length;
		for (int i = 0; i < length; ++i) {
			numbers[i] = 0;
		}

		int k = 0;
		for (int j = 0; j < this.labels.size(); ++j) {
			for (int i = 0; i < this.getInstances().size(); ++i) {
				if (!instances.get(i).getClassLabel().isEmpty() && getInstances().get(i).getFinalLabel() != null)
					if (this.getLabels().get(j).getId() == this.getInstances().get(i).getFinalLabel().getId()) {
						numbers[k] += 1;
						
					}
			}
			k++;
		}
		double total = findLabelledInstances();

		for (int j = 0; j < numbers.length; ++j) {
			System.out.println(
					labels.get(j).getId() + ": " + labels.get(j).getText() + ": %" + (numbers[j] / total) * 100);
		}

	}

	// Find the percentage of the labeled instances
	public double datasetpercentage() {
		// 1.
		double howManyLabelled = 0;
		for (int i = 0; i < instances.size(); ++i) {
			if (!instances.get(i).getClassLabel().isEmpty()) {
				howManyLabelled++;
			}
		}
		double percentage = (howManyLabelled / instances.size()) * 100;
		return percentage;
	}
	public boolean isInside(User user) {

		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getId() == user.getId()) {
				return true;
			}
		}
		return false;

	}
	public Label findExist(int id) {

		for (Label label : getLabels()) {
			if (label.getId() == id)
				return label;
		}
		return null;
	}

	

}
