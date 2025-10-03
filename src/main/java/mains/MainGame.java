package mains;

import controller.Add_level;
import controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.LevelGame_model;
import model.Sysbox;
import view.Paintt;

import static mains.Filee.level_gamemodel;
import static mains.Filee.level_gamemodel_start;
import static mains.Start_menu.static_market_pane;
import static view.Paintt.*;

public class MainGame {
    public static int level;

    static Configg cons = Configg.getInstance();
    public static Pane just_game_pane = new Pane();
    public static Boolean stop_wiring = false;
    public static Pane HUDpane = new Pane();
    public static StackPane main_game_root = new StackPane(just_game_pane, HUDpane);
    public static boolean user_changing=true;
    public static boolean virtual_run = false;
    public static int signal_run_frame_counter = 0;
    public static int dead_count = 0;
    private static boolean first_time = true;
    public static Stage primaryStage_static;
    public static Timeline signals_virtual_run = new Timeline(new KeyFrame(Duration.millis(1000/cons.getVirtual_frequency()), event -> {
        if (stop_wiring) {
            System.out.println("//////////in virtual run");
            Controller.Signals_Update();
            Controller.check_and_do_collision();
            signal_run_frame_counter++;
        }
//            if(gameTimer.getTime_sec()>goToTime_sec){
//                signals_virtual_run.stop();
//            }
    }));

    public static Timeline signals_run =new Timeline(new KeyFrame(Duration.millis(17), event -> {
        if (stop_wiring && !virtual_run) {
            if(first_time){
                for (Sysbox sysbox: level_gamemodel.sysboxes){
                    System.out.println("before clone sysbox.signal_bank.size() "+sysbox.signal_bank.size());
                }
                level_gamemodel_start = level_gamemodel.getClone();

                for (Sysbox sysbox: level_gamemodel.sysboxes){
                    System.out.println("before wiring sysbox.signal_bank.size() "+sysbox.signal_bank.size());
                }
                first_time=false;
            }
            System.out.println("////////////// in real run");


            Controller.Signals_Update();
            Controller.check_and_do_collision();
            Controller.ending_check();


            gameTimer.setStopping(false);
            Paintt.marketPaneupdate();
            Paintt.HUD_signal_run_update();
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

    public static void start(Stage primaryStage ,int l) throws Exception {

        level =l;
        primaryStage_static = primaryStage;
        Paintt paintt = new Paintt();
        Configg cons = Configg.getInstance();



        just_game_pane = new Pane();
        stop_wiring = false;
        HUDpane = new Pane();
        main_game_root = new StackPane(just_game_pane, HUDpane);
        level_gamemodel =new LevelGame_model();
        gameTimer.restart();
        signal_run_frame_counter = 0;
        Paintt.HUD_signal_run_update();




        Add_level.start(level);
        paintt.initial_UI(primaryStage);
        Scene main_game_scene = new Scene(main_game_root);
        primaryStage.setScene(main_game_scene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // غیرفعال‌سازی ESC
        primaryStage.setFullScreen(true);
        primaryStage.setScene(main_game_scene);
        primaryStage.show();
        Controller.signal_log_enable(main_game_scene);
        paintt.addtopane_sysboxsandindicators();
        paintt.addtopane_gates();

//        paintt.addtopane_signals();


//       wiring mode (run some listener)
        Controller.wiring();
        Controller.edit_wires();



        just_game_pane.getChildren().add(static_market_pane);

        Timeline timeline_wiring = new Timeline(new KeyFrame(Duration.millis(17*6), event -> {
            if (!stop_wiring) {
                Controller.indicator_update();
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
//this is MainGame.java:144
    public static void show_ending_stage() {
        Pane show_ending_pane;

        if(Controller.is_winner_and_update_dead_count()){
            show_ending_pane = win_ending_pane;
        }
        else {
            show_ending_pane = lose_ending_pane;
        }
        if(show_ending_pane.getScene()!=null) {
            show_ending_pane.getScene().setRoot(new Pane());
        }

        Paintt.add_ratio_to_ending_pane();
        end_stage_scene.setRoot(show_ending_pane);
        primaryStage_static.setScene(end_stage_scene);
        primaryStage_static.setFullScreen(true);
    }
//this is MainGame.java:156
}
