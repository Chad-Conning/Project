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
import java.util.logging.Level;
import java.util.logging.Logger;

public class modifyAnimalStatusController {
    Database queries = new Database();
    Connection connection;

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

    @FXML private ComboBox selectTag;
    @FXML private RadioButton toggleInCentre;
    @FXML private RadioButton toggleReleased;
    @FXML private RadioButton toggleDeceased;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Label lblUserInformation;
    @FXML private TextField tfieldName;

    Staff staffUser;
    Scene scene;
    String status;

    LoginManager loginManager;
    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) {
        this.loginManager = loginManager;
        this.staffUser = staffUser;
        this.scene = scene;

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        try {
            queries.connectDB();
            menuController menu = new menuController(queries.connection, menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS, btnMenuAddReadmitA);
            menu.btnMenuEditModA.setDisable(true);
            ResultSet rs = queries.getAliveAnimals();
            while (rs.next()) {
                selectTag.getItems().add(rs.getString("Tag_No"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        selectTag.setOnAction(actionEvent -> populateFields(selectTag.getValue().toString()));

        btnSave.setOnAction(actionEvent -> updateStatus());

        btnCancel.setOnAction(actionEvent -> showMainView());
    }

    private void populateFields(String tagNo) {
        Animal temp = queries.getAnimalByTag(tagNo);
        if (temp == null)
            return;
        else {
            tfieldName.setText(temp.getName());
            status = temp.getStatus();
            toggleInCentre.setSelected(true);
        }
    }

    private void updateStatus() {
        connection = queries.connection;
        String tagNo = selectTag.getValue().toString();

        if (toggleReleased.isSelected())
            status = "Released";
        else if (toggleDeceased.isSelected())
            status = "Deceased";

        if (queries.modifyAnimalStatus(tagNo, status)) {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The animal with Tag_No "+ selectTag.getValue().toString()+ " has been modified.");
            added.showAndWait();
        } else {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The staff member with staff ID "+ selectTag.getValue().toString()+ " could not be modified.");
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
            queries.connection.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
