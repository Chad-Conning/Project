package UseCaseControllers;

import DataValidation.dataValidation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.Database;
import sample.LoginManager;
import sample.Staff;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    @FXML private Label lblUserInformation;

    @FXML private Label vLabelName;
    @FXML private Label vLabelSurname;
    @FXML private Label vLabelEmail;
    @FXML private Label vLabelPhone;
    @FXML private Label vLabelTax;
    @FXML private Label vLabelPassword;
    Staff staffUser;
    Scene scene;

    public void initSessionID(Scene scene, Staff staffUser) {
        comboStaffType.getItems().addAll("Administrator", "Admission", "Handler");
        comboStaffType.getSelectionModel().select("Admission");
        this.staffUser = staffUser;

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        this.scene = scene;
        try {
            queries.connectDB();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnSRegister.setOnAction(actionEvent -> {
            //validation from https://www.youtube.com/watch?v=8OJMLeXyC4M
            if (validationControl())
                addStaff();
        });

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
            controller.initSessionID(loginManager, this.scene, staffUser);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //validation helper method
    private Boolean validationControl() {
        boolean name = dataValidation.checkValidation(tfieldName,vLabelName,1,"");
        boolean surname = dataValidation.checkValidation(tfieldSurname,vLabelSurname,1,"");
        boolean email = dataValidation.checkValidation(tfieldEmail,vLabelEmail,3,"");
        boolean phone = dataValidation.checkValidation(tfieldPhone,vLabelPhone,2, "10");
        boolean tax = dataValidation.checkValidation(tfieldTax,vLabelTax,2,"10");
        boolean password = dataValidation.checkValidation(tfieldPassword,vLabelPassword,4,"");

        if (name && surname && email && phone && tax && password)
            return true;
        else return false;
    }

}
