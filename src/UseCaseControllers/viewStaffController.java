package UseCaseControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import sample.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class viewStaffController {
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
    @FXML private Button btnAddStaff;
    @FXML private Button btnSearch;
    @FXML private Button btnExport;
    @FXML private Button btnViewAll;
    @FXML private TextField tfieldFilter;
    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, String> Staff_ID;
    @FXML private TableColumn<Staff, String> Staff_FName;
    @FXML private TableColumn<Staff, String> Staff_LName;
    @FXML private TableColumn<Staff, String> Staff_ContactNum;
    @FXML private TableColumn<Staff, String> Staff_Email;
    @FXML private TableColumn<Staff, String> Staff_TaxNumber;
    @FXML private TableColumn<Staff, String> Staff_Type;
    @FXML private TableColumn<Staff, Boolean> isEmployed;
    Button editButton = new Button("Edit");

    ObservableList<Staff> excelData = FXCollections.observableArrayList();

    Staff staffUser;
    Scene scene;

    private static final String IDLE_BUTTON_STYLE = "-fx-border-color: #78c2ad; -fx-border-radius: 3; -fx-border-style: solid; -fx-border-width: 2; -fx-background-color: #78c2ad;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-border-color: #609b8a; -fx-border-radius: 3; -fx-border-style: solid; -fx-border-width: 2; -fx-background-color: #66a593;";

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
            menu.btnMenuDisplayS.setDisable(true);

            Staff_ID.setCellValueFactory(cellData -> cellData.getValue().staffIDProperty().asString());
            Staff_FName.setCellValueFactory(cellData -> cellData.getValue().fNameProperty());
            Staff_LName.setCellValueFactory(cellData -> cellData.getValue().lNameProperty());
            Staff_ContactNum.setCellValueFactory(cellData -> cellData.getValue().contactNumProperty());
            Staff_Email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
            Staff_TaxNumber.setCellValueFactory(cellData -> cellData.getValue().taxNumProperty());
            Staff_Type.setCellValueFactory(cellData -> cellData.getValue().staffTypeProperty());
            isEmployed.setCellValueFactory(cellData -> cellData.getValue().boolEmpProperty());
            isEmployed.setCellFactory(column -> new CheckBoxTableCell<>());
            addButtonToTable();

            ResultSet rs = queries.getStaffList();
            populateTableView(rs);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnClose.setOnAction(actionEvent -> showMainView());

        btnAddStaff.setOnAction(actionEvent -> showAddStaff());

        btnSearch.setOnAction(actionEvent -> displaySearch(tfieldFilter.getText()));
        tfieldFilter.setOnKeyPressed(actionEvent -> displaySearch(tfieldFilter.getText()));

        btnViewAll.setOnAction(actionEvent -> {
            ResultSet rs = queries.getStaffList();
            populateTableView(rs);
        });

        btnExport.setOnAction(actionEvent -> WriteToExcel());

        btnViewAll.setStyle(IDLE_BUTTON_STYLE);
        btnViewAll.setOnMouseEntered(e -> btnViewAll.setStyle(HOVERED_BUTTON_STYLE));
        btnViewAll.setOnMouseExited(e -> btnViewAll.setStyle(IDLE_BUTTON_STYLE));
        btnSearch.setStyle(IDLE_BUTTON_STYLE);
        btnSearch.setOnMouseEntered(e -> btnSearch.setStyle(HOVERED_BUTTON_STYLE));
        btnSearch.setOnMouseExited(e -> btnSearch.setStyle(IDLE_BUTTON_STYLE));
        btnAddStaff.setStyle(IDLE_BUTTON_STYLE);
        btnAddStaff.setOnMouseEntered(e -> btnAddStaff.setStyle(HOVERED_BUTTON_STYLE));
        btnAddStaff.setOnMouseExited(e -> btnAddStaff.setStyle(IDLE_BUTTON_STYLE));
        btnClose.setStyle(IDLE_BUTTON_STYLE);
        btnClose.setOnMouseEntered(e -> btnClose.setStyle(HOVERED_BUTTON_STYLE));
        btnClose.setOnMouseExited(e -> btnClose.setStyle(IDLE_BUTTON_STYLE));
        btnExport.setStyle(IDLE_BUTTON_STYLE);
        btnExport.setOnMouseEntered(e -> btnExport.setStyle(HOVERED_BUTTON_STYLE));
        btnExport.setOnMouseExited(e -> btnExport.setStyle(IDLE_BUTTON_STYLE));
    }

    private void WriteToExcel() {
        //Add export to excel
        excelData = staffTable.getItems();
        try (PrintWriter writer = new PrintWriter("out/Reports/StaffReports.csv.")) {
            StringBuilder sb = new StringBuilder();
            //String columns = "Tag No,Name,Gender,Adult,Species,Location Retrieved,Date,\n";
            //sb.append(columns);
            for (Staff staff : excelData) {
                sb.append(staff.getStaffID());
                sb.append(',');
                sb.append(staff.getfName());
                sb.append(',');
                sb.append(staff.getlName());
                sb.append(',');
                sb.append(staff.getEmail());
                sb.append(',');
                sb.append(staff.getTaxNum());
                sb.append(',');
                sb.append(staff.getStaffType());
                sb.append(',');
                sb.append(staff.isBoolEmp());
                sb.append("\n");
                /*String text = animal.getTagNo() + "," + animal.getAnimalName() + "," + animal.getAnimalGender() + "," + animal.getIsAdult() + ","
                        + animal.getAnimalSpecies() + animal.getLocationRetrieved() + animal.getAdmissionDate() + "\n";*/
            }
            writer.write(sb.toString());
            writer.close();
            System.out.println("File saved!");
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The file has been saved.");
            added.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addButtonToTable() {
        TableColumn<Staff, Void> colBtn = new TableColumn("Action");

        Callback<TableColumn<Staff, Void>, TableCell<Staff, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Staff, Void> call(final TableColumn<Staff, Void> param) {
                final TableCell<Staff, Void> cell = new TableCell<>() {

                    private final Button btnEdit = new Button("Edit");

                    {
                        btnEdit.setOnAction((ActionEvent event) -> {
                            showModifyStaff(getTableView().getItems().get(getIndex()).getStaffID());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnEdit);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);

        staffTable.getColumns().add(colBtn);
    }

    private void displaySearch(String searchString) {
        ResultSet rs = queries.getStaffList();
        List<Staff> staffData = populateList(rs);

        List<Staff> searchStaff = new ArrayList<>();
        for (int i = 0; i <= staffData.size() - 1; i++) {
            if (staffData.get(i).getStaffID().equals(searchString) || staffData.get(i).getfName().equalsIgnoreCase(searchString)
                    || staffData.get(i).getlName().equalsIgnoreCase(searchString))
                searchStaff.add(staffData.get(i));
        }
        staffTable.getItems().setAll(searchStaff);
    }

    private void populateTableView(ResultSet rs) {
        connection = queries.connection;
        List<Staff> staffData = populateList(rs);
        staffTable.getItems().setAll(staffData);
    }

    private List<Staff> populateList(ResultSet rs) {
        List<Staff> staffData = new ArrayList<>();
        try {
            Staff emp;
            while (rs.next()) {
                emp = new Staff();
                emp.setStaffID(rs.getInt("Staff_ID"));
                emp.setStaffPassword(rs.getString("Password"));
                emp.setStaffType(rs.getString("Staff_Type"));
                emp.setfName(rs.getString("Staff_FName"));
                emp.setlName(rs.getString("Staff_LName"));
                emp.setContactNum(rs.getString("Staff_ContactNum"));
                emp.setEmail(rs.getString("Staff_Email"));
                emp.setTaxNum(rs.getString("Staff_TaxNumber"));
                emp.setBoolEmp(rs.getBoolean("is_Employed"));
                staffData.add(emp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return staffData;
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

    private void showModifyStaff(String staffID) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/modifyStaff.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            modifyStaffController controller =
                    loader.getController();   // gets the controller specified in the fxml
            queries.connection.close();
            controller.initSessionID(loginManager, scene, staffUser, staffID);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
