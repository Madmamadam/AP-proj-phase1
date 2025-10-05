package mains;

import controller.Add_level;
import controller.Controller;
import controller.Methods;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import org.locationtech.jts.geom.Coordinate;
import view.Paintt;

import java.util.Objects;

import static mains.Filee.level_gamemodel_start;
import static mains.Start_menu.static_market_pane;
import static view.Paintt.*;

public class MainGame_ViewAndModelAndController {
    public LevelGame_StaticDataModel staticDataModel=new LevelGame_StaticDataModel();
    public Paintt view;
    public Controller controller;
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


            gameTimer.setStopping(false);
            view.marketPaneupdate();
            view.HUD_signal_run_update();
            signal_run_frame_counter++;
        }
        else {
            gameTimer.setStopping(true);
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
        gameTimer.restart();
        signal_run_frame_counter = 0;
        view.HUD_signal_run_update();




        Add_level.start(level,staticDataModel);
        view.initial_UI(primaryStage);


//        paintt.addtopane_signals();


//       wiring mode (run some listener)
        controller.wiring();
        controller.edit_wires();



        view.just_game_pane.getChildren().add(static_market_pane);

        Timeline timeline_wiring = new Timeline(new KeyFrame(Duration.millis(17*6), event -> {
            if (!staticDataModel.stop_wiring) {
                controller.indicator_update();
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
    public void show_ending_stage() {
        Pane show_ending_pane;

        if(is_winner_and_update_dead_count()){
            show_ending_pane = win_ending_pane;
        }
        else {
            show_ending_pane = lose_ending_pane;
        }
        if(show_ending_pane.getScene()!=null) {
            show_ending_pane.getScene().setRoot(new Pane());
        }

        view.add_ratio_to_ending_pane();
        end_stage_scene.setRoot(show_ending_pane);
        primaryStage_static.setScene(end_stage_scene);
        primaryStage_static.setFullScreen(true);
    }
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
        if(gameTimer.getTime_sec()>= staticDataModel.constraintss.getMaximum_time_sec()){
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
        if(mainGameViewAndModel.virtual_run){
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

}
