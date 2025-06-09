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
import model.Level_Stack;
import model.Sysbox;
import view.Paintt;

import static mains.Filee.level_stack;
import static mains.Filee.level_stack_start;
import static view.Paintt.*;

public class MainGame {
    public static Pane just_game_pane = new Pane();
    public static Boolean stop_wiring = false;
    public static Pane HUDpane = new Pane();
    public static StackPane main_game_root = new StackPane(just_game_pane, HUDpane);
    public static boolean user_changing=true;
    public static boolean virtual_run = false;
    public static int signal_run_frame_counter = 0;
    private static boolean first_time = true;
    public static Stage primaryStage_static;

    public static Timeline signals_run = new Timeline();
//    public static void main(String[] args) {


//        -------------------------------------load initial value of level to model

//        -------------------------------------creative game instance
//        -------------------------------------
//    }

    public static void start(Stage primaryStage) throws Exception {
        primaryStage_static = primaryStage;
        Paintt paintt = new Paintt();
        Configg cons = Configg.getInstance();



        just_game_pane = new Pane();
        stop_wiring = false;
        HUDpane = new Pane();
        main_game_root = new StackPane(just_game_pane, HUDpane);
        level_stack=new Level_Stack();
        gameTimer.restart();
        signal_run_frame_counter = 0;
        Paintt.HUD_signal_run_update();





        paintt.initial_UI(primaryStage);
        Scene main_game_scene = new Scene(main_game_root);
        primaryStage.setScene(main_game_scene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // غیرفعال‌سازی ESC
        primaryStage.setFullScreen(true);
        primaryStage.setScene(main_game_scene);
        primaryStage.show();
        Add_level.start(1);
        paintt.addtopane_sysboxsandindicators();
        paintt.addtopane_gates();


//        paintt.addtopane_signals();


//       wiring mode
        Controller.wiring();

        Timeline timeline_wiring = new Timeline(new KeyFrame(Duration.millis(17*6), event -> {
            if (!stop_wiring) {
                Controller.indicator_update();
            }
        }));

        timeline_wiring.setCycleCount(Timeline.INDEFINITE);
        timeline_wiring.play();

//      in signal move mode
        signals_run = new Timeline(new KeyFrame(Duration.millis(17), event -> {
            if (stop_wiring && !virtual_run) {
                if(first_time){
                    for (Sysbox sysbox:level_stack.sysboxes){
                        System.out.println("before clone sysbox.signal_bank.size() "+sysbox.signal_bank.size());
                    }
                    level_stack_start=level_stack.getClone();

                    for (Sysbox sysbox:level_stack.sysboxes){
                        System.out.println("before wiring sysbox.signal_bank.size() "+sysbox.signal_bank.size());
                    }
                    first_time=false;
                }
                System.out.println("////////////// in real run");


                Controller.Signals_Update();
                Controller.check_and_do_collision();
                Controller.ending_check();


                gameTimer.setStopping(false);
                Paintt.HUD_signal_run_update();
                Add_level.add_source_signals();
                signal_run_frame_counter++;
            }
            else {
                gameTimer.setStopping(true);
            }
        }));
        signals_run.setCycleCount(Timeline.INDEFINITE);
        signals_run.play();

//        Timeline signals_virtual_run = new Timeline(new KeyFrame(Duration.millis(cons.getVirtual_frequency()), event -> {
//
//        }));
    }

    public static void show_ending_stage() {
        Pane show_pane = new Pane();
        if(Controller.is_winner()){
            show_pane = win_ending_pane;
        }
        else {
            show_pane= lose_ending_pane;
        }
        Scene end_stage_scene = new Scene(show_pane);
        primaryStage_static.setScene(end_stage_scene);
    }
}
