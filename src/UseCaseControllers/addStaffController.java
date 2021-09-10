package UseCaseControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import sample.Database;
import sample.LoginManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

public class addStaffController {
    Database queries = new Database();
    Connection connection;

    @FXML private Button btnSRegister;
    @FXML private Button btnSCancel;
    @FXML private TextField tfieldName;
    @FXML private TextField tfieldSurname;
    @FXML private TextField tfieldPhone;
    @FXML private TextField tfieldEmail;
    @FXML private TextField tfieldTax;
    @FXML private TextField tfieldPassword;
    @FXML private ComboBox comboStaffType;
    String staffRole;
    ValidationSupport validationSupport = new ValidationSupport();
    Scene scene;

    public void initSessionID(Scene scene, String staffRole) {
        comboStaffType.getItems().addAll("Administrator", "Admission", "Handler");
        comboStaffType.getSelectionModel().select("Admission");
        this.staffRole = staffRole;
        this.scene = scene;
        try {
            queries.connectDB();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        validationControl();

        btnSRegister.setOnAction(actionEvent -> addStaff());

        btnSCancel.setOnAction(actionEvent -> showMainView());
    }

    private void addStaff() {
        //on action register

        connection = queries.connection;
        String txtPassword = tfieldPassword.getText();
        String txtfName = tfieldName.getText();
        String txtlName = tfieldSurname.getText();
        String txtContact = tfieldPhone.getText();
        String txtEmail = tfieldEmail.getText();
        String txtTaxNum = tfieldTax.getText();
        String txtStaffType = comboStaffType.getValue().toString();

        if (queries.addStaff(txtPassword, txtfName, txtlName, txtContact, txtEmail, txtTaxNum, txtStaffType)) {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new staff member has been added.");
            added.showAndWait();
        } else {
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new staff member could not be added.");
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
            controller.initSessionID(loginManager, this.scene, staffRole);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //validation helper method
    private void validationControl(){
        //validation using controlsfx https://jar-download.com/artifacts/org.controlsfx/controlsfx/11.1.0
        //https://www.tabnine.com/code/java/methods/org.controlsfx.validation.Validator/createRegexValidator
        //validationSupport.setErrorDecorationEnabled(false);
        //validate for empty, validate for correct type(regex) to be implemented
        //Pattern stringPattern = Pattern.compile("[a-zA-Z]");
//        String regex = "[a-zA-Z]";
//        Pattern stringRegex = Pattern.compile(regex);
        //validationSupport.registerValidator(tfieldName, Validator.createEmptyValidator("A name is required")); //if field is blank throws validation error
        //validationSupport.registerValidator(tfieldName, Validator.createRegexValidator());
//        isValidName(tfieldName.getText());
//        validationSupport.registerValidator(tfieldSurname, Validator.createEmptyValidator("A surname is required"));
//        validationSupport.registerValidator(tfieldPhone, Validator.createEmptyValidator("A phone number is required"));
//        validationSupport.registerValidator(tfieldEmail, Validator.createEmptyValidator("An email is required"));
//        validationSupport.registerValidator(tfieldTax, Validator.createEmptyValidator("A tax number is required"));

        Validator<String> noWhitespaceName
                = Validator.createRegexValidator("Field cannot contain whitespace", "\\[a-zA-Z]", Severity.ERROR);
        Validator<String> emptyName = Validator.createEmptyValidator("Field cannot be empty");
        validationSupport.registerValidator(tfieldName, Validator.combine(noWhitespaceName, emptyName));
        validationSupport.registerValidator(tfieldSurname, Validator.combine(noWhitespaceName, emptyName));
        validationSupport.registerValidator(tfieldPhone, Validator.combine(noWhitespaceName, emptyName));
        validationSupport.registerValidator(tfieldEmail, Validator.combine(noWhitespaceName, emptyName));
        validationSupport.registerValidator(tfieldTax, Validator.combine(noWhitespaceName, emptyName));

    }


    //string validation
//    public static boolean isValidName(String name){
//        // Regex to check valid username.
//        String regex = "[A-Za-z]";
//
//        // Compile the ReGex
//        Pattern p = Pattern.compile(regex);
//
//        // If the username is empty
//        // return false
//        if (name == null) {
//            return false;
//        }
//
//        // Pattern class contains matcher() method
//        // to find matching between given username
//        // and regular expression.
//        Matcher m = p.matcher(name);
//
//        // Return if the username
//        // matched the ReGex
//        return m.matches();
//    }

}
