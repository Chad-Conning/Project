package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import sample.Database;
import sample.LoginManager;
import sample.Staff;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainViewController {
    Database queries = new Database();

    @FXML private Button btnLogOut;
    @FXML private ImageView imgAddStaff;
    @FXML private Label lblAddStaff;
    @FXML private ImageView imgRegAnimal;
    @FXML private Label lblRegAnimal;
    @FXML private ImageView imgModifyStaff;
    @FXML private Label lblModifyStaff;
    @FXML private ImageView imgModifyAnimalStatus;
    @FXML private Label lblModifyAnimalStatus;
    @FXML private ImageView imgUpdateLogbook;
    @FXML private Label lblUpdateLogbook;
    @FXML private ImageView imgViewAnimalReports;
    @FXML private Label lblViewAnimalReports;
    @FXML private ImageView imgViewDailyAdmissions;
    @FXML private Label lblViewDailyAdmissions;
    @FXML private ImageView imgViewStaffReports;
    @FXML private Label lblViewStaffReports;
    @FXML private ImageView imgReAdmitAnimal;
    @FXML private Label lblReAdmitAnimal;
    @FXML private ImageView imgViewLogsPerAnimal;
    @FXML private Label lblViewLogsPerAnimal;
    @FXML private ImageView imgViewDailyLogs;
    @FXML private Label lblViewDailyLogs;

    @FXML private Pane addStaffPane;
    @FXML private Pane modifyStaffPane;
    @FXML private Pane viewStaffPane;
    @FXML private Pane registerAnimalPane;
    @FXML private Pane updateLogbookPane;
    @FXML private Pane logbookPane;
    @FXML private Pane readmitAnimalPane;
    @FXML private Label txtIntroHeading;
    @FXML private Label lblUserInformation;
    Staff staffUser;

    private Scene scene;

    LoginManager loginManager;
    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) {
        this.loginManager = loginManager;

        this.staffUser = staffUser;
        txtIntroHeading.setText("Welcome, " + staffUser.getfName() + "!");
        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        if (!staffUser.getStaffType().equals("Administrator")) {
            addStaffPane.setOpacity(0.4);
            addStaffPane.setDisable(true);

            modifyStaffPane.setOpacity(0.4);
            modifyStaffPane.setDisable(true);

            viewStaffPane.setOpacity(0.4);
            viewStaffPane.setDisable(true);

            if (staffUser.getStaffType().equals("Handler")) {
                registerAnimalPane.setOpacity(0.4);
                registerAnimalPane.setDisable(true);

                readmitAnimalPane.setOpacity(0.4);
                readmitAnimalPane.setDisable(true);

            } else if (staffUser.getStaffType().equals("Admission")) {
                updateLogbookPane.setOpacity(0.4);
                updateLogbookPane.setDisable(true);

                logbookPane.setOpacity(0.4);
                logbookPane.setDisable(true);
            }
        }

        this.scene = scene;
        try {
            queries.connectDB();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        btnLogOut.setOnAction(event -> loginManager.logout());

        imgAddStaff.setOnMouseClicked(actionEvent -> {
            showAddStaff();
        });
        lblAddStaff.setOnMouseClicked(actionEvent -> {
            showAddStaff();
        });

        imgRegAnimal.setOnMouseClicked(actionEvent -> {
            showRegAnimals();
        });
        lblRegAnimal.setOnMouseClicked(actionEvent -> {
            showRegAnimals();
        });

        imgModifyStaff.setOnMouseClicked(actionEvent -> {
            showModifyStaff();
        });
        lblModifyStaff.setOnMouseClicked(actionEvent -> {
            showModifyStaff();
        });

        imgModifyAnimalStatus.setOnMouseClicked(actionEvent -> {
            showUpdateAnimalStatus();
        });
        lblModifyAnimalStatus.setOnMouseClicked(actionEvent -> {
            showUpdateAnimalStatus();
        });

        imgUpdateLogbook.setOnMouseClicked(actionEvent -> {
            showUpdateLogbook();
        });
        lblUpdateLogbook.setOnMouseClicked(actionEvent -> {
            showUpdateLogbook();
        });

        imgViewAnimalReports.setOnMouseClicked(actionEvent -> {
            showViewAnimalsReport();
        });
        lblViewAnimalReports.setOnMouseClicked(actionEvent -> {
            showViewAnimalsReport();
        });

        imgViewDailyAdmissions.setOnMouseClicked(actionEvent -> {
            showViewAdmissionsReport();
        });
        lblViewDailyAdmissions.setOnMouseClicked(actionEvent -> {
            showViewAdmissionsReport();
        });

        imgViewStaffReports.setOnMouseClicked(actionEvent -> {
            showViewStaffReport();
        });
        lblViewStaffReports.setOnMouseClicked(actionEvent -> {
            showViewStaffReport();
        });

        imgReAdmitAnimal.setOnMouseClicked(actionEvent -> {
            showReadmitAnimals();
        });
        lblReAdmitAnimal.setOnMouseClicked(actionEvent -> {
            showReadmitAnimals();
        });

        imgViewDailyLogs.setOnMouseClicked(actionEvent -> {
            showViewDailyReports();
        });
        lblViewDailyLogs.setOnMouseClicked(actionEvent -> {
            showViewDailyReports();
        });

        imgViewLogsPerAnimal.setOnMouseClicked(actionEvent -> {
            showViewLogsPerAnimal();
        });
        lblViewLogsPerAnimal.setOnMouseClicked(actionEvent -> {
            showViewLogsPerAnimal();
        });

    }

    private void showViewLogsPerAnimal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/viewLogsPerAnimal.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            logsPerAnimalController controller =
                    loader.getController();   // gets the controller specified in the fxml
            queries.connection.close();
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
            queries.connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showAddStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/addStaff.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            addStaffController controller =
                    loader.getController();   // gets the controller specified in the fxml
            queries.connection.close();
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
            queries.connection.close();
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
            queries.connection.close();
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
            queries.connection.close();
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
            queries.connection.close();
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
            queries.connection.close();
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
            queries.connection.close();
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
            queries.connection.close();
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
            queries.connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
