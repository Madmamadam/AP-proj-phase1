package mains;

import controller.Add_level;
import controller.Controller;
import controller.Inital_Load;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.Paintt;

import static mains.Filee.level_stack;

public class Main extends Application {
    public static Pane just_game_pane = new Pane();
    public static Boolean stop_wiring = false;
//    public static void main(String[] args) {


//        -------------------------------------load initial value of level to model

//        -------------------------------------creative game instance
//        -------------------------------------
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Paintt paintt = new Paintt();
        Scene main_game_scene = new Scene(just_game_pane);
        primaryStage.setScene(main_game_scene);
        primaryStage.show();
        Add_level.start();
        paintt.addtopane_sysboxsandindicators();
        paintt.addtopane_gates();
//        paintt.addtopane_signals();



//       wiring mode
        Controller.wiring();


//      in signal move mode
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), event -> {
//            Controller.wiring();

//            Controller.Signals_Update();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
