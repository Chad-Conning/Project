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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dailyAdmissionsController {
    Database queries = new Database();
    Connection connection;

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

    @FXML private Button btnClose;
    @FXML private Button btnViewAll;
    @FXML private Button btnFilter;
    @FXML private CheckBox checkAdultFilter;
    @FXML private TextField txtFieldSpeciesFilter;
    @FXML private TextField txtFieldLocationFilter;
    @FXML private DatePicker dateFilter;
    @FXML private TableView<AnimalAdmission> admissionsTable;
    @FXML private TableColumn<AnimalAdmission, String> Tag_No;
    @FXML private TableColumn<AnimalAdmission, String> Animal_Name;
    @FXML private TableColumn<AnimalAdmission, String> Animal_Gender;
    @FXML private TableColumn<AnimalAdmission, Boolean> is_Adult;
    @FXML private TableColumn<AnimalAdmission, String> Animal_Species;
    @FXML private TableColumn<AnimalAdmission, String> Location_Retrieved;
    @FXML private TableColumn<AnimalAdmission, String> Admission_Date;

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
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS);
            menu.btnMenuDisplayAdmis.setDisable(true);

            Tag_No.setCellValueFactory(cellData -> cellData.getValue().tagNoProperty());
            Animal_Name.setCellValueFactory(cellData -> cellData.getValue().animalNameProperty());
            Animal_Gender.setCellValueFactory(cellData -> cellData.getValue().animalGenderProperty());
            is_Adult.setCellValueFactory(cellData -> cellData.getValue().isAdultProperty());
            Animal_Species.setCellValueFactory(cellData -> cellData.getValue().animalSpeciesProperty());
            Location_Retrieved.setCellValueFactory(cellData -> cellData.getValue().locationRetrievedProperty());
            Admission_Date.setCellValueFactory(cellData -> cellData.getValue().admissionDateProperty());

            ResultSet rs = queries.getAnimalAdmissions();
            populateTableView(rs);

            dateFilter.setValue(LocalDate.now());

            dateFilter.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
                }
            });

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnClose.setOnAction(actionEvent -> showMainView());

        btnViewAll.setOnAction(actionEvent -> {
            ResultSet rs = queries.getAnimalAdmissions();
            populateTableView(rs);
        });

        btnFilter.setOnAction(actionEvent -> filterAdmissions());
    }

    private void filterAdmissions() {
        Boolean isAdult = checkAdultFilter.isSelected();
        String species = txtFieldSpeciesFilter.getText();
        String location = txtFieldLocationFilter.getText();
        LocalDate admissionDate = dateFilter.getValue();
    }

    private void populateTableView(ResultSet rs) {
        connection = queries.connection;
        List<AnimalAdmission> admissionData = populateList(rs);
        admissionsTable.getItems().setAll(admissionData);
    }

    private List<AnimalAdmission> populateList(ResultSet rs) {
        List<AnimalAdmission> admissionData = new ArrayList<>();
        try {
            AnimalAdmission emp;
            while (rs.next()) {
                emp = new AnimalAdmission();
                emp.setTagNo(rs.getString("Tag_No"));
                emp.setAnimalName(rs.getString("Animal_Name"));
                emp.setAnimalGender(rs.getString("Animal_Gender"));
                emp.setIsAdult(rs.getBoolean("is_Adult"));
                emp.setAnimalSpecies(rs.getString("Animal_Species"));
                emp.setLocationRetrieved(rs.getString("Location_Retrieved"));
                emp.setAdmissionDate(rs.getString("Admission_Date"));
                admissionData.add(emp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return admissionData;
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
            queries.connection.close();
            controller.initSessionID(loginManager, this.scene, staffUser);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
