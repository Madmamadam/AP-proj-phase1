package controller;

import mains.MainGame_Logics;
import view.Paintt;

import static mains.Start_menu.primaryStage_static;

public class HighLevelController {
    MainGame_Logics mainGameViewAndModelAndController;
    Controller controller;
    public void startTheGameFromFirstLevel() throws Exception {

        mainGameViewAndModelAndController = new MainGame_Logics();
        controller = new Controller();
        Paintt view=new Paintt();
        architevtureLoad(mainGameViewAndModelAndController, controller, view);

        mainGameViewAndModelAndController.controller = controller;
        mainGameViewAndModelAndController.start(primaryStage_static,1);
    }
    private void architevtureLoad(MainGame_Logics mainGameViewAndModelAndController, Controller controller , Paintt view){
        mainGameViewAndModelAndController.controller = controller;
        mainGameViewAndModelAndController.view=view;
        mainGameViewAndModelAndController.staticDataModel.view=view;
        controller.mainGameViewAndModel=mainGameViewAndModelAndController;
        controller.view=view;
        view.controller=controller;

//        view.architectureLoad();



    }

}
