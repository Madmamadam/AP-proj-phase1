package controller;

import javafx.geometry.Point2D;
import javafx.scene.Group;
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

    public static void locateACurve(CubicCurve cubicCurve,double firstX,double firstY,double lastX,double lastY,boolean firstLocationIsGate,boolean secondLocationIsGate){
        Configg cons=Configg.getInstance();
        double horizontal_distance=lastX-firstX;
        cubicCurve.setStartX(firstX);
        cubicCurve.setStartY(firstY);
        cubicCurve.setEndX(lastX);
        cubicCurve.setEndY(lastY);
        cubicCurve.setControlY1(firstY);
        cubicCurve.setControlY2(lastY);
        if(firstLocationIsGate || horizontal_distance>=0) {
            cubicCurve.setControlX1(firstX + cons.getControlXConstant());
        }
        else {
            cubicCurve.setControlX1(firstX - cons.getControlXConstant());
        }
        if(secondLocationIsGate || horizontal_distance>=0) {
            cubicCurve.setControlX2(lastX - cons.getControlXConstant());
        }
        else {
            cubicCurve.setControlX2(lastX + cons.getControlXConstant());
        }
    }


    public static double calculateWireLength(Wire wire) {
        Configg cons=Configg.getInstance();
        double lengthSum = 0;
        for (Node cubicCurveNode : wire.getAllOfCurve_Group().getChildren()) {
            if (cubicCurveNode.getClass() == CubicCurve.class) {
                CubicCurve cubicCurve = (CubicCurve) cubicCurveNode;
                lengthSum=lengthSum+getCubicCurveLength(cubicCurve,cons.getLengthIterationStep());
            }
            else {
                System.out.println("Error: Not a cubic curve in group");
            }
        }
        return lengthSum;
    }

    public static Point2D positionOnALength(Wire wire, double length){
        double remainingLength = length;
        if (length<0){
            System.out.println("Error: Length is negative");
        }
        Configg cons=Configg.getInstance();
        for(Node cubicCurveNode : wire.getAllOfCurve_Group().getChildren()){
            if (cubicCurveNode.getClass() == CubicCurve.class) {
                CubicCurve cubicCurve = (CubicCurve) cubicCurveNode;
                double cubicCurveLength = getCubicCurveLength(cubicCurve,cons.getLengthIterationStep());
                if(remainingLength < cubicCurveLength){
                    return evaluateCubicCurve(cubicCurve,remainingLength/cubicCurveLength);
                }
                else {
                    remainingLength -= cubicCurveLength;
                }
            }
            else {
                System.out.println("Error: Not a cubic curve in group");
            }
        }
        System.out.println("Error: Not a cubic curve in group");
        return null;
    }






    /**
     * Calculates the approximate length of a CubicCurve by flattening it into a series of line segments.
     * The accuracy of the result depends on the number of steps.
     *
     * @param curve The CubicCurve to measure.
     * @param steps The number of line segments to use for the approximation. A value of 1000 is often sufficient.
     * @return The approximate length of the curve.
     */
    private static double getCubicCurveLength(CubicCurve curve, int steps) {
        if (curve == null || steps <= 0) {
            return 0.0;
        }

        double totalLength = 0.0;

        // Get the starting point of the curve
        Point2D previousPoint = new Point2D(curve.getStartX(), curve.getStartY());

        for (int i = 1; i <= steps; i++) {
            // 't' is the parameter that goes from 0.0 to 1.0
            double t = (double) i / steps;

            // Calculate the point on the curve for the current 't'
            Point2D currentPoint = evaluateCubicCurve(curve, t);

            // Add the distance between the last point and the current point
            totalLength += previousPoint.distance(currentPoint);

            // Update the previous point for the next iteration
            previousPoint = currentPoint;
        }

        return totalLength;
    }

    /**
     * Evaluates the coordinates of a point on a CubicCurve at a given parameter 't'.
     * The cubic Bézier formula is: B(t) = (1-t)³P₀ + 3(1-t)²tP₁ + 3(1-t)t²P₂ + t³P₃
     *
     * @param curve The CubicCurve.
     * @param t     The parameter, which ranges from 0.0 (start of the curve) to 1.0 (end of the curve).
     * @return The Point2D coordinate on the curve for the given t.
     */
    private static Point2D evaluateCubicCurve(CubicCurve curve, double t) {
        // Pre-calculate powers of t and (1-t) for efficiency
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;

        // Apply the cubic Bézier formula
        double x = uuu * curve.getStartX() +
                3 * uu * t * curve.getControlX1() +
                3 * u * tt * curve.getControlX2() +
                ttt * curve.getEndX();

        double y = uuu * curve.getStartY() +
                3 * uu * t * curve.getControlY1() +
                3 * u * tt * curve.getControlY2() +
                ttt * curve.getEndY();

        return new Point2D(x, y);
    }

    public static Group allCurve_justData_Clone(Wire wire) {
        Group cloneGroup = new Group();
        for (Node cubicCurveNode : wire.getAllOfCurve_Group().getChildren()) {
            if (cubicCurveNode.getClass() == CubicCurve.class) {
                CubicCurve cubicCurve = (CubicCurve) cubicCurveNode;
                cloneGroup.getChildren().add(get_one_curve_clone(cubicCurve));
            }
            else {
                System.out.println("Error: Not a cubic curve in group");
            }
        }
        return cloneGroup;
    }

    private static Node get_one_curve_clone(CubicCurve cubicCurve) {
        CubicCurve cloneCubicCurve = new CubicCurve();
        cloneCubicCurve.setStartX(cubicCurve.getStartX());
        cloneCubicCurve.setStartY(cubicCurve.getStartY());
        cloneCubicCurve.setControlX1(cubicCurve.getControlX1());
        cloneCubicCurve.setControlY1(cubicCurve.getControlY1());
        cloneCubicCurve.setControlX2(cubicCurve.getControlX2());
        cloneCubicCurve.setControlY2(cubicCurve.getControlY2());
        cloneCubicCurve.setEndX(cubicCurve.getEndX());
        cloneCubicCurve.setEndY(cubicCurve.getEndY());
        return cloneCubicCurve;
    }
}
