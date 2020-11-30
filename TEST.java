package CSE3063F20P1_GRP2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TEST {

    public static void main(String[] args){
		JSONParser parser = new JSONParser();
		
		
		
		try {
			Object obj = parser.parse(new FileReader("CES3063F20_LabelingProject_Input-1.json"));
			JSONObject jsonObject = (JSONObject) obj;
			
			int datasetID = (int)(long)jsonObject .get("dataset id");
			//System.out.println("dataset id: "+ datasetID);
			
			String datasetName = (String)jsonObject.get("dataset name");
			//System.out.println("dataset name: "+ datasetName);
			
			int maxNoLabels = (int)(long)jsonObject.get("maximum number of labels per instance");
			//System.out.println("maximum number of labels per instance: "+ maxNoLabels);
			//System.out.println();
			
			ArrayList<Label> label_list = new ArrayList<Label>();
			JSONArray labels = (JSONArray) jsonObject.get("class labels");
			for(int i=0; i<labels.size(); ++i) {
				
				JSONObject labelObject = (JSONObject) labels.get(i);

				int labelId = (int)(long)labelObject.get("label id");
				String labelText = (String)labelObject.get("label text");

				Label label = new Label(labelId, labelText);
				label_list.add(label);
				//System.out.println("label id: "+ labelId);
				//System.out.println("label text: "+ labelText);
			}

			//System.out.println();
			ArrayList<Instance> instance_list = new ArrayList<Instance>();
			JSONArray instances = (JSONArray) jsonObject.get("instances");
			for(int i=0; i<instances.size(); ++i) {
				
				JSONObject instanceObject = (JSONObject) instances.get(i);
				
				int instanceId = (int)(long)instanceObject.get("id");
				String instanceText = (String)instanceObject.get("instance");

				Instance instance = new Instance(instanceId, instanceText);
				instance_list.add(instance);
				//System.out.println("instance id:  "+ instanceId);
				//System.out.println("instance text: "+ instanceText);
			}

			Dataset tDataset = new Dataset(datasetID,datasetName,maxNoLabels, label_list, instance_list);

			tDataset.printDataset();

			User user1 = new RandomLabelingMechanism("user1", "random");
			User user2 = new RandomLabelingMechanism("user2", "random");
			User user3 = new RandomLabelingMechanism("user3", "random");
			User user4 = new RandomLabelingMechanism("user4", "random");

			ArrayList<User> users = new ArrayList<User>();

			ArrayList<LabelAssignments> assignments = new ArrayList<LabelAssignments>();

			for (int a=0;a< tDataset.getInstances().size();a++){
				for(int b=0; b<users.size(); b++){
					LabelAssignments assignment = new LabelAssignments(tDataset.getInstances().get(a), users.get(b));
					Label label = getRandomLabel(tDataset);
					users.get(b).label(assignment, label);
				}
			}

			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
    }
    }

public Label getRandomLabel(Dataset ds){
	Label RandomLabel = new Label(1, "S");
	return RandomLabel;
}