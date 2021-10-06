package sample;

import UseCaseControllers.addStaffController;
import UseCaseControllers.modifyStaffController;
import UseCaseControllers.regAnimalController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class menuController {
    @FXML public MenuItem btnMenuAddRegisterA;
    @FXML public MenuItem btnMenuAddAddS;
    @FXML public MenuItem btnMenuAddUpdateL;
    @FXML public MenuItem btnMenuEditModA;
    @FXML public MenuItem btnMenuEditModS;
    @FXML public MenuItem btnMenuDisplayAdmis;
    @FXML public MenuItem btnMenuDisplayLog;
    @FXML public MenuItem btnMenuDisplayAR;
    @FXML public MenuItem btnMenuDisplayLogsA;
    @FXML public MenuItem btnMenuDisplayS;

    Scene scene;
    Staff staffUser;

    public menuController(Scene scene, Staff staffUser, MenuItem btnMenuAddRegisterA, MenuItem btnMenuAddAddS, MenuItem btnMenuAddUpdateL, MenuItem btnMenuEditModA, MenuItem btnMenuEditModS,
                          MenuItem btnMenuDisplayAdmis, MenuItem btnMenuDisplayLog, MenuItem btnMenuDisplayAR, MenuItem btnMenuDisplayLogsA, MenuItem btnMenuDisplayS) {
        this.scene = scene;
        this.staffUser = staffUser;

        this.btnMenuAddRegisterA = btnMenuAddRegisterA;
        this.btnMenuAddAddS = btnMenuAddAddS;
        this.btnMenuAddUpdateL = btnMenuAddUpdateL;
        this.btnMenuEditModA = btnMenuEditModA;
        this.btnMenuEditModS = btnMenuEditModS;
        this.btnMenuDisplayAdmis = btnMenuDisplayAdmis;
        this.btnMenuDisplayLog = btnMenuDisplayLog;
        this.btnMenuDisplayAR = btnMenuDisplayAR;
        this.btnMenuDisplayLogsA = btnMenuDisplayLogsA;
        this.btnMenuDisplayS = btnMenuDisplayS;

        btnMenuAddAddS.setOnAction(event -> showAddStaff());
        btnMenuAddRegisterA.setOnAction(event -> showRegAnimals());
        btnMenuEditModS.setOnAction(event -> showModifyStaff());

    }

    public menuController(MenuItem btnMenuAddRegisterA, MenuItem btnMenuAddAddS, MenuItem btnMenuAddUpdateL, MenuItem btnMenuEditModA, MenuItem btnMenuEditModS,
                          MenuItem btnMenuDisplayAdmis, MenuItem btnMenuDisplayLog, MenuItem btnMenuDisplayAR, MenuItem btnMenuDisplayLogsA, MenuItem btnMenuDisplayS) {
        this.btnMenuAddRegisterA = btnMenuAddRegisterA;
        this.btnMenuAddAddS = btnMenuAddAddS;
        this.btnMenuAddUpdateL = btnMenuAddUpdateL;
        this.btnMenuEditModA = btnMenuEditModA;
        this.btnMenuEditModS = btnMenuEditModS;
        this.btnMenuDisplayAdmis = btnMenuDisplayAdmis;
        this.btnMenuDisplayLog = btnMenuDisplayLog;
        this.btnMenuDisplayAR = btnMenuDisplayAR;
        this.btnMenuDisplayLogsA = btnMenuDisplayLogsA;
        this.btnMenuDisplayS = btnMenuDisplayS;

        btnMenuAddRegisterA.setOnAction(event -> showAlert());
        btnMenuAddAddS.setOnAction(event -> showAlert());
        btnMenuAddUpdateL.setOnAction(event -> showAlert());
        btnMenuEditModA.setOnAction(event -> showAlert());
        btnMenuEditModS.setOnAction(event -> showAlert());
        btnMenuDisplayAdmis.setOnAction(event -> showAlert());
        btnMenuDisplayLog.setOnAction(event -> showAlert());
        btnMenuDisplayAR.setOnAction(event -> showAlert());
        btnMenuDisplayLogsA.setOnAction(event -> showAlert());
        btnMenuDisplayS.setOnAction(event -> showAlert());
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please cancel the admission and then select a menu item.");
        alert.showAndWait();
    }

    private void showAddStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/addStaff.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            addStaffController controller =
                    loader.getController();   // gets the controller specified in the fxml

            controller.initSessionID(scene, staffUser);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showModifyStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/modifyStaff.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            modifyStaffController controller =
                    loader.getController();   // gets the controller specified in the fxml

            controller.initSessionID(scene, staffUser);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showRegAnimals() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/registerAnimal.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            regAnimalController controller =
                    loader.getController();   // gets the controller specified in the fxml

            controller.initSessionID(scene, staffUser);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
