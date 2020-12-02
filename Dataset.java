package CSE3063F20P1_GRP2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Dataset {
    
    private int id;
    private String name;
    private int maxNoLabels;
    private ArrayList<Label> labels;
    private ArrayList<Instance> instances;

    
    public Dataset() {}
    public Dataset(int id, String name, int maxNoLabels, ArrayList<Label> labels, ArrayList<Instance> instances){
    this.id = id;
    this.name = name;
    this.maxNoLabels = maxNoLabels;
    this.labels = new ArrayList<Label>(labels);
    this.instances = new ArrayList<Instance>(instances);
    }

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

    public void printLabels(){
        System.out.println("Class Labels :");
        for(int i=0; i< this.labels.size(); i++){
            System.out.print("Label id: " + this.labels.get(i).getId() + "\n" +
            "Label text: " + this.labels.get(i).getText() + "\n");
        }
    }

    public void printInstances(){
        System.out.println("Instances :");
        for(int i=0; i< this.instances.size(); i++){
            System.out.print("id: " + this.instances.get(i).getId() + "\n" +
            "Instance: " + this.instances.get(i).getDocument() + "\n");
        }
    }

    public void printDataset(){
        System.out.print("Dataset id: " + getId() + "\n" +
        "Dataset Name: " + getName() + "\n" +
        "Maximum number of labels per instance: " + getMaxNoLabels() + "\n");

        printLabels();
        printInstances();
    }
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

}
