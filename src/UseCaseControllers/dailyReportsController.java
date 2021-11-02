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

    @FXML private TableView<Animal> tblDailyReports;
    @FXML private TableColumn<Animal, String> colTagNo;
    @FXML private TableColumn<Animal, String> colName;
    @FXML private TableColumn<Animal, String> colCentre;
    @FXML private TableColumn<Animal, String> colCondition;
    @FXML private TableColumn<Animal, String> colFoodGiven;
    @FXML private TableColumn<Animal, String> colMedication;
    @FXML private TableColumn<Animal, String> colAction;

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

        while (Animals.next())
        {

        }

    }
}
