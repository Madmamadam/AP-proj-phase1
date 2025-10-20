package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import model.*;

import java.util.ArrayList;

public class Wiring {
    public MainGame_Logics mainModel;
    Wire decoy_wire;
    boolean isStartedInGate ;
    boolean isEndedInGate = false;
    boolean isMovingACurveHandler=false;
    ArrayList<Timeline> movingTimelines= new ArrayList<>();
    double mouseLocationX;
    double mouseLocationY;



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



        mainModel.view.just_game_pane.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            if(mainModel.staticDataModel.stop_wiring) return;


            if(event.getButton() == MouseButton.SECONDARY) {
                //wire removing
                Wire wire = checkReleaseWasOnAWire(event);
                if (wire != null) {
                    System.out.println("wire is founded");
                    mainModel.time_to_remove_wire(wire);
                }
            }


            if(event.getButton() == MouseButton.PRIMARY) {
                //check that was on moving a curve handler
                if(isMovingACurveHandler) {
                    curve_handler_end_of_moving();
                }

                else {
                    //second check curve handler
                    CurveHandler curveHandler = checkReleaseWasOnACurveHandler(event);
                    if (curveHandler != null) {
                        System.out.println("curveHandler is founded");
                        isMovingACurveHandler = true;
                        curveHandler_start_moving(curveHandler);
                    }

                    //Curve handler add to a wire
                    Wire wire = checkReleaseWasOnAWire(event);
                    if (wire != null) {
                        CubicCurve cubicCurve = curveThatClickedOn_find(wire, event);
                        mainModel.controller.request_to_add_curveHandler(wire, event, cubicCurve);


                    }
                }
            }

        });
        //capture mouse location
        mainModel.view.just_game_pane.addEventHandler(MouseEvent.MOUSE_MOVED,event -> {
            this.mouseLocationX=event.getX();
            this.mouseLocationY=event.getY();
        });
    }

    private void curve_handler_end_of_moving() {
        System.out.println("curve_handler_end_of_moving");
        isMovingACurveHandler=false;
        for (Timeline timeline : movingTimelines) {
            timeline.pause();
        }
    }


    private void curveHandler_start_moving(CurveHandler curveHandler) {
        Timeline movingTimeline = new Timeline(new KeyFrame(Duration.millis(0.17),event -> {
            curveHandler_settle_on_new_location(curveHandler,mouseLocationX,mouseLocationY);
        }));
        movingTimelines.add(movingTimeline);
        movingTimeline.setCycleCount(Timeline.INDEFINITE);
        movingTimeline.play();
    }

    private void curveHandler_settle_on_new_location(CurveHandler curveHandler, double x, double y) {
        curveHandler.setSafeXY(x,y);
        curveHandler.update_two_curves();
        mainModel.update_Level_wires_length();
    }


    private Wire checkReleaseWasOnAWire(MouseEvent event) {
        Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

        for(Wire wire: mainModel.staticDataModel.wires){
            for (int i=0;i<wire.getAllOfCurve_Group().getChildren().size();i++) {
                Node curveNode = wire.getAllOfCurve_Group().getChildren().get(i);
                if (nodeUnderMouse == curveNode || curveNode.equals(nodeUnderMouse) || curveNode.isHover()) {
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
                Node curveHandlerNode = curveHandler.getViewCircle();
                if (nodeUnderMouse == curveHandlerNode || curveHandlerNode.equals(nodeUnderMouse) || curveHandlerNode.isHover()) {
                    return curveHandler;
                }
            }
        }
        System.out.println("return is null");
        return null;
    }

    private CubicCurve curveThatClickedOn_find(Wire wire,MouseEvent event) {
        Node nodeUnderMouse = event.getPickResult().getIntersectedNode();
        for (Node curve : wire.getAllOfCurve_Group().getChildren()) {
            if (nodeUnderMouse == curve || curve.equals(nodeUnderMouse) || curve.isHover()) {
                return (CubicCurve) curve;
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
