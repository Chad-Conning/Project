package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.Database;
import sample.LoginManager;
import sample.Staff;
import sample.menuController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class updateLogbookController {
    @FXML private DatePicker dateLog ;
    @FXML private ComboBox cbxTagNo;
    @FXML private RadioButton rbtICU;
    @FXML private RadioButton rbtRehab;
    @FXML private ComboBox cbxCondition;
    @FXML private ComboBox cbxFood;
    @FXML private ComboBox cbxMedication;
    @FXML private Button btnUpdateLogbookSave;
    @FXML private Button btnUpdateLogbookCancel;

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

    Database queries = new Database();
    Connection connection;

    LoginManager loginManager;

    Staff staffUser;
    Scene scene;

    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser)
    {
        this.loginManager = loginManager;
        this.staffUser = staffUser;
        this.scene = scene;

        dateLog.setValue(LocalDate.now());

        cbxCondition.getItems().addAll("Recovered", "Stable", "Critical");
        try {
            queries.connectDB();
            menuController menu = new menuController(queries.connection, menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS);
            menu.btnMenuEditModS.setDisable(true);
            ResultSet TagNoList = queries.getAnimalList();
            ResultSet FoodDesc = queries.getFoodList();
            ResultSet MedsDesc = queries.getMedsList();

            while (TagNoList.next()) {
                cbxTagNo.getItems().add(TagNoList.getString("Tag_No"));
            }

            while (FoodDesc.next()){
                cbxFood.getItems().add(FoodDesc.getString("Food_Description"));
            }

            while (MedsDesc.next())
            {
                cbxMedication.getItems().add(FoodDesc.getString("Med_Description"));
            }
            }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnUpdateLogbookSave.setOnAction(
                actionEvent -> AddLog());
        btnUpdateLogbookCancel.setOnAction(actionEvent -> showMainView());
    }

    public void AddLog()
    {
        connection = queries.connection;

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
