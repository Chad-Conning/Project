package UseCaseControllers;

import DataValidation.dataValidation;
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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class modifyStaffController {
    Database queries = new Database();
    Connection connection;

    @FXML private Button btnMSRegister;
    @FXML private Button btnMSCancel;
    @FXML private TextField tfieldMSName;
    @FXML private TextField tfieldMSSurname;
    @FXML private TextField tfieldMSPhone;
    @FXML private TextField tfieldMSEmail;
    @FXML private TextField tfieldMSTax;
    @FXML private ComboBox comboMSStaffType;
    @FXML private RadioButton radioEmployed;
    @FXML private RadioButton radioNotEmployed;
    @FXML private ComboBox comboSelectStaff;
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
    @FXML public Button btnSearch;
    @FXML public Menu menuLogout;

    //Validation
    @FXML private Label vLabelName;
    @FXML private Label vLabelSurname;
    @FXML private Label vLabelEmail;
    @FXML private Label vLabelPhone;
    @FXML private Label vLabelTax;

    Staff staffUser;
    Scene scene;

    LoginManager loginManager;
    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) {
        this.loginManager = loginManager;

        comboMSStaffType.getItems().addAll("Administrator", "Admission", "Handler");
        comboMSStaffType.getSelectionModel().select("Administrator");

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        this.staffUser = staffUser;
        this.scene = scene;

        try {
            queries.connectDB();
            menuController menu = new menuController(queries.connection, menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS);
            menu.btnMenuEditModS.setDisable(true);
            ResultSet rs = queries.getStaffList();
            while (rs.next()) {
                comboSelectStaff.getItems().add(rs.getString("Staff_ID"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnMSRegister.setOnAction(actionEvent -> {
            //if all validation checks pass then modify staff
            if(validationControl())
             modifyStaff();
        });

        btnMSCancel.setOnAction(actionEvent -> showMainView());

        comboSelectStaff.setOnAction(actionEvent -> populateFields(comboSelectStaff.getValue().toString()));

        btnSearch.setOnAction(actionEvent -> searchOnSurname(tfieldMSSurname.getText()));
    }

    private void searchOnSurname(String surname) {
        populateFields(surname);
    }

    private void populateFields(String staffVariable) {
        Staff temp;
        if (!tfieldMSSurname.getText().equals("")) {
            temp = queries.getStaffBySurname(staffVariable);
            if (temp == null) {
                return;
            }
        }
        else temp = queries.getStaff(staffVariable);

        comboSelectStaff.getSelectionModel().select(temp.getStaffID());
        tfieldMSName.setText(temp.getfName());
        tfieldMSSurname.setText(temp.getlName());
        tfieldMSPhone.setText(temp.getContactNum());
        tfieldMSEmail.setText(temp.getEmail());
        tfieldMSTax.setText(temp.getTaxNum());
        comboMSStaffType.getSelectionModel().select(temp.getStaffType());
        if (!temp.isBoolEmp())
            radioNotEmployed.setSelected(true);
    }

    private void modifyStaff() {

        connection = queries.connection;
        String txtfName = tfieldMSName.getText();
        String txtlName = tfieldMSSurname.getText();
        String txtContact = tfieldMSPhone.getText();
        String txtEmail = tfieldMSEmail.getText();
        String txtTaxNum = tfieldMSTax.getText();
        String txtStaffType = comboMSStaffType.getValue().toString();
        Boolean isEmployed;
        if (radioEmployed.isSelected())
            isEmployed = true;
        else isEmployed = false;

        //add employed

        if (queries.modifyStaff(comboSelectStaff.getValue().toString(), txtfName, txtlName, txtContact, txtEmail, txtTaxNum, txtStaffType, isEmployed)) {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The staff member with staff ID "+ comboSelectStaff.getValue().toString()+ " has been modified.");
            added.showAndWait();
        } else {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The staff member with staff ID "+ comboSelectStaff.getValue().toString()+ " could not be modified.");
            added.showAndWait();
        }
        try {
            queries.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        showMainView();
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

    //validation helper method
    private Boolean validationControl() {
        boolean name = dataValidation.checkValidation(tfieldMSName,vLabelName,1,"");
        boolean surname = dataValidation.checkValidation(tfieldMSSurname,vLabelSurname,1,"");
        boolean email = dataValidation.checkValidation(tfieldMSEmail,vLabelEmail,3,"");
        boolean phone = dataValidation.checkValidation(tfieldMSPhone,vLabelPhone,2, "10");
        boolean tax = dataValidation.checkValidation(tfieldMSTax,vLabelTax,2,"10");

        if (name && surname && email && phone && tax)
            return true;
        else return false;
    }

}
