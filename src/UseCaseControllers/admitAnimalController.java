package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class admitAnimalController {
    Database queries = new Database();
    Connection connection;
    Scene scene;
    Staff staffUser;
    Animal newAnimal;

    @FXML private Button btnAdmit;
    @FXML private Button btnACancel;
    @FXML private TextField tfieldTag;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboLocation;
    @FXML private TextArea txtNotes;
    @FXML private Label lblUserInformation;

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

    LoginManager loginManager;
    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser, Animal newAnimal) {
        this.loginManager = loginManager;

        comboLocation.getItems().addAll("Beachview Beach", "Bluewater Bay Beach", "Hobie Beach", "Humewood Beach", "King's Beach", "Maitland's Beach",
                "New Brighton Beach", "Pollock Beach", "Sardinia Bay Beach", "Schoenmakerskop Beach", "St Georges Strand", "Swartkops Beach", "Van Stadens Beach", "Born on site");
        comboLocation.getSelectionModel().select("Born on site");

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        datePicker.setValue(LocalDate.now());

        this.scene = scene;
        this.staffUser = staffUser;
        this.newAnimal = newAnimal;

        menuController menu = new menuController(loginManager, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS, btnMenuAddReadmitA);

        //tfieldTag.getSelectionModel().select(newAnimal.getTagNo());
        tfieldTag.setText(newAnimal.getTagNo());

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
        String txtLocation = comboLocation.getValue();
        String admissionNotes = txtNotes.getText();

        if (queries.admitAnimal(txtTag, txtLocation, staffUser.getStaffID(), admissionNotes)) {
            try {
                queries.connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new animal has been admitted.");
            added.showAndWait();
        } else {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new animal could not be admitted.");
            added.showAndWait();
        }

        showMainView();
    }

    private void cancelReg() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancelling will deregister the animal. Would you like to cancel admission process?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            queries.deRegAnimal(newAnimal.getTagNo());
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/registerAnimal.fxml")   // load fxml
                );
                scene.setRoot(loader.load());   // create scene for mainView screen
                regAnimalController controller =
                        loader.getController();   // gets the controller specified in the fxml

                controller.initOtherSession(loginManager, scene, staffUser, newAnimal.getName(), newAnimal.getGender(), newAnimal.getAdult(), newAnimal.getSpecies());
            } catch (IOException ex) {
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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
