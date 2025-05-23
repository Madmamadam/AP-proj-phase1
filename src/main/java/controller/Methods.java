package controller;

import javafx.scene.shape.Polygon;
import mains.Configg;
import model.Gate;
import model.Signal;
import model.Sysbox;
import model.Wire;

import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Methods {
    //simple just find first better
    public Object recommended_gate(Sysbox sysbox, Signal signal) {
        boolean second_found = false;
        Gate secound_gate = null;
        for(Gate gate:sysbox.outer_gates){
            if (Objects.equals(gate.getTypee().getName(),signal.getTypee().getName()) && !gate.isIn_use()){
                return gate;
            }
            if(!gate.isIn_use() && !second_found){
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
        if(Objects.equals(signal.getTypee().getName(),"rectangle")){
            poly.getPoints().addAll(signal.getX()-cons.getSignal_rectangle_width()/2,signal.getY()-cons.getSignal_rectangle_height()/2);
            poly.getPoints().addAll(signal.getX()-cons.getSignal_rectangle_width()/2,signal.getY()+cons.getSignal_rectangle_height()/2);
            poly.getPoints().addAll(signal.getX()+cons.getSignal_rectangle_width()/2,signal.getY()+cons.getSignal_rectangle_height()/2);
            poly.getPoints().addAll(signal.getX()+cons.getSignal_rectangle_width()/2,signal.getY()-cons.getSignal_rectangle_height()/2);
            poly.setFill(cons.getSignal_rectangle_color());

        }
        if(Objects.equals(signal.getTypee().getName(),"triangle")){
            for (int i=0 ; i<3;i++) {
                poly.getPoints().addAll(signal.getX()-cons.getSignal_triangle_radius()*sin(i*2*pi/3), signal.getY() - cons.getSignal_triangle_radius()*cos(i*2*pi/3));
            }
            poly.setFill(cons.getSignal_triangle_color());
        }
    }


    public double calculate_wire_length(Wire wire) {
        double dx =  wire.getFirstgate().getX() - wire.getSecondgate().getX();
        double dy =  wire.getFirstgate().getY() - wire.getSecondgate().getY();
        return Math.sqrt(dx*dx+dy*dy);
    }
}
