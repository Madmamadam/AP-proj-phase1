package model;

import controller.AllCurvesMethods;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import mains.Configg;

public class CurveHandler {
    Configg cons =Configg.getInstance();

    Wire wire;
    private double x, y;
    CubicCurve firstCurve;
    CubicCurve secondAddedCurve;
    Circle viewCircle=new Circle();

    public CurveHandler(double x, double y, Wire wire, CubicCurve cubicCurve) {
        this.x = x;
        this.y = y;
        this.wire=wire;
        this.firstCurve=cubicCurve;

        Node viewCirclenode =(Node) viewCircle;


        viewCircle.setCenterX(x);
        viewCircle.setCenterY(y);
        viewCircle.setRadius(cons.getCurveHandler_radius()+10);
        viewCircle.setFill(cons.getCurveHandler_color());
        if(wire!=null){
            wire.newCurveHandlerAdded(this);
        }
        add_second_curve_and_update_first_curve();
    }

    private void add_second_curve_and_update_first_curve() {
        double endX=firstCurve.getEndX();
        double endY=firstCurve.getEndY();
        AllCurvesMethods.locateACurve(firstCurve,firstCurve.getStartX(),firstCurve.getStartY(),this.x, this.y);
        secondAddedCurve = new CubicCurve();
        AllCurvesMethods.locateACurve(secondAddedCurve,this.x,this.y,endX,endY);
        add_second_curve_to_wire();
    }

    public void update_two_curves(){
        AllCurvesMethods.locateACurve(firstCurve,firstCurve.getStartX(),firstCurve.getStartY(),this.x, this.y);
        AllCurvesMethods.locateACurve(secondAddedCurve,this.x,this.y,secondAddedCurve.getEndX(),secondAddedCurve.getEndY());
        wire.curveAdded_so_update_needed();
    }

    private void add_second_curve_to_wire() {
        if(wire!=null){
            int index = wire.getAllOfCurve_Group().getChildren().indexOf(firstCurve);
            wire.getAllOfCurve_Group().getChildren().add(index+1,secondAddedCurve);
            wire.curveAdded_so_update_needed();
        }
        else {
            System.out.println("Error wire is null");
        }
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setSafeX(double x) {
        this.x = x;
        viewCircle.setCenterX(x);
    }
    public void setSafeY(double y) {
        this.y = y;
        viewCircle.setCenterY(y);
    }
    public Circle getViewCircle() {
        return viewCircle;
    }

    public void updateCurves() {
    }

    public void setSafeXY(double x, double y) {
        this.x=x;
        this.y=y;
        viewCircle.setCenterX(x);
        viewCircle.setCenterY(y);
        this.wire.update_length_AfterNewAllOfCurves();
    }
}
