package mains;

import controller.Add_level;
import controller.Controller;
import controller.Methods;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import org.locationtech.jts.geom.Coordinate;
import view.Paintt;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static mains.Start_menu.static_market_pane;


public class MainGame_ViewAndModelAndController {
    public LevelGame_StaticDataModel staticDataModel=new LevelGame_StaticDataModel();
    public Paintt view;
    public Controller controller;

    public LevelGame_StaticDataModel level_gamemodel_start ;
    private Methods methods = new Methods(this);

    public static int level;

    static Configg cons = Configg.getInstance();

    public boolean user_changing=true;
    public boolean virtual_run = false;
    public int signal_run_frame_counter = 0;
    public int dead_count = 0;
    private boolean first_time = true;
    public static Stage primaryStage_static;
    public Timeline signals_virtual_run = new Timeline(new KeyFrame(Duration.millis(1000/cons.getVirtual_frequency()), event -> {
        if (staticDataModel.stop_wiring) {
            System.out.println("//////////in virtual run");
            Signals_Update();
            check_and_do_collision();
            signal_run_frame_counter++;
        }
//            if(gameTimer.getTime_sec()>goToTime_sec){
//                signals_virtual_run.stop();
//            }
    }));

    public Timeline signals_run =new Timeline(new KeyFrame(Duration.millis(17), event -> {
        if (staticDataModel.stop_wiring && !virtual_run) {
            if(first_time){
                for (Sysbox sysbox: staticDataModel.sysboxes){
                    System.out.println("before clone sysbox.signal_bank.size() "+sysbox.signal_bank.size());
                }
                level_gamemodel_start = staticDataModel.getClone();

                for (Sysbox sysbox: staticDataModel.sysboxes){
                    System.out.println("before wiring sysbox.signal_bank.size() "+sysbox.signal_bank.size());
                }
                first_time=false;
            }
            System.out.println("////////////// in real run");


            Signals_Update();
            check_and_do_collision();
            ending_check();


            view.gameTimer.setStopping(false);
            view.marketPaneupdate();
            view.HUD_signal_run_update();
            signal_run_frame_counter++;
        }
        else {
            view.gameTimer.setStopping(true);
        }
    }));


//    public static void main(String[] args) {


//        -------------------------------------load initial value of level to model

//        -------------------------------------creative game instance
//        -------------------------------------
//    }

    public void start(Stage primaryStage, int l) throws Exception {

        level =l;
        primaryStage_static = primaryStage;
        Configg cons = Configg.getInstance();



        staticDataModel.stop_wiring = false;
        view.gameTimer.restart();
        signal_run_frame_counter = 0;
        view.HUD_signal_run_update();




        Add_level.start(level,staticDataModel);
        view.initial_UI(primaryStage);


//        paintt.addtopane_signals();


//       wiring mode (run some listener)
        wiring();
        controller.edit_wires();



        view.just_game_pane.getChildren().add(static_market_pane);

        Timeline timeline_wiring = new Timeline(new KeyFrame(Duration.millis(17*6), event -> {
            if (!staticDataModel.stop_wiring) {
                indicator_update();
            }
        }));

        timeline_wiring.setCycleCount(Timeline.INDEFINITE);
        timeline_wiring.play();

//      in signal move mode
        signals_run.setCycleCount(Timeline.INDEFINITE);
        signals_run.play();

//        Timeline signals_virtual_run = new Timeline(new KeyFrame(Duration.millis(cons.getVirtual_frequency()), event -> {
//
//        }));
    }
//this is MainGame_ViewAndModelAndController.java:144
//this is MainGame_ViewAndModelAndController.java:156


