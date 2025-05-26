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
import view.Paintt;

public class MainGame {
    public static Pane just_game_pane = new Pane();
    public static Boolean stop_wiring = false;
    public static Pane HUDpane = new Pane();
    public static StackPane main_game_root = new StackPane(just_game_pane, HUDpane);

//    public static void main(String[] args) {


//        -------------------------------------load initial value of level to model

//        -------------------------------------creative game instance
//        -------------------------------------
//    }

    public static void start(Stage primaryStage) throws Exception {
        Paintt paintt = new Paintt();






        paintt.initial_UI();
        Scene main_game_scene = new Scene(main_game_root);
        primaryStage.setScene(main_game_scene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // غیرفعال‌سازی ESC
        primaryStage.setFullScreen(true);
        primaryStage.setScene(main_game_scene);
        primaryStage.show();
        Add_level.start();
        paintt.addtopane_sysboxsandindicators();
        paintt.addtopane_gates();
//        paintt.addtopane_signals();



//       wiring mode
        Controller.wiring();


//      in signal move mode
        Timeline timeline_wiring = new Timeline(new KeyFrame(Duration.millis(17*6), event -> {
            if (!stop_wiring) {
                Controller.indicator_update();
            }
        }));

        timeline_wiring.setCycleCount(Timeline.INDEFINITE);
        timeline_wiring.play();

        Timeline timeline_signals_run = new Timeline(new KeyFrame(Duration.millis(17), event -> {
            if (stop_wiring) {
                Controller.Signals_Update();
                Controller.check_and_do_collision();
            }
        }));
        timeline_signals_run.setCycleCount(Timeline.INDEFINITE);
        timeline_signals_run.play();
    }
}
