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
		JSONArray users = (JSONArray) jsonObject1.get("users");

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
			data.setPath(path);
			try {
				data.readFileDataset(data.getPath());
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
			PrintStream originalPrintStream = System.out;
			ArrayList<User> user_list = new ArrayList<User>();

			ArrayList<Dataset> data_list = new ArrayList<Dataset>(); // general dataset for system simulation
			int current = readFileConfig("config.json", user_list, data_list);

			String objectFileName = "data.dat";

			File file_exist = new File(objectFileName);
			boolean cond1 = file_exist.exists();

			// Read the objects that are kept in the file
			if (cond1) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objectFileName));
				try {
					data_list = (ArrayList<Dataset>) ois.readObject();
					user_list = (ArrayList<User>) ois.readObject();
				} catch (EOFException e) {

				} catch (InvalidClassException e) {

				}

			}

			// Find current dataset
			Dataset data = new Dataset();
			for (int i = 0; i < data_list.size(); i++) {
				if (data_list.get(i).getId() == current) {
					data = data_list.get(i);
				}
			}

			// Find the maximum number for labeling assignment
			int complete_count = 0;
			for (int i = 0; i < data.getUsers().size(); i++) {
				if (data.getUsers().get(i) instanceof RandomLabelingMechanism) {
					complete_count++;
				}
			}
			complete_count *= data.getInstances().size();

			if (user_list.size() != 0) {
				
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
							data.getUsers().get(j).label(la, data);
							
							if (!la.getClassLabel().isEmpty()) {
								data.getAssignments().add(la);
							}

							// output .json
							WriteJsonFile a = new WriteJsonFile(data_list, user_list);
							a.printToFile("output_test");

							// metrics output
							File file = new File("metrics.txt");
							// Instantiating the PrintStream class
							PrintStream stream = new PrintStream(file);
							System.setOut(stream);
							for (int x = 0; x < data_list.size(); x++) {
								if (data_list.get(x).datasetpercentage() != 0) {
									System.out.println("********Dataset" + data_list.get(x).getId() + " "
											+ data_list.get(x).getName() + " " + "Metrics********");
									data_list.get(x).datasetMetrics();
									System.out.println();
									System.out.println("*************** Instance Metrics ***************");
									for (int i = 0; i < data_list.get(x).getInstances().size(); i++) {
										if (!data_list.get(x).getInstances().get(i).getClassLabel().isEmpty())
											data_list.get(x).getInstances().get(i).instanceMetrics(data_list.get(x),
													data_list.get(x).getAssignments());
										System.out.println();

									}

								}

							}
							System.out.println("*************** User Metrics ***************");
							for (int m = 0; m < user_list.size(); ++m) {
								user_list.get(m).userMetrics(data_list, data);
								System.out.println();
							}
							// Save the objects in file
							File file2 = new File(objectFileName);
							ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file2));
							objectOutputStream.writeObject(data_list);
							objectOutputStream.writeObject(user_list);

							// metrics write to output file here
							completed = 0;

							for (int x = 0; x < data.getInstances().size(); x++) {
								completed += data.getInstances().get(x).uniqueUsers(data.getAssignments());

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
