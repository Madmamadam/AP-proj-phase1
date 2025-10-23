package controller;

import model.MainGame_Logics;
import view.Paintt;

import static mains.Start_menu.primaryStage_static;

public class HighLevelController {
    MainGame_Logics mainGameViewAndModelAndController;
    Controller controller;
    public void startTheGameFromFirstLevel() throws Exception {

        mainGameViewAndModelAndController = new MainGame_Logics();
        controller = new Controller();
        Paintt view=new Paintt();
        architectureLoad(mainGameViewAndModelAndController, controller, view);

        mainGameViewAndModelAndController.controller = controller;
        mainGameViewAndModelAndController.start(2);

    }
    private void architectureLoad(MainGame_Logics mainGameViewAndModelAndController, Controller controller , Paintt view){
        mainGameViewAndModelAndController.controller = controller;
        mainGameViewAndModelAndController.view=view;
        mainGameViewAndModelAndController.staticDataModel.view=view;
        controller.mainGameViewAndModel=mainGameViewAndModelAndController;
        controller.view=view;
        view.controller=controller;
        view.primaryStageClone=primaryStage_static;

//        view.architectureLoad();



    }

}
