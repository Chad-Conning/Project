package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.Database;

import java.sql.Connection;

public class admitAnimalController {
    Database queries = new Database();
    Connection connection;
    Scene scene;
    String staffRole;

    @FXML private Button btnAdmit;
    @FXML private Button btnACancel;
    @FXML private TextField tfieldTag;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox comboLocation;
    @FXML private TextArea txtNotes;



}
