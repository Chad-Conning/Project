package UseCaseControllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import sample.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dailyReportsController {
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

    @FXML private DatePicker dtpDailyLogReportDate;

    @FXML private TableView<AnimalForDailyLogs> tblDailyLogs;
    @FXML private TableColumn<AnimalForDailyLogs, String> colTagNo;
    @FXML private TableColumn<AnimalForDailyLogs, String> colName;
    @FXML private TableColumn<AnimalForDailyLogs, String> colCentre;
    @FXML private TableColumn<AnimalForDailyLogs, String> colCondition;
    @FXML private TableColumn<AnimalForDailyLogs, String> colFoodGiven;
    @FXML private TableColumn<AnimalForDailyLogs, String> colMedication;

    @FXML private Button btnNewLog;
    @FXML private Button btnExport;
    @FXML private Button btnClose;

    ObservableList<AnimalForDailyLogs> excelData = FXCollections.observableArrayList();

    Staff staffUser;
    Scene scene;

    private static final String IDLE_BUTTON_STYLE = "-fx-border-color: #78c2ad; -fx-border-radius: 3; -fx-border-style: solid; -fx-border-width: 2; -fx-background-color: #78c2ad;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-border-color: #609b8a; -fx-border-radius: 3; -fx-border-style: solid; -fx-border-width: 2; -fx-background-color: #66a593;";

    LoginManager loginManager;
    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) throws SQLException {
        this.loginManager = loginManager;
        this.staffUser = staffUser;

        lblUserInformation.setText("Logged in Staff ID: " + staffUser.getStaffID() + ", " + staffUser.getfName() + " " + staffUser.getlName());

        this.scene = scene;

        try {
            queries.connectDB();

            dtpDailyLogReportDate.setValue(LocalDate.now());

            dtpDailyLogReportDate.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
                }
            });

            menuController menu = new menuController(queries.connection, menuLogout, loginManager, scene, staffUser, btnMenuAddRegisterA, btnMenuAddAddS, btnMenuAddUpdateL, btnMenuEditModA, btnMenuEditModS,
                    btnMenuDisplayAdmis, btnMenuDisplayLog, btnMenuDisplayAR, btnMenuDisplayLogsA, btnMenuDisplayS, btnMenuAddReadmitA);

            colTagNo.setCellValueFactory(cellData -> cellData.getValue().tagNoProperty());
            colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            colCentre.setCellValueFactory(cellData -> cellData.getValue().centreProperty());
            colCondition.setCellValueFactory(cellData -> cellData.getValue().conditionProperty());
            colFoodGiven.setCellValueFactory(cellData -> cellData.getValue().foodProperty());
            colMedication.setCellValueFactory(cellData -> cellData.getValue().medicationProperty());
            addButtonToTable();
            //colTagNo.setCellValueFactory(cellData -> cellData.getValue().tagNoProperty());

            LocalDate date = dtpDailyLogReportDate.getValue();
            ResultSet rs = queries.getLogsPerDate(date);
            doOnDateChange(rs);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnClose.setOnAction(ActionEvent -> showMainView());
        btnExport.setOnAction(ActionEvent -> ExportDailyLogs());
        btnNewLog.setOnAction(ActionEvent -> NewLog());

        dtpDailyLogReportDate.setOnAction(ActionEvent -> {
            try {
                queries.connectDB();
                LocalDate date = dtpDailyLogReportDate.getValue();
                ResultSet rs = queries.getLogsPerDate(date);
                doOnDateChange(rs);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
        btnNewLog.setStyle(IDLE_BUTTON_STYLE);
        btnNewLog.setOnMouseEntered(e -> btnNewLog.setStyle(HOVERED_BUTTON_STYLE));
        btnNewLog.setOnMouseExited(e -> btnNewLog.setStyle(IDLE_BUTTON_STYLE));
        btnExport.setStyle(IDLE_BUTTON_STYLE);
        btnExport.setOnMouseEntered(e -> btnExport.setStyle(HOVERED_BUTTON_STYLE));
        btnExport.setOnMouseExited(e -> btnExport.setStyle(IDLE_BUTTON_STYLE));
        btnClose.setStyle(IDLE_BUTTON_STYLE);
        btnClose.setOnMouseEntered(e -> btnClose.setStyle(HOVERED_BUTTON_STYLE));
        btnClose.setOnMouseExited(e -> btnClose.setStyle(IDLE_BUTTON_STYLE));
    }

    private void addButtonToTable() {
        TableColumn<AnimalForDailyLogs, Void> colBtn = new TableColumn("Action");

        Callback<TableColumn<AnimalForDailyLogs, Void>, TableCell<AnimalForDailyLogs, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<AnimalForDailyLogs, Void> call(final TableColumn<AnimalForDailyLogs, Void> param) {
                final TableCell<AnimalForDailyLogs, Void> cell = new TableCell<>() {

                    private final Button btnLogs = new Button("Logs");

                    {
                        btnLogs.setOnAction((ActionEvent event) -> {
                            showViewLogsPerAnimal(getTableView().getItems().get(getIndex()).getTagNo());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnLogs);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);

        tblDailyLogs.getColumns().add(colBtn);
    }

    private void showViewLogsPerAnimal(String tagNo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/viewLogsPerAnimal.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            logsPerAnimalController controller =
                    loader.getController();   // gets the controller specified in the fxml
            queries.connection.close();
            controller.initSessionID(loginManager, scene, staffUser, tagNo);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void NewLog() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/updateLogbook.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            updateLogbookController controller =
                    loader.getController();   // gets the controller specified in the fxml
            queries.connection.close();
            controller.initSessionID(loginManager, scene, staffUser);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ExportDailyLogs() {
        //Add code for export daily logs
        excelData = tblDailyLogs.getItems();
        try (PrintWriter writer = new PrintWriter("out/Reports/"+ dtpDailyLogReportDate.getValue().toString() +"Logs.csv.")) {
            StringBuilder sb = new StringBuilder();
            //String columns = "Tag No,Name,Gender,Adult,Species,Location Retrieved,Date,\n";
            //sb.append(columns);
            for (AnimalForDailyLogs animal : excelData) {
                sb.append(animal.getTagNo());
                sb.append(',');
                sb.append(animal.getName());
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

    private ObservableList<AnimalForDailyLogs> populateList(ResultSet rs) {
        ObservableList<AnimalForDailyLogs> dailyLogs = FXCollections.observableArrayList();
        try {
            AnimalForDailyLogs temp;
            while (rs.next()) {
                temp = new AnimalForDailyLogs();
                temp.setTagNo(rs.getString("Tag_No"));
                temp.setName(rs.getString("Animal_Name"));
                temp.setCentre(rs.getString("Centre"));
                temp.setCondition(rs.getString("Condition"));
                //System.out.println(queries.getFoodByID(Integer.parseInt(rs.getString("Food_Code"))));
                temp.setFood(queries.getFoodByID(Integer.parseInt(rs.getString("Food_Code"))));
                temp.setMedication(queries.getMedsByID(Integer.parseInt(rs.getString("Medication_ID"))));
                dailyLogs.add(temp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dailyLogs;
    }

    public void doOnDateChange(ResultSet rs) throws SQLException {
        ObservableList<AnimalForDailyLogs> dailyLogs = populateList(rs);
        tblDailyLogs.setItems(dailyLogs);
        queries.connection.close();

//When date is changed (selected)

        /*LocalDate toFind = dtpDailyLogReportDate.getValue();
        Date ToFind = Date.valueOf(toFind);
        String dateForQuery = ToFind.toString();

        //Call the select statement
        ResultSet Animals = queries.getLogsPerDate(dateForQuery);

        List<AnimalForDailyLogs> AnimalsLogs = new ArrayList<>();
        while (Animals.next())
        {
            //While there are still animals in the resultant set
            String AnimalTagNo = Animals.getString("Tag_No");

            Animal temp = queries.getAnimalByTag(AnimalTagNo);
            String AnimalName = temp.getName();

            String Centre = Animals.getString("Centre");

            String Condition = Animals.getString("Condition");

            int FoodGiven = Animals.getInt("Food_Code");
            String FoodDesc = queries.getFoodByID(FoodGiven);

            int MedID = Animals.getInt("Medication_ID");
            String MedDesc = queries.getMedsByID(MedID);

            AnimalForDailyLogs Temp = new AnimalForDailyLogs(AnimalTagNo, AnimalName, Centre, Condition, FoodDesc, MedDesc);
            AnimalsLogs.add(Temp);
        }

        for (int i = 0; i < AnimalsLogs.size(); i++)
        {
            tblDailyReports.getItems().add(AnimalsLogs.get(i));
        }*/
    }
}

class AnimalForDailyLogs{
    private final SimpleStringProperty TagNo = new SimpleStringProperty();
    private final SimpleStringProperty Name = new SimpleStringProperty();
    private final SimpleStringProperty Centre = new SimpleStringProperty();
    private final SimpleStringProperty Condition = new SimpleStringProperty();
    private final SimpleStringProperty Food = new SimpleStringProperty();
    private final SimpleStringProperty Medication = new SimpleStringProperty();

    public String getTagNo() {
        return TagNo.get();
    }

    public SimpleStringProperty tagNoProperty() {
        return TagNo;
    }

    public void setTagNo(String tagNo) {
        this.TagNo.set(tagNo);
    }

    public String getName() {
        return Name.get();
    }

    public SimpleStringProperty nameProperty() {
        return Name;
    }

    public void setName(String name) {
        this.Name.set(name);
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

    public String getCondition() {
        return Condition.get();
    }

    public SimpleStringProperty conditionProperty() {
        return Condition;
    }

    public void setCondition(String condition) {
        this.Condition.set(condition);
    }

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

    //String TagNo, Name, Centre, Condition, Food, Medication;
    /*public String getTagNo() {
        return TagNo;
    }

    public void setTagNo(String tagNo) {
        TagNo = tagNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCentre() {
        return Centre;
    }

    public void setCentre(String centre) {
        Centre = centre;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String food) {
        Food = food;
    }

    public String getMedication() {
        return Medication;
    }

    public void setMedication(String medication) {
        Medication = medication;
    }

    public AnimalForDailyLogs(String tagNo, String name, String centre, String condition, String food, String medication) {
        TagNo = tagNo;
        Name = name;
        Centre = centre;
        Condition = condition;
        Food = food;
        Medication = medication;


    }*/
}
