package oosd;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LabelSystem {

	public static ArrayList<User> readFileUsers(String fileName) throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		Object obj1 = parser.parse(new FileReader(fileName));

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

			} else {
				User user = new OtherMechanism(userId, userName, userType);
				user_list.add(user);
			}

		}
		return user_list;

	}

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

		Dataset data = new Dataset();
		data.readFileDataset("input.json");
		ArrayList<User> user_list = readFileUsers("user.json");

		// label assignments objects
		ArrayList<LabelAssignment> assignment_list = new ArrayList<LabelAssignment>();

		for (int b = 0; b < data.getInstances().size(); b++) {
			for (int a = 0; a < user_list.size(); a++) {
				LabelAssignment labelAssignment = new LabelAssignment(data.getInstances().get(b), user_list.get(a));
				assignment_list.add(labelAssignment);
			}
		}

		// get labeled
		for (int i = 0; i < assignment_list.size(); i++) {
			(assignment_list.get(i).getUser()).label(assignment_list.get(i), data.getLabels(), data.getMaxNoLabels());
			(assignment_list.get(i).getUser()).trace(assignment_list.get(i));
		}

		// output
		WriteJsonFile a = new WriteJsonFile(data, assignment_list, user_list);
		a.printToFile("output_test.json");

	}

}