package controller;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
import mains.Configg;
import model.Wire;

public class AllCurvesMethods {
    public static void wire_setStokeWide(Wire wire , Double strokeWide){
        for(Node cubicCurve :wire.getAllOfCurve_Group().getChildren()){
            if(cubicCurve.getClass() == CubicCurve.class){
                ((CubicCurve) cubicCurve).setStrokeWidth(strokeWide);
            }
            else {
                System.out.println("Error: Not a cubic curve in group");
            }

        }
    }
    public static void wire_setStroke(Wire wire , Paint stroke){
        for(Node cubicCurve :wire.getAllOfCurve_Group().getChildren()){
            if(cubicCurve.getClass() == CubicCurve.class){
                ((CubicCurve) cubicCurve).setStroke(stroke);
            }
            else {
                System.out.println("Error: Not a cubic curve in group");
            }

        }
    }
    public static void wire_setFill(Wire wire , Paint fill){
        for(Node cubicCurve :wire.getAllOfCurve_Group().getChildren()){
            if(cubicCurve.getClass() == CubicCurve.class){
                ((CubicCurve) cubicCurve).setFill(fill);
            }
            else{
                System.out.println("Error: Not a cubic curve in group");
            }
        }
    }

    public static void wire_setLikeSample(Wire wire, CubicCurve sampleCurve) {
        AllCurvesMethods.wire_setStroke(wire,sampleCurve.getStroke());
        AllCurvesMethods.wire_setStokeWide(wire,sampleCurve.getStrokeWidth());
        AllCurvesMethods.wire_setFill(wire,sampleCurve.getFill());

    }

    public static void locateACurve(CubicCurve cubicCurve,double firstX,double firstY,double lastX,double lastY){
        double horizontal_distance = lastX - firstX;
        Configg cons=Configg.getInstance();

        cubicCurve.setStartX(firstX);
        cubicCurve.setStartY(firstY);
        cubicCurve.setEndX(lastX);
        cubicCurve.setEndY(lastY);
        cubicCurve.setControlX1(firstX+horizontal_distance/cons.getControlXConstant());
        cubicCurve.setControlY1(firstY);
        cubicCurve.setControlX2(lastX-horizontal_distance/cons.getControlXConstant());
        cubicCurve.setControlY2(lastY);
    }


}
