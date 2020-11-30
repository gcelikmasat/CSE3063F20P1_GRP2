package CSE3063F20P1_GRP2;

import java.util.Random;

public class RandomLabelingMechanism extends User {
    
    public RandomLabelingMechanism(String name, String user_type) {
        super(name, user_type);
    }

    @Override
    public void label(LabelAssignments la, Label label){
        la.addLabel(label);
    }    
}
