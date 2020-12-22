package CSE3063F20P1_GRP2;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Instance implements Serializable {

	private int id;
	private String document;
	private ArrayList<Label> classLabel = new ArrayList<Label>();
	private ArrayList<Label> uniqueLabel = new ArrayList<Label>();
	private Label finalLabel;

	public Instance() {
	}

	public Instance(int id, String document) {
		this.id = id;
		this.document = document;
	}

	public Instance(Instance ins) {
		this.id = ins.getId();
		this.document = ins.getDocument();
		this.classLabel = ins.getClassLabel();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public ArrayList<Label> getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(ArrayList<Label> classLabel) {
		this.classLabel = classLabel;
	}

	public Label getFinalLabel() {
		return finalLabel;
	}

	public void setFinalLabel(Label finalLabel) {
		this.finalLabel = finalLabel;
	}

	public ArrayList<Label> getUniqueLabel() {
		return uniqueLabel;
	}

	public void setUniqueLabel(ArrayList<Label> uniqueLabel) {
		this.uniqueLabel = uniqueLabel;
	}

	public void setUniqueLabelAssignments(Dataset data) {

		for (int x = 0; x < classLabel.size(); x++) {
			if (!uniqueLabel.contains(classLabel.get(x)))
				uniqueLabel.add(classLabel.get(x));
		}
	}

	public int totalAssignments(ArrayList<LabelAssignment> labelAssignment) {

		int count = 0;
		for (int x = 0; x < labelAssignment.size(); x++) {
			if (this.getId() == labelAssignment.get(x).getInstance().getId())
				count++;
		}
		return count;
	}

	public int uniqueUsers(ArrayList<LabelAssignment> labelAssignment) {

		ArrayList<Integer> index = new ArrayList<Integer>();
		for (int x = 0; x < labelAssignment.size(); x++) {

			if (labelAssignment.get(x).getInstance().getId() == this.getId()) {
				if (!index.contains(labelAssignment.get(x).getUser().getId()))
					index.add(labelAssignment.get(x).getUser().getId());
			}
		}

		return index.size();
	}

	public void classlabelsPercentages() {

		int count = 0;
		double frequency = 0;
		for (int i = 0; i < uniqueLabel.size(); ++i) {
			count = 0;
			for (int j = 0; j < classLabel.size(); ++j) {
				if (uniqueLabel.get(i).getId() == this.classLabel.get(j).getId()) {
					count++;
				}
			}
			frequency = ((double) count / classLabel.size()) * 100;
			uniqueLabel.get(i).setFreq(frequency);
		}
	}

	public void instanceMetrics(Dataset data, ArrayList<LabelAssignment> labelAssignment) {

		setUniqueLabelAssignments(data);
		// 1. Total number of label assignments
		System.out.println("Instance: " + getId());
		System.out.println("Total number of assignment = " + totalAssignments(labelAssignment));

		// 2.
		System.out.println("Number of unique label assignments: " + uniqueLabel.size());

		// 3.
		System.out.println("Number of unique users:" + uniqueUsers(labelAssignment));

		// 4.
		System.out.println("List class labels and percentages: ");
		classlabelsPercentages();

		ArrayList<Label> maxs = new ArrayList<>();
		Label max = uniqueLabel.get(0);
		maxs.add(max);
		for (int i = 0; i < uniqueLabel.size(); ++i) {
			if (max.getFreq() < uniqueLabel.get(i).getFreq()) {
				max = uniqueLabel.get(i);
				maxs.clear();
				maxs.add(max);

			} else if (max.getFreq() == uniqueLabel.get(i).getFreq()) {
				maxs.add(max);
			}
		}

		// Final Label
		int size = maxs.size();
		if (size == 1) {
			this.finalLabel = maxs.get(0);
		} else {
			int rand = (int) ((Math.random() * (size)));
			this.finalLabel = maxs.get(rand);
		}
		System.out.println("Max frequency label:  " + max.getId() + ":" + max.getText() + " %:" + max.getFreq());
		System.out.println("Final label:: " + this.finalLabel);

		// 5,
		for (int i = 0; i < uniqueLabel.size(); ++i) {
			System.out.println(uniqueLabel.get(i).getId() + ":" + uniqueLabel.get(i).printFreq());
		}

		// 6,
		double result = 0;
		int base = uniqueLabel.size();

		System.out.println("Entropy: ");

		if (base != 1) {

			for (Label uniqueLabel : uniqueLabel) {

				System.out.print(
						"-1*" + uniqueLabel.getFreq() / 100 + "*log(" + uniqueLabel.getFreq() / 100 + ";" + base + ")");
				result += -1
						* ((uniqueLabel.getFreq() / 100) * ((Math.log(uniqueLabel.getFreq() / 100) / Math.log(base))));
			}
		}
		System.out.println("\nResult: " + "= " + result);

	}

	@Override
	public String toString() {
		return "\nInstance [id=" + id + ", document=" + document + ", \nclassLabel=" + classLabel + "]\n";
	}

}