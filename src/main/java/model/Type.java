package model;

public class Type {
    private String name;
    private int id;
    public Type(int id) {
        this.id = id;
        if(id==1){
            this.name = "rectangle";
        }
        else if(id==2){
            this.name = "triangle";
        }
    }
}
