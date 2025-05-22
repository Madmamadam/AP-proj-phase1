package controller;

import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import mains.Configg;
import model.Gate;
import model.Signal;
import model.Sysbox;
import model.Wire;
import view.Paintt;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static mains.Filee.level_stack;
import static mains.Main.just_game_pane;
import static mains.Main.stop_wiring;

public class Controller {

    public static void Signals_Update(){
        Methods methods = new Methods();
        Configg cons = Configg.getInstance();
        //it's after load signals stack

        //first: update signals that on sysbox  (Assign wire)
        for (Sysbox sysbox : level_stack.sysboxes) {

            for (int i=0;i<cons.getBank_capacity() &&sysbox.signal_bank.getFirst()!=null;i++){
                Signal signal=sysbox.signal_bank.getFirst();
                signal.setIs_updated(true);
                if(methods.recommended_gate(sysbox,signal) != null){
                    Gate recom_gate = (Gate) methods.recommended_gate(sysbox,signal);
                    signal.setLinked_wire(recom_gate.getWire());
                    signal.setLength_on_wire(0.0);

                    sysbox.signal_bank.removeFirst();
                    signal.setState("on_wire");
                    just_game_pane.getChildren().add(signal.poly);
                }
                //if every gate is used
                else {
                    break;
                }
            }
        }
        //second: update signals that on wire (move on wire or add them to a sysbox)
        for(Signal signal : level_stack.signals){
            if(!signal.isIs_updated() && Objects.equals(signal.getState(), "on_wire")){
                signal.setLength_on_wire(signal.getLength_on_wire()+cons.getDelta_wire_length());
                if(signal.getLength_on_wire()>signal.getLinked_wire().getLength()){
                    Sysbox sysbox =signal.getLinked_wire().getSecondgate().getSysbox();
                    sysbox.signal_bank.add(signal);
                    just_game_pane.getChildren().remove(signal.poly);
                    if(sysbox.is_startter){
                        signal.setState("ended");
                    }
                    else {
                        signal.setState("on_sysbox");
                    }
                }
                else {
                    methods.update_signal_onwire(signal);
                }
            }
            else{
                signal.setIs_updated(false);
            }

        }

    }


    public static void wiring() {
        //running till signal_play butten pressed
//        System.out.println("in wiring");
        AtomicReference<Wire> decoy_wire = new AtomicReference<>();
        AtomicBoolean isStartedinGate = new AtomicBoolean();

        //-----------------------------first selection
        for (Sysbox sysbox : level_stack.sysboxes) {
            for(Gate gate:sysbox.inner_gates){
                gate.poly.setOnMousePressed(e -> {if(stop_wiring) return;
                    System.out.println("caught in inner gates");
                    Wire candidate_wire = new Wire();
                    candidate_wire.setFirstgate(gate);
                    decoy_wire.set(candidate_wire);
                    isStartedinGate.set(true);
                });
            }
            for(Gate gate:sysbox.outer_gates){
                gate.poly.setOnMousePressed(e -> {if(stop_wiring) return;
                    System.out.println("caught in outer gates");
                    Wire candidate_wire = new Wire();
                    candidate_wire.setFirstgate(gate);
                    decoy_wire.set(candidate_wire);
                    isStartedinGate.set(true);

                });
            }
        }


        //----------------------------second selection
        boolean ended_correctly = false;
        just_game_pane.setOnMouseReleased(event -> { if(stop_wiring) return;
            System.out.println("just released (isstartedinGate="+isStartedinGate.get()+")");
            if(isStartedinGate.get()) {
                isStartedinGate.set(false);
                System.out.println("started from a gate");
                Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

                AtomicBoolean isEndedinGate = new AtomicBoolean(false);

                for (Sysbox sysbox : level_stack.sysboxes) {
                    for (Gate gate : sysbox.inner_gates) {
                        Polygon poly = gate.poly;
                        if (nodeUnderMouse == poly || poly.equals(nodeUnderMouse) || poly.isHover()) {
                            Wire candidate_wire = decoy_wire.get();
                            candidate_wire.setSecondgate(gate);
                            decoy_wire.set(candidate_wire);
                            isEndedinGate.set(true);
                            Controller.wire_check_to_add(candidate_wire.cloneWire());
                        }
                    }
                    for (Gate gate : sysbox.outer_gates) {
                        Polygon poly = gate.poly;
                        if (nodeUnderMouse == poly || poly.equals(nodeUnderMouse) || poly.isHover()) {
                            Wire candidate_wire = decoy_wire.get();
                            candidate_wire.setSecondgate(gate);
                            decoy_wire.set(candidate_wire);
                            isEndedinGate.set(true);
                            Controller.wire_check_to_add(candidate_wire.cloneWire());
                        }
                    }
                }

            }
        });

    }

    private static void wire_check_to_add(Wire wire) {
        Paintt paintt = new Paintt();
        Methods methods = new Methods();
        wire.setLength(methods.calculate_wire_length(wire));

        if(   wire.getFirstgate().getWire()!=null
            ||wire.getSecondgate().getWire()!=null){
        }
        else if(        !Objects.equals(wire.getFirstgate().getTypee().getName(),wire.getSecondgate().getTypee().getName())
                || Objects.equals(wire.getFirstgate().getSysbox(),wire.getSecondgate().getSysbox())
                || !wire.getFirstgate().isIs_outer()
                || wire.getSecondgate().isIs_outer()
                || level_stack.getLevel_wires_length() + wire.getLength() > level_stack.constraintss.getMaximum_length())
            {
            Controller.add_wrong_wire(wire);
        }
        else {
            Controller.add_corrected_wire(wire);
        }
    }

    private static void add_corrected_wire(Wire wire) {
        Configg configg = Configg.getInstance();

        wire.getFirstgate().setWire(wire);
        wire.getSecondgate().setWire(wire);
        level_stack.wires.add(wire);
        level_stack.setLevel_wires_length(level_stack.getLevel_wires_length() + wire.getLength());
        //paint it forever
        just_game_pane.getChildren().add(wire.getLine());
    }
    public static void add_wrong_wire(Wire wire) {
        Configg cons = Configg.getInstance();


        wire.getLine().setStroke(cons.getWrong_line_color());

        just_game_pane.getChildren().add(wire.getLine());
        PauseTransition pause = new PauseTransition(Duration.seconds(cons.getSeeing_wrong_line_duration()));
        pause.setOnFinished(event -> {
            just_game_pane.getChildren().remove(wire.getLine());
            wire.getFirstgate().setWire(null);
            wire.getSecondgate().setWire(null);
        });
        pause.play();
    }

}
