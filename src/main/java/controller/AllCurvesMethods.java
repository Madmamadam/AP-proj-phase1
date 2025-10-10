package controller;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
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
}
