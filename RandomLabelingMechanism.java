package CSE3063F20P1_GRP2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RandomLabelingMechanism extends User {

	
	public RandomLabelingMechanism(int id, String name, String user_type) {
		super(id, name, user_type);
	}

	@Override
	public void label(LabelAssignment la, ArrayList<Label> l, int max,FileHandler fh, Logger logger){
        
        Date currentDate = new Date();
        la.setDate(currentDate);
		int times = (int) (Math.random() * max)+1;
		
		ArrayList<Label> clone= (ArrayList<Label>)l.clone();
		
		for (int x = 0; x < times; x++) {
			int random=(int) (Math.random() * clone.size());
			la.getClassLabel().add(clone.get(random));
			clone.remove(random);
		}
        System.out.print("instance id: " + la.getInstance().getId());
		
		(la.getClassLabel()).sort(Comparator.comparing(Label::getId));
		
		System.out.print(" class label id: ");
		for (int x = 0; x < times; x++) {
			System.out.print(la.getClassLabel().get(x).getId()+" ");
		}

		System.out.print(" user id: " + la.getUser().getId());
        System.out.println(" date: " + la.getDate());

      
        
        try {  

            // This block configure the logger with handler and formatter   
             
    
            // the following statement is used to log any messages  
            logger.info(currentDate + "[Instance tagger] INFO user id: " + la.getUser().getId() + " " + la.getUser().getName() + " tagged instance id: " + la.getInstance().getId() + " with class label: ");
            
        for(int i=0;i< times;i++){
            logger.info(la.getClassLabel().get(i).getId() + " " + la.getClassLabel().get(i).getText() );
        }

        logger.info("\n"); 
    
        } catch (SecurityException e) {  
            e.printStackTrace();  
        }  

        logger.setUseParentHandlers(false);
       
	}
}

