package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import sample.Database;
import sample.LoginManager;
import sample.Staff;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.sql.*;

import static java.lang.Integer.parseInt;

public class LoginController {
    Database queries = new Database();

    @FXML private TextField user;
    @FXML private TextField password;
    @FXML private Button loginButton;
    @FXML private Label errorLbl;
    Staff staffUser;

    private static final String IDLE_BUTTON_STYLE = "-fx-border-color: #78c2ad; -fx-border-radius: 3; -fx-border-style: solid; -fx-border-width: 2; -fx-background-color: #78c2ad;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-border-color: #609b8a; -fx-border-radius: 3; -fx-border-style: solid; -fx-border-width: 2; -fx-background-color: #66a593;";

    public void initManager(final LoginManager loginManager) throws SQLException, ClassNotFoundException {
        errorLbl.setText("");
        queries.connectDB();

        loginButton.setOnAction(event -> {
            login(loginManager);
        });

        password.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER))
                login(loginManager);
        });

        loginButton.setStyle(IDLE_BUTTON_STYLE);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(HOVERED_BUTTON_STYLE));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(IDLE_BUTTON_STYLE));
    }

    private void login(final LoginManager loginManager) {
        try {
            if (authorize()) {
                loginManager.authenticated(staffUser);
            }
            else {    // handles incorrect credentials
                user.setText("");
                password.setText("");
                errorLbl.setText("Incorrect user credentials!");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Check authorization credentials.
     *
     * If accepted, return a sessionID for the authorized session
     * otherwise, return null.
     */
    private Boolean authorize() throws SQLException, ClassNotFoundException {
        ResultSet rs = queries.getStaffList();
        Staff emp;
        while (rs.next()) {
            emp = new Staff();
            emp.setStaffID(rs.getInt("Staff_ID"));
            emp.setStaffPassword(rs.getString("Password"));
            emp.setStaffType(rs.getString("Staff_Type"));
            emp.setfName(rs.getString("Staff_FName"));
            emp.setlName(rs.getString("Staff_LName"));
            emp.setBoolEmp(rs.getBoolean("is_Employed"));

            if (emp.getStaffID().equals(user.getText()) && emp.getStaffPassword().equals(password.getText())) {
                if (!emp.boolEmpProperty().get())
                    return false;
                staffUser = emp;

                System.out.println("Connected Successfully");
                queries.connection.close();
                return true;
            }
        }
        return false;
    }
}
