package model;

public class Typee {
    private String name;
    private int id;



    private Typee(){}
    public Typee(int id) {
        this.id = id;
        if(id==1){
            this.name = "rectangle";
        }
        else if(id==2){
            this.name = "triangle";
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
