package sample;

import UseCaseControllers.LoginController;
import UseCaseControllers.MainViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Manages control flow for logins */
public class LoginManager {

    private final Scene scene;

    public LoginManager(Scene scene) {
        this.scene = scene;
    }

    /**
     * Callback method invoked to notify that a user has been authenticated.
     * Will show the main application screen.
     */
    public void authenticated(Staff staffUser) {
        showMainView(staffUser);
    }

    /**
     * Callback method invoked to notify that a user has logged out of the main application.
     * Will show the login application screen.
     */
    public void logout() {
        System.out.println("Disconnected successfully");
        showLoginScreen();

    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/loginUI.fxml")  // load fxml
            );
            scene.setRoot(loader.load());   // create scene for login fxml
            LoginController controller =
                    loader.getController();   // gets the controller specified in the fxml

            controller.initManager(this);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showMainView(Staff staffUser) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/LandingPage.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            MainViewController controller =
                    loader.getController();   // gets the controller specified in the fxml

            controller.initSessionID(this, this.scene, staffUser);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
