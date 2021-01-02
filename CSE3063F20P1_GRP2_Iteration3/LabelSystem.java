/*
 * 
 * LabelSystem.java
 * 
 * Data labeling system as a simulation and with user interface
 * 
 * 
 * */

package CSE3063F20P1_GRP2;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("serial")
public class LabelSystem implements Serializable {

	// reads users from a given .json file
	public static int readFileConfig(String fileName, ArrayList<User> user_list, ArrayList<Dataset> data_list)
			throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		Object obj1 = parser.parse(new FileReader(fileName));
		JSONObject jsonObject1 = (JSONObject) obj1;

		int currentDatasetId = (int) (long) jsonObject1.get("CurrentDatasetId");
		double consistencyCheckProbability = (double) jsonObject1.get("ConsistencyCheckProbability");

		// keeps users information
		JSONArray users = (JSONArray) jsonObject1.get("Bot Users");

		// user id,name,type
		for (int i = 0; i < users.size(); ++i) {
			JSONObject userObject = (JSONObject) users.get(i);
			int userId = (int) (long) userObject.get("user id");
			String userName = (String) userObject.get("user name");
			String userType = (String) userObject.get("user type");
			if (userType.equals("RandomBot")) {
				User user = new RandomLabelingMechanism(userId, userName, userType); // user can have
				user.setConsistencyCheckProbability(consistencyCheckProbability);
				user_list.add(user);

			} else {
				User user = new OtherMechanism(userId, userName, userType);
				user_list.add(user);

			}
		}
		/*
		 * "user type": "Human", "user id": 7, "user name": "Leyla", "password": "123",
		 * "ConsistencyCheckProbability": 0.1
		 */
		// user id,name,type
		JSONArray human_users = (JSONArray) jsonObject1.get("Human Users");
		for (int i = 0; i < human_users.size(); ++i) {
			JSONObject userObject = (JSONObject) human_users.get(i);
			String userType = (String) userObject.get("user type");
			int userId = (int) (long) userObject.get("user id");
			String userName = (String) userObject.get("user name");
			String password = (String) userObject.get("password");
			double consistencyCheckProbability2 = (double) userObject.get("ConsistencyCheckProbability");

			User user = new Human(userId, userName, userType, password);
			user.setConsistencyCheckProbability(consistencyCheckProbability2);
			user_list.add(user);

		}

		ArrayList<Integer> userIds = new ArrayList<Integer>();
		// set which dataset to use

		JSONArray datasets = (JSONArray) jsonObject1.get("datasets");
		for (int i = 0; i < datasets.size(); ++i) {
			Dataset data = new Dataset();
			userIds.clear();

			JSONObject datasetObject = (JSONObject) datasets.get(i);

			int datasetId = (int) (long) datasetObject.get("dataset id");
			data.setId(datasetId);
			String datasetName = (String) datasetObject.get("dataset name");
			data.setName(datasetName);

			JSONArray userIdsJson = (JSONArray) datasetObject.get("UsersForLabelingTheDataset");
			for (int j = 0; j < userIdsJson.size(); ++j) {
				int userObject = (int) (long) userIdsJson.get(j);
				userIds.add(userObject);
			}
			// adding users for particular dataset
			for (int k = 0; k < user_list.size(); ++k) {
				if (userIds.contains(user_list.get(k).getId())) {
					data.getUsers().add(user_list.get(k));
					user_list.get(k).getDataset().add(data);
				}
			}
			String path = (String) datasetObject.get("path");

			try {
				data.readFileDataset(path);
			} catch (FileNotFoundException e) {
				System.out.println("Invalid dataset id: " + e);
			}
			data_list.add(data);

		}

