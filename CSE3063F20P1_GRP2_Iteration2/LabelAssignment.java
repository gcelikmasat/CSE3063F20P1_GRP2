/*
 * LabelAssignment.java 
 * This class keeps the information for labelling.
 * 
 */

package CSE3063F20P1_GRP2;

import java.util.Date;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class LabelAssignment implements Serializable{

	private ArrayList<Label> classLabel = new ArrayList<Label>();
	private Date date;
	private Instance instance;
	private User user;

	public LabelAssignment(User user) {
		this.user = user;
	}

	public void addLabel(Label m) {
		this.classLabel.add(m);
	}

	public void printClassLabels() {
		System.out.print("class label ids: [");
		for (int i = 0; i < classLabel.size(); i++) {
			System.out.print(" " + this.classLabel.get(i) + " ");
		}
		System.out.println(" ] \n");
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public ArrayList<Label> getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(ArrayList<Label> classLabel) {
		this.classLabel = classLabel;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "\nLabelAssignment [classLabel=" + classLabel + ", date=" + date + ", \ninstance=" + instance
				+ ", \nuser=" + user + "]";
	}

}
