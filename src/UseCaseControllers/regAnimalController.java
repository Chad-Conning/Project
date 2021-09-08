package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import sample.Database;

import java.sql.Connection;

public class regAnimalController {
    Database queries = new Database();
    Connection connection;

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

    }
}
