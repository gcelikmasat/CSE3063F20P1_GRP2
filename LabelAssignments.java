package CSE3063F20P1_GRP2;

import java.util.Date;
import java.util.ArrayList;

public class LabelAssignments{
    private ArrayList<Integer> classLabel;
    private Date date;
    private Instance instance;
    private User user;

    public LabelAssignments(ArrayList<Integer> classLabel, Date date, Instance instance, User user) {
        this.classLabel = classLabel;
        this.date = date;
        this.instance = instance;
        this.user = user;
    }
    public void addLabel(Integer m){
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

    public ArrayList<Integer> getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(ArrayList<Integer> classLabel) {
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