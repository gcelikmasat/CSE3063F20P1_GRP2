/*
* OtherMechanism.java
* This class is for other kinds of users who labels an instance differently.
*/


package CSE3063F20P1_GRP2;

import java.util.ArrayList;

public class OtherMechanism extends User {

	public OtherMechanism(int id, String name, String user_type) {
		super(id, name, user_type);
	}

	//labels an instance
	@Override
	public void label(LabelAssignment la, ArrayList<Label> l, int max) {

	}

	//trace actions
	@Override
	public void trace(LabelAssignment la) {

	}

}
