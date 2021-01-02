/*
 * Metric.java
 *  
 * Statistics are collected and the results are given at the end of the labeling operation.
 * User,dataset and instance performance.
 * 
 */

package CSE3063F20P1_GRP2;

import java.util.ArrayList;

public class Metric {

	private Dataset data;

	// default constructor
	public Metric() {
	}

	// getter setter
	public Dataset getData() {
		return data;
	}

	public void setData(Dataset data) {
		this.data = data;
	}

	// A-User performance metrics
	public void userMetrics(User user) {
		System.out.println("\nUser: " + user.getId());
		// 1.
		System.out.println("Number of datasets assigned: " + user.getDataset().size());

		// 2.
		System.out.print("List of all datasets with their completeness percentage: ");
		int sizeDataset = user.getDataset().size();
		for (int i = 0; i < sizeDataset; ++i) {
			System.out.print(user.getDataset().get(i).getName() + ": %"
					+ user.completenessDataset(user.getDataset().get(i)) + "\t");
		}
		System.out.println("");

		// 3.
		System.out.println("Total number of instances labeled: " + user.getInstances().size());

		// 4.
		System.out.println("Total number of unique instances labeled: " + user.getUniqueInstances().size());

		// 5.
		System.out.println(
				"Consistency percentage of user " + user.getId() + ": %" + user.consistencyPercentageGeneral());

		// 6.
		double total = 0;
		int spendSize = user.getSpendTime().size();
		for (int i = 0; i < spendSize; ++i)
			total += user.getSpendTime().get(i);
		if (spendSize == 0) {
			System.out.println("Average time spent in labeling an instance in seconds: 0");
		} else {
			System.out.println("Average time spent in labeling an instance in seconds: " + total / (spendSize * 1.0));
		}

		// 7.
		System.out.println(
				"Std. dev. of  time spent in labeling an instance in second: " + user.calculateSD(user.getSpendTime()));

	}

	//C-Dataset performance metrics
	public void datasetMetrics() {

		// 1. Completeness percentage (e.g. what percentage of the instances are
		// labeled)
		System.out.println("Percentage of the instances are labeled in dataset " + data.getId() + " : %"
				+ data.datasetpercentage());

		// 2. Class distribution based on final instance labels (e.g. 70% positive, 30%
		// negative )
		System.out.println("Class distribution based on final instance labels: ");
		data.classDistribution();

		// 3. List number of unique instances for each class label ()
		System.out.println("List number of unique instances for each class label: ");

		int size = data.getLabels().size();
		for (int i = 0; i < size; ++i) {
			ArrayList<Integer> index = data.uniqueInstancesOfLabels(data.getLabels().get(i));
			System.out
					.println(data.getLabels().get(i).getId() + ": " + data.getLabels().get(i).getText() + ": " + index);
		}
		// 4. Number of users assigned to this dataset
		System.out.println("Number of users assigned: " + data.getUsers().size());

		/*
		 * 5. List of users assigned and their completeness percentage (e.g. (user 1,
		 * 99%), (user2, %80), and (user3,30%), meaning user 3 has labeled 30% of the
		 * unique instances in this dataset)
		 */
		int userSize = data.getUsers().size();
		for (int i = 0; i < userSize; ++i) {
			System.out.println("completeness percentage of user " + data.getUsers().get(i).getId() + ": %"
					+ data.getUsers().get(i).completenessDataset(data));
		}
		System.out.println();
		// 6. List of users assigned and their consistency percentage
		// (e.g. (user 1, 99%), (user2, %89), and (user3,70%)
		for (int i = 0; i < userSize; ++i) {
			System.out.println("consistency percentage of user " + data.getUsers().get(i).getId() + ": %"
					+ data.getUsers().get(i).consistencyPercentage(data));
		}
	}

	// B- Instance Performance Metrics
	public void instanceMetrics(Instance instance) {

		instance.setUniqueLabelAssignments(data);
		// 1. Total number of label assignments
		System.out.println("Instance: " + instance.getId());
		System.out.println("Total number of assignment = " + instance.totalAssignments(data.getAssignments()));

		// 2. Number of unique label assignments
		System.out.println("Number of unique label assignments: " + instance.getUniqueLabel().size());

		// 3. Number of unique users
		System.out.println("Number of unique users:" + instance.uniqueUsers(data.getAssignments()));

		// 4. Most frequent class label and percentage
		System.out.println("List class labels and percentages: ");
		instance.classlabelsPercentages();

		Label max = instance.mostFrequencyLabel();
		System.out.println("Max frequency label:  " + max.getId() + ":" + max.getText() + " %:" + max.getFreq());
		System.out.println("Final label:: " + instance.getFinalLabel());

		// 5. List class labels and percentages
		int sizeUnique = instance.getUniqueLabel().size();
		for (int i = 0; i < sizeUnique; ++i) {
			System.out.println(
					instance.getUniqueLabel().get(i).getId() + ":" + instance.getUniqueLabel().get(i).printFreq());
		}

		// 6. Entropy (e.g. for labeling assignments
		double result = 0;
		int base = instance.getUniqueLabel().size();

		System.out.println("Entropy: ");

		if (base != 1) {

			for (Label uniqueLabel : instance.getUniqueLabel()) {

				System.out.print(
						"-1*" + uniqueLabel.getFreq() / 100 + "*log(" + uniqueLabel.getFreq() / 100 + ";" + base + ")");
				result += -1
						* ((uniqueLabel.getFreq() / 100) * ((Math.log(uniqueLabel.getFreq() / 100) / Math.log(base))));
			}
		}
		System.out.println("\nResult: " + "= " + result);

	}

}