    public void Signals_Update(){
        Configg cons = Configg.getInstance();
        //it's after load signals stack
        System.out.println("///Signals_Update()");


        //zero: add signals to start Sysbox
        for(After_Frame_And_Signal_start d_signal : staticDataModel.After_signals){
            if(!d_signal.added && d_signal.adding_frame<signal_run_frame_counter){
                d_signal.added = true;
                signal_add_to_start(d_signal.signal);
            }
        }



        //first: update signals that on sysbox  (Assign wire)
        for (Sysbox sysbox : staticDataModel.sysboxes) {
            System.out.println("sysbox.signal_bank.size() "+sysbox.signal_bank.size());
            for (int i=0;i<sysbox.signal_bank.size() && !sysbox.signal_bank.isEmpty();){
                Signal signal=sysbox.signal_bank.get(i);
                signal.setIs_updated(true);
                System.out.println("nice1  sysbox_index: "+ staticDataModel.sysboxes.indexOf(sysbox));
                //just check outer_gates
                System.out.println("just check outer_gates");
                for (Gate gate:sysbox.outer_gates){
                    System.out.println("gate.isIn_use() "+gate.isIn_use());
                    if(gate.getWire()==null){
                        System.out.println("gate.getWire()==null");
                    }
                }
                System.out.println("end check");
                //end of checking

                if(methods.recommended_gate(sysbox,signal) != null && signal.getState()=="on_sysbox"){
                    Gate recom_gate = (Gate) methods.recommended_gate(sysbox,signal);
                    System.out.println("sysbox.signal_bank.size() "+sysbox.signal_bank.size());
                    System.out.println("nice2");
                    signal_go_to_wire(signal,recom_gate);
                    System.out.println("nice3");
                }
                else{
                    i++;
                }
            }
        }
        //second: update signals that on wire (move on wire or add them to a sysbox)
        for(Signal signal : staticDataModel.signals){
            check_noise(signal);
            check_signal_wire_distance(signal);
            System.out.println("signal.isIs_updated() "+signal.isIs_updated());
            System.out.println("signal.getState() "+signal.getState());
            if((!signal.isIs_updated()) && Objects.equals(signal.getState(),"on_wire")){
                if(signal.getLinked_wire()==null){
                    System.out.println("signal.getLinked_wire() is null ---");
                }
                //speed adjusst
                signal_one_step_on_wire(signal);
                signal.setLength_on_wire(signal.getLength_on_wire()+cons.getDefault_delta_wire_length());
                //
                if(signal.getLength_on_wire()>signal.getLinked_wire().getLength()){
                    signal_go_to_bank(signal);
                }
                else {
                    methods.update_signal_onwire(signal);
//                    System.out.println("update on wire ");
                }
            }
            else{
                signal.setIs_updated(false);
            }

        }
        System.out.println("/// end-  Signals_Update()");

    }

    public void check_and_do_collision() {
        if(!staticDataModel.Oairyaman) {

            for(int i = 0; i< staticDataModel.signals.size(); i++) {
                Signal signal1 = staticDataModel.signals.get(i);
                for (int j = 0; j < i; j++) {

                    Signal signal2 = staticDataModel.signals.get(j);
                    if (null != methods.checkCollisionAndGetPoint(signal1.poly, signal2.poly)) {
                        if (!methods.found_in_pairs(signal1, signal2)) {
                            just_collapse_noise(signal1,signal2);

                            collapse_happen_in_a_location((Coordinate) Methods.checkCollisionAndGetPoint(signal1.poly, signal2.poly) ,signal1,signal2 );
                        }
                    }
                    colapsedpairs_update();
                }
//                check_noise(signal1);
            }
        }

    }

    public boolean is_winner_and_update_dead_count() {
        //counter dead
        dead_count=0;
        for (Signal signal :staticDataModel.signals) {
            if(signal.getState()!="ended") {
                dead_count++;
                //really dead or just not ended
            }
        }
        double dead_ratio = (double)dead_count/(double) staticDataModel.signals.size();

        if(dead_ratio> staticDataModel.constraintss.getMaximum_dead_ratio()) {
            return false;
        }
        else {
            return true;
        }
    }

