package oosd;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RandomLabelingMechanism extends User {

	public RandomLabelingMechanism(int id, String name, String user_type) {
		super(id, name, user_type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void label(LabelAssignment la, ArrayList<Label> l, int max) {

		Date currentDate = new Date();
		la.setDate(currentDate);
		int times = (int) (Math.random() * max) + 1;

		ArrayList<Label> clone = (ArrayList<Label>) l.clone();

		for (int x = 0; x < times; x++) {
			int random = (int) (Math.random() * clone.size());

			la.getClassLabel().add(clone.get(random));
			clone.remove(random);
		}
		// System.out.print("instance id: " + la.getInstance().getId());

		la.getClassLabel().sort(Comparator.comparing(Label::getId));

		// System.out.print(" class label id: ");
		/*
		 * for (int x = 0; x < times; x++) {
		 * System.out.print(la.getClassLabel().get(x).getId() + " ");
		 * 
		 * }
		 */

		// System.out.print(" user id: " + la.getUser().getId());
		// System.out.println(" date: " + la.getDate());

	}

	public void trace(LabelAssignment la) {

		Logger logger = LogManager.getLogger();
		logger.info("user id: " + this.getId() + " " + this.getName() + " tagged instance id: "
				+ la.getInstance().getId() + " with class label " + la.getClassLabel().toString() + " instance:"
				+ la.getInstance().getDocument());

	}

}
