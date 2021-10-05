package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.Database;
import sample.LoginManager;
import sample.Staff;

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

    Staff staffUser;
    //ValidationSupport validationSupport = new ValidationSupport();
    Scene scene;

    public void initSessionID(Scene scene, Staff staffUser) {
        comboMSStaffType.getItems().addAll("Administrator", "Admission", "Handler");
        comboMSStaffType.getSelectionModel().select("Administrator");
        this.staffUser = staffUser;
        this.scene = scene;
        try {
            queries.connectDB();
            ResultSet rs = queries.getStaffList();
            while (rs.next()) {
                comboSelectStaff.getItems().add(rs.getString("Staff_ID"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
//        validationControl();

        btnMSRegister.setOnAction(actionEvent -> {
             modifyStaff();
        });

        btnMSCancel.setOnAction(actionEvent -> showMainView());

        comboSelectStaff.setOnAction(actionEvent -> populateFields(comboSelectStaff.getValue().toString()));
    }

    private void populateFields(String staffID) {
        Staff temp = queries.getStaff(staffID);
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
    /*private boolean validationControl(){
        boolean result = true;
        //validation using controlsfx https://jar-download.com/artifacts/org.controlsfx/controlsfx/11.1.0
        //https://www.tabnine.com/code/java/methods/org.controlsfx.validation.Validator/createRegexValidator
        Validator<String> noWhitespaceName
                = Validator.createRegexValidator("Field cannot contain whitespace", "\\[a-zA-Z]", Severity.ERROR);
        Validator<String> emptyName = Validator.createEmptyValidator("Field cannot be empty");
        validationSupport.registerValidator(tfieldMSName, Validator.combine(noWhitespaceName, emptyName));
        if (!validationSupport.isInvalid())
            result = false;
        // return false;

//        validationSupport.registerValidator(tfieldSurname, Validator.combine(noWhitespaceName, emptyName));
        Validator<String> noWhitespaceSurname
                = Validator.createRegexValidator("Field cannot contain whitespace", "\\[a-zA-Z]", Severity.ERROR);
        Validator<String> emptySurname = Validator.createEmptyValidator("Field cannot be empty");
        validationSupport.registerValidator(tfieldMSSurname, Validator.combine(noWhitespaceSurname, emptySurname));
        if (!validationSupport.isInvalid())
            result = false;

//        validationSupport.registerValidator(tfieldPhone, Validator.combine(noWhitespaceName, emptyName));
        Validator<String> noWhitespacePhone
                = Validator.createRegexValidator("Field cannot contain whitespace", "\\[a-zA-Z]", Severity.ERROR);
        Validator<String> emptyPhone = Validator.createEmptyValidator("Field cannot be empty");
        validationSupport.registerValidator(tfieldMSPhone, Validator.combine(noWhitespacePhone, emptyPhone));
        if (!validationSupport.isInvalid())
            result = false;

//        validationSupport.registerValidator(tfieldEmail, Validator.combine(noWhitespaceName, emptyName));
        Validator<String> noWhitespaceEmail
                = Validator.createRegexValidator("Field cannot contain whitespace", "\\[a-zA-Z]", Severity.ERROR);
        Validator<String> emptyEmail = Validator.createEmptyValidator("Field cannot be empty");
        validationSupport.registerValidator(tfieldMSEmail, Validator.combine(noWhitespaceEmail, emptyEmail));
        if (!validationSupport.isInvalid())
            result = false;

//        validationSupport.registerValidator(tfieldTax, Validator.combine(noWhitespaceName, emptyName));
        Validator<String> noWhitespaceTax
                = Validator.createRegexValidator("Field cannot contain whitespace", "\\[a-zA-Z]", Severity.ERROR);
        Validator<String> emptyTax = Validator.createEmptyValidator("Field cannot be empty");
        validationSupport.registerValidator(tfieldMSTax, Validator.combine(noWhitespaceTax, emptyTax));
        if (!validationSupport.isInvalid())
            result = false;

        return result;
    }*/
}
