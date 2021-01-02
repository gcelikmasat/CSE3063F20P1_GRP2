/*
 * Action.java
 * 
 * A class for user interface
 * 
 * */

package CSE3063F20P1_GRP2;

import java.util.Scanner;

public class Action {

	private Scanner input = new Scanner(System.in);

	// default
	public Action() {
	}

	// takes user name and password from the user and return that user if it exist
	public User findUser(Dataset data) {

		int currentUser = -1;
		boolean exit = true;
		while (exit) {
			System.out.println("Enter UserName: ");
			String userName = input.nextLine();

			System.out.println("Enter Password: ");
			String password = input.nextLine();

			if (userName.isEmpty() && password.isEmpty()) {
				System.out.println("Bots are ready for labelling...");
				return null;
			}
			for (int i = 0; i < data.getUsers().size(); i++) {
				if (data.getUsers().get(i) instanceof Human) {
					if (((Human) data.getUsers().get(i)).checkCredentials(password, userName)) {

						System.out.println("Welcome " + userName);
						currentUser = i;
						exit = false;
						break;
					} else if (i == data.getUsers().size() - 1) {
						System.out.println("Invalid user name or password.");
					}
				}
			}
		}

		return data.getUsers().get(currentUser);
	}

	// take label id from user for tagging an instance
	public int takeLabel() {

		int classLabelId = input.nextInt();
		return classLabelId;

	}

}
