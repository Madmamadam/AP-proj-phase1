package mains;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;


public class Start_menu extends Application {
    private boolean isPlaying = true;
    private boolean save = true;
    private MediaPlayer mediaPlayer;


    public static void main() {
        launch();
    }


    public void start(Stage primaryStage) throws Exception {

//        filee.read();


        //---------------------------------------------------------------------------
        //----------------------------------------------------------------------music
        //---------------------------------------------------------------------------
//        Media sound = new Media(new File("src/main/resources/roa-music-city-lights.mp3").toURI().toString());
//        mediaPlayer = new MediaPlayer(sound);
//        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
//        mediaPlayer.play();
        //---------------------------------------------------------------------------
        //----------------------------------------------------------------------music
        //---------------------------------------------------------------------------



        //---------------------------------------------------------------------------
        //-----------------------------------------------------------------menulayout
        //---------------------------------------------------------------------------

        // Define styles for buttons
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);";
        String offbuttonStyle = "-fx-background-color: #af1818; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);";

        String buttonHoverStyle = "-fx-background-color: #66BB6A; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);";
        String buttonoffHoverStyle = "-fx-background-color: #c63636; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);";

        String textStyle = "-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold";


        // Create label for game name
        Label gameNameLabel = new Label("print Hell");
        gameNameLabel.setStyle(textStyle);


        // Create buttons
        Button newGameButton = new Button("New Game");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");
        Button backButton = new Button("Back");



        // Apply styles to buttons
        newGameButton.setStyle(buttonStyle);
        newGameButton.setOnMouseEntered(e -> newGameButton.setStyle(buttonHoverStyle));
        newGameButton.setOnMouseExited(e -> newGameButton.setStyle(buttonStyle));


        settingsButton.setStyle(buttonStyle);
        settingsButton.setOnMouseEntered(e -> settingsButton.setStyle(buttonHoverStyle));
        settingsButton.setOnMouseExited(e -> settingsButton.setStyle(buttonStyle));

        exitButton.setStyle(buttonStyle);
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(buttonHoverStyle));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(buttonStyle));





        backButton.setStyle(buttonStyle);
        backButton.setOnMouseEntered(e -> backButton.setStyle(buttonHoverStyle));
        backButton.setOnMouseExited(e -> backButton.setStyle(buttonStyle));



        // Create layout with background color
        VBox menuLayout = new VBox(20);  // Spacing between elements
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setPadding(new Insets(30));
        menuLayout.setStyle("-fx-background-color: #212121;");  // Dark background
        menuLayout.getChildren().addAll(gameNameLabel, newGameButton, settingsButton, exitButton);

        Scene menuscene = new Scene(menuLayout);
        primaryStage.setScene(menuscene);
        primaryStage.show();
        double height=menuLayout.getHeight();
        double width=menuLayout.getWidth();



        //setting sub menu
        Label nameLabel2 = new Label("Setting");
        nameLabel2.setStyle(textStyle);



        // Volume slider and its controller
        Label volumeLabel = new Label("Volume");
        volumeLabel.setStyle(textStyle);
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setMajorTickUnit(0.25);
        volumeSlider.setBlockIncrement(0.1);
        volumeSlider.setPrefWidth(200);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            mediaPlayer.setVolume(newVal.doubleValue());
        });








        VBox subsettingLayout = new VBox(20);
        subsettingLayout.setAlignment(Pos.CENTER);
        subsettingLayout.setPadding(new Insets(30));
        subsettingLayout.setStyle("-fx-background-color: #212121;");
        subsettingLayout.getChildren().addAll(nameLabel2,volumeLabel,volumeSlider,backButton);

        Scene submenuscene = new Scene(subsettingLayout);







        //---------------------------------------------------------------------------
        //-----------------------------------------------------------------menulayout
        //---------------------------------------------------------------------------













        newGameButton.setOnAction(event ->{
            try {
                MainGame.start(primaryStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        primaryStage.setOnHidden(event -> {
            Platform.runLater(() -> {
                primaryStage.setScene(menuscene);
                primaryStage.show();
            });
        });



        settingsButton.setOnAction(event -> {
            primaryStage.setScene(submenuscene);

        });


        backButton.setOnAction(event ->{
            primaryStage.setScene(menuscene);
        } );
    }
}
