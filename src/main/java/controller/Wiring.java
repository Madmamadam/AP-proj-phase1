package controller;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import mains.MainGame_ViewAndModelAndController;
import model.Gate;
import model.Sysbox;
import model.Wire;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Wiring {
    public MainGame_ViewAndModelAndController mainModel;
    AtomicReference<Wire> decoy_wire = new AtomicReference<>();
    AtomicBoolean isStartedInGate = new AtomicBoolean();
    AtomicBoolean isEndedInGate = new AtomicBoolean(false);



    public Wiring(MainGame_ViewAndModelAndController MainModel) {
        this.mainModel = MainModel;
    }


    public void run_listeners(){
        //running till signal_play butten pressed
//        System.out.println("in wiring");

        //-----------------------------first selection
        for (Sysbox sysbox : mainModel.staticDataModel.sysboxes) {
            for(Gate gate:sysbox.inner_gates){
                gate.poly.setOnMousePressed(e -> {
                    FirstClickedOnA_InnerGate(gate , e);
                });
            }
            for(Gate gate:sysbox.outer_gates){
                gate.poly.setOnMousePressed(e -> {
                    FirstClickedOnA_OuterGate(gate,e);
                });
            }
        }


        //----------------------------second selection
//        boolean ended_correctly = false;
        mainModel.view.just_game_pane.setOnMouseReleased(event -> { if(mainModel.staticDataModel.stop_wiring) return;
            if(isStartedInGate.get()) {
                isStartedInGate.set(false);
                Node nodeUnderMouse = event.getPickResult().getIntersectedNode();


                for (Sysbox sysbox : mainModel.staticDataModel.sysboxes) {
                    for (Gate gate : sysbox.inner_gates) {
                        Polygon poly = gate.poly;
                        if (nodeUnderMouse == poly || poly.equals(nodeUnderMouse) || poly.isHover()) {
                            SecondClickedOnA_InnerGate(gate);
                        }
                    }
                    for (Gate gate : sysbox.outer_gates) {
                        Polygon poly = gate.poly;
                        if (nodeUnderMouse == poly || poly.equals(nodeUnderMouse) || poly.isHover()) {
                            SecondClickedOnA_OuterGate(gate);
                        }
                    }
                }

            }
        });


        //wire removing

        mainModel.view.just_game_pane.setOnMouseClicked(event ->{
            if(mainModel.staticDataModel.stop_wiring) return;
            if(event.getButton() != MouseButton.SECONDARY) return;

            Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

            for(Wire wire: mainModel.staticDataModel.wires){
                Line poly =wire.getLine();
//                System.out.println("right before if");
                if(nodeUnderMouse == poly || poly.equals(nodeUnderMouse) || poly.isHover()){
//                    System.out.println("right after if");
                    mainModel.time_to_remove_wire(wire);
                    return;
                }
            }
        });
//        System.out.println("number of wire right before for:"+level_gamemodel.wires.size());
//        for(Wire wire: level_gamemodel.wires){
//            System.out.println("wire");
//            wire.getLine().setOnMouseClicked(e2 -> {
//                if(stop_wiring) return;
//                System.out.println("time to check mouse input");
//
//                if(e2.getButton() != MouseButton.SECONDARY) return;
//                System.out.println("time ho remove wire");
//                time_to_remove_wire(wire);
//            });
//        }


    }

    private void SecondClickedOnA_OuterGate(Gate gate) {
        Wire candidate_wire = decoy_wire.get();
        candidate_wire.setSecondgate(gate);
        decoy_wire.set(candidate_wire);
        isEndedInGate.set(true);
        mainModel.wire_check_to_add(candidate_wire.cloneWire());
    }

    private void SecondClickedOnA_InnerGate(Gate gate) {
        Wire candidate_wire = decoy_wire.get();
        candidate_wire.setSecondgate(gate);
        decoy_wire.set(candidate_wire);
        isEndedInGate.set(true);
        mainModel.wire_check_to_add(candidate_wire.cloneWire());
    }

    private void FirstClickedOnA_OuterGate(Gate gate, MouseEvent e) {
        if(mainModel.staticDataModel.stop_wiring) return;
        if (e.getButton() != MouseButton.PRIMARY) {return;}
        Wire candidate_wire = new Wire();
        candidate_wire.setFirstgate(gate);
        decoy_wire.set(candidate_wire);
        isStartedInGate.set(true);
    }

    private void FirstClickedOnA_InnerGate(Gate gate, MouseEvent e) {
        if(mainModel.staticDataModel.stop_wiring) return;
        if (e.getButton() != MouseButton.PRIMARY) {return;}
        Wire candidate_wire = new Wire();
        candidate_wire.setFirstgate(gate);
        decoy_wire.set(candidate_wire);
        isStartedInGate.set(true);
    }

}
