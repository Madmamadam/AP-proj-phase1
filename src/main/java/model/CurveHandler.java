package model;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import mains.Configg;

public class CurveHandler {
    Configg cons =Configg.getInstance();

    Wire wire;
    private double x, y;
    Circle viewCircle=new Circle();

    public CurveHandler(double x, double y,Wire wire) {
        this.x = x;
        this.y = y;
        this.wire=wire;
        Node viewCirclenode =(Node) viewCircle;


        viewCircle.setCenterX(x);
        viewCircle.setCenterY(y);
        viewCircle.setRadius(cons.getCurveHandler_radius()+10);
        viewCircle.setFill(cons.getCurveHandler_color());
        if(wire!=null){
            wire.newCurveHandlerAdded(this);
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

}
