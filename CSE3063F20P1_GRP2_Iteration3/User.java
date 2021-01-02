/*
 * User.java
 * 
 *  This is an abstract class for different type of users.
 * 
 */


package CSE3063F20P1_GRP2;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
abstract class User implements Serializable {

	private int id;
	private String name;
	private String user_type;
	
	private ArrayList<Instance> instances = new ArrayList<Instance>();
	private ArrayList<Instance> uniqueInstances = new ArrayList<Instance>();
	private ArrayList<Dataset> dataset = new ArrayList<Dataset>();

	private ArrayList<Float> spendTime = new ArrayList<Float>();
	private double consistencyCheckProbability;
	private double consistencyRate;

	//constructor
	public User(int id, String name, String user_type) {
		this.id = id;
		this.name = name;
		this.user_type = user_type;
		trace();
	}

	public abstract void label(LabelAssignment la, Dataset data) throws InterruptedException;
	public abstract void trace(LabelAssignment la);

	//trace for user creation
	private void trace() {

		Logger logger = LogManager.getLogger();
		logger.info("created " + this.name + " as " + this.user_type);
	}

	//getter setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Instance> getInstances() {
		return instances;
	}

	public void setInstances(ArrayList<Instance> instances) {
		this.instances = instances;
	}

	public double getConsistencyCheckProbability() {
		return consistencyCheckProbability;
	}

	public void setConsistencyCheckProbability(double consistencyCheckProbability) {
		this.consistencyCheckProbability = consistencyCheckProbability;
	}

	public ArrayList<Instance> getUniqueInstances() {
		return uniqueInstances;
	}

	public void setUniqueInstances(ArrayList<Instance> uniqueInstances) {
		this.uniqueInstances = uniqueInstances;
	}

	public double getConsistencyRate() {
		return consistencyRate;
	}

	public void setConsistencyRate(double consistencyRate) {
		this.consistencyRate = consistencyRate;
	}

	public ArrayList<Float> getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(ArrayList<Float> spendTime) {
		this.spendTime = spendTime;
	}

	public ArrayList<Dataset> getDataset() {
		return dataset;
	}

	public void setDataset(ArrayList<Dataset> dataset) {
		this.dataset = dataset;
	}

	//checks that given document
	public boolean find(String s) {
		for (int j = 0; j < this.uniqueInstances.size(); ++j) {
			if (this.uniqueInstances.get(j).getDocument() == s) {
				return false;
			}
		}
		
		return true;
	}

	//finds the unique instances in all instance that a user labeled.
	public void setUniqueInstance() {
		for (int i = 0; i < this.instances.size(); ++i) {
			if (find(this.instances.get(i).getDocument())) {
				this.uniqueInstances.add(instances.get(i));
			}
		}
	}
	
	//checks if given instance is inside the list
	public boolean isInside(Instance ins) {

		for (int i = 0; i < getInstances().size(); i++) {
			if (getInstances().get(i).getDocument().equalsIgnoreCase(ins.getDocument())) {
				return true;
			}
		}
		return false;

	}
	
	//calculates the consistency percentage in given dataset
	public double consistencyPercentage(Dataset data) {

		if (instances.isEmpty())
			return 0;
		
		int instanceNumber = 0;
		int uniqueInstanceNumber = 0;
		for (int x = 0; x < getInstances().size(); x++) {
			for (int y = 0; y < data.getInstances().size(); y++) {
				if (getInstances().get(x).getDocument() == data.getInstances().get(y).getDocument()) {
					instanceNumber++;
				}
			}

		}
		for (int x = 0; x < getUniqueInstances().size(); x++) {
			for (int y = 0; y < data.getInstances().size(); y++) {
				if (getUniqueInstances().get(x).getDocument() == data.getInstances().get(y).getDocument()) {
					uniqueInstanceNumber++;
				}
			}

		}
		double rate = 0; 
		if(instanceNumber != 0) {
			double recurrence = instanceNumber - uniqueInstanceNumber;
			rate = (recurrence / instanceNumber) * 100;
			
		}
		
		return rate;
	}
	
	//calculates the general consistency percentage of user
	public double consistencyPercentageGeneral() {

		if (instances.isEmpty())
			return 0;

		double recurrence = this.instances.size() - this.uniqueInstances.size();
		double rate = (recurrence / this.instances.size()) * 100;
		return rate;

	}

	//calculates the completeness of given dataset for a user
	public double completenessDataset(Dataset data) {

		int size = data.getInstances().size();
		setUniqueInstance();
		int count = 0;
		for (int x = 0; x < getUniqueInstances().size(); x++) {
			for (int y = 0; y < data.getInstances().size(); y++) {
				if (getUniqueInstances().get(x).getDocument() == data.getInstances().get(y).getDocument()) {
					count++;
				}
			}

		}

		if (instances.isEmpty())
			return 0;
		return ((double) count / size) * 100;
	}

	//calculate standart deviation
	public double calculateSD(ArrayList<Float> numArray) {
		double sum = 0, standardDeviation = 0;
		int length = numArray.size();

		for (float num : numArray) {
			sum += num;
		}

		double mean = sum / length;

		for (float num : numArray) {
			standardDeviation += Math.pow(num - mean, 2);
		}

		return Math.sqrt(standardDeviation / length);
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", user_type=" + user_type + "id: " + id + "]";
	}

}