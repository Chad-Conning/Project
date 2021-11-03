package UseCaseControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import sample.*;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//import static jdk.internal.net.http.common.Utils.close;

public class dailyAdmissionsController {
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

    @FXML private Button btnClose;
    @FXML private Button btnViewAll;
    @FXML private Button btnFilter;
    @FXML private Button btnExport;
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

    ObservableList<AnimalAdmission> tableData = FXCollections.observableArrayList();
    ObservableList<AnimalAdmission> excelData = FXCollections.observableArrayList();
    FilteredList<AnimalAdmission> adults = new FilteredList<>(tableData, p -> true);

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
            menu.btnMenuDisplayAdmis.setDisable(true);

            Tag_No.setCellValueFactory(cellData -> cellData.getValue().tagNoProperty());
            Animal_Name.setCellValueFactory(cellData -> cellData.getValue().animalNameProperty());
            Animal_Gender.setCellValueFactory(cellData -> cellData.getValue().animalGenderProperty());
            is_Adult.setCellValueFactory(cellData -> cellData.getValue().isAdultProperty());
            is_Adult.setCellFactory(column -> new CheckBoxTableCell<>());
            Animal_Species.setCellValueFactory(cellData -> cellData.getValue().animalSpeciesProperty());
            Location_Retrieved.setCellValueFactory(cellData -> cellData.getValue().locationRetrievedProperty());
            Admission_Date.setCellValueFactory(cellData -> cellData.getValue().admissionDateProperty());

            ResultSet rs = queries.getAnimalAdmissions();
            populateTableView(rs);

            /*DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd").toFormatter();
            dateFilter.setValue(LocalDate.parse(LocalDate.now().toString(), fmt));*/

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
            txtFieldSpeciesFilter.setText("");
            txtFieldLocationFilter.setText("");
            dateFilter.setValue(LocalDate.now());
        });

        btnFilter.setOnAction(actionEvent -> filterAdmissions());

        /////////////////
        changeData();

        checkAdultFilter.selectedProperty().addListener((observable, oldValue, newValue) -> {
            adults.setPredicate(animalAdmission -> {
                Boolean isAdult = newValue;
                if (animalAdmission.getIsAdult() == isAdult)
                    return true;
                else return false;
            });
            SortedList<AnimalAdmission> sortedData = new SortedList<>(adults);

            sortedData.comparatorProperty().bind(admissionsTable.comparatorProperty());

            admissionsTable.setItems(sortedData);
        });
        /////////////////
        btnExport.setOnAction(actionEvent -> {
            try {
                writeExcel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void changeData() {
        tableData = admissionsTable.getItems();
        adults = new FilteredList<>(tableData, p -> true);
    }

    private void filterAdmissions() {
        //Boolean isAdult = checkAdultFilter.isSelected();
        String species = txtFieldSpeciesFilter.getText();
        String location = txtFieldLocationFilter.getText();

        /*DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .toFormatter();
        LocalDate admissionDate = LocalDate.parse(dateFilter.getValue().toString(), fmt);*/
        ResultSet rs = queries.getFilteredAnimalList(species.toLowerCase(), location.toLowerCase(), dateFilter.getValue());
        populateTableView(rs);
    }

    private void populateTableView(ResultSet rs) {
        connection = queries.connection;
        ObservableList<AnimalAdmission> admissionData = populateList(rs);
        //admissionsTable.getItems().setAll(admissionData);
        admissionsTable.setItems(admissionData);
        changeData();
    }

    private ObservableList<AnimalAdmission> populateList(ResultSet rs) {
        ObservableList<AnimalAdmission> admissionData = FXCollections.observableArrayList();
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
                emp.setAdmissionDate(rs.getString("Admission_Date").substring(0, 10));
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

    public void writeExcel() throws Exception {
        excelData = admissionsTable.getItems();
        try (PrintWriter writer = new PrintWriter("C:\\Users\\user pc\\Documents\\GitHub\\Project\\out\\AdmissionsReport.csv.")) {
            StringBuilder sb = new StringBuilder();
            //String columns = "Tag No,Name,Gender,Adult,Species,Location Retrieved,Date,\n";
            //sb.append(columns);
            for (AnimalAdmission animal : excelData) {
                sb.append(animal.getTagNo());
                sb.append(',');
                sb.append(animal.getAnimalName());
                sb.append(',');
                sb.append(animal.getAnimalGender());
                sb.append(',');
                sb.append(animal.getIsAdult());
                sb.append(',');
                sb.append(animal.getAnimalSpecies());
                sb.append(',');
                sb.append(animal.getLocationRetrieved());
                sb.append(',');
                sb.append(animal.getAdmissionDate());
                sb.append("\n");
                /*String text = animal.getTagNo() + "," + animal.getAnimalName() + "," + animal.getAnimalGender() + "," + animal.getIsAdult() + ","
                        + animal.getAnimalSpecies() + animal.getLocationRetrieved() + animal.getAdmissionDate() + "\n";*/
            }
            writer.write(sb.toString());
            writer.close();
            System.out.println("File saved!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
