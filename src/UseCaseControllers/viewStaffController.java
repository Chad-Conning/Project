package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Database;
import sample.LoginManager;
import sample.Staff;
import sample.menuController;

import java.io.IOException;
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

            ResultSet rs = queries.getStaffList();
            populateTableView(rs);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnClose.setOnAction(actionEvent -> showMainView());

        btnAddStaff.setOnAction(actionEvent -> showAddStaff());

        btnSearch.setOnAction(actionEvent -> displaySearch(tfieldFilter.getText()));

        btnViewAll.setOnAction(actionEvent -> {
            ResultSet rs = queries.getStaffList();
            populateTableView(rs);
        });
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
}
