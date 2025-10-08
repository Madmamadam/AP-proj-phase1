package controller;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import javafx.stage.Stage;
import javafx.util.Duration;
import mains.MainGame_Logics;
import mains.Start_menu;
import model.*;
import view.Paintt;

import static mains.Start_menu.primaryStage_static;
import static mains.Start_menu.static_market_pane;

public class Controller {
    public MainGame_Logics mainGameViewAndModel;
    public Paintt view;
    Methods methods =new Methods(mainGameViewAndModel);


    public static void exit() {
        System.exit(1);
    }

    public void run_stop_button_pressed(Stage primaryStage) throws Exception {
        if(mainGameViewAndModel.staticDataModel.stop_wiring){
            mainGameViewAndModel.time_to_restart(primaryStage);
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
                mainGameViewAndModel.time_to_stop_wiring();
            }
            else {
                false_try_for_stop_wiring();
            }

        }

    }



    private void false_try_for_stop_wiring() {
    }




    public void virtual_time_clicked(double virtual_ratio) {
        double max_t= mainGameViewAndModel.staticDataModel.constraintss.getMaximum_time_sec();
        double go_to_time_sec = virtual_ratio*max_t;
        System.out.println("in controller virtual_time_clicked");
        mainGameViewAndModel.half_restart(go_to_time_sec);

    }







    public void menuBtn_clicked() {
        Start_menu.show_menu();
    }

    public void restartBtn_clicked() throws Exception {
        mainGameViewAndModel.time_to_restart(primaryStage_static);
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

            mainGameViewAndModel.reset_all_noise();


        }
    }



    public void edit_wires() {

    }
}
