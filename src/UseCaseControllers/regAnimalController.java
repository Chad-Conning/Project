package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    Staff staffUser;

    @FXML private Button btnARegister;
    @FXML private Button btnACancel;
    @FXML private TextField tfieldName;
    @FXML private RadioButton toggleMale;
    @FXML private RadioButton toggleYes;
    @FXML private ComboBox comboSpecies;

    public void initSessionID(Scene scene, Staff staffUser) {
        comboSpecies.getItems().addAll("Seal", "Penguin", "Turtle", "Seagull", "Unknown");
        comboSpecies.getSelectionModel().select("Seal");
        this.scene = scene;
        this.staffUser = staffUser;
        try {
            queries.connectDB();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnARegister.setOnAction(actionEvent -> regAnimal());

        btnACancel.setOnAction(actionEvent -> showMainView());
    }

    private void regAnimal(){
        connection = queries.connection;
        String txtName = tfieldName.getText();
        String txtGender;
        if (toggleMale.isSelected())
            txtGender = "Male";
        else
            txtGender = "Female";
        Boolean isAdult;
        if (toggleYes.isSelected())
            isAdult = true;
        else
            isAdult = false;
        String txtSpecies = comboSpecies.getValue().toString();

        if (queries.regAnimal(txtName, isAdult, txtGender, "Alive", txtSpecies)) {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new animal member has been registered.");
            added.showAndWait();
        } else {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new animal member could not be registered.");
            added.showAndWait();
        }
        try {
            queries.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        showMainView();
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
            controller.initSessionID(loginManager, this.scene, staffUser);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
