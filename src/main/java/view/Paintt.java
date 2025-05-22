package view;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import mains.Configg;
import model.Gate;
import model.Signal;
import model.Sysbox;
import model.Wire;

import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static mains.Filee.level_stack;
import static mains.Main.just_game_pane;


public class Paintt {
    public void addtopane_signals() {
        for (Signal signal : level_stack.signals) {
            just_game_pane.getChildren().add(signal.poly);
        }
    }
    public void addtopane_sysboxsandindicators(){
        for (Sysbox sysbox : level_stack.sysboxes) {
            just_game_pane.getChildren().add(sysbox.getRectangle());
            just_game_pane.getChildren().add(sysbox.getIndicator_rectangle());
        }
    }
    public void addtopane_gates(){
        for (Sysbox sysbox1 : level_stack.sysboxes) {
            for (Gate gate :sysbox1.inner_gates){
                one_gate_update_polygan(gate);
                just_game_pane.getChildren().add(gate.poly);
            }

            for (Gate gate2 :sysbox1.outer_gates){
                one_gate_update_polygan(gate2);
                just_game_pane.getChildren().add(gate2.poly);
            }
        }
    }

    private void one_gate_update_polygan(Gate gate) {
        Configg cons = Configg.getInstance();
        double pi=3.1415;
        if(Objects.equals(gate.getTypee().getName(),"rectangle")){
            gate.poly.getPoints().clear();
            gate.poly.getPoints().addAll(gate.getX()-cons.getGate_rectangle_width()/2, gate.getY()-cons.getGate_rectangle_height()/2);
            gate.poly.getPoints().addAll(gate.getX()-cons.getGate_rectangle_width()/2, gate.getY()+cons.getGate_rectangle_height()/2);
            gate.poly.getPoints().addAll(gate.getX()+cons.getGate_rectangle_width()/2, gate.getY()+cons.getGate_rectangle_height()/2);
            gate.poly.getPoints().addAll(gate.getX()+cons.getGate_rectangle_width()/2, gate.getY()-cons.getGate_rectangle_height()/2);
        }
        if(Objects.equals(gate.getTypee().getName(),"triangle")){
            gate.poly.getPoints().clear();
            for (int i=0 ; i<3;i++) {
                gate.poly.getPoints().addAll(gate.getX()-cons.getGate_triangle_radius()*sin(i*2*pi/3), gate.getY() - cons.getGate_triangle_radius()*cos(i*2*pi/3));
            }
        }
    }



}
