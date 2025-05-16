package model;

import javafx.scene.shape.Polygon;

public class Signal {
//  anything
    private Typee typee;
//    can be on_wire on_sysbox ended
    private String state;
    private int noise;
    private double length_on_wire;
    private Wire linked_wire;
    private boolean is_updated;

    private double x;
    private double y;
    public Polygon poly;



    public Signal() {}

    public Typee getTypee() {
        return typee;
    }

    public void setTypee(Typee typee) {
        this.typee = typee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getNoise() {
        return noise;
    }

    public void setNoise(int noise) {
        this.noise = noise;
    }

    public double getLength_on_wire() {
        return length_on_wire;
    }

    public void setLength_on_wire(double length_on_wire) {
        this.length_on_wire = length_on_wire;
    }

    public Wire getLinked_wire() {
        return linked_wire;
    }

    public void setLinked_wire(Wire linked_wire) {
        this.linked_wire = linked_wire;
    }

    public boolean isIs_updated() {
        return is_updated;
    }

    public void setIs_updated(boolean is_updated) {
        this.is_updated = is_updated;
    }
}