		return currentDatasetId;
	}

	@SuppressWarnings({ "unchecked", "resource" })
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, ParseException, InterruptedException {

		try {
			String objectFileName = "data.dat";
			PrintStream originalPrintStream = System.out;
			ArrayList<User> user_list = new ArrayList<User>();
			ArrayList<Dataset> data_list = new ArrayList<Dataset>(); // general dataset for system simulation

			int current = readFileConfig("config.json", user_list, data_list);

			File file_exist = new File(objectFileName);
			boolean cond1 = file_exist.exists();

			if (cond1) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objectFileName));
				try {
					data_list = (ArrayList<Dataset>) ois.readObject();
					user_list = (ArrayList<User>) ois.readObject();
				} catch (EOFException e) {
					System.out.println("exception: " + e);
				} catch (InvalidClassException e) {
					System.out.println("exception: " + e);
				}
				ois.close();
			}

			// Find current dataset
			Dataset data = new Dataset();
			for (int i = 0; i < data_list.size(); i++) {
				if (data_list.get(i).getId() == current) {
					data = data_list.get(i);
				}
			}

			// labeling operations
			if (!data.getUsers().isEmpty()) {

				Action action = new Action();
				User currentUser = action.findUser(data);

				if (currentUser != null) {
					System.out.println("Simulation is starting..");
					double completed = currentUser.completenessDataset(data);
					while (completed != 100) {

						LabelAssignment la = new LabelAssignment(currentUser);

						// label
						currentUser.label(la, data);

						if (!la.getClassLabel().isEmpty()) {
							data.getAssignments().add(la);

							// output .json and metric outputs
							WriteOutputFile a = new WriteOutputFile(data_list);
							a.printToFile("output_test");
							a.writeMetrics(data, user_list);
							// Save the objects in file
							File file = new File(objectFileName);
							ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
							objectOutputStream.writeObject(data_list);
							objectOutputStream.writeObject(user_list);
							objectOutputStream.close();

						}

						completed = currentUser.completenessDataset(data);

					}
				} else if (currentUser == null) { // user is a bot

					// Find the maximum number for labeling assignment
					int complete_count = 0;
					for (int i = 0; i < data.getUsers().size(); i++) {
						if (!(data.getUsers().get(i) instanceof Human)) {
							complete_count++;
						}
					}
					complete_count *= data.getInstances().size();
					int completed = 0;

					while (completed != complete_count) {
						completed = 0;
						for (int x = 0; x < data.getInstances().size(); x++) {
							completed += data.getInstances().get(x).uniqueUsers(data.getAssignments());
						}
						// End of the labeling operation
						if (completed == complete_count) {
							break;
						}

						for (int j = 0; j < data.getUsers().size(); ++j) { // it should be start with

							Random rd = new Random(); // creating Random object
							if (rd.nextBoolean()) { // displaying a random boolean
								if (data.getUsers().get(j).completenessDataset(data) == 100) {
									break;
								}
								LabelAssignment la = new LabelAssignment(data.getUsers().get(j));

								// User labels an instance
								if (!(data.getUsers().get(j) instanceof Human)) {
									data.getUsers().get(j).label(la, data);
								}
								if (!la.getClassLabel().isEmpty()) {
									data.getAssignments().add(la);
								}

								// output .json and metric output
								WriteOutputFile a = new WriteOutputFile(data_list);
								a.printToFile("output_test");
								a.writeMetrics(data, user_list);

								// Save the objects in file
								File file = new File(objectFileName);
								ObjectOutputStream objectOutputStream = new ObjectOutputStream(
										new FileOutputStream(file));
								objectOutputStream.writeObject(data_list);
								objectOutputStream.writeObject(user_list);
								objectOutputStream.close();

								// metrics write to output file here
								completed = 0;
								for (int x = 0; x < data.getInstances().size(); x++) {
									completed += data.getInstances().get(x).uniqueUsers(data.getAssignments());

								}

							}
						}

					}

				}
				System.setOut(originalPrintStream);
				System.out.println("DONE!");

			} else
				System.out.println("There is no user");
		} catch (FileNotFoundException e) {
			System.out.println("Configuration file does not exist: " + e);
		}

	}
}
