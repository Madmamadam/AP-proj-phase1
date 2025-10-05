package controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;
import mains.Configg;
import mains.MainGame_ViewAndModelAndController;
import mains.Start_menu;
import model.*;
import org.locationtech.jts.geom.Coordinate;
import view.Paintt;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static mains.Filee.level_gamemodel_start;
import static mains.MainGame_ViewAndModelAndController.*;
import static mains.Start_menu.static_market_pane;
import static view.Paintt.gameTimer;

public class Controller {
    public MainGame_ViewAndModelAndController mainGameViewAndModel;
    public Paintt view;
    Methods methods =new Methods(mainGameViewAndModel);


    private static void signal_one_step_on_wire(Signal signal) {
        Configg cons = Configg.getInstance();
        if (signal.getTypee().getName() == "rectangle"){
            //first gate and end gate have same type
            if (signal.getLinked_wire().getFirstgate().getTypee().getName() == "rectangle") {
                signal.setLength_on_wire(signal.getLength_on_wire() + cons.getDefault_delta_wire_length());
            }
            if (signal.getLinked_wire().getFirstgate().getTypee().getName() == "triangle") {
                signal.setLength_on_wire(signal.getLength_on_wire() + cons.getDefault_delta_wire_length() / 2);
            }
            else {
                System.out.println("+++++type not found error");
            }
        }
        if(signal.getTypee().getName()=="triangle"){
            if(signal.getLinked_wire().getFirstgate().getTypee().getName()=="rectangle") {
                double ratio = signal.getLength_on_wire()/signal.getLinked_wire().getLength();
                signal.setLength_on_wire(signal.getLength_on_wire() + (1+2*ratio)*cons.getDefault_delta_wire_length());
            }
            if(signal.getLinked_wire().getFirstgate().getTypee().getName()=="triangle"){
                signal.setLength_on_wire(signal.getLength_on_wire() + cons.getDefault_delta_wire_length());
            }
            else {
                System.out.println("+++++type not found error");
            }
        }

    }

    private void signal_add_to_start(Signal signal) {
        System.out.println("********* signal_add_to_start  frame counter ="+mainGameViewAndModel.signal_run_frame_counter);
        mainGameViewAndModel.staticDataModel.signals.add(signal);
        mainGameViewAndModel.staticDataModel.sysboxes.getFirst().signal_bank.add(signal);
    }

    private void check_signal_wire_distance(Signal signal) {
        Configg cons = Configg.getInstance();
        double x=signal.getX_ekhtelaf();
        double y=signal.getY_ekhtelaf();
        double ekhtelaf_r =Math.sqrt(x*x+y*y);
//        در داک شرط خاصی برای حذف کردن نیامده
        if(signal.getTypee().getId()==1){
            if(2*ekhtelaf_r>Math.min(cons.getGate_rectangle_height(),cons.getGate_rectangle_width())){
                go_to_dead(signal);
            }
        }
        if(signal.getTypee().getId()==2){
            if(2*ekhtelaf_r>cons.getSignal_triangle_radius()){
                go_to_dead(signal);
            }
        }

    }

    private void signal_go_to_bank(Signal signal) {
        Configg cons = Configg.getInstance();

        Sysbox sysbox =signal.getLinked_wire().getSecondgate().getSysbox();

        if(sysbox.signal_bank.size()>5 && !sysbox.isStarter()){
            //lost
            signal.setState("lost");
            return;
        }

        sysbox.signal_bank.add(signal);
        if(signal.getTypee().getId()==1) {
            mainGameViewAndModel.staticDataModel.setSekke(mainGameViewAndModel.staticDataModel.getSekke() + cons.getRectangle_signal_sekke_added());
        }
        if(signal.getTypee().getId()==2) {
            mainGameViewAndModel.staticDataModel.setSekke(mainGameViewAndModel.staticDataModel.getSekke() + cons.getTraiangle_signal_sekke_added());
        }
        view.just_game_pane.getChildren().remove(signal.poly);
        signal.getLinked_wire().getFirstgate().setIn_use(false);

        if(sysbox.isStarter()){
            signal.setState("ended");
        }
        else {
//           did not use method because it's easy
            signal.setState("on_sysbox");

        }
    }

