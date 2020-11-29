package CSE3063F20P1_GRP2;

abstract class User {
    private String name;
    private String user_type;

    public User(String name, String user_type) {
        this.name = name;
        this.user_type = user_type;
    }

    abstract void label();

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
    
}