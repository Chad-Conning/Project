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

    //validation
    @FXML private Label vLabelName;
    @FXML private Label vLabelSurname;
    @FXML private Label vLabelEmail;
    @FXML private Label vLabelPhone;
    @FXML private Label vLabelTax;
    @FXML private Label vLabelPassword;

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

    Staff staffUser;
    Scene scene;

    private static final String IDLE_BUTTON_STYLE = "-fx-border-color: #78c2ad; -fx-border-radius: 3; -fx-border-style: solid; -fx-border-width: 2; -fx-background-color: #78c2ad;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-border-color: #609b8a; -fx-border-radius: 3; -fx-border-style: solid; -fx-border-width: 2; -fx-background-color: #66a593;";

    LoginManager loginManager;
    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) {
        this.loginManager = loginManager;

        comboStaffType.getItems().addAll("Administrator", "Admission", "Handler");
        comboStaffType.getSelectionModel().select("Admission");
        this.staffUser = staffUser;

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        this.scene = scene;

        try {
            queries.connectDB();
            menuController menu = new menuController(queries.connection, menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS, btnMenuAddReadmitA);
            menu.btnMenuAddAddS.setDisable(true);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnSRegister.setOnAction(actionEvent -> {
            //validation from https://www.youtube.com/watch?v=8OJMLeXyC4M
            if (validationControl())
                addStaff();
        });

        btnSCancel.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to cancel this process?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES)
            {
                showMainView();
            }
        });

        btnSRegister.setStyle(IDLE_BUTTON_STYLE);
        btnSRegister.setOnMouseEntered(e -> btnSRegister.setStyle(HOVERED_BUTTON_STYLE));
        btnSRegister.setOnMouseExited(e -> btnSRegister.setStyle(IDLE_BUTTON_STYLE));
        btnSCancel.setStyle(IDLE_BUTTON_STYLE);
        btnSCancel.setOnMouseEntered(e -> btnSCancel.setStyle(HOVERED_BUTTON_STYLE));
        btnSCancel.setOnMouseExited(e -> btnSCancel.setStyle(IDLE_BUTTON_STYLE));
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
            Staff temp = queries.getStaffBySurname(txtlName);

            Alert added = new Alert(Alert.AlertType.INFORMATION, "Staff member with Staff_ID " + temp.getStaffID() + " has been added.");
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
            queries.connection.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //validation helper method
    private Boolean validationControl() {
        boolean name = dataValidation.checkValidation(tfieldName,vLabelName,1,"");
        boolean surname = dataValidation.checkValidation(tfieldSurname,vLabelSurname,1,"");
        boolean email = dataValidation.checkValidation(tfieldEmail,vLabelEmail,3,"");
        boolean phone = dataValidation.checkNumberValidity(tfieldPhone,vLabelPhone, "10");
        boolean tax = dataValidation.checkNumberValidity(tfieldTax, vLabelTax, "10");
        boolean password = dataValidation.checkValidation(tfieldPassword,vLabelPassword,4,"");

        if (name && surname && email && phone && tax && password)
            return true;
        else return false;
    }

}
