package controller;

public class MainMenuController {
    public static void newGamebutton_Clicked() throws Exception {
        HighLevelController highLevelController = new HighLevelController();

        highLevelController.startTheGameFromFirstLevel();
    }
}
