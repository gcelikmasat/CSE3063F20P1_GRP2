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

	public User(int id, String name, String user_type) {
		this.id = id;
		this.name = name;
		this.user_type = user_type;
		trace();
	}

	public abstract void label(LabelAssignment la, Dataset data) throws InterruptedException;

	public abstract void trace(LabelAssignment la);

	private void trace() {

		Logger logger = LogManager.getLogger();
		logger.info("created " + this.name + " as " + this.user_type);
	}

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

	public boolean find(String s) {
		for (int j = 0; j < this.uniqueInstances.size(); ++j) {
			if (this.uniqueInstances.get(j).getDocument() == s) {
				return false;
			}
		}
		
		return true;
	}

	public void setUniqueInstance() {
		for (int i = 0; i < this.instances.size(); ++i) {
			if (find(this.instances.get(i).getDocument())) {
				this.uniqueInstances.add(instances.get(i));
			}
		}
	}

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

		double recurrence = instanceNumber - uniqueInstanceNumber;
		double rate = (recurrence / instanceNumber) * 100;
		return rate;

	}

	public double consistencyPercentageGeneral() {

		if (instances.isEmpty())
			return 0;

		double recurrence = this.instances.size() - this.uniqueInstances.size();
		double rate = (recurrence / this.instances.size()) * 100;
		return rate;

	}

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

	/*
	 * A- User Performance Metrics and Reports 1- Number of datasets assigned (e.g.
	 * 4) 2- List of all datasets with their completeness percentage (e.g. dataset1
	 * %100, dataset2 %90, dataset3 15%, dataset 4 0%) 3- Total number of instances
	 * labeled 4- Total number of unique instances labeled 5- Consistency percentage
	 * (e.g. %60 of the recurrent instances are labeled with the same class) 6-
	 * Average time spent in labeling an instance in seconds 7- Std. dev. of time
	 * spent in labeling an instance in seconds
	 */

	public void userMetrics(ArrayList<Dataset> data_list, Dataset data) {
		System.out.println("\nUser: " + id);
		// 1.
		System.out.println("Number of datasets assigned: " + dataset.size());

		// 2.
		System.out.print("List of all datasets with their completeness percentage: ");
		for (int i = 0; i < data_list.size(); ++i) {
			System.out.print(data_list.get(i).getName() + ": %" + completenessDataset(data_list.get(i)) + "\t");
		}
		System.out.println("");

		// 3.
		System.out.println("Total number of instances labeled: " + this.instances.size());

		// 4.
		System.out.println("Total number of unique instances labeled: " + this.uniqueInstances.size());

		// 5.
		System.out.println("Consistency percentage of user " + id + ": %" + consistencyPercentageGeneral());

		// 6.
		double total = 0;

		for (int i = 0; i < spendTime.size(); ++i)
			total += spendTime.get(i);
		if (spendTime.size() == 0) {
			System.out.println("Average time spent in labeling an instance in seconds: 0");
		} else {
			System.out.println("Average time spent in labeling an instance in seconds: " + total / (spendTime.size() * 1.0));
		}

		// 7.
		System.out.println("Std. dev. of  time spent in labeling an instance in second: " + calculateSD(spendTime));

	}

	@Override
	public String toString() {
		return "User [name=" + name + ", user_type=" + user_type + "id: " + id + "]";
	}

}