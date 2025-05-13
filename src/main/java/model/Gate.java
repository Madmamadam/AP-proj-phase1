package model;

public class Gate {
    private int sysbox_id;
    private Typee typee;
    private boolean is_outer;
    private double x;
    private double y;

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

    public int getSysbox_id() {
        return sysbox_id;
    }

    public void setSysbox_id(int sysbox_id) {
        this.sysbox_id = sysbox_id;
    }

    public Typee getType() {
        return typee;
    }

    public void setType(Typee typee) {
        this.typee = typee;
    }

    public boolean isIs_outer() {
        return is_outer;
    }

    public void setIs_outer(boolean is_outer) {
        this.is_outer = is_outer;
    }

    public Gate() {

    }

    public Gate(Typee typee, boolean is_outer) {
        this.typee = typee;
        this.is_outer = is_outer;
    }
}
