package sample;

import UseCaseControllers.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class menuController {
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

    LoginManager loginManager;

    Scene scene;
    Staff staffUser;

    public menuController(Connection connection, Menu menuLogout, final LoginManager loginManager, Scene scene, Staff staffUser, MenuItem btnMenuAddRegisterA, MenuItem btnMenuAddAddS, MenuItem btnMenuAddUpdateL, MenuItem btnMenuEditModA, MenuItem btnMenuEditModS,
                          MenuItem btnMenuDisplayAdmis, MenuItem btnMenuDisplayLog, MenuItem btnMenuDisplayAR, MenuItem btnMenuDisplayLogsA, MenuItem btnMenuDisplayS, MenuItem btnMenuAddReadmitA) {
        this.loginManager = loginManager;
        this.connection = connection;

        this.scene = scene;
        this.staffUser = staffUser;

        this.btnMenuAddRegisterA = btnMenuAddRegisterA;
        this.btnMenuAddAddS = btnMenuAddAddS;
        this.btnMenuAddUpdateL = btnMenuAddUpdateL;
        this.btnMenuAddReadmitA = btnMenuAddReadmitA;
        this.btnMenuEditModA = btnMenuEditModA;
        this.btnMenuEditModS = btnMenuEditModS;
        this.btnMenuDisplayAdmis = btnMenuDisplayAdmis;
        this.btnMenuDisplayLog = btnMenuDisplayLog;
        this.btnMenuDisplayAR = btnMenuDisplayAR;
        this.btnMenuDisplayLogsA = btnMenuDisplayLogsA;
        this.btnMenuDisplayS = btnMenuDisplayS;
        this.menuLogout = menuLogout;

        menuLogout.setOnAction(event -> logout());

        btnMenuEditModA.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES)
            {
                showUpdateAnimalStatus();
            }
        });
        btnMenuDisplayAdmis.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES)
            {
                showViewAdmissionsReport();
            }

        });
        btnMenuDisplayAR.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES)
            {
                showViewAnimalsReport();
            }

        });
        btnMenuDisplayLog.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES)
            {
                showViewDailyReports();
            }
        });
        btnMenuAddUpdateL.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES)
            {
                showUpdateLogbook();
            }
        });
        btnMenuDisplayLogsA.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES)
            {
                showViewLogsPerAnimal();
            }
        });

        if (staffUser.getStaffType().equals("Administrator")) {
            btnMenuAddAddS.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES)
                {
                    showAddStaff();
                }

            });
            btnMenuEditModS.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES)
                {
                    showModifyStaff();
                }

            });
            btnMenuAddRegisterA.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES)
                {
                    showRegAnimals();
                }
            });
            btnMenuDisplayS.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES)
                {
                    showViewStaffReport();
                }

            });
            btnMenuAddReadmitA.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES)
                {
                    showReadmitAnimals();
                }
            });
        } else if (staffUser.getStaffType().equals("Handler")) {
            btnMenuAddRegisterA.setDisable(true);
            btnMenuAddAddS.setDisable(true);
            btnMenuEditModS.setDisable(true);
            btnMenuDisplayS.setDisable(true);
        } else if (staffUser.getStaffType().equals("Admission")) {
            btnMenuAddRegisterA.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES)
                {
                    showRegAnimals();
                }
            });
            btnMenuAddReadmitA.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES)
                {
                    showReadmitAnimals();
                }
            });
            btnMenuAddAddS.setDisable(true);
            btnMenuEditModS.setDisable(true);
            btnMenuDisplayS.setDisable(true);
        }
    }

    public menuController(final LoginManager loginManager, MenuItem btnMenuAddRegisterA, MenuItem btnMenuAddAddS, MenuItem btnMenuAddUpdateL, MenuItem btnMenuEditModA, MenuItem btnMenuEditModS,
                          MenuItem btnMenuDisplayAdmis, MenuItem btnMenuDisplayLog, MenuItem btnMenuDisplayAR, MenuItem btnMenuDisplayLogsA, MenuItem btnMenuDisplayS, MenuItem btnMenuAddReadmitA) {
        /*try {
          //  connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/
        this.loginManager = loginManager;

        this.btnMenuAddRegisterA = btnMenuAddRegisterA;
        this.btnMenuAddAddS = btnMenuAddAddS;
        this.btnMenuAddUpdateL = btnMenuAddUpdateL;
        this.btnMenuAddReadmitA = btnMenuAddReadmitA;
        this.btnMenuEditModA = btnMenuEditModA;
        this.btnMenuEditModS = btnMenuEditModS;
        this.btnMenuDisplayAdmis = btnMenuDisplayAdmis;
        this.btnMenuDisplayLog = btnMenuDisplayLog;
        this.btnMenuDisplayAR = btnMenuDisplayAR;
        this.btnMenuDisplayLogsA = btnMenuDisplayLogsA;
        this.btnMenuDisplayS = btnMenuDisplayS;

        //menuLogout.setOnAction(event -> showLogoutAlert());

        btnMenuAddRegisterA.setOnAction(event -> showAlert());
        btnMenuAddAddS.setOnAction(event -> showAlert());
        btnMenuAddUpdateL.setOnAction(event -> showAlert());
        btnMenuAddReadmitA.setOnAction(event -> showAlert());
        btnMenuEditModA.setOnAction(event -> showAlert());
        btnMenuEditModS.setOnAction(event -> showAlert());
        btnMenuDisplayAdmis.setOnAction(event -> showAlert());
        btnMenuDisplayLog.setOnAction(event -> showAlert());
        btnMenuDisplayAR.setOnAction(event -> showAlert());
        btnMenuDisplayLogsA.setOnAction(event -> showAlert());
        btnMenuDisplayS.setOnAction(event -> showAlert());
    }

    private void logout() {
        try {
            connection.close();
            loginManager.logout();
            System.out.println("Disconnected successfully");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please cancel the admission and then select a menu item.");
        alert.showAndWait();
    }

   /* private void showLogoutAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please cancel the admission and then log out.");
        alert.showAndWait();
    }*/

    private void showAddStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/addStaff.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            addStaffController controller =
                    loader.getController();   // gets the controller specified in the fxml
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
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
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
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
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showReadmitAnimals() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/reAdmitAnimal.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            readmitAnimalController controller =
                    loader.getController();   // gets the controller specified in the fxml
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showUpdateAnimalStatus() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/modifyAnimalStatus.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            modifyAnimalStatusController controller =
                    loader.getController();   // gets the controller specified in the fxml
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showUpdateLogbook() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/updateLogbook.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            updateLogbookController controller =
                    loader.getController();   // gets the controller specified in the fxml
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showViewStaffReport() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/viewStaffReports.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            viewStaffController controller =
                    loader.getController();   // gets the controller specified in the fxml
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showViewAdmissionsReport() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/viewAdmissionsReport.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            dailyAdmissionsController controller =
                    loader.getController();   // gets the controller specified in the fxml
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showViewAnimalsReport() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/viewAnimalsReport.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            viewAnimalsController controller =
                    loader.getController();   // gets the controller specified in the fxml
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showViewLogsPerAnimal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/viewLogsPerAnimal.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            logsPerAnimalController controller =
                    loader.getController();   // gets the controller specified in the fxml
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showViewDailyReports() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/viewDailyReports.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            dailyReportsController controller =
                    loader.getController();   // gets the controller specified in the fxml
            connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
