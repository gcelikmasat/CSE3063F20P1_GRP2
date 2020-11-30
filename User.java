package CSE3063F20P1_GRP2;

import java.util.ArrayList;

abstract class User {
	private int id;
    private String name;
    private String user_type;

    public User(int id,String name, String user_type) {
    	this.id=id;
        this.name = name;
        this.user_type = user_type;
    }

    abstract void label(LabelAssignment la,ArrayList<Label> l,int max,FileHandler fh);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", user_type=" + user_type + "]";
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
    
}
