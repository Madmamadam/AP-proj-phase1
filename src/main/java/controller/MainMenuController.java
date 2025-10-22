package controller;

public class MainMenuController {
    public static void newGameButton_Clicked() throws Exception {
        HighLevelController highLevelController = new HighLevelController();

        highLevelController.startTheGameFromFirstLevel();
    }
}
