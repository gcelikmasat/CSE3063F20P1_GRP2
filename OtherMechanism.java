package CSE3063F20P1_GRP2;

import java.util.ArrayList;

public class OtherMechanisms extends User {

	public OtherMechanisms(int id, String name, String user_type) {
		super(id, name, user_type);
	}

	@Override
	public void label(LabelAssignments la, ArrayList<Label> l,int max) {
		System.out.println("Other Mechanisms. ");

	}
}