package UseCaseControllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import sample.*;

import javax.swing.text.html.HTML;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class logsPerAnimalController {
    Database queries = new Database();
    Connection connection;

    @FXML private Label lblUserInformation;
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

    @FXML private ComboBox cmbxTagNo;

    @FXML private TableView<AnimalForAnimalLog> AnimalLogs;
    @FXML private TableColumn<AnimalForAnimalLog, String> colName;
    @FXML private TableColumn<AnimalForAnimalLog, String> colDate;
    @FXML private TableColumn<AnimalForAnimalLog, String> colCentre;
    @FXML private TableColumn<AnimalForAnimalLog, String> colCondition;
    @FXML private TableColumn<AnimalForAnimalLog, String> colFoodGiven;
    @FXML private TableColumn<AnimalForAnimalLog, String> colMedication;

    @FXML private Button btnNewLog;
    @FXML private Button btnExport;
    @FXML private Button btnClose;

    private HashMap<String, String> Animals = new HashMap<>();
    ObservableList<AnimalForAnimalLog> excelData = FXCollections.observableArrayList();

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
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS, btnMenuAddReadmitA);

            colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            colCentre.setCellValueFactory(cellData -> cellData.getValue().centreProperty());
            colCondition.setCellValueFactory(cellData -> cellData.getValue().conditionProperty());
            colFoodGiven.setCellValueFactory(cellData -> cellData.getValue().foodProperty());
            colMedication.setCellValueFactory(cellData -> cellData.getValue().medicationProperty());

            ResultSet TagNoList = queries.getAnimalList();

            while (TagNoList.next())
            {
                String TagNo = TagNoList.getString("Tag_No");
                String Name = TagNoList.getString("Animal_Name");
                String ListAdd = TagNo + " - " + Name;
                cmbxTagNo.getItems().add(ListAdd);
                Animals.put(ListAdd, TagNo);
            }

            btnClose.setOnAction(ActionEvent -> closeForm());
            btnNewLog.setOnAction(ActionEvent -> viewNewLog(Animals.get((String)cmbxTagNo.getValue())));
            btnExport.setOnAction(ActionEvent -> WriteToExcel());
            cmbxTagNo.setOnAction(ActionEvent -> {
                try {
                    AnimalTagChanged();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser, String tagNo) {
        this.loginManager = loginManager;
        this.staffUser = staffUser;

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        this.scene = scene;

        try {
            queries.connectDB();
            menuController menu = new menuController(queries.connection, menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS, btnMenuAddReadmitA);

            colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            colCentre.setCellValueFactory(cellData -> cellData.getValue().centreProperty());
            colCondition.setCellValueFactory(cellData -> cellData.getValue().conditionProperty());
            colFoodGiven.setCellValueFactory(cellData -> cellData.getValue().foodProperty());
            colMedication.setCellValueFactory(cellData -> cellData.getValue().medicationProperty());

            ResultSet TagNoList = queries.getAnimalList();

            while (TagNoList.next())
            {
                String TagNo = TagNoList.getString("Tag_No");
                String Name = TagNoList.getString("Animal_Name");
                String ListAdd = TagNo + " - " + Name;
                cmbxTagNo.getItems().add(ListAdd);
                Animals.put(ListAdd, TagNo);
            }

            Animal animal = queries.getAnimalByTag(tagNo);
            cmbxTagNo.getSelectionModel().select(tagNo + " - " + animal.getName());
            AnimalTagChanged();

            btnClose.setOnAction(ActionEvent -> closeForm());
            btnNewLog.setOnAction(ActionEvent -> viewNewLog(Animals.get((String)cmbxTagNo.getValue())));
            btnExport.setOnAction(ActionEvent -> WriteToExcel());
            cmbxTagNo.setOnAction(ActionEvent -> {
                try {
                    AnimalTagChanged();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void AnimalTagChanged() throws SQLException {
        String TagNo = Animals.get((String)cmbxTagNo.getValue()); //Gets the tagNo from the display for Select Statement
        ResultSet Anims = queries.getAnimalLogs(TagNo);

        ObservableList<AnimalForAnimalLog> Disp = populateList(Anims, TagNo);
        AnimalLogs.setItems(Disp);
    }

    private void WriteToExcel() {
        //Add export to excel
        excelData = AnimalLogs.getItems();
        try (PrintWriter writer = new PrintWriter("out/Reports/"+ Animals.get((String)cmbxTagNo.getValue()) +"Logs.csv.")) {
            StringBuilder sb = new StringBuilder();
            //String columns = "Tag No,Name,Gender,Adult,Species,Location Retrieved,Date,\n";
            //sb.append(columns);
            for (AnimalForAnimalLog animal : excelData) {
                sb.append(animal.getName());
                sb.append(',');
                sb.append(animal.getDate());
                sb.append(',');
                sb.append(animal.getCentre());
                sb.append(',');
                sb.append(animal.getCondition());
                sb.append(',');
                sb.append(animal.getFood());
                sb.append(',');
                sb.append(animal.getMedication());
                sb.append("\n");
                /*String text = animal.getTagNo() + "," + animal.getAnimalName() + "," + animal.getAnimalGender() + "," + animal.getIsAdult() + ","
                        + animal.getAnimalSpecies() + animal.getLocationRetrieved() + animal.getAdmissionDate() + "\n";*/
            }
            writer.write(sb.toString());
            writer.close();
            System.out.println("File saved!");
            Alert added = new Alert(Alert.AlertType.INFORMATION, "The file has been saved.");
            added.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<AnimalForAnimalLog> populateList(ResultSet Anims, String TagNo) throws SQLException {
        AnimalForAnimalLog toDisp = new AnimalForAnimalLog();
        ObservableList<AnimalForAnimalLog> ret = FXCollections.observableArrayList();

        while (Anims.next())
        {
            //There are still animals in the list
            String Name = queries.getAnimalByTag(TagNo).getName();
            toDisp.setName(Name);

            Date logDate = Anims.getDate("Log_Date");
            String date = logDate.toString();
            toDisp.setDate(date);

            String Centre = Anims.getString("Centre");
            toDisp.setCentre(Centre);

            String Cond = Anims.getString("Condition");
            toDisp.setCondition(Cond);

            //System.out.println(queries.getFoodByID(Integer.parseInt(Anims.getString("Food_Code"))));
            String FoodCode = (queries.getFoodByID(Integer.parseInt(Anims.getString("Food_Code"))));
            toDisp.setFood(FoodCode);

            String MedCode = (queries.getMedsByID(Integer.parseInt(Anims.getString("Medication_ID"))));
            toDisp.setMedication(MedCode);

            ret.add(toDisp);
        }

        return ret;
    }

    private void viewNewLog(String tagNo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/updateLogbook.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            updateLogbookController controller =
                    loader.getController();   // gets the controller specified in the fxml
            queries.connection.close();
            controller.initSessionID(loginManager, scene, staffUser, tagNo);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeForm() {
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
}

class AnimalForAnimalLog
{
    private final SimpleStringProperty Name = new SimpleStringProperty();
    private final SimpleStringProperty Date = new SimpleStringProperty();
    private final SimpleStringProperty Centre = new SimpleStringProperty();
    private final SimpleStringProperty Condition = new SimpleStringProperty();
    private final SimpleStringProperty Food = new SimpleStringProperty();
    private final SimpleStringProperty Medication = new SimpleStringProperty();

    public String getFood() {
        return Food.get();
    }

    public SimpleStringProperty foodProperty() {
        return Food;
    }

    public void setFood(String food) {
        this.Food.set(food);
    }

    public String getMedication() {
        return Medication.get();
    }

    public SimpleStringProperty medicationProperty() {
        return Medication;
    }

    public void setMedication(String medication) {
        this.Medication.set(medication);
    }

    public String getCondition() {
        return Condition.get();
    }

    public SimpleStringProperty conditionProperty() {
        return Condition;
    }

    public void setCondition(String condition) {
        this.Condition.set(condition);
    }

    private final SimpleStringProperty FoodGiven = new SimpleStringProperty();
    private final SimpleStringProperty MedicationGiven = new SimpleStringProperty();

    public String getName() {return Name.get();}

    public SimpleStringProperty nameProperty() {return Name;}

    public void setName(String name) {this.Name.set(name); }

    public String getDate() {
        return Date.get();
    }

    public SimpleStringProperty dateProperty() {
        return Date;
    }

    public void setDate(String date) {
        this.Date.set(date);
    }

    public String getCentre() {
        return Centre.get();
    }

    public SimpleStringProperty centreProperty() {
        return Centre;
    }

    public void setCentre(String centre) {
        this.Centre.set(centre);
    }

    public String getFoodGiven() {
        return FoodGiven.get();
    }

    public SimpleStringProperty foodGivenProperty() {
        return FoodGiven;
    }

    public void setFoodGiven(String foodGiven) {
        this.FoodGiven.set(foodGiven);
    }

    public String getMedicationGiven() {
        return MedicationGiven.get();
    }

    public SimpleStringProperty medicationGivenProperty() {
        return MedicationGiven;
    }

    public void setMedicationGiven(String medicationGiven) {
        this.MedicationGiven.set(medicationGiven);
    }
}
