package model;

import javafx.scene.shape.Polygon;

import java.util.Objects;

public class Gate {
    private Sysbox sysbox;
    private Typee typee;
    private boolean is_outer;
    private double x;
    private double y;
    private boolean in_use;
    private Wire wire;
    public Polygon poly=new Polygon();
    private double line_width=12;


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Sysbox getSysbox() {
        return sysbox;
    }

    public void setSysbox(Sysbox sysbox) {
        this.sysbox = sysbox;
    }

    public boolean isIs_outer() {
        return is_outer;
    }

    public Wire getWire() {
        return wire;
    }

    public void setWire(Wire wire) {
        this.wire = wire;
    }

    public boolean Is_outer() {
        return is_outer;
    }

    public void setIs_outer(boolean is_outer) {
        this.is_outer = is_outer;
    }

    public Typee getTypee() {
        return typee;
    }

    public void setTypee(Typee typee) {
        this.typee = typee;
    }

    public boolean isIn_use() {
        return in_use;
    }

    public void setIn_use(boolean in_use) {
        this.in_use = in_use;
    }

    public double getLine_width() {
        return line_width;
    }

    public void setLine_width(double line_width) {
        this.line_width = line_width;
    }

    public Gate() {

    }


    public Gate cloneGate() {
        return new Gate(this.typee, this.is_outer);
    }
    public Gate(Typee typee, boolean is_outer) {
        this.typee = typee;
        this.is_outer = is_outer;
//        if (Objects.equals(this.typee.getName(), "rectangle")) {
//            poly.getPoints().clear();
//            poly.getPoints().addAll(this.x);
//
//        }
    }
}
