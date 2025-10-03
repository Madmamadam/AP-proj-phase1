package view;

import controller.Controller;
import controller.GameTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
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
import static mains.Filee.level_gamemodel;
import static mains.MainGame.*;
import static mains.Start_menu.static_market_pane;


public class Paintt {
    static Slider virtualTimeSlider = new Slider(0, 1, 0.0);
    private static Text timetext = new Text("");
    public static GameTimer gameTimer = new GameTimer();
    static Line showline = new Line();
    public static Pane win_ending_pane=new Pane();
    public static Pane lose_ending_pane=new Pane();
    public static Scene end_stage_scene ;
    public static Label coins = new Label("💰 1277");


    public static void HUD_signal_run_update() {

            user_changing = false;
            virtualTimeSlider.setValue(gameTimer.getTime_sec() / level_gamemodel.constraintss.getMaximum_time_sec());
            user_changing = true;

        double time = gameTimer.getTime_sec();
        timetext.setText(String.format("Time: %.1fs", time));



    }
    public static void HUD_wiring_update() {
        Configg cons = Configg.getInstance();
        double ratio=1 - level_gamemodel.getLevel_wires_length()/ level_gamemodel.constraintss.getMaximum_length();

        showline.setEndX(700+ratio*cons.getHealth_bar_back_length());
    }

    public static void add_ratio_to_ending_pane() {
        Label scoreLabelWin = new Label("dead ratio: " + (double) dead_count/ level_gamemodel.signals.size());
        scoreLabelWin.setStyle("-fx-font-size: 24px; -fx-text-fill: darkgreen;");
        scoreLabelWin.setLayoutX(350);
        scoreLabelWin.setLayoutY(180);

        Label scoreLabelLose = new Label("dead ratio: " + (double) dead_count/ level_gamemodel.signals.size());
        scoreLabelLose.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        scoreLabelLose.setLayoutX(350);
        scoreLabelLose.setLayoutY(180);

        win_ending_pane.getChildren().add(scoreLabelWin);
        lose_ending_pane.getChildren().add(scoreLabelLose);

    }

    public static void marketPaneupdate() {
        coins.setText("💰 " + level_gamemodel.getSekke());
    }

    public void addtopane_signals() {
        for (Signal signal : level_gamemodel.signals) {
            just_game_pane.getChildren().add(signal.poly);
        }
    }
    public void addtopane_sysboxsandindicators(){
        for (Sysbox sysbox : level_gamemodel.sysboxes) {
            just_game_pane.getChildren().add(sysbox.getRectangle());
            just_game_pane.getChildren().add(sysbox.getIndicator_rectangle());
        }
    }
    public void addtopane_gates(){
        for (Sysbox sysbox1 : level_gamemodel.sysboxes) {
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

        setup_ending_panes();
        setup_market_pane();


        main_game_root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Controller.exit();
            }
        });
        Pane trash_pane = new Pane();
        end_stage_scene = new Scene(trash_pane);





    }

    private void setup_market_pane() {
        static_market_pane =buildShopPane();
        //مکث برای رندر کردن
        Platform.runLater(() -> {
            double w = static_market_pane.getPrefWidth();
            double h = static_market_pane.getPrefHeight();
            static_market_pane.setLayoutX((just_game_pane.getWidth() - w) / 2);
            static_market_pane.setLayoutY((just_game_pane.getHeight() - h) / 2);
        });
        }
    private VBox buildShopPane() {
        VBox shop = new VBox(15);
        shop.setAlignment(Pos.CENTER);
        shop.setStyle("-fx-background-color: #4e4e46; -fx-padding: 30; -fx-background-radius: 20;");
        shop.setPrefWidth(200);
        shop.setPrefHeight(300);

        Label title = new Label("SHOP");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");


        coins.setStyle("-fx-font-size: 18px;");

        Button shieldBtn = new Button("O' Atar (3)");
        shieldBtn.setOnAction(e -> Controller.OAtar_clicked());

        Button healthBtn = new Button("O’ Airyaman (4)");
        healthBtn.setOnAction(e -> Controller.OAiryman_clicked());

        Button speedBtn = new Button("O' Anahita (5)");
        speedBtn.setOnAction(e -> Controller.OAnahita_clicked());

        Button closeBtn = new Button("✖");
        closeBtn.setOnAction(e -> shop.setVisible(false));

        shop.getChildren().addAll(title, coins, shieldBtn, healthBtn, speedBtn, closeBtn);

        shop.setVisible(false);

        return shop;
    }


    private void setup_ending_panes() {
        // ---------- Lose Ending Pane ----------
        lose_ending_pane.setStyle("-fx-background-color: red;");

        Label loseLabel = new Label("You Lost!");
        loseLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: white;");
        loseLabel.setLayoutX(320);
        loseLabel.setLayoutY(120);


        Button menuBtnLose = new Button("Menu");
        menuBtnLose.setLayoutX(350);
        menuBtnLose.setLayoutY(250);
        menuBtnLose.setOnAction(event ->
            Controller.menuBtn_clicked());

        Button restartBtn = new Button("Restart");
        restartBtn.setLayoutX(350);
        restartBtn.setLayoutY(300);
        restartBtn.setOnAction(event ->
        {
            try {
                Controller.restartBtn_clicked();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        lose_ending_pane.getChildren().addAll(loseLabel, menuBtnLose, restartBtn);

        // ---------- Win Ending Pane ----------
        win_ending_pane.setStyle("-fx-background-color: lightgreen;");

        Label winLabel = new Label("You Won!");
        winLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: darkgreen;");
        winLabel.setLayoutX(320);
        winLabel.setLayoutY(120);


        Button menuBtnWin = new Button("Menu");
        menuBtnWin.setLayoutX(350);
        menuBtnWin.setLayoutY(250);
        menuBtnWin.setOnAction(event ->
                Controller.menuBtn_clicked());

        Button nextLevelBtn = new Button("Next Level");
        nextLevelBtn.setLayoutX(350);
        nextLevelBtn.setLayoutY(300);
        nextLevelBtn.setOnAction(event ->
                Controller.nextLevelBtn_clicked());

        win_ending_pane.getChildren().addAll(winLabel, menuBtnWin, nextLevelBtn);

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

        Line backline = new Line(700,25,700+cons.getHealth_bar_back_length(),25);
        backline.setStrokeWidth(cons.getHealth_bar_width());
        backline.setStroke(cons.getHealth_bar_back_color());


        showline.setStartX(700);
        showline.setStartY(25);
        showline.setEndX(700+cons.getHealth_bar_back_length());
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
        timetext.setText(String.format("max Time: %.1fs",  level_gamemodel.constraintss.getMaximum_time_sec()));



        //market button
        Button marketButton = new Button("Market");
        marketButton.setOnAction(event -> {
            //خرید در realtime انجام میشود
            try {
                Controller.marketButtonClicked();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });



        // HBox برای چیدن عناصر کنار هم
        HBox hudControls = new HBox(10); // فاصله بین اجزا
        hudControls.setPadding(new Insets(10));
        hudControls.setAlignment(Pos.TOP_LEFT);
        hudControls.getChildren().addAll(runStopButton, volumeLabel, virtualTimeSlider, timetext , marketButton);

        // اضافه کردن به HUDpane
        HUDpane.getChildren().add(hudControls);
        HUDpane.getChildren().add(backline);
        HUDpane.getChildren().add(showline);
    }




}
