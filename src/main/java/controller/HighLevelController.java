package controller;

import mains.MainGame_ViewAndModelAndController;

import static mains.Start_menu.primaryStage_static;

public class HighLevelController {
    MainGame_ViewAndModelAndController mainGameViewAndModelAndController;
    Controller controller;
    public void startTheGameFromFirstLevel() throws Exception {

        mainGameViewAndModelAndController = new MainGame_ViewAndModelAndController();
        controller = new Controller(mainGameViewAndModelAndController);
        mainGameViewAndModelAndController.controller = controller;
        mainGameViewAndModelAndController.start(primaryStage_static,1);
    }

}
