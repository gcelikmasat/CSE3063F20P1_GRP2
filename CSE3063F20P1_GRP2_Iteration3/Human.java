/*
 * Human.java 
 * 
 * A type of user that Labels and instance based on user input.
 * 
 * */

package CSE3063F20P1_GRP2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({ "serial" })
public class Human extends User implements Serializable {

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Human(int id, String name, String user_type, String password) {
		super(id, name, user_type);
		this.setPassword(password);
	}

	@Override
	public void label(LabelAssignment la, Dataset data) throws InterruptedException {
		System.out.println("-----------------------------------------------------------");
		Action action = new Action();
		long start = System.currentTimeMillis();
		// some time passes

		// choose instance with probabilty
		Instance temp = chooseInstanceWithProbability(data);
		la.setInstance(temp);
		Date currentDate = new Date();
		la.setDate(currentDate);

		int index = 0;
		for (int i = 0; i < data.getInstances().size(); i++) {
			if (data.getInstances().get(i).getId() == temp.getId()) {
				index = i;
			}
		}
		System.out.println("Instance " + temp.getId() + " :" + temp.getDocument());
		System.out.println("\nEnter the ids of the class labels' that you want to label with (Enter -1 to stop): ");

		System.out.println(data.getLabels());
		System.out.println("Maximum number of labels you can take is " + data.getMaxNoLabels());

		ArrayList<Integer> classLabelIds = new ArrayList<>();
		int classLabelId = 0;
		boolean labelled = false;
		int counter = 0;
		while (classLabelId != -1 && counter != data.getMaxNoLabels()) {

			try {
				// get label ids
				classLabelId = action.takeLabel();
				Label label = data.findExist(classLabelId);

				if (label != null && !classLabelIds.contains(classLabelId)) {

					classLabelIds.add(classLabelId);
					la.getClassLabel().add(label);
					data.getInstances().get(index).getClassLabel().add(label);
					temp.getClassLabel().add(label);
					labelled = true;
					counter++;

				} else if (label == null && classLabelId != -1) {
					System.out.println("There is no such label id " + classLabelId);
				}
			} catch (InputMismatchException e) {
				System.out.println("you should enter a number.");
				break;
			}

		}
		if (labelled) {
			la.getClassLabel().sort(Comparator.comparing(Label::getId));
			super.getInstances().add(temp);

			TimeUnit.MILLISECONDS.sleep(60);

			long end = System.currentTimeMillis();
			long elapsedTime = end - start;

			float elapsedTimeSec = elapsedTime / 1000F;
			super.getSpendTime().add(elapsedTimeSec);
			trace(la);
		}
	}

	// choose instance
	public Instance chooseInstanceWithProbability(Dataset data) {

		int index = 0;
		Instance b = new Instance();
		int rand = (int) (Math.random() * 100);
		// Choose an instance which is labeled before with consistency check probability
		if (rand < getConsistencyCheckProbability() * 100 && (super.getInstances().size() != 0)) {
			System.out.println("**Same instance** ");
			int randomInstance = (int) ((Math.random() * (super.getInstances().size())));
			Instance a = new Instance(super.getInstances().get(randomInstance));
			for (int i = 0; i < data.getInstances().size(); i++) {
				if (data.getInstances().get(i) == a) {

					index = i;
				}
			}
		} else {
			if (!super.getInstances().isEmpty()) {
				for (int i = 0; i < data.getInstances().size(); i++) {
					if (!isInside(data.getInstances().get(i))) {
						index = i;
						break;
					}
				}
			}
		}

		b.setId(data.getInstances().get(index).getId());
		b.setDocument(data.getInstances().get(index).getDocument());
		return b;
	}

	// checks that given name and password is correct
	boolean checkCredentials(String password, String userName) {

		if (this.password.equalsIgnoreCase(password) && super.getName().equalsIgnoreCase(userName)) {
			return true;

		}
		return false;

	}

	@Override
	public void trace(LabelAssignment la) {
		// TODO Auto-generated method stub
		Logger logger = LogManager.getLogger();
		logger.info("user id: " + this.getId() + " " + this.getName() + " tagged instance id: "
				+ la.getInstance().getId() + " with class label " + la.getClassLabel().toString() + " instance:"
				+ la.getInstance().getDocument());

	}

}
