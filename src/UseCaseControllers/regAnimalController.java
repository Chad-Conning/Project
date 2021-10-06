package UseCaseControllers;

import DataValidation.dataValidation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class regAnimalController {
    Database queries = new Database();
    Connection connection;
    Scene scene;
    Staff staffUser;
    String tagNo;

    @FXML private Button btnARegister;
    @FXML private Button btnACancel;
    @FXML private TextField tfieldName;
    @FXML private RadioButton toggleMale;
    @FXML private RadioButton toggleFemale;
    @FXML private RadioButton toggleYes;
    @FXML private RadioButton toggleNo;
    @FXML private ComboBox comboSpecies;
    @FXML private Label lblUserInformation;

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
    @FXML public Menu menuLogout;

    @FXML private Label vLabelName;

    LoginManager loginManager;
    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) {
        comboSpecies.getItems().addAll("Seal", "Penguin", "Turtle", "Seagull", "Unknown");
        comboSpecies.getSelectionModel().select("Seal");

        setUp(loginManager, scene, staffUser);
    }

    public void initOtherSession(final LoginManager loginManager, Scene scene, Staff staffUser, String name, String gender, Boolean isAdult, String species) {
        comboSpecies.getItems().addAll("Seal", "Penguin", "Turtle", "Seagull", "Unknown");
        comboSpecies.getSelectionModel().select(species);

        tfieldName.setText(name);
        if (gender.equals("Female"))
            toggleFemale.setSelected(true);
        if (!isAdult)
            toggleNo.setSelected(true);

        setUp(loginManager, scene, staffUser);
    }

    private Boolean validationControl() {
        boolean name = dataValidation.checkValidation(tfieldName,vLabelName,1,"");
        if (name)
            return true;
        else return false;
    }

    private void setUp(final LoginManager loginManager, Scene scene, Staff staffUser) {
        this.loginManager = loginManager;
        this.scene = scene;
        this.staffUser = staffUser;

        menuController menu = new menuController(menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS);

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        try {
            queries.connectDB();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnARegister.setOnAction(actionEvent -> {
            if (validationControl())
                regAnimal();
        });

        btnACancel.setOnAction(actionEvent -> showMainView());
    }

    private void regAnimal(){
        connection = queries.connection;
        String txtName = tfieldName.getText();
        String txtGender;
        if (toggleMale.isSelected())
            txtGender = "Male";
        else
            txtGender = "Female";
        Boolean isAdult;
        if (toggleYes.isSelected())
            isAdult = true;
        else
            isAdult = false;
        String txtSpecies = comboSpecies.getValue().toString();

        if (queries.regAnimal(txtName, isAdult, txtGender, "Alive", txtSpecies)) {
            tagNo = queries.getTagNo(txtName);
            Animal newAnimal = new Animal(tagNo, txtName, isAdult, txtGender, txtSpecies);

            try {
                queries.connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            showAdmitAnimals(newAnimal);

            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new animal has been registered.");
            added.showAndWait();
        } else {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new animal could not be registered.");
            added.showAndWait();
        }

    }

    private void showAdmitAnimals(Animal newAnimal) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/admitAnimal.fxml")   // load fxml
            );
            scene.setRoot(loader.load());
            admitAnimalController controller = loader.getController();

            controller.initSessionID(loginManager, scene, staffUser, newAnimal);
        } catch (IOException e) {
            e.printStackTrace();
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
