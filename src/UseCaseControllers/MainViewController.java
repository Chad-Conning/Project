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

    @FXML private Pane addStaffPane;
    @FXML private Pane modifyStaffPane;
    @FXML private Pane viewStaffPane;
    @FXML private Pane registerAnimalPane;
    @FXML private Pane admitAnimalPane;
    @FXML private Pane viewLogsPane;
    @FXML private Pane logbookPane;
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

                admitAnimalPane.setOpacity(0.4);
                admitAnimalPane.setDisable(true);
            } else if (staffUser.getStaffType().equals("Admission")) {
                viewLogsPane.setOpacity(0.4);
                viewLogsPane.setDisable(true);

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
    }

    private void showAddStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/addStaff.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            addStaffController controller =
                    loader.getController();   // gets the controller specified in the fxml

            controller.initSessionID(loginManager, scene, staffUser);
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

            controller.initSessionID(loginManager, scene, staffUser);
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

            controller.initSessionID(loginManager, scene, staffUser);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   /*

    public Stage createEditingStage(Stage owner, int CDID) {
        Stage stage = new Stage();
        Button btnOK;

        VBox root = new VBox();
        root.setSpacing(10);
        root.setFillWidth(true);
        root.setPadding(new Insets(5));

        if (CDID == 0) {
            TextField txtTitle = new TextField();
            txtTitle.setId("txtTitle");
            TextField txtYear = new TextField();
            txtYear.setId("txtYear");

            btnOK = new Button("Save");
            btnOK.setId("ok");
            btnOK.setMaxWidth(Double.MAX_VALUE);

            root.getChildren().addAll(
                    new Label("Title"), txtTitle,
                    new Label("Year"), txtYear,
                    btnOK
            );
            // set stage details
            stage.setTitle("Album details");
            btnOK.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful. The CD table has been updated.");
                alert.showAndWait();
                stage.hide();
            });
        }
        else {
            TextField txtCDID = new TextField();
            txtCDID.setId("txtCDID");
            TextField txtTrackNum = new TextField();
            txtTrackNum.setId("txtTrackNum");
            TextField txtName = new TextField();
            txtName.setId("txtName");
            TextField txtArtist = new TextField();
            txtArtist.setId("txtArtist");

            btnOK = new Button("Save");
            btnOK.setId("ok");
            btnOK.setMaxWidth(Double.MAX_VALUE);

            root.getChildren().addAll(
                    new Label("CDID"), txtCDID,
                    new Label("Track Number"), txtTrackNum,
                    new Label("Name"), txtName,
                    new Label("Artist"), txtArtist,
                    btnOK
            );
            // set stage details
            stage.setTitle("Song details");
            btnOK.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful. The Track table has been updated.");
                alert.showAndWait();
                stage.hide();
            });
        }

        // bit of a cheat, but really too simple to create a new controller

        stage.setScene(new Scene(root));

        // only has close button and cannot be resized
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);

        // rest of application cannot be interacted with until this stage is closed
        stage.initModality(Modality.APPLICATION_MODAL);

        // this stage belongs to another, i.e. will close if the owner does
        stage.initOwner(owner);

        return stage;
    }*/

}
