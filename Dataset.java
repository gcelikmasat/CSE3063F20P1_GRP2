package CSE3063F20P1_GRP2;

import java.util.ArrayList;


public class Dataset {
    
    private int id;
    private String name;
    private int maxNoLabels;
    private ArrayList<Label> labels;
    private ArrayList<Instance> instances;

    public Dataset(int id, String name, int maxNoLabels, ArrayList<Label> labels, ArrayList<Instance> instances){
    this.id = id;
    this.name = name;
    this.maxNoLabels = maxNoLabels;
    this.labels = new ArrayList<Label>(labels);
    this.instances = new ArrayList<Instance>(instances);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxNoLabels() {
        return maxNoLabels;
    }

    public void setMaxNoLabels(int maxNoLabels) {
        this.maxNoLabels = maxNoLabels;
    }

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    public ArrayList<Instance> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<Instance> instances) {
        this.instances = instances;
    }

    public void printLabels(){
        System.out.println("Class Labels :");
        for(int i=0; i< this.labels.size(); i++){
            System.out.print("Label id: " + this.labels.get(i).getId() + "\n" +
            "Label text: " + this.labels.get(i).getText() + "\n");
        }
    }

    public void printInstances(){
        System.out.println("Instances :");
        for(int i=0; i< this.instances.size(); i++){
            System.out.print("id: " + this.instances.get(i).getId() + "\n" +
            "Instance: " + this.instances.get(i).getDocument() + "\n");
        }
    }

    public void printDataset(){
        System.out.print("Dataset id: " + getId() + "\n" +
        "Dataset Name: " + getName() + "\n" +
        "Maximum number of labels per instance: " + getMaxNoLabels() + "\n");

        printLabels();
        printInstances();
    }
    
}
