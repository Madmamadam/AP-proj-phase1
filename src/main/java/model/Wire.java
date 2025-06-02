package model;

import controller.Methods;
import javafx.scene.shape.Line;
import mains.Configg;

public class Wire {
    private Gate firstgate;
    private Gate secondgate;
    private double length;
    private Line line ;


    public Wire(){
        Configg cons = Configg.getInstance();
        line=new Line();
        line.setStrokeWidth(cons.getLine_width());
        line.setStroke(cons.getLine_color());


    }
    public Wire(Gate firstgate, Gate secondgate) {
        Configg cons = Configg.getInstance();
        Methods methods = new Methods();
        //first is outer. maybe...
        this.firstgate = firstgate;
        this.secondgate = secondgate;
        this.line=new Line(firstgate.getX(),firstgate.getY(),secondgate.getX(),secondgate.getY());
        length= methods.calculate_wire_length(this);
        line.setStrokeWidth(cons.getLine_width());
        line.setStroke(cons.getLine_color());
    }

    public Gate getFirstgate() {
        return firstgate;
    }

    public void setFirstgate(Gate firstgate) {
        Configg cons = Configg.getInstance();
        this.firstgate = firstgate;
        if(secondgate!= null){
            line=new Line(firstgate.getX(),firstgate.getY(),secondgate.getX(),secondgate.getY());
            line.setStrokeWidth(cons.getLine_width());
            line.setStroke(cons.getLine_color());
        }
    }

    public Gate getSecondgate() {
        return secondgate;
    }

    public void setSecondgate(Gate secondgate) {
        Configg cons = Configg.getInstance();
        this.secondgate = secondgate;
        if(firstgate!= null){
            line=new Line(firstgate.getX(),firstgate.getY(),secondgate.getX(),secondgate.getY());
            line.setStrokeWidth(cons.getLine_width());
            line.setFill(cons.getLine_color());
        }
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Wire cloneWire() {
        return new Wire(this.firstgate, this.secondgate);
    }
}
