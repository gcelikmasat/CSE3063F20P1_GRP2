package CSE3063F20P1_GRP2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ReadJsonFile {

	public Dataset readFileDataset(String fileName) throws FileNotFoundException, IOException, ParseException{

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
			
			Dataset tDataset = new Dataset(datasetID, datasetName, maxNoLabels, label_list, instance_list);
			return tDataset;
	
	}

	//reads users
	public ArrayList<User> readFileUsers(String fileName) throws FileNotFoundException, IOException, ParseException{
		boolean append = false;
        Logger logger = Logger.getLogger("MyLog");  
		FileHandler fh;
		Date currentDate = new Date();
		
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

				try {  

					// This block configure the logger with handler and formatter  
					fh = new FileHandler("default.log", append);  
					logger.addHandler(fh);
					SimpleFormatter formatter = new SimpleFormatter();  
					fh.setFormatter(formatter);  
			
					// the following statement is used to log any messages  
					logger.info(currentDate + "[User Manager] INFO user id: " + user.getId() + " created " + user.getName() + " as " + user.getUser_type() + " \n ");
			
				} catch (SecurityException e) {  
					e.printStackTrace();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  

			}
			else {
				User user = new OtherMechanism(userId, userName, userType);
				user_list.add(user);

				try {  

					// This block configure the logger with handler and formatter  
					fh = new FileHandler("default.log", append);  
					logger.addHandler(fh);
					SimpleFormatter formatter = new SimpleFormatter();  
					fh.setFormatter(formatter);  
			
					// the following statement is used to log any messages  
					logger.info(currentDate + "[User Manager] INFO user id: " + user.getId() + " created " + user.getName() + " as " + user.getUser_type() + " \n ");
			
				} catch (SecurityException e) {  
					e.printStackTrace();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}
			}
			logger.setUseParentHandlers(false);

		}
		return user_list;
		
	}

}