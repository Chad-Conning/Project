package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.*;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class viewAnimalsController {
    Database queries = new Database();
    Connection connection;

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
    @FXML public Menu menuLogout;
    @FXML public Button btnVARClose;
    @FXML public Button btnVARExport;
    @FXML public ListView listViewAnimalsReport;

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
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS);

            menu.btnMenuDisplayAR.setDisable(true);
            ResultSet rs = queries.getAnimalList();
            while (rs.next()) {
                //unsure about this - need to add the other fields
                listViewAnimalsReport.getItems().add(rs.getString("Tag_No"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        btnVARClose.setOnAction(actionEvent -> showMainView());

        //btnVARExport.setOnAction(actionEvent -> writeExcel());
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

//    public void writeExcel() throws Exception {
//        Writer writer = null;
//        try {
//            File file = new File("C:\\AnimalReport.csv.");
//            writer = new BufferedWriter(new FileWriter(file));
//            for (Animal animal : Tag_No) {
//
//                String text = Animal.getTagNo() + "," + Animal.getName() + "," + Animal.getAdult() + "," + Animal.getGender() + "," + Animal.getSpecies() + "\n";
//
//                writer.write(text);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        finally {
//
//            writer.flush();
//            writer.close();
//        }
//    }
}
