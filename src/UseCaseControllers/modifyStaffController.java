package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
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
    @FXML private ToggleGroup staffTypeToggle;

    @FXML private TextField tfieldPassword;

    Staff staffUser;
    ValidationSupport validationSupport = new ValidationSupport();
    Scene scene;

    public void initSessionID(Scene scene, Staff staffUser) {
        comboMSStaffType.getItems().addAll("Administrator", "Admission", "Handler");
        comboMSStaffType.getSelectionModel().select("Administrator");
        this.staffUser = staffUser;
        this.scene = scene;
        try {
            queries.connectDB();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
//        validationControl();

        btnMSRegister.setOnAction(actionEvent -> {
            Boolean result = validationControl();
            if (result)
                modifyStaff();
        });

        btnMSCancel.setOnAction(actionEvent -> showMainView());
    }

//    private void modifyStaff() {
//        //on action register
//
//        connection = queries.connection;
//        String txtPassword = tfieldPassword.getText();
//        String txtfName = tfieldMSName.getText();
//        String txtlName = tfieldMSSurname.getText();
//        String txtContact = tfieldMSPhone.getText();
//        String txtEmail = tfieldMSEmail.getText();
//        String txtTaxNum = tfieldMSTax.getText();
//        String txtStaffType = comboMSStaffType.getValue().toString();
//
//        if (queries.addStaff(txtPassword, txtfName, txtlName, txtContact, txtEmail, txtTaxNum, txtStaffType)) {
//            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new staff member has been added.");
//            added.showAndWait();
//        } else {
//            Alert added = new Alert(Alert.AlertType.INFORMATION, "The new staff member could not be added.");
//            added.showAndWait();
//        }
//        try {
//            queries.connection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        //showMainView();
//    }

    private void modifyStaff() {

        try
        {
            // create our mysql database connection
//            String myDriver = "org.gjt.mm.mysql.Driver";
//            String myUrl = "jdbc:mysql://localhost/test";
//            Class.forName(myDriver);
//            Connection conn = DriverManager.getConnection(myUrl, "root", "");
            connection = queries.connection;

            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT * FROM Staff";

            // create the java statement
            //Statement st = conn.createStatement();
            Statement st = connection.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next())
            {
                int id = rs.getInt("Staff_ID");
                //String Password = rs.getString("Password");
                String firstName = rs.getString("Staff_FName");
                String lastName = rs.getString("Staff_LName");
                String contact = rs.getString("Staff_ContactNum");
                String email = rs.getString("Staff_Email");
                String staffType = rs.getString("Staff_Type");
                boolean isEmployed = rs.getBoolean("is_Employed");


                // print the results
                System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, contact, email, staffType, isEmployed);
            }
            st.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
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

    //validation helper method
    private boolean validationControl(){
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
    }
}
