package controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;
import mains.Configg;
import mains.MainGame;
import mains.Start_menu;
import model.*;
import org.locationtech.jts.geom.Coordinate;
import view.Paintt;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.input.KeyCode.U;
import static mains.Filee.level_stack;
import static mains.Filee.level_stack_start;
import static mains.MainGame.*;
import static mains.Start_menu.static_market_pane;
import static view.Paintt.gameTimer;
import static view.Paintt.win_ending_pane;

public class Controller {

    public static void Signals_Update(){
        Methods methods = new Methods();
        Configg cons = Configg.getInstance();
        //it's after load signals stack
        System.out.println("///Signals_Update()");


        //zero: add signals to start Sysbox
        for(After_Frame_And_Signal_start d_signal :level_stack.After_signals){
            if(!d_signal.added && d_signal.adding_frame<signal_run_frame_counter){
                d_signal.added = true;
                signal_add_to_start(d_signal.signal);
            }
        }



        //first: update signals that on sysbox  (Assign wire)
        for (Sysbox sysbox : level_stack.sysboxes) {
            System.out.println("sysbox.signal_bank.size() "+sysbox.signal_bank.size());
            for (int i=0;i<sysbox.signal_bank.size() && !sysbox.signal_bank.isEmpty();){
                Signal signal=sysbox.signal_bank.get(i);
                signal.setIs_updated(true);
                System.out.println("nice1  sysbox_index: "+level_stack.sysboxes.indexOf(sysbox));
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
        for(Signal signal : level_stack.signals){
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

    private static void signal_one_step_on_wire(Signal signal) {
        Configg cons = Configg.getInstance();
        if(signal.getTypee().getName()=="rectangle")
            //first gate and end gate have same type
            if(signal.getLinked_wire().getFirstgate().getTypee().getName()=="rectangle") {
                signal.setLength_on_wire(signal.getLength_on_wire() + cons.getDefault_delta_wire_length());
            }
            if(signal.getLinked_wire().getFirstgate().getTypee().getName()=="triangle"){
                signal.setLength_on_wire(signal.getLength_on_wire() + cons.getDefault_delta_wire_length()/2);
            }
            else {
                System.out.println("+++++type not found error");
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

    private static void signal_add_to_start(Signal signal) {
        System.out.println("********* signal_add_to_start  frame counter ="+signal_run_frame_counter);
        level_stack.signals.add(signal);
        level_stack.sysboxes.getFirst().signal_bank.add(signal);
    }

    private static void check_signal_wire_distance(Signal signal) {
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

    private static void signal_go_to_bank(Signal signal) {
        Configg cons = Configg.getInstance();

        Sysbox sysbox =signal.getLinked_wire().getSecondgate().getSysbox();

        if(sysbox.signal_bank.size()>5 && !sysbox.isStarter()){
            //lost
            signal.setState("lost");
            return;
        }

        sysbox.signal_bank.add(signal);
        if(signal.getTypee().getId()==1) {
            level_stack.setSekke(level_stack.getSekke() + cons.getRectangle_signal_sekke_added());
        }
        if(signal.getTypee().getId()==2) {
            level_stack.setSekke(level_stack.getSekke() + cons.getTraiangle_signal_sekke_added());
        }
        just_game_pane.getChildren().remove(signal.poly);
        signal.getLinked_wire().getFirstgate().setIn_use(false);

        if(sysbox.isStarter()){
            signal.setState("ended");
        }
        else {
//           did not use method because it's easy
            signal.setState("on_sysbox");

        }
    }

    private static void signal_go_to_wire(Signal signal, Gate recom_gate) {
        signal.setLinked_wire(recom_gate.getWire());
        signal.setLength_on_wire(0.0);
        System.out.println("recom_gate.getSysbox().signal_bank.size() "+recom_gate.getSysbox().signal_bank.size());
        recom_gate.getSysbox().signal_bank.remove(signal);
        signal.setState("on_wire");
//        System.out.println("go to on wire ");
        recom_gate.setIn_use(true);
        just_game_pane.getChildren().add(signal.poly);
    }


    public static void wiring() {
        //running till signal_play butten pressed
//        System.out.println("in wiring");
        AtomicReference<Wire> decoy_wire = new AtomicReference<>();
        AtomicBoolean isStartedinGate = new AtomicBoolean();
        //-----------------------------first selection
        for (Sysbox sysbox : level_stack.sysboxes) {
            for(Gate gate:sysbox.inner_gates){
                gate.poly.setOnMousePressed(e -> {
                    if(stop_wiring) return;
                    if (e.getButton() != MouseButton.PRIMARY) {return;}
                    Wire candidate_wire = new Wire();
                    candidate_wire.setFirstgate(gate);
                    decoy_wire.set(candidate_wire);
                    isStartedinGate.set(true);

                });
            }
            for(Gate gate:sysbox.outer_gates){
                gate.poly.setOnMousePressed(e -> {
                    if(stop_wiring) return;
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
        just_game_pane.setOnMouseReleased(event -> { if(stop_wiring) return;
            if(isStartedinGate.get()) {
                isStartedinGate.set(false);
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


        //wire removing

        just_game_pane.setOnMouseClicked(event ->{
            if(stop_wiring) return;
            if(event.getButton() != MouseButton.SECONDARY) return;

            Node nodeUnderMouse = event.getPickResult().getIntersectedNode();

            for(Wire wire: level_stack.wires){
                Line poly =wire.getLine();
//                System.out.println("right before if");
                if(nodeUnderMouse == poly || poly.equals(nodeUnderMouse) || poly.isHover()){
//                    System.out.println("right after if");
                    time_to_remove_wire(wire);
                    return;
                }
            }
        });
//        System.out.println("number of wire right before for:"+level_stack.wires.size());
//        for(Wire wire: level_stack.wires){
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

    private static void time_to_remove_wire(Wire wire) {
        just_game_pane.getChildren().remove(wire.getLine());
        wire.getFirstgate().setWire(null);
        wire.getSecondgate().setWire(null);
        level_stack.wires.remove(wire);
        level_stack.setLevel_wires_length(level_stack.getLevel_wires_length() - wire.getLength());

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
//        System.out.println("number of wires:"+level_stack.wires.size());
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

    public static void exit() {
        System.exit(1);
    }

    public static void run_stop_button_pressed(Stage primaryStage) throws Exception {
        if(stop_wiring){
            time_to_restart(primaryStage);
        }
        else {
            boolean access=true;
            for(Sysbox sysbox: level_stack.sysboxes) {
                if(!sysbox.isIndicator_on_state()){
                    access=false;
                    break;
                }
                for(Gate gate: sysbox.inner_gates) {
                    if (gate.getWire() == null) {
                        access = false;
                        break;
                    }
                }
                for(Gate gate: sysbox.outer_gates) {
                    if (gate.getWire() == null) {
                        access = false;
                        break;
                    }
                }
            }
            if(access){
                time_to_stop_wiring();
            }
            else {
                false_try_for_stop_wiring();
            }

        }

    }

    private static void time_to_restart(Stage primaryStage) throws Exception {
        stop_wiring=false;
        primaryStage.hide();
        MainGame.start(primaryStage,level);

    }

    private static void false_try_for_stop_wiring() {
    }

    private static void time_to_stop_wiring() {
        stop_wiring=true;
    }


    public static void indicator_update() {
        for(Sysbox sysbox : level_stack.sysboxes) {
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

    public static void check_and_do_collision() {
        Methods methods = new Methods();
        if(!level_stack.Oairyaman) {

            for(int i=0;i<level_stack.signals.size();i++) {
                Signal signal1 = level_stack.signals.get(i);
                for (int j = 0; j < i; j++) {

                    Signal signal2 = level_stack.signals.get(j);
                    if (null != Methods.checkCollisionAndGetPoint(signal1.poly, signal2.poly)) {
                        if (!Methods.found_in_pairs(signal1, signal2)) {
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

    private static void check_noise(Signal signal) {
        Configg cons = Configg.getInstance();
        if(signal.getNoise()>level_stack.constraintss.getMaximum_noise()){
            go_to_dead(signal);
        }
    }

    private static void go_to_dead(Signal signal) {
        signal.setIs_updated(true);

        if(signal.getState()=="on_wire")
        {
        //خالی کردن اون خط
            signal.getLinked_wire().getFirstgate().setIn_use(false);
            signal.getLinked_wire().getSecondgate().setIn_use(false);
        }
        signal.setState("lost");
        just_game_pane.getChildren().remove(signal.poly);
    }

    private static void just_collapse_noise(Signal signal1, Signal signal2) {
        Configg cons = Configg.getInstance();
        signal1.setNoise(signal1.getNoise()+cons.getNoise_add_every_hit());
        signal2.setNoise(signal2.getNoise()+cons.getNoise_add_every_hit());
    }

    private static void colapsedpairs_update() {
        Configg cons= Configg.getInstance();
        long long_current_time = System.currentTimeMillis();
        double current_time = long_current_time/1000000000.0;
        for(Pairs pair :level_stack.collapsedPairs){
            if(current_time-pair.adding_time > cons.getImpulse_resttime()){
                level_stack.collapsedPairs.remove(pair);
            }
        }
    }

    private static void  collapse_happen_in_a_location(Coordinate coordinate,Signal signal1,Signal signal2) {
//        if(virtual_run) return;

        Methods methods = new Methods();
        Configg cons = Configg.getInstance();
////      just show
//        Circle impulse_circle = new Circle();
//        impulse_circle.setCenterX(coordinate.x);
//        impulse_circle.setCenterY(coordinate.y);
//        impulse_circle.setFill(cons.getImpulse_color());
//
//        level_stack.impulse_circles.add(impulse_circle);
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
        level_stack.collapsedPairs.add(new Pairs(signal1,signal2));
        for(Signal signal: level_stack.signals) {
            if(signal.getState()=="on_wire"){
                //central of signal is matter
                if(methods.calculate_distance(signal.getX(),signal.getY(),coordinate.getX(),coordinate.getY())< cons.getImpulse_radius()){
                    in_radius_impulse_wave(signal,coordinate);
                }
            }
        }
    }

    private static double runratio() {
        Configg cons = Configg.getInstance();
        if(virtual_run){
            return (1/(cons.getVirtual_frequency()/60));
        }
        else
            return 1;
    }

    private static void in_radius_impulse_wave(Signal signal, Coordinate coordinate) {
        if(level_stack.Oatar){return;}

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

    public static void virtual_time_clicked(double virtual_ratio) {
        double max_t=level_stack.constraintss.getMaximum_time_sec();
        double go_to_time_sec = virtual_ratio*max_t;
        half_restart(go_to_time_sec);

    }

    private static void half_restart(double goToTime_sec) {
        gameTimer.setTime_sec(goToTime_sec);
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

    private static void restart_level_signals() {
        for (Signal signal : level_stack.signals) {
            just_game_pane.getChildren().remove(signal.poly);
        }
//        for (Circle circle: level_stack.impulse_circles){
//            just_game_pane.getChildren().remove(circle);
//        }

        level_stack = level_stack_start.getClone();

        System.out.println("signals size now: " + level_stack.signals.size());

        for (Signal signal : level_stack.signals) {
            System.out.println("signal_state: "+signal.getState());
        }

        System.out.println("level_stack.sysboxes.getFirst().signal_bank.size() "+level_stack.sysboxes.getFirst().signal_bank.size());

//        update_gate_from_wires();
    }

    private static void update_gate_from_wires() {
        for (Wire wire: level_stack.wires) {
            wire.getFirstgate().setWire(wire);
            wire.getSecondgate().setWire(wire);
        }
    }

    public static boolean ending_check() {
        boolean is_ended=true;
        if(gameTimer.getTime_sec()>=level_stack.constraintss.getMaximum_time_sec()){
//            is_ended=true;
        }
        else {
            for (Signal signal : level_stack.signals) {
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

    private static void level_ended() {
        System.out.println("******************** LEVEL ENDED *******************");
        //Check
        System.out.println("level_stack.signals.size() "+level_stack.signals.size());
        for (Signal signal : level_stack.signals) {
            System.out.println("signal_state: "+signal.getState());
        }

        //end of Check
        stop_wiring=false;
        Paintt.add_ratio_to_ending_pane();
        MainGame.show_ending_stage();

    }

    public static boolean is_winner() {
        //counter dead
        int dead_count=0;;
        for (Signal signal : level_stack.signals) {
            if(signal.getState()=="dead") {
                dead_count++;
            }
        }
        double dead_ratio = (double)dead_count/(double)level_stack.signals.size();

        if(dead_ratio>level_stack.constraintss.getMaximum_dead_ratio()) {
            return false;
        }
        else {
            return true;
        }
    }

    public static void menuBtn_clicked() {
        Start_menu.show_menu();
    }

    public static void restartBtn_clicked() throws Exception {
        time_to_restart(primaryStage_static);
    }

    public static void nextLevelBtn_clicked() {
        try {
            MainGame.start(primaryStage_static,2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void signal_log_enable(Scene scene) {
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().toString().equals("U")) {
                print_signal_log();
            }
        });


    }

    private static void print_signal_log() {
        System.out.println("------------singnal and sysbox log-------------");
        System.out.println("level_stack.signals.size() "+level_stack.signals.size());
        for (Signal signal : level_stack.signals) {
            System.out.println("signal.getState() "+signal.getState());
            System.out.println("signal.getLinked_wire()" +signal.getLinked_wire());
            if(signal.getLinked_wire()!=null) {
                System.out.println("level_stack.sysboxes.indexOf(signal.getLinked_wire().getFirstgate().getSysbox()) " +level_stack.sysboxes.indexOf(signal.getLinked_wire().getFirstgate().getSysbox()));
            }
        }

        for (int i = 0; i < level_stack.sysboxes.size(); i++) {
            Sysbox sysbox = level_stack.sysboxes.get(i);
            System.out.println("sysbox number "+i +"    sysbox.signal_bank.size() "+sysbox.signal_bank.size() );
        }
        System.out.println("-----------------------------------------------");
    }

    public static void marketButtonClicked() {
        if(stop_wiring){
            //for safety
            Paintt.marketPaneupdate();
            static_market_pane.setVisible(true);
        }
    }
}
