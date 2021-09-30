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
            if (emp.getStaffID().equals(user.getText()) && emp.getStaffPassword().equals(password.getText())) {
                staffUser = emp;

                System.out.println("Connected Successfully");
                queries.connection.close();
                return true;
            }
        }
        return false;
    }
}
