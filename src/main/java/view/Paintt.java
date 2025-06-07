package view;

import controller.Controller;
import controller.GameTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mains.Configg;
import model.Gate;
import model.Signal;
import model.Sysbox;

import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static mains.Filee.level_stack;
import static mains.MainGame.*;


public class Paintt {
    static Slider virtualTimeSlider = new Slider(0, 1, 0.0);
    private static Text timetext = new Text("");
    public static GameTimer gameTimer = new GameTimer();
    static Line showline = new Line();

    public static void HUD_signal_run_update() {

            user_changing = false;
            virtualTimeSlider.setValue(gameTimer.getTime_sec() / level_stack.constraintss.getMaximum_time_sec());
            user_changing = true;

        double time = gameTimer.getTime_sec();
        timetext.setText(String.format("Time: %.1fs", time));



    }
    public static void HUD_wiring_update() {
        Configg cons = Configg.getInstance();
        double ratio=1 - level_stack.getLevel_wires_length()/level_stack.constraintss.getMaximum_length();

        showline.setEndX(500+ratio*cons.getHealth_bar_back_length());
    }

    public void addtopane_signals() {
        for (Signal signal : level_stack.signals) {
            just_game_pane.getChildren().add(signal.poly);
        }
    }
    public void addtopane_sysboxsandindicators(){
        for (Sysbox sysbox : level_stack.sysboxes) {
            just_game_pane.getChildren().add(sysbox.getRectangle());
            just_game_pane.getChildren().add(sysbox.getIndicator_rectangle());
        }
    }
    public void addtopane_gates(){
        for (Sysbox sysbox1 : level_stack.sysboxes) {
            for (Gate gate :sysbox1.inner_gates){
                one_gate_update_polygan(gate);
                just_game_pane.getChildren().add(gate.poly);
            }

            for (Gate gate2 :sysbox1.outer_gates){
                one_gate_update_polygan(gate2);
                just_game_pane.getChildren().add(gate2.poly);
            }
        }
    }
    private void one_gate_update_polygan(Gate gate) {
        Configg cons = Configg.getInstance();
        double pi=3.1415;
        if(Objects.equals(gate.getTypee().getName(),"rectangle")){
            gate.poly.getPoints().clear();
            gate.poly.getPoints().addAll(gate.getX()-cons.getGate_rectangle_width()/2, gate.getY()-cons.getGate_rectangle_height()/2);
            gate.poly.getPoints().addAll(gate.getX()-cons.getGate_rectangle_width()/2, gate.getY()+cons.getGate_rectangle_height()/2);
            gate.poly.getPoints().addAll(gate.getX()+cons.getGate_rectangle_width()/2, gate.getY()+cons.getGate_rectangle_height()/2);
            gate.poly.getPoints().addAll(gate.getX()+cons.getGate_rectangle_width()/2, gate.getY()-cons.getGate_rectangle_height()/2);
        }
        if(Objects.equals(gate.getTypee().getName(),"triangle")){
            gate.poly.getPoints().clear();
            for (int i=0 ; i<3;i++) {
                gate.poly.getPoints().addAll(gate.getX()-cons.getGate_triangle_radius()*sin(i*2*pi/3), gate.getY() - cons.getGate_triangle_radius()*cos(i*2*pi/3));
            }
        }
    }
    public void initial_UI(Stage primaryStage){
        StackPane.setAlignment(HUDpane, Pos.TOP_LEFT); // مکان کل HUDpane
        HUDpane.setStyle("-fx-background-color: rgba(92,82,82,0.5);");
        HUDpane.setPrefWidth(300);
        HUDpane.setPrefHeight(100);
        HUDpane.setMaxHeight(Region.USE_PREF_SIZE);
        setupHUD(primaryStage);


        main_game_root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Controller.exit();
            }
        });



    }

    public void setupHUD(Stage primaryStage) {
        String textStyle = "-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold";

        // اسلایدر و لیبل
        Label volumeLabel = new Label("time line");
        volumeLabel.setStyle(textStyle);

        virtualTimeSlider.setShowTickMarks(true);
        virtualTimeSlider.setShowTickLabels(true);
        virtualTimeSlider.setMajorTickUnit(0.1);
        virtualTimeSlider.setBlockIncrement(0.1);
        virtualTimeSlider.setPrefWidth(200);
        virtualTimeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(user_changing) {
                Controller.virtual_time_clicked(newVal.doubleValue());
            }
        });


        //نوار طول سیم
        Configg cons = Configg.getInstance();

        Line backline = new Line(500,25,500+cons.getHealth_bar_back_length(),25);
        backline.setStrokeWidth(cons.getHealth_bar_width());
        backline.setStroke(cons.getHealth_bar_back_color());


        showline.setStartX(500);
        showline.setStartY(25);
        showline.setEndX(500+cons.getHealth_bar_back_length());
        showline.setEndY(25);
        showline.setStrokeWidth(cons.getHealth_bar_width());
        showline.setStroke(cons.getHealth_bar_show_color());






        // دکمه شروع/توقف
        Button runStopButton = new Button("Run");
        runStopButton.setOnAction(event -> {
            try {
                Controller.run_stop_button_pressed(primaryStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            runStopButton.setText(stop_wiring ? "wiring" : "Run");
        });

        double time = gameTimer.getTime_sec();
        timetext.setText(String.format("Title: %.1fs", time));




        // HBox برای چیدن عناصر کنار هم
        HBox hudControls = new HBox(20); // فاصله بین اجزا
        hudControls.setPadding(new Insets(10));
        hudControls.setAlignment(Pos.TOP_LEFT);
        hudControls.getChildren().addAll(runStopButton, volumeLabel, virtualTimeSlider, timetext);

        // اضافه کردن به HUDpane
        HUDpane.getChildren().add(hudControls);
        HUDpane.getChildren().add(backline);
        HUDpane.getChildren().add(showline);
    }




}
