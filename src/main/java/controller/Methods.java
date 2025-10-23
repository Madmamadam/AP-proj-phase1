package controller;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import mains.Configg;
import model.MainGame_Logics;
import model.*;
import org.locationtech.jts.geom.*;

import java.util.List;
import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Methods {
    MainGame_Logics mainGameModel;
    public Methods(MainGame_Logics mainGamemodel){
        mainGameModel = mainGamemodel;
    }

    public boolean found_in_pairs(Signal signal1, Signal signal2) {
        for(Pairs pair : mainGameModel.staticDataModel.collapsedPairs){
            if(pair.signal1==signal1 && pair.signal2==signal2 || pair.signal1==signal2 && pair.signal2==signal1){
                return true;
            }

        }

        return false;
    }

    //simple just find first better
    public Object recommended_gate(Sysbox sysbox, Signal signal) {
        boolean second_found = false;
        Gate secound_gate = null;
        for(Gate gate:sysbox.outer_gates){
            if(gate.getWire().getSecondgate().getSysbox().isHealthy()) {
                if (Objects.equals(gate.getTypee().getShapeName(), signal.getTypee().getShapeName()) && !gate.isIn_use() && gate.getWire() != null) {
                    return gate;
                }
                if (!gate.isIn_use() && !second_found && gate.getWire() != null) {
                    second_found = true;
                    secound_gate = gate;
                }
            }
        }
        if(second_found){
            return secound_gate;
        }
        return null;

    }
    public void update_signal_OnWire(Signal signal) {
        Wire wire=signal.getLinked_wire();

        Point2D pos = AllCurvesMethods.positionOnALength(wire,signal.getLength_on_wire());
        signal.setX_on_wire(pos.getX());
        signal.setY_on_wire(pos.getY());
        one_signal_update_polygon(signal);
    }
    private void one_signal_update_polygon(Signal signal){
        Configg cons = Configg.getInstance();
        double pi=3.01415;

        Polygon poly =signal.poly;
        poly.getPoints().clear();

        //just for debugging
        if(mainGameModel.virtual_run){
            System.out.println("virtual run polygon");
        }

        if(Objects.equals(signal.getTypee().getShapeName(),"rectangle")){
            poly.getPoints().addAll(signal.getX()-cons.getSignal_rectangle_width()/2,signal.getY()-cons.getSignal_rectangle_height()/2);
            poly.getPoints().addAll(signal.getX()-cons.getSignal_rectangle_width()/2,signal.getY()+cons.getSignal_rectangle_height()/2);
            poly.getPoints().addAll(signal.getX()+cons.getSignal_rectangle_width()/2,signal.getY()+cons.getSignal_rectangle_height()/2);
            poly.getPoints().addAll(signal.getX()+cons.getSignal_rectangle_width()/2,signal.getY()-cons.getSignal_rectangle_height()/2);
            poly.setFill(cons.getSignal_rectangle_color());
            System.out.println("add rectangle");
        }
        if(Objects.equals(signal.getTypee().getShapeName(),"triangle")){
            for (int i=0 ; i<3;i++) {
                poly.getPoints().addAll(signal.getX()-cons.getSignal_triangle_radius()*sin(i*2*pi/3), signal.getY() - cons.getSignal_triangle_radius()*cos(i*2*pi/3));
            }
            poly.setFill(cons.getSignal_triangle_color());
            System.out.println("add triangle");
        }
        if(Objects.equals(signal.getTypee().getShapeName(),"two6")){
            /*
             * This implements "two hexagons" as a single 10-sided polygon
             * (decagon) representing two regular hexagons touching along
             * a vertical edge. This is necessary because signal.poly
             * can only be one continuous polygon.
             *
             * Requires:
             * 1. cons.getSignal_two6_radius() [double]: The radius from center
             * to vertex of one of the hexagons.
             * 2. cons.getSignal_two6_color() [javafx.scene.paint.Paint]
             *
             * Note: Uses Math.PI and Math.cos/sin for accuracy, differing
             * from the 'pi' variable and assumed static imports in the
             * "triangle" implementation.
             */

            // Center point of the combined shape
            double cx = signal.getX();
            double cy = signal.getY();

            // Radius of a single hexagon
            double r = cons.getSignal_two6_radius();

            // Pre-calculate geometric offsets based on a 60-degree (PI/3) angle
            // w_offset is the horizontal distance from a hex center to its vertical edge
            double w_offset = r * Math.sin(Math.PI / 3.0); // r * 0.866
            // h_offset is the vertical distance from a hex center to its shared vertex
            double h_offset = r * Math.cos(Math.PI / 3.0); // r * 0.5

            // Add the 10 vertices of the combined outline in counter-clockwise order
            // The shape is centered around (cx, cy)

            // Start at the top-middle "waist" vertex
            poly.getPoints().addAll(cx, cy - h_offset);             // V1

            // Left Hexagon outline
            poly.getPoints().addAll(cx - w_offset, cy - r);         // V2
            poly.getPoints().addAll(cx - (2 * w_offset), cy - h_offset); // V3
            poly.getPoints().addAll(cx - (2 * w_offset), cy + h_offset); // V4
            poly.getPoints().addAll(cx - w_offset, cy + r);         // V5

            // Bottom-middle "waist" vertex
            poly.getPoints().addAll(cx, cy + h_offset);             // V6

            // Right Hexagon outline
            poly.getPoints().addAll(cx + w_offset, cy + r);         // V7
            poly.getPoints().addAll(cx + (2 * w_offset), cy + h_offset); // V8
            poly.getPoints().addAll(cx + (2 * w_offset), cy - h_offset); // V9
            poly.getPoints().addAll(cx + w_offset, cy - r);         // V10

            poly.setFill(cons.getSignal_two6_color());
            System.out.println("add two6 (decagon)");
        }
        System.out.println("end poly");
    }


    public static double calculate_wire_length(Wire wire) {
        double dx =  wire.getFirstgate().getX() - wire.getSecondgate().getX();
        double dy =  wire.getFirstgate().getY() - wire.getSecondgate().getY();
        return Math.sqrt(dx*dx+dy*dy);
    }


















    private static final GeometryFactory factory = new GeometryFactory();

    public static Object checkCollisionAndGetPoint(Polygon fxPoly1, Polygon fxPoly2) {



        org.locationtech.jts.geom.Polygon jtsPoly1 = toJTSPolygon(fxPoly1);
        org.locationtech.jts.geom.Polygon jtsPoly2 = toJTSPolygon(fxPoly2);

        if (jtsPoly1 == null || jtsPoly2 == null) return null;
        double mean_x=0;
        double mean_y=0;
        if (jtsPoly1.intersects(jtsPoly2)) {
            Geometry intersection = jtsPoly1.intersection(jtsPoly2);
            for (Coordinate coord : intersection.getCoordinates()) {
                mean_x+=coord.getX()/intersection.getCoordinates().length;
                mean_y+=coord.getY()/intersection.getCoordinates().length;
            }
            Coordinate interaction_point = new Coordinate(mean_x, mean_y);
            return interaction_point;
        } else {
            return null; // بدون برخورد
        }
    }

    private static org.locationtech.jts.geom.Polygon toJTSPolygon(Polygon fxPolygon) {
        List<Double> fxPoints = fxPolygon.getPoints();
        int numPoints = fxPoints.size();
        if (numPoints < 6 || numPoints % 2 != 0) return null;

        Coordinate[] coords = new Coordinate[(numPoints / 2) + 1];
        for (int i = 0; i < numPoints; i += 2) {
            coords[i / 2] = new Coordinate(fxPoints.get(i), fxPoints.get(i + 1));
        }
        coords[coords.length - 1] = coords[0]; // بستن حلقه

        return factory.createPolygon(coords);
    }

    public double calculate_distance(double x, double y, double x1, double y1) {
        double dx=x-x1;
        double dy=y-y1;
        return Math.sqrt(dx*dx+dy*dy);
    }
}
