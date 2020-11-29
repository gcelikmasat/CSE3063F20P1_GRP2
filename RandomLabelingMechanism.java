package CSE3063F20P1_GRP2;

public class RandomLabelingMechanism extends User {
    
    public RandomLabelingMechanism(String name, String user_type) {
        super(name, user_type);
    }

    @Override
    void label(){
        System.out.println("This is the label method inside rand. lab. mech.");
    }    
}