    private void  collapse_happen_in_a_location(Coordinate coordinate,Signal signal1,Signal signal2) {
//        if(virtual_run) return;
        Configg cons = Configg.getInstance();
////      just show
//        Circle impulse_circle = new Circle();
//        impulse_circle.setCenterX(coordinate.x);
//        impulse_circle.setCenterY(coordinate.y);
//        impulse_circle.setFill(cons.getImpulse_color());
//
//        level_gamemodel.impulse_circles.add(impulse_circle);
//        just_game_pane.getChildren().add(impulse_circle);
//
//        int maxcyclecount =(int) (cons.getImpulse_show_time() / 0.017);
//        AtomicInteger cyclecount= new AtomicInteger();
//        cyclecount.set(0);
//        Timeline timeline_signals_run = new Timeline(new KeyFrame(Duration.millis(17), event -> {
//
//            cyclecount.set(cyclecount.get()+1);
//            double ratio=(double) cyclecount.get()/maxcyclecount;
////            System.out.println("collapse ratio: "+ratio+" maxcyclecount: "+maxcyclecount);
//
//            impulse_circle.setRadius(cons.getImpulse_radius()*ratio);
//
//        }));
//        timeline_signals_run.setCycleCount(maxcyclecount);
//        timeline_signals_run.play();
//        timeline_signals_run.setOnFinished(event -> {
//            just_game_pane.getChildren().remove(impulse_circle);
//        });


//        control
        staticDataModel.collapsedPairs.add(new Pairs(signal1,signal2));
        for(Signal signal: staticDataModel.signals) {
            if(signal.getState()=="on_wire"){
                //central of signal is matter
                if(methods.calculate_distance(signal.getX(),signal.getY(),coordinate.getX(),coordinate.getY())< cons.getImpulse_radius()){
                    in_radius_impulse_wave(signal,coordinate);
                }
            }
        }
    }

    private void in_radius_impulse_wave(Signal signal, Coordinate coordinate) {
        if(staticDataModel.Oatar ){return;}

        Configg cons=Configg.getInstance();
        double dx=signal.getX()-coordinate.getX();
        double dy=signal.getY()-coordinate.getY();
        double r=Math.sqrt(dx*dx+dy*dy);
        double step = (double) 1/ (int)(cons.getImpulse_move_time()/0.017);
        Timeline signal_shouting = new Timeline(new KeyFrame(Duration.millis(17), event -> {

            signal.setX_ekhtelaf(signal.getX_ekhtelaf()+(dx/r)*step*cons.getImpulse_delta_r());
            signal.setY_ekhtelaf(signal.getY_ekhtelaf()+(dy/r)*step*cons.getImpulse_delta_r());

        }));
        signal_shouting.setCycleCount((int) (cons.getImpulse_move_time()/0.017));
        signal_shouting.play();
    }

