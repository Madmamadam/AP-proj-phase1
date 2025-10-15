package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import mains.Configg;
import model.Wire;

public class EventHandlerView {
    private VBox vBox=new VBox(10);
    private Paintt view;
    int type;
    Label firstLabel;
    Label secondLabel;
    double x,y;
    Button yesButton = new Button("Yes");
    Button noButton  = new Button("No");
    Timeline remover ;
    Wire wire;

    private EventHandlerView(){}

    public EventHandlerView(double x , double y, Paintt view, int type, Wire wire){
        Configg cons = Configg.getInstance();
        System.out.println("in EventHandlerView");

        this.view =view;
        this.type = type;
        this.x = x;
        this.y = y;
        this.wire = wire;
        vBox.setLayoutX(x);
        vBox.setLayoutY(y);
        vBox.setPrefSize(cons.getEvent_Handler_view_pref_width(), cons.getEvent_Handler_view_pref_height());

        if(type == 1) {


            firstLabel = new Label("💰 " + view.controller.mainGameViewAndModel.staticDataModel.getSekke());
            secondLabel = new Label("-"+cons.getCurve_handler_cost());

//            yesButton = new Button("Yes");
//            noButton =

        }

        yesButton.setOnAction(e -> {
            yesButtonClicked();

        });
        noButton.setOnAction(e -> {
            noButtonClicked();

        });
        vBox.getChildren().addAll(firstLabel,secondLabel,yesButton,noButton);
        view.just_game_pane.getChildren().add(vBox);
        run_remover();
    }



    private void run_remover() {
        remover = new Timeline(new KeyFrame(Duration.millis(34), event -> {

        }));
        remover.setCycleCount(2*30);
        remover.play();
        remover.setOnFinished(e -> {
           view.just_game_pane.getChildren().remove(vBox);
        });
    }

    private void yesButtonClicked() {
        Configg cons = Configg.getInstance();
        if(type==1){
            if(view.controller.money_is_possible_for_add_a_curve_handler()) {
                view.controller.time_to_add_curveHandler(wire, x, y);
            }
        }
        else {
            System.out.println("type not set correctly");
        }
        view.just_game_pane.getChildren().remove(vBox);
    }
    private void noButtonClicked() {
        view.just_game_pane.getChildren().remove(vBox);
    }
}
