package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(new StackPane());

        //stylesheet
        //scene.getStylesheets().add("bootstrap.css");

        // initializes loginManager to manage logins and to display different screens
        LoginManager loginManager = new LoginManager(scene);
        loginManager.showLoginScreen();

        primaryStage.setTitle("Bay Marine Rescue");
        primaryStage.getIcons().add(new Image("file:Resources/Images/Images/penguin (2).png"));
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}