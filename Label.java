package CSE3063F20P1_GRP2;

public class Label {
    private int id;
    private String text;

    public Label(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Label [id=" + id + ", text=" + text + "]";
    }

    
}
