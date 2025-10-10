package controller;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import model.MainGame_Logics;
import model.Gate;
import model.Sysbox;
import model.Wire;

public class Wiring {
    public MainGame_Logics mainModel;
    Wire decoy_wire;
    boolean isStartedInGate ;
    boolean isEndedInGate = false;



    public Wiring(MainGame_Logics MainModel) {
        this.mainModel = MainModel;
    }


    public void run_listeners(){
        //running till signal_play butten pressed
//        System.out.println("in wiring");

        //-----------------------------first selection
        for (Sysbox sysbox : mainModel.staticDataModel.sysboxes) {
            for(Gate gate:sysbox.inner_gates){
                gate.poly.setOnMousePressed(e -> {
                    if(mainModel.staticDataModel.stop_wiring) return;
                    if (e.getButton() != MouseButton.PRIMARY) {return;}
                    FirstClickedOnA_InnerGate(gate);
                });
            }
            for(Gate gate:sysbox.outer_gates){
                gate.poly.setOnMousePressed(e -> {
                    if(mainModel.staticDataModel.stop_wiring) return;
                    if (e.getButton() != MouseButton.PRIMARY) {return;}
                    FirstClickedOnA_OuterGate(gate);
                });
            }
        }


        //----------------------------second selection
        mainModel.view.just_game_pane.setOnMouseReleased(event -> { if(mainModel.staticDataModel.stop_wiring) return;
            if(isStartedInGate) {
                isStartedInGate=false;
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
            checkReleaseWasOnAWre(event);
        });
    }

    private void checkReleaseWasOnAWre(MouseEvent event) {
        Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

        for(Wire wire: mainModel.staticDataModel.wires){
            for (Node curveNode : wire.getAllOfCurve_Group().getChildren()) {
//                System.out.println("right before if");
                if (nodeUnderMouse == curveNode || curveNode.equals(nodeUnderMouse) || curveNode.isHover()) {
//                    System.out.println("right after if");
                    mainModel.time_to_remove_wire(wire);
                    return;
                }
            }
        }
    }

    private void SecondClickedOnA_OuterGate(Gate gate) {
        decoy_wire.setSecondgate(gate);
        isEndedInGate=true;
        mainModel.wire_check_to_add(decoy_wire.cloneWire());
    }

    private void SecondClickedOnA_InnerGate(Gate gate) {
        decoy_wire.setSecondgate(gate);
        isEndedInGate=true;
        mainModel.wire_check_to_add(decoy_wire.cloneWire());
    }

    private void FirstClickedOnA_OuterGate(Gate gate) {

        decoy_wire = new Wire();
        decoy_wire.setFirstgate(gate);
        isStartedInGate=true;
    }

    private void FirstClickedOnA_InnerGate(Gate gate) {
        decoy_wire= new Wire();
        decoy_wire.setFirstgate(gate);
        isStartedInGate=true;
    }

}