    private void signal_go_to_wire(Signal signal, Gate recom_gate) {
        signal.setLinked_wire(recom_gate.getWire());
        signal.setLength_on_wire(0.0);
        System.out.println("recom_gate.getSysbox().signal_bank.size() "+recom_gate.getSysbox().signal_bank.size());
        recom_gate.getSysbox().signal_bank.remove(signal);
        signal.setState("on_wire");
//        System.out.println("go to on wire ");
        recom_gate.setIn_use(true);
        view.just_game_pane.getChildren().add(signal.poly);
    }


    public void wiring() {
        //running till signal_play butten pressed
//        System.out.println("in wiring");
        AtomicReference<Wire> decoy_wire = new AtomicReference<>();
        AtomicBoolean isStartedinGate = new AtomicBoolean();
        //-----------------------------first selection
        for (Sysbox sysbox : mainGameViewAndModel.staticDataModel.sysboxes) {
            for(Gate gate:sysbox.inner_gates){
                gate.poly.setOnMousePressed(e -> {
                    if(mainGameViewAndModel.staticDataModel.stop_wiring) return;
                    if (e.getButton() != MouseButton.PRIMARY) {return;}
                    Wire candidate_wire = new Wire();
                    candidate_wire.setFirstgate(gate);
                    decoy_wire.set(candidate_wire);
                    isStartedinGate.set(true);

                });
            }
            for(Gate gate:sysbox.outer_gates){
                gate.poly.setOnMousePressed(e -> {
                    if(mainGameViewAndModel.staticDataModel.stop_wiring) return;
                    if (e.getButton() != MouseButton.PRIMARY) {return;}
                    Wire candidate_wire = new Wire();
                    candidate_wire.setFirstgate(gate);
                    decoy_wire.set(candidate_wire);
                    isStartedinGate.set(true);

                });
            }
        }


        //----------------------------second selection
        boolean ended_correctly = false;
        view.just_game_pane.setOnMouseReleased(event -> { if(mainGameViewAndModel.staticDataModel.stop_wiring) return;
            if(isStartedinGate.get()) {
                isStartedinGate.set(false);
                Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

                AtomicBoolean isEndedinGate = new AtomicBoolean(false);

                for (Sysbox sysbox : mainGameViewAndModel.staticDataModel.sysboxes) {
                    for (Gate gate : sysbox.inner_gates) {
                        Polygon poly = gate.poly;
                        if (nodeUnderMouse == poly || poly.equals(nodeUnderMouse) || poly.isHover()) {
                            Wire candidate_wire = decoy_wire.get();
                            candidate_wire.setSecondgate(gate);
                            decoy_wire.set(candidate_wire);
                            isEndedinGate.set(true);
                            wire_check_to_add(candidate_wire.cloneWire());
                        }
                    }
                    for (Gate gate : sysbox.outer_gates) {
                        Polygon poly = gate.poly;
                        if (nodeUnderMouse == poly || poly.equals(nodeUnderMouse) || poly.isHover()) {
                            Wire candidate_wire = decoy_wire.get();
                            candidate_wire.setSecondgate(gate);
                            decoy_wire.set(candidate_wire);
                            isEndedinGate.set(true);
                            wire_check_to_add(candidate_wire.cloneWire());
                        }
                    }
                }

            }
        });


        //wire removing

        view.just_game_pane.setOnMouseClicked(event ->{
            if(mainGameViewAndModel.staticDataModel.stop_wiring) return;
            if(event.getButton() != MouseButton.SECONDARY) return;

            Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

            for(Wire wire: mainGameViewAndModel.staticDataModel.wires){
                Line poly =wire.getLine();
//                System.out.println("right before if");
                if(nodeUnderMouse == poly || poly.equals(nodeUnderMouse) || poly.isHover()){
//                    System.out.println("right after if");
                    time_to_remove_wire(wire);
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

    private void time_to_remove_wire(Wire wire) {
        view.just_game_pane.getChildren().remove(wire.getLine());
        wire.getFirstgate().setWire(null);
        wire.getSecondgate().setWire(null);
        mainGameViewAndModel.staticDataModel.wires.remove(wire);
        mainGameViewAndModel.staticDataModel.setLevel_wires_length(mainGameViewAndModel.staticDataModel.getLevel_wires_length() - wire.getLength());

    }

    private void wire_check_to_add(Wire wire) {
        wire.setLength(methods.calculate_wire_length(wire));
        if(   wire.getFirstgate().getWire()!=null
            ||wire.getSecondgate().getWire()!=null){
        }
        else if(        !Objects.equals(wire.getFirstgate().getTypee().getName(),wire.getSecondgate().getTypee().getName())
                || Objects.equals(wire.getFirstgate().getSysbox(),wire.getSecondgate().getSysbox())
                || !wire.getFirstgate().isIs_outer()
                || wire.getSecondgate().isIs_outer()
                || mainGameViewAndModel.staticDataModel.getLevel_wires_length() + wire.getLength() > mainGameViewAndModel.staticDataModel.constraintss.getMaximum_length())
            {
            add_wrong_wire(wire);
        }
        else {
            add_corrected_wire(wire);
        }
    }

    private void add_corrected_wire(Wire wire) {
        corrected_wire_add_to_model(wire);
        corrected_wire_add_to_view(wire);
    }

    private void corrected_wire_add_to_view(Wire wire) {
        //paint it forever
        view.just_game_pane.getChildren().add(wire.getLine());
    }

    private void corrected_wire_add_to_model(Wire wire) {
        wire.getFirstgate().setWire(wire);
        wire.getSecondgate().setWire(wire);
        mainGameViewAndModel.staticDataModel.wires.add(wire);
//        System.out.println("number of wires:"+level_gamemodel.wires.size());
        mainGameViewAndModel.staticDataModel.setLevel_wires_length(mainGameViewAndModel.staticDataModel.getLevel_wires_length() + wire.getLength());
    }

    public void add_wrong_wire(Wire wire) {
        Configg cons = Configg.getInstance();


        wire.getLine().setStroke(cons.getWrong_line_color());

        view.just_game_pane.getChildren().add(wire.getLine());
        PauseTransition pause = new PauseTransition(Duration.seconds(cons.getSeeing_wrong_line_duration()));
        pause.setOnFinished(event -> {
            view.just_game_pane.getChildren().remove(wire.getLine());
            wire.getFirstgate().setWire(null);
            wire.getSecondgate().setWire(null);
        });
        pause.play();
    }

    public static void exit() {
        System.exit(1);
    }

    public void run_stop_button_pressed(Stage primaryStage) throws Exception {
        if(mainGameViewAndModel.staticDataModel.stop_wiring){
            time_to_restart(primaryStage);
        }
        else {
            boolean accesss =true;
            for(Sysbox sysbox: mainGameViewAndModel.staticDataModel.sysboxes) {
                if(!sysbox.isIndicator_on_state()){
                    accesss =false;
                    break;
                }
                for(Gate gate: sysbox.inner_gates) {
                    if (gate.getWire() == null) {
                        accesss = false;
                        break;
                    }
                }
                for(Gate gate: sysbox.outer_gates) {
                    if (gate.getWire() == null) {
                        accesss = false;
                        break;
                    }
                }
            }
            if(accesss){
                time_to_stop_wiring();
            }
            else {
                false_try_for_stop_wiring();
            }

        }

    }

    private void time_to_restart(Stage primaryStage) throws Exception {
        mainGameViewAndModel.staticDataModel.stop_wiring=false;
        primaryStage.hide();
        this.mainGameViewAndModel.start(primaryStage,level);

    }

    private void false_try_for_stop_wiring() {
    }

    private void time_to_stop_wiring() {
        mainGameViewAndModel.staticDataModel.stop_wiring=true;
    }


    public void indicator_update() {
        for(Sysbox sysbox : mainGameViewAndModel.staticDataModel.sysboxes) {
            if(sysbox.isStarter()){
                sysbox.setIndicator_on_state(true);
            }
            else {
                boolean found = false;
    //            System.out.println("sysbox.isStarter()"+sysbox.isStarter());
                for (Gate gate : sysbox.inner_gates) {
                    if(gate.getWire()!=null){
                        if(gate.getWire().getFirstgate().getSysbox().isIndicator_on_state()){
                            sysbox.setIndicator_on_state(true);
                            found = true;
                            break;
                        }
                    }
                }
                if(!found){
                    sysbox.setIndicator_on_state(false);
                }
            }
        }
    }


    private void check_noise(Signal signal) {
        Configg cons = Configg.getInstance();
        if(signal.getNoise()> mainGameViewAndModel.staticDataModel.constraintss.getMaximum_noise()){
            go_to_dead(signal);
        }
    }

    private void go_to_dead(Signal signal) {
        signal.setIs_updated(true);

        if(signal.getState()=="on_wire")
        {
        //خالی کردن اون خط
            signal.getLinked_wire().getFirstgate().setIn_use(false);
            signal.getLinked_wire().getSecondgate().setIn_use(false);
        }
        signal.setState("lost");
        view.just_game_pane.getChildren().remove(signal.poly);
    }

    private void just_collapse_noise(Signal signal1, Signal signal2) {
        Configg cons = Configg.getInstance();
        signal1.setNoise(signal1.getNoise()+cons.getNoise_add_every_hit());
        signal2.setNoise(signal2.getNoise()+cons.getNoise_add_every_hit());
    }

    private void colapsedpairs_update() {
        Configg cons= Configg.getInstance();
        long long_current_time = System.currentTimeMillis();
        double current_time = long_current_time/1000000000.0;
        for(Pairs pair : mainGameViewAndModel.staticDataModel.collapsedPairs){
            if(current_time-pair.adding_time > cons.getImpulse_resttime()){
                mainGameViewAndModel.staticDataModel.collapsedPairs.remove(pair);
            }
        }
    }


    private double runratio() {
        Configg cons = Configg.getInstance();
        if(mainGameViewAndModel.virtual_run){
            return (1/(cons.getVirtual_frequency()/60));
        }
        else
            return 1;
    }


    public void virtual_time_clicked(double virtual_ratio) {
        double max_t= mainGameViewAndModel.staticDataModel.constraintss.getMaximum_time_sec();
        double go_to_time_sec = virtual_ratio*max_t;
        half_restart(go_to_time_sec);

    }

    private void half_restart(double goToTime_sec) {
        gameTimer.setTime_sec(goToTime_sec);
        Configg cons = Configg.getInstance();
        double cyclecount=goToTime_sec*60;
        restart_level_signals();
        mainGameViewAndModel.virtual_run=true;

        if(cyclecount<3){cyclecount=3;}
        mainGameViewAndModel.signals_virtual_run.setCycleCount((int) (cyclecount));
        mainGameViewAndModel.signals_virtual_run.play();
        mainGameViewAndModel.signals_virtual_run.setOnFinished(event2 -> {
            mainGameViewAndModel.virtual_run=false;
        });
    }

    private void restart_level_signals() {
        for (Signal signal : mainGameViewAndModel.staticDataModel.signals) {
            view.just_game_pane.getChildren().remove(signal.poly);
        }
//        for (Circle circle: level_gamemodel.impulse_circles){
//            just_game_pane.getChildren().remove(circle);
//        }

        mainGameViewAndModel.staticDataModel = level_gamemodel_start.getClone();

        System.out.println("signals size now: " + mainGameViewAndModel.staticDataModel.signals.size());

        for (Signal signal : mainGameViewAndModel.staticDataModel.signals) {
            System.out.println("signal_state: "+signal.getState());
        }

        System.out.println("level_gamemodel.sysboxes.getFirst().signal_bank.size() "+ mainGameViewAndModel.staticDataModel.sysboxes.getFirst().signal_bank.size());

//        update_gate_from_wires();
    }

    private void update_gate_from_wires() {
        for (Wire wire: mainGameViewAndModel.staticDataModel.wires) {
            wire.getFirstgate().setWire(wire);
            wire.getSecondgate().setWire(wire);
        }
    }


    private void level_ended() {
        System.out.println("******************** LEVEL ENDED *******************");


        //Check
        System.out.println("level_gamemodel.signals.size() "+ mainGameViewAndModel.staticDataModel.signals.size());
        for (Signal signal : mainGameViewAndModel.staticDataModel.signals) {
            System.out.println("signal_state: "+signal.getState());
        }

        //end of Check


        mainGameViewAndModel.staticDataModel.stop_wiring=false;
        mainGameViewAndModel.show_ending_stage();

    }


    public void menuBtn_clicked() {
        Start_menu.show_menu();
    }

    public void restartBtn_clicked() throws Exception {
        time_to_restart(primaryStage_static);
    }

    public void nextLevelBtn_clicked() {
        try {
            this.mainGameViewAndModel.start(primaryStage_static,2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void signal_log_enable(Scene scene) {
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().toString().equals("U")) {
                print_signal_log();
            }
        });


    }

    private void print_signal_log() {
        System.out.println("------------singnal and sysbox log-------------");
        System.out.println("level_gamemodel.signals.size() "+ mainGameViewAndModel.staticDataModel.signals.size());
        for (Signal signal : mainGameViewAndModel.staticDataModel.signals) {
            System.out.println("signal.getState() "+signal.getState());
            System.out.println("signal.getLinked_wire()" +signal.getLinked_wire());
            if(signal.getLinked_wire()!=null) {
                System.out.println("level_gamemodel.sysboxes.indexOf(signal.getLinked_wire().getFirstgate().getSysbox()) " + mainGameViewAndModel.staticDataModel.sysboxes.indexOf(signal.getLinked_wire().getFirstgate().getSysbox()));
            }
        }

        for (int i = 0; i < mainGameViewAndModel.staticDataModel.sysboxes.size(); i++) {
            Sysbox sysbox = mainGameViewAndModel.staticDataModel.sysboxes.get(i);
            System.out.println("sysbox number "+i +"    sysbox.signal_bank.size() "+sysbox.signal_bank.size() );
        }
        System.out.println("-----------------------------------------------");
    }

    public void marketButtonClicked() {
        if(mainGameViewAndModel.staticDataModel.stop_wiring){
            static_market_pane.setVisible(true);
        }
    }



    //if ran virtual_run so magic going reset
    public void OAtar_clicked() {
        if(mainGameViewAndModel.staticDataModel.getSekke() > 3) {
            mainGameViewAndModel.staticDataModel.setSekke(mainGameViewAndModel.staticDataModel.getSekke() - 3);
            mainGameViewAndModel.staticDataModel.Oatar = true;

            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(e -> mainGameViewAndModel.staticDataModel.Oatar = false);
            pause.play();
        }
    }

    public void OAiryman_clicked() {
        if (mainGameViewAndModel.staticDataModel.getSekke() > 4) {
            mainGameViewAndModel.staticDataModel.setSekke(mainGameViewAndModel.staticDataModel.getSekke() - 4);
            mainGameViewAndModel.staticDataModel.Oairyaman = true;

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> mainGameViewAndModel.staticDataModel.Oairyaman = false);
            pause.play();
        }
    }

    public void OAnahita_clicked() {
        if(mainGameViewAndModel.staticDataModel.getSekke() > 5){
            mainGameViewAndModel.staticDataModel.setSekke(mainGameViewAndModel.staticDataModel.getSekke()-5);

            reset_all_noise();


        }
    }

    private void reset_all_noise() {
        for (Signal signal : mainGameViewAndModel.staticDataModel.signals) {
            signal.setNoise(0);
        }
    }

    public void edit_wires() {

    }
}
