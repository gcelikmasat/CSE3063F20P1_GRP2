package CSE3063F20P1_GRP2;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OtherMechanism extends User implements Serializable {

	public OtherMechanism(int id, String name, String user_type) {
		super(id, name, user_type);
	}

	@Override
	public void label(LabelAssignment la, Dataset data) {

	}

	@Override
	public void trace(LabelAssignment la) {

	}

}