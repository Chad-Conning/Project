package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import net.ucanaccess.jdbc.DeleteResultSet;
import sample.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class updateLogbookController {
    @FXML private DatePicker dateLog;
    @FXML private ComboBox cbxTagNo;
    @FXML private RadioButton rbtICU;
    @FXML private RadioButton rbtRehab;
    @FXML private ComboBox cbxCondition;
    @FXML private ComboBox cbxFood;
    @FXML private ComboBox cbxMedication;
    @FXML private Button btnUpdateLogbookSave;
    @FXML private Button btnUpdateLogbookCancel;
    @FXML private Label lblUserInformation;
    @FXML private TextField tfieldSpecies;

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

    private HashMap FoodGiven = new HashMap();
    private HashMap Medications = new HashMap();

    Database queries = new Database();
    Connection connection;

    LoginManager loginManager;

    Staff staffUser;
    Scene scene;

    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) {
        this.loginManager = loginManager;
        this.staffUser = staffUser;
        this.scene = scene;

        dateLog.setValue(LocalDate.now());
        int foodCount = 0;
        int MedCount = 0;

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        cbxCondition.getItems().addAll("Recovered", "Stable", "Critical");
        try {
            queries.connectDB();
            menuController menu = new menuController(queries.connection, menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS);

            dateLog.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
                }
            });

            menu.btnMenuAddUpdateL.setDisable(true);
            ResultSet TagNoList = queries.getAliveAnimals();
            ResultSet FoodDesc = queries.getFoodList();
            ResultSet MedsDesc = queries.getMedsList();

            while (TagNoList.next()) {
                cbxTagNo.getItems().add(TagNoList.getString("Tag_No"));
            }

            while (FoodDesc.next()) {
                cbxFood.getItems().add(FoodDesc.getString("Food_Description"));
                foodCount++;
                FoodGiven.put(FoodDesc.getString("Food_Description"), foodCount);
            }

            while (MedsDesc.next()) {
                cbxMedication.getItems().add(MedsDesc.getString("Med_Description"));
                MedCount++;
                Medications.put(MedsDesc.getString("Med_Description"), MedCount);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        cbxTagNo.setOnAction(actionEvent -> populateFields(cbxTagNo.getValue().toString()));

        btnUpdateLogbookSave.setOnAction(actionEvent -> AddLog());

        btnUpdateLogbookCancel.setOnAction(actionEvent -> CancelLog());

    }

    private void populateFields(String tagNo) {
        Animal temp = queries.getAnimalByTag(tagNo);
        if (temp == null)
            return;
        else {
            tfieldSpecies.setText(temp.getSpecies());
        }

    }

    public void AddLog() {
        connection = queries.connection;
        String TagNo = (String) cbxTagNo.getValue();
        String Centre;
        if (rbtRehab.isSelected()) {
            Centre = "Rehab";
        } else {
            Centre = "ICU";
        }
        String Condition = (String) cbxCondition.getValue();
        String Food = (String) cbxFood.getValue();
        String Medication = (String) cbxMedication.getValue();
        String StaffID = staffUser.getStaffID();

        LocalDate logDate = dateLog.getValue();


        if (queries.updateLogbook(logDate, TagNo, Centre, Condition, StaffID, getFoodCode(Food), getMedCode(Medication))) {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new log has been added.");
            added.showAndWait();
        } else {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new log could not be added");
            added.showAndWait();
        }

        try {
            queries.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        showMainView();
    }

    private void CancelLog()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel logging process?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES)
        {
            showMainView();
        }

    }

    private int getFoodCode(String FoodDesc)
    {
        return (int) FoodGiven.get(FoodDesc);
    }

    private int getMedCode(String MedDesc)
    {
        return (int) Medications.get(MedDesc);
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
