package CSE3063F20P1_GRP2;

import java.util.Date;
import java.util.ArrayList;

public class LabelAssignments{
    private ArrayList<Label> classLabel;
    private Date date;
    private Instance instance;
    private User user;

    public LabelAssignments(Instance instance, User user) {
    	ArrayList<Label>classLabel=new ArrayList<Label>();
        this.classLabel = classLabel;
        Date date = new Date(); 
        this.date = date;
        this.instance = instance;
        this.user = user;
    }
    public void addLabel(Label m){
        this.classLabel.add(m);
    }

    public void printClassLabels() {
        System.out.print("class label ids: [");
        for(int i=0; i< classLabel.size(); i++){
            System.out.print(" " + this.classLabel.get(i) + " ");
        }
        System.out.println(" ] \n");
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public ArrayList<Label> getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(ArrayList<Label> classLabel) {
        this.classLabel = classLabel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
