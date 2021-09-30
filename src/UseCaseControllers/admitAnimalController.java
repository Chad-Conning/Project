package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Callback;
import sample.Database;
import sample.Staff;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

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

    public void initSessionID(Scene scene, Staff staffUser) {
        comboLocation.getItems().addAll("Beachview Beach", "Bluewater Bay Beach", "Hobie Beach", "Humewood Beach", "King's Beach", "Maitland's Beach",
                "New Brighton Beach", "Pollock Beach", "Sardinia Bay Beach", "Schoenmakerskop Beach", "St Georges Strand", "Swartkops Beach", "Van Stadens Beach", "");
        comboLocation.getSelectionModel().select("");
        datePicker.dayCellFactoryProperty().setValue((Callback<DatePicker, DateCell>) Date.valueOf(LocalDate.now()));
        this.scene = scene;
        this.staffRole = staffRole;
        try {
            queries.connectDB();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnAdmit.setOnAction(actionEvent -> admitAnimal());

        btnACancel.setOnAction(actionEvent -> cancelReg());
    }

    private void admitAnimal() {
        connection = queries.connection;
        String txtTag = tfieldTag.getText();
        String txtLocation = comboLocation.getValue().toString();

        //if (queries.admitAnimal(txtTag, txtLocation, ))
    }

    private void cancelReg() {

    }

}
