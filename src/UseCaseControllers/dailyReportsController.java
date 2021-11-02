package UseCaseControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dailyReportsController {
    Database queries = new Database();
    Connection connection;

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
    @FXML public Menu menuLogout;

    @FXML private DatePicker dtpDailyLogReportDate;

    @FXML private TableView<AnimalForDailyLogs> tblDailyReports;
    @FXML private TableColumn<AnimalForDailyLogs, String> colTagNo;
    @FXML private TableColumn<AnimalForDailyLogs, String> colName;
    @FXML private TableColumn<AnimalForDailyLogs, String> colCentre;
    @FXML private TableColumn<AnimalForDailyLogs, String> colCondition;
    @FXML private TableColumn<AnimalForDailyLogs, String> colFoodGiven;
    @FXML private TableColumn<AnimalForDailyLogs, String> colMedication;

    @FXML private Button btnNewLog;
    @FXML private Button btnExport;
    @FXML private Button btnClose;

    Staff staffUser;
    Scene scene;

    LoginManager loginManager;
    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) {
        this.loginManager = loginManager;
        this.staffUser = staffUser;

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        this.scene = scene;

        try {
            queries.connectDB();
            menuController menu = new menuController(queries.connection, menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS, btnMenuAddReadmitA);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        dtpDailyLogReportDate.setValue(LocalDate.now());
        dtpDailyLogReportDate.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
            }
        });

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

    public void DateChanged(ActionEvent actionEvent) throws SQLException {
        //When date is changed (selected)
        LocalDate toFind = dtpDailyLogReportDate.getValue();
        Date ToFind = Date.valueOf(toFind);

        //Call the select statement
        ResultSet Animals = queries.getLogsPerDate(ToFind);

        List<AnimalForDailyLogs> AnimalsLogs = new ArrayList<>();
        while (Animals.next())
        {
            //While there are still animals in the resultant set
            String AnimalTagNo = Animals.getString("Tag_No");

            Animal temp = queries.getAnimalByTag(AnimalTagNo);
            String AnimalName = temp.getName();

            String Centre = Animals.getString("Centre");

            String Condition = Animals.getString("Condition");

            int FoodGiven = Animals.getInt("Food_Code");
            String FoodDesc = queries.getFoodByID(FoodGiven);

            int MedID = Animals.getInt("Medication_ID");
            String MedDesc = queries.getMedsByID(MedID);

            AnimalForDailyLogs Temp = new AnimalForDailyLogs(AnimalTagNo, AnimalName, Centre, Condition, FoodDesc, MedDesc);
            AnimalsLogs.add(Temp);
        }

        for (int i = 0; i < AnimalsLogs.size(); i++)
        {
            tblDailyReports.getItems().add(AnimalsLogs.get(i));
        }

    }
}

class AnimalForDailyLogs{
    String TagNo, Name, Centre, Condition, Food, Medication;

    public AnimalForDailyLogs(String tagNo, String name, String centre, String condition, String food, String medication) {
        TagNo = tagNo;
        Name = name;
        Centre = centre;
        Condition = condition;
        Food = food;
        Medication = medication;
    }
}
