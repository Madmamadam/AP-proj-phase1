package model;

import controller.AllCurvesMethods;
import controller.Methods;
import javafx.scene.Group;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import mains.Configg;

import java.util.ArrayList;

public class Wire {
    private Gate firstgate;
    private Gate secondgate;
    private double length;
    private Line line ;
    private ArrayList<CubicCurve> cubicCurves= new ArrayList<>();
    private Group AllOFCurves = new Group();



    public Wire(){
        Configg cons = Configg.getInstance();

        CubicCurve firstCurve = new CubicCurve();
        cubicCurves.add(firstCurve);
        cubicCurves.getFirst().setStrokeWidth(cons.getLine_width());
        cubicCurves.getFirst().setStroke(cons.getLine_color());
        cubicCurves.getFirst().setFill(null);
//        line=new Line();
//        line.setStrokeWidth(cons.getLine_width());
//        line.setStroke(cons.getLine_color());


    }
    public Wire(Gate firstgate, Gate secondgate) {
        Configg cons = Configg.getInstance();
        this();
        //first is outer. maybe...
        this.firstgate = firstgate;
        this.secondgate = secondgate;
        double horizontal_distance = secondgate.getX() - firstgate.getX();

        cubicCurves.getFirst().setStartX(firstgate.getX());
        cubicCurves.getFirst().setStartY(firstgate.getY());
        cubicCurves.getFirst().setEndX(secondgate.getX());
        cubicCurves.getFirst().setEndY(secondgate.getY());
        cubicCurves.getFirst().setControlX1(firstgate.getX()+horizontal_distance/cons.getControlXConstant());
        cubicCurves.getFirst().setControlY1(firstgate.getY());
        cubicCurves.getFirst().setControlX2(secondgate.getX()-horizontal_distance/cons.getControlXConstant());
        cubicCurves.getFirst().setControlY2(secondgate.getY());
//        cubicCurves.get


        this.line=new Line(firstgate.getX(),firstgate.getY(),secondgate.getX(),secondgate.getY());

        length= Methods.calculate_wire_length(this);
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

    public Line getLine() { return line;}

    public void setLine(Line line) { this.line = line;}

    public ArrayList<CubicCurve> getCubicCurvesModels() {
        return cubicCurves;
    }

    public Group getAllOfCurve_Group(){
        return AllOFCurves;
    }

    public Wire cloneWire() {
        return new Wire(this.firstgate, this.secondgate);
    }


}
