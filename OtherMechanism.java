
package CSE3063F20P1_GRP2;

import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class OtherMechanism extends User {

	public OtherMechanism(int id, String name, String user_type) {
		super(id, name, user_type);
	}

	@Override
	public void label(LabelAssignment la, ArrayList<Label> l, int max, FileHandler fh, Logger logger) {
	}
}