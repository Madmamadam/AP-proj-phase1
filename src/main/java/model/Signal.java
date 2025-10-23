package model;

import javafx.scene.shape.Polygon;
import mains.Configg;

public class Signal {
//  anything
    private Typee typee;
//    can be 1.on_wire 2.on_sysbox 3.ended 4.lost
    private String state;
    private double noise;
    private double length_on_wire;
    private Wire linked_wire;
    private boolean is_updated;
    private boolean going_forward;
    private double each_frame_length_delta ; //that means velocity

    private double x;
    private double y;
    public Polygon poly=new Polygon();
    private double x_on_wire;
    private double y_on_wire;
    private double x_ekhtelaf;
    private double y_ekhtelaf;

    public Signal(Typee typee) {
        Configg cons = Configg.getInstance();
        x_ekhtelaf=0.0;
        y_ekhtelaf=0.0;
        noise=0.0;
        length_on_wire=0.0;
        is_updated=false;
        going_forward=true;
        this.typee=typee;
        state="on_wire";
        each_frame_length_delta = cons.getDefault_delta_wire_length();
    }
    public Signal(Typee typee,Sysbox sysbox) {
        x_ekhtelaf=0.0;
        y_ekhtelaf=0.0;
        noise=0.0;
        length_on_wire=0.0;
        is_updated=false;
        this.typee=typee;
        state="on_wire";
    }

    public Signal cloneSignal() {
        Signal clone = new Signal(this.typee);
        clone.setState("on_sysbox");
        return clone;
    }

    public boolean isGoing_forward() {
        return going_forward;
    }

    public void setGoing_forward(boolean going_forward) {
        this.going_forward = going_forward;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX_on_wire() {
        return x_on_wire;
    }

    public void setX_on_wire(double x_on_wire) {
        this.x_on_wire = x_on_wire;
        this.x=this.x_on_wire+this.x_ekhtelaf;
    }

    public double getY_on_wire() {
        return y_on_wire;
    }

    public void setY_on_wire(double y_on_wire) {
        this.y_on_wire = y_on_wire;
        this.y=this.y_on_wire+this.y_ekhtelaf;
    }

    public double getX_ekhtelaf() {
        return x_ekhtelaf;
    }

    public void setX_ekhtelaf(double x_ekhtelaf) {
        this.x_ekhtelaf = x_ekhtelaf;
    }

    public double getY_ekhtelaf() {
        return y_ekhtelaf;
    }

    public void setY_ekhtelaf(double y_ekhtelaf) {
        this.y_ekhtelaf = y_ekhtelaf;
    }

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

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
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

    public double getEach_frame_length_delta() {
        return each_frame_length_delta;
    }

    public void setEach_frame_length_delta(double each_frame_length_delta) {
        this.each_frame_length_delta = each_frame_length_delta;
    }
}
