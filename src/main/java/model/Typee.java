package model;

public class Typee {
    private String shapeName;
    private String rollName;
    private int id;



    private Typee(){}
    public Typee(int id) {
        this.id = id;
        if(id==1){
            this.shapeName = "rectangle";
        }
        else if(id==2){
            this.shapeName = "triangle";
        }
        else if(id==3){
            this.shapeName = "two6";
        }
    }

    public int getId() {
        return id;
    }

    public String getShapeName() {
        return shapeName;
    }
}
