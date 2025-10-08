package controller;

import javafx.scene.shape.Polygon;
import mains.Configg;
import mains.MainGame_Logics;
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
            if (Objects.equals(gate.getTypee().getName(),signal.getTypee().getName()) && !gate.isIn_use() && gate.getWire()!=null){
                return gate;
            }
            if(!gate.isIn_use() && !second_found && gate.getWire()!=null){
                second_found = true;
                secound_gate = gate;
            }
        }
        if(second_found){
            return secound_gate;
        }
        return null;

    }
    public void update_signal_onwire(Signal signal) {
        Wire wire=signal.getLinked_wire();
        double ratio = signal.getLength_on_wire()/ wire.getLength();
        signal.setX_on_wire(wire.getFirstgate().getX()*(1-ratio) + wire.getSecondgate().getX()*ratio);
        signal.setY_on_wire(wire.getFirstgate().getY()*(1-ratio) + wire.getSecondgate().getY()*ratio);
        one_signal_update_polygan(signal);
    }
    private void one_signal_update_polygan(Signal signal){
        Configg cons = Configg.getInstance();
        double pi=3.01415;

        Polygon poly =signal.poly;
        poly.getPoints().clear();
        //just for debugging
        if(mainGameModel.virtual_run){
            System.out.println("virtual run polygon");
        }
        if(Objects.equals(signal.getTypee().getName(),"rectangle")){
            poly.getPoints().addAll(signal.getX()-cons.getSignal_rectangle_width()/2,signal.getY()-cons.getSignal_rectangle_height()/2);
            poly.getPoints().addAll(signal.getX()-cons.getSignal_rectangle_width()/2,signal.getY()+cons.getSignal_rectangle_height()/2);
            poly.getPoints().addAll(signal.getX()+cons.getSignal_rectangle_width()/2,signal.getY()+cons.getSignal_rectangle_height()/2);
            poly.getPoints().addAll(signal.getX()+cons.getSignal_rectangle_width()/2,signal.getY()-cons.getSignal_rectangle_height()/2);
            poly.setFill(cons.getSignal_rectangle_color());
            System.out.println("add rectangle");
        }
        if(Objects.equals(signal.getTypee().getName(),"triangle")){
            for (int i=0 ; i<3;i++) {
                poly.getPoints().addAll(signal.getX()-cons.getSignal_triangle_radius()*sin(i*2*pi/3), signal.getY() - cons.getSignal_triangle_radius()*cos(i*2*pi/3));
            }
            poly.setFill(cons.getSignal_triangle_color());
            System.out.println("add triangle");
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
