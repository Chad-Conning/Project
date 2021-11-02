package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class readmitAnimalController {
    Database queries = new Database();
    Connection connection;
    Scene scene;
    Staff staffUser;

    @FXML private Button btnAdmit;
    @FXML private Button btnACancel;
    @FXML private ComboBox comboTagNumber;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboLocation;
    @FXML private TextArea txtNotes;
    @FXML private Label lblUserInformation;
    @FXML private Label lblTagMsg;

    @FXML public MenuItem btnMenuAddRegisterA;
    @FXML public MenuItem btnMenuAddAddS;
    @FXML public MenuItem btnMenuAddUpdateL;
    @FXML public MenuItem btnMenuAddReadmitA;
    @FXML public MenuItem btnMenuEditModA;
    @FXML public MenuItem btnMenuEditModS;
    @FXML public MenuItem btnMenuDisplayAdmis;
    @FXML public MenuItem btnMenuDisplayLog;
    @FXML public MenuItem btnMenuDisplayAR;
    @FXML public MenuItem btnMenuDisplayLogsA;
    @FXML public MenuItem btnMenuDisplayS;
    @FXML public Menu menuLogout;

    LoginManager loginManager;
    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) {
        this.loginManager = loginManager;

        comboLocation.getItems().addAll("Beachview Beach", "Bluewater Bay Beach", "Hobie Beach", "Humewood Beach", "King's Beach", "Maitland's Beach",
                "New Brighton Beach", "Pollock Beach", "Sardinia Bay Beach", "Schoenmakerskop Beach", "St Georges Strand", "Swartkops Beach", "Van Stadens Beach");
        comboLocation.getSelectionModel().select("Beachview Beach");

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        datePicker.setValue(LocalDate.now());

        this.scene = scene;
        this.staffUser = staffUser;

        try {
            queries.connectDB();
            menuController menu = new menuController(queries.connection, menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS, btnMenuAddReadmitA);
            menu.btnMenuAddReadmitA.setDisable(true);
            ResultSet rs = queries.getAnimalsToAdmit();
            while (rs.next()) {
                if (!comboTagNumber.getItems().contains(rs.getString("Tag_No")))
                    comboTagNumber.getItems().add(rs.getString("Tag_No"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnAdmit.setOnAction(actionEvent -> readmitAnimal());

        btnACancel.setOnAction(actionEvent -> showMainView());
    }

    private void readmitAnimal() {
        connection = queries.connection;
        String txtTag = comboTagNumber.getValue().toString();
        String txtLocation = comboLocation.getValue();
        String admissionNotes = txtNotes.getText();

        if (queries.admitAnimal(txtTag, txtLocation, staffUser.getStaffID(), admissionNotes)) {
            queries.modifyAnimalStatus(txtTag, "In center");
            try {
                queries.connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            Alert added = new Alert(Alert.AlertType.INFORMATION, "The animal has been readmitted.");
            added.showAndWait();
        } else {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The animal could not be readmitted.");
            added.showAndWait();
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
            queries.connection.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