    public boolean ending_check() {
        boolean is_ended=true;
        if(view.gameTimer.getTime_sec()>= staticDataModel.constraintss.getMaximum_time_sec()){
//            is_ended=true;
        }
        else {
            for (Signal signal : staticDataModel.signals) {
                if(signal.getState()==null){
                    is_ended=false;
                }
                if (!(signal.getState() == "lost" || signal.getState() == "ended")) {
                    is_ended = false;
                }
            }
        }
        if(is_ended) {
            level_ended();
        }
        return is_ended;
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
            staticDataModel.setSekke(staticDataModel.getSekke() + cons.getRectangle_signal_sekke_added());
        }
        if(signal.getTypee().getId()==2) {
            staticDataModel.setSekke(staticDataModel.getSekke() + cons.getTraiangle_signal_sekke_added());
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

    private void signal_one_step_on_wire(Signal signal) {
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
        System.out.println("********* signal_add_to_start  frame counter ="+ signal_run_frame_counter);
        staticDataModel.signals.add(signal);
        staticDataModel.sysboxes.getFirst().signal_bank.add(signal);
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

    private void check_noise(Signal signal) {
        Configg cons = Configg.getInstance();
        if(signal.getNoise()> staticDataModel.constraintss.getMaximum_noise()){
            go_to_dead(signal);
        }
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
        for(Pairs pair : staticDataModel.collapsedPairs){
            if(current_time-pair.adding_time > cons.getImpulse_resttime()){
                staticDataModel.collapsedPairs.remove(pair);
            }
        }
    }


    private double runratio() {
        Configg cons = Configg.getInstance();
        if(virtual_run){
            return (1/(cons.getVirtual_frequency()/60));
        }
        else
            return 1;
    }

    public void time_to_restart(Stage primaryStage) throws Exception {
        staticDataModel.stop_wiring=false;
        primaryStage.hide();
        this.start(primaryStage,level);

    }

    public void reset_all_noise() {
        for (Signal signal : staticDataModel.signals) {
            signal.setNoise(0);
        }
    }

    public void wiring() {
        //running till signal_play butten pressed
//        System.out.println("in wiring");
        AtomicReference<Wire> decoy_wire = new AtomicReference<>();
        AtomicBoolean isStartedinGate = new AtomicBoolean();
        //-----------------------------first selection
        for (Sysbox sysbox : staticDataModel.sysboxes) {
            for(Gate gate:sysbox.inner_gates){
                gate.poly.setOnMousePressed(e -> {
                    if(staticDataModel.stop_wiring) return;
                    if (e.getButton() != MouseButton.PRIMARY) {return;}
                    Wire candidate_wire = new Wire();
                    candidate_wire.setFirstgate(gate);
                    decoy_wire.set(candidate_wire);
                    isStartedinGate.set(true);

                });
            }
            for(Gate gate:sysbox.outer_gates){
                gate.poly.setOnMousePressed(e -> {
                    if(staticDataModel.stop_wiring) return;
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
        view.just_game_pane.setOnMouseReleased(event -> { if(staticDataModel.stop_wiring) return;
            if(isStartedinGate.get()) {
                isStartedinGate.set(false);
                Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

                AtomicBoolean isEndedinGate = new AtomicBoolean(false);

                for (Sysbox sysbox : staticDataModel.sysboxes) {
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
            if(staticDataModel.stop_wiring) return;
            if(event.getButton() != MouseButton.SECONDARY) return;

            Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

            for(Wire wire: staticDataModel.wires){
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
        staticDataModel.wires.remove(wire);
        staticDataModel.setLevel_wires_length(staticDataModel.getLevel_wires_length() - wire.getLength());

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
                || staticDataModel.getLevel_wires_length() + wire.getLength() > staticDataModel.constraintss.getMaximum_length())
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
        staticDataModel.wires.add(wire);
//        System.out.println("number of wires:"+level_gamemodel.wires.size());
        staticDataModel.setLevel_wires_length(staticDataModel.getLevel_wires_length() + wire.getLength());
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

    public void time_to_stop_wiring() {
        staticDataModel.stop_wiring=true;
    }

    private void level_ended() {
        System.out.println("******************** LEVEL ENDED *******************");


        //Check
        System.out.println("level_gamemodel.signals.size() "+ staticDataModel.signals.size());
        for (Signal signal : staticDataModel.signals) {
            System.out.println("signal_state: "+signal.getState());
        }

        //end of Check


        staticDataModel.stop_wiring=false;
        view.show_ending_stage();

    }

    public void half_restart(double goToTime_sec) {
        view.gameTimer.setTime_sec(goToTime_sec);
        Configg cons = Configg.getInstance();
        double cyclecount=goToTime_sec*60;
        restart_level_signals();
        virtual_run=true;

        if(cyclecount<3){cyclecount=3;}
        signals_virtual_run.setCycleCount((int) (cyclecount));
        signals_virtual_run.play();
        signals_virtual_run.setOnFinished(event2 -> {
           virtual_run=false;
        });
    }

    private void restart_level_signals() {
        for (Signal signal : staticDataModel.signals) {
            view.just_game_pane.getChildren().remove(signal.poly);
        }
//        for (Circle circle: level_gamemodel.impulse_circles){
//            just_game_pane.getChildren().remove(circle);
//        }

        staticDataModel = level_gamemodel_start.getClone();

        System.out.println("signals size now: " + staticDataModel.signals.size());

        for (Signal signal : staticDataModel.signals) {
            System.out.println("signal_state: "+signal.getState());
        }

        System.out.println("level_gamemodel.sysboxes.getFirst().signal_bank.size() "+ staticDataModel.sysboxes.getFirst().signal_bank.size());

//        update_gate_from_wires();
    }

    private void update_gate_from_wires() {
        for (Wire wire:staticDataModel.wires) {
            wire.getFirstgate().setWire(wire);
            wire.getSecondgate().setWire(wire);
        }
    }

    public void indicator_update() {
        for(Sysbox sysbox : staticDataModel.sysboxes) {
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

}
