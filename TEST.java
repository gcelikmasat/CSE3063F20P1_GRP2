package CSE3063F20P1_GRP2;
/*
 * Reads given input json file
 * 	 
 * 
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class TEST {

	public static void main(String[] args) {

		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader("input2.json"));
			JSONObject jsonObject = (JSONObject) obj;

			int id = (int) (long) jsonObject.get("dataset id");
			System.out.println("dataset id: " + id);

			String name = (String) jsonObject.get("dataset name");
			System.out.println("dataset name: " + name);

			int maxNumber = (int) (long) jsonObject.get("maximum number of labels per instance");
			System.out.println("maximum number of labels per instance: " + maxNumber);

			System.out.println();

			JSONArray labels = (JSONArray) jsonObject.get("class labels");
			ArrayList<Label> label_list = new ArrayList<Label>();
			for (int i = 0; i < labels.size(); ++i) {

				JSONObject labelObject = (JSONObject) labels.get(i);
				int labelId = (int) (long) labelObject.get("label id");
				String labelText = (String) labelObject.get("label text");

				Label label = new Label(labelId, labelText);
				label_list.add(label);
			}

			/*
			 * for(Label a:label_list) { System.out.println(a.getId()+" "+a.getText()); }
			 */

			System.out.println();

			JSONArray instances = (JSONArray) jsonObject.get("instances");
			ArrayList<Instance> instance_list = new ArrayList<Instance>();
			for (int i = 0; i < instances.size(); ++i) {

				JSONObject instanceObject = (JSONObject) instances.get(i);
				int instanceId = (int) (long) instanceObject.get("id");
				String instanceText = (String) instanceObject.get("instance");

				Instance instance = new Instance(instanceId, instanceText);
				instance_list.add(instance);

			}
			/*
			 * for(Instance b:instance_list) {
			 * System.out.println(b.getId()+" "+b.getText()); }
			 */

			/*
			 * ArrayList<User> userList = new ArrayList<User>(); User user1 = new
			 * RandomLabelingMechanism("ali", "random"); userList.add(user1); User user2 =
			 * new MachineLearning("veli", "random"); userList.add(user2);
			 */

			Dataset data = new Dataset(id, name, maxNumber, label_list, instance_list);

			Object obj1 = parser.parse(new FileReader("user.json"));
			JSONObject jsonObject1 = (JSONObject) obj1;
			JSONArray users = (JSONArray) jsonObject1.get("users");
			ArrayList<User> user_list = new ArrayList<User>();
			for (int i = 0; i < users.size(); ++i) {

				JSONObject userObject = (JSONObject) users.get(i);
				int userId = (int) (long) userObject.get("user id");
				String userName = (String) userObject.get("user name");
				String userType = (String) userObject.get("user type");

				if (userType.equals("RandomBot")) {
					User user = new RandomLabelingMechanism(userId, userName, userType);
					user_list.add(user);

				}
				if (userType.equals("MachineLearningBot")) {
					User user = new MachineLearning(userId, userName, userType);
					user_list.add(user);
				}

			}
			ArrayList<LabelAssignments> assignment_list = new ArrayList<LabelAssignments>();
			for (int a = 0; a < user_list.size(); a++) {
				for (int b = 0; b < instance_list.size(); b++) {
					LabelAssignments labelAssignment = new LabelAssignments(instance_list.get(b), user_list.get(a));
					assignment_list.add(labelAssignment);

				}
			}

			for (int i = 0; i < assignment_list.size(); i++) {
				(assignment_list.get(i).getUser()).label(assignment_list.get(i), label_list, maxNumber);
			}

			/*
			 * for (int c = 0; c < assignment_list.size(); c++) {
			 * assignment_list.get(c).getClassLabel().add(label_list.get((int)
			 * (Math.random() * label_list.size()))); }
			 * 
			 * for (LabelAssignments c : assignment_list) {
			 * System.out.println(c.getClassLabel().get(0).getText() + " - " +
			 * c.getInstance().getDocument() + " " + c.getUser().getName() + " " +
			 * c.getDate()); }
			 */
			WriteJsonFile a = new WriteJsonFile(data.getId(), data.getName(), data.getMaxNoLabels(), label_list,
					instance_list, assignment_list, user_list);
			a.printToFile("output_test.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
