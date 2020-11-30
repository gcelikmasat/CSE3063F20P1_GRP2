package CSE3063F20P1_GRP2;

public class Instance {
    private int id;
    private String document;

    public Instance(int id, String document){
        this.id = id;
        this.document = document;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "Instance [document=" + document + ", id=" + id + "]";
    }
        
}
