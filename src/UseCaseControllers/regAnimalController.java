package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import sample.Database;
import sample.LoginManager;
import sample.Staff;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class regAnimalController {
    Database queries = new Database();
    Connection connection;
    Scene scene;
    String staffRole;

    @FXML private Button btnARegister;
    @FXML private Button btnACancel;
    @FXML private TextField tfieldTag;
    @FXML private TextField tfieldName;
    @FXML private RadioButton toggleMale;
    @FXML private RadioButton toggleFemale;
    @FXML private RadioButton toggleYes;
    @FXML private RadioButton toggleNo;
    @FXML private ComboBox comboSpecies;

    public void initSessionID(Scene scene, String staffRole) {
        this.scene = scene;
        this.staffRole = staffRole;
        try {
            queries.connectDB();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnARegister.setOnAction(actionEvent -> regAnimal());

        btnACancel.setOnAction(actionEvent -> showMainView());
    }

    private void regAnimal(){

    }

    private void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/LandingPage.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            MainViewController controller =
                    loader.getController();   // gets the controller specified in the fxml

            LoginManager loginManager = new LoginManager(scene);
            controller.initSessionID(loginManager, this.scene, staffRole);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
