/*
 * 
 * OtherMechanism.java
 * 
 * A type of user that Labels and instance based on a trained data.
 * 
 * */


package CSE3063F20P1_GRP2;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public class OtherMechanism extends User implements Serializable {

	public OtherMechanism(int id, String name, String user_type) {
		super(id, name, user_type);
	}

	@Override
	public void label(LabelAssignment la, Dataset data) throws InterruptedException {
		if (data.getName().equalsIgnoreCase("Sentiment Dataset")) {
			final String MODEL = "review.dat";
			Train wt = new Train();

			// long start = System.nanoTime();
			long start = System.currentTimeMillis();
			// some time passes

			if (new File(MODEL).exists()) {
				wt.loadModel(MODEL);
			} else {
				wt.transform();
				wt.fit();
				wt.saveModel(MODEL);
			}

			// find the instance that is next for labelling.
			int index = 0;
			if (!super.getInstances().isEmpty()) {
				for (int i = 0; i < data.getInstances().size(); i++) {
					if (!isInside(data.getInstances().get(i))) {
						index = i;
						break;
					}
				}
			}

			String document = data.getInstances().get(index).getDocument();
			String predictedLabel = wt.predict(document);

			// find label inside the dataset
			int labelId = -1;
			for (int i = 0; i < data.getLabels().size(); ++i) {
				if (data.getLabels().get(i).getText().equalsIgnoreCase(predictedLabel))
					labelId = i + 1;
			}
			if (labelId == -1)
				System.out.println("Unknown label id");

			// set label
			Label label = new Label(labelId, predictedLabel);

			// set instance
			Instance temp = new Instance();
			temp.setDocument(document);
			temp.getClassLabel().add(label);
			temp.setId(data.getInstances().get(index).getId());
			temp.getClassLabel().add(label);
			// set label assignment
			la.setInstance(temp);
			Date currentDate = new Date();
			la.setDate(currentDate);
			la.getClassLabel().add(label);

			data.getInstances().get(index).getClassLabel().add(label);
			super.getInstances().add(temp);

			TimeUnit.MILLISECONDS.sleep(60);

			long end = System.currentTimeMillis();
			long elapsedTime = end - start;

			float elapsedTimeSec = elapsedTime / 1000F;
			super.getSpendTime().add(elapsedTimeSec);
			trace(la);
		}

	}

	@Override
	public void trace(LabelAssignment la) {

		Logger logger = LogManager.getLogger();
		logger.info("user id: " + this.getId() + " " + this.getName() + " tagged instance id: "
				+ la.getInstance().getId() + " with class label " + la.getClassLabel().toString() + " instance:"
				+ la.getInstance().getDocument());

	}

}