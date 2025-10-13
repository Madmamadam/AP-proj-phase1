package controller;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import model.*;

public class Wiring {
    public MainGame_Logics mainModel;
    Wire decoy_wire;
    boolean isStartedInGate ;
    boolean isEndedInGate = false;



    public Wiring(MainGame_Logics MainModel) {
        this.mainModel = MainModel;
    }


    public void run_listeners(){
        //running till signal_play button pressed
        //System.out.println("in wiring");

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



        mainModel.view.just_game_pane.setOnMouseClicked(event ->{
            if(mainModel.staticDataModel.stop_wiring) return;

            //wire removing
            if(event.getButton() == MouseButton.SECONDARY) {
                //first check curve handler
                CurveHandler curveHandler = checkReleaseWasOnACurveHandler(event);
                if(curveHandler != null) {
                    System.out.println("curveHandler is founded");

                }

                Wire wire = checkReleaseWasOnAWire(event);
                if (wire != null) {
                    System.out.println("wire is founded");
                    mainModel.time_to_remove_wire(wire);
                }
            }
            //wire adding a curve
            if(event.getButton() == MouseButton.PRIMARY) {
                Wire wire = checkReleaseWasOnAWire(event);
                if (wire != null) {
                    mainModel.controller.request_to_add_curveHandler(wire, event);
                }
            }

        });
    }


    private Wire checkReleaseWasOnAWire(MouseEvent event) {
        Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

        for(Wire wire: mainModel.staticDataModel.wires){
            for (Node curveNode : wire.getAllOfCurve_Group().getChildren()) {
//                System.out.println("right before if");
                if (nodeUnderMouse == curveNode || curveNode.equals(nodeUnderMouse) || curveNode.isHover()) {
//                    System.out.println("right after if");
                    return wire;
                }
            }
        }
        System.out.println("return is null");
        return null;
    }

    private CurveHandler checkReleaseWasOnACurveHandler(MouseEvent event) {
        Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

        for(Wire wire: mainModel.staticDataModel.wires){
            for (CurveHandler curveHandler : wire.getCurveHandlers()) {
//                System.out.println("right before if");
                Node curvehandlerNode = curveHandler.getViewCircle();
                if (nodeUnderMouse == curvehandlerNode || curvehandlerNode.equals(nodeUnderMouse) || curvehandlerNode.isHover()) {
//                    System.out.println("right after if");
                    return curveHandler;
                }
            }
        }
        System.out.println("return is null");
        return null;
    }

    private void SecondClickedOnA_OuterGate(Gate gate) {
        decoy_wire.setSecondgate(gate);
        isEndedInGate=true;
        mainModel.wire_check_to_add(decoy_wire);
    }

    private void SecondClickedOnA_InnerGate(Gate gate) {
        decoy_wire.setSecondgate(gate);
        isEndedInGate=true;
        mainModel.wire_check_to_add(decoy_wire);
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
