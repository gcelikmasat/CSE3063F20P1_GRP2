/* 
* RandomLabellingMechanism.java
* This class is subclass of User. 
* It creates RandomBots and labels instances randomly. 
*
*/

package CSE3063F20P1_GRP2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({ "unchecked", "serial" })

public class RandomLabelingMechanism extends User implements Serializable {

	public RandomLabelingMechanism(int id, String name, String user_type) {
		super(id, name, user_type);
	}

	// labels instances randomly
	@Override
	public void label(LabelAssignment la, Dataset data) throws InterruptedException {

		// long start = System.nanoTime();
		long start = System.currentTimeMillis();
		// some time passes

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

		int times = (int) (Math.random() * data.getMaxNoLabels()) + 1;

		ArrayList<Label> clone = (ArrayList<Label>) data.getLabels().clone();
		for (int x = 0; x < times; x++) {

			int random = (int) (Math.random() * clone.size());
			la.getClassLabel().add(clone.get(random));
			temp.getClassLabel().add(clone.get(random)); 
			data.getInstances().get(index).getClassLabel().add(clone.get(random));
			clone.remove(random);

		}
		la.getClassLabel().sort(Comparator.comparing(Label::getId));
		super.getInstances().add(temp);

		TimeUnit.MILLISECONDS.sleep(60);

		long end = System.currentTimeMillis();
		long elapsedTime = end - start;

		float elapsedTimeSec = elapsedTime / 1000F;
		super.getSpendTime().add(elapsedTimeSec);
		trace(la);
	}

	// choose random instance
	public Instance chooseInstanceWithProbability(Dataset data) {

		int index = 0;
		Instance b = new Instance();

		int rand = (int) (Math.random() * 100);
		// Choose an instance which is labeled before with consistency check probability
		if (rand < getConsistencyCheckProbability() * 100 && (super.getInstances().size() != 0)) {
			
			int randomInstance = (int) ((Math.random() * (super.getInstances().size())));
			Instance a = new Instance(super.getInstances().get(randomInstance));
			for (int i = 0; i < data.getInstances().size(); i++) {
				if (data.getInstances().get(i) == a) {
					index = i;
				}
			}
		} else {
			boolean done = true;
			while (done) {
				index = (int) ((Math.random() * (data.getInstances().size())));
				if (!super.getInstances().contains(data.getInstances().get(index)))
					done = false;
			}

		}
		b.setId(data.getInstances().get(index).getId());
		b.setDocument(data.getInstances().get(index).getDocument());
		return b;
	}

	// trace actions
	public void trace(LabelAssignment la) {

		Logger logger = LogManager.getLogger();
		logger.info("user id: " + this.getId() + " " + this.getName() + " tagged instance id: "
				+ la.getInstance().getId() + " with class label " + la.getClassLabel().toString() + " instance:"
				+ la.getInstance().getDocument());

	}

}
