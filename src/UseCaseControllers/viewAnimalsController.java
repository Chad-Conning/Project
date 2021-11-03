package UseCaseControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
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
    @FXML public MenuItem btnMenuAddReadmitA;
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
    @FXML public TableView<ViewAnimal> animalsTable;
    @FXML private TableColumn<ViewAnimal, String> colTagNo;
    @FXML private TableColumn<ViewAnimal, String> colName;
    @FXML private TableColumn<ViewAnimal, Boolean> colAdult;
    @FXML private TableColumn<ViewAnimal, String> colGender;
    @FXML private TableColumn<ViewAnimal, String> colStatus;
    @FXML private TableColumn<ViewAnimal, String> colSpecies;

    ObservableList<ViewAnimal> tableData = FXCollections.observableArrayList();
    ObservableList<ViewAnimal> excelData = FXCollections.observableArrayList();
    FilteredList<ViewAnimal> adults = new FilteredList<>(tableData, p -> true);
    //https://stackoverflow.com/questions/18941093/how-to-fill-up-a-tableview-with-database-data

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
            menu.btnMenuDisplayAR.setDisable(true);

            colTagNo.setCellValueFactory(cellData -> cellData.getValue().tagNoProperty());
            colName.setCellValueFactory(cellData -> cellData.getValue().animalNameProperty());
            colAdult.setCellValueFactory(cellData -> cellData.getValue().isAdultProperty());
            colAdult.setCellFactory(column -> new CheckBoxTableCell<>());
            colGender.setCellValueFactory(cellData -> cellData.getValue().animalGenderProperty());
            colStatus.setCellValueFactory(cellData -> cellData.getValue().animalStatusProperty());
            colSpecies.setCellValueFactory(cellData -> cellData.getValue().animalSpeciesProperty());


            ResultSet rs = queries.getAnimalList();
            populateTableView(rs);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        btnVARClose.setOnAction(actionEvent -> showMainView());

        btnVARExport.setOnAction(actionEvent -> {
            try {
                writeExcel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
            queries.connection.close();
            controller.initSessionID(loginManager, this.scene, staffUser);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateTableView(ResultSet rs) {
        connection = queries.connection;
        ObservableList<ViewAnimal> viewData = populateList(rs);
        //admissionsTable.getItems().setAll(admissionData);
        animalsTable.setItems(viewData);
        changeData();
    }

    private ObservableList<ViewAnimal> populateList(ResultSet rs) {
        ObservableList<ViewAnimal> viewData = FXCollections.observableArrayList();
        try {
            ViewAnimal emp;
            while (rs.next()) {
                emp = new ViewAnimal();
                emp.setTagNo(rs.getString("Tag_No"));
                emp.setName(rs.getString("Animal_Name"));
                emp.setAdult(rs.getBoolean("is_Adult"));
                emp.setGender(rs.getString("Animal_Gender"));
                emp.setStatus(rs.getString("Animal_Status"));
                emp.setSpecies(rs.getString("Animal_Species"));
                viewData.add(emp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return viewData;
    }

    private void changeData() {
        tableData = animalsTable.getItems();
        adults = new FilteredList<>(tableData, p -> true);
    }

    public void writeExcel() throws Exception {
        excelData = animalsTable.getItems();
        try (PrintWriter writer = new PrintWriter("out/Reports/AnimalReport.csv.")) {
            StringBuilder sb = new StringBuilder();
            String columns = "Tag No,Name,Adult,Gender,Status,Species,\n";
            sb.append(columns);
            for (ViewAnimal animal : excelData) {
                sb.append(animal.getTagNo());
                sb.append(',');
                sb.append(animal.getName());
                sb.append(',');
                sb.append(animal.getAdult());
                sb.append(',');
                sb.append(animal.getGender());
                sb.append(',');
                sb.append(animal.getStatus());
                sb.append(',');
                sb.append(animal.getSpecies());
                sb.append(',');
                sb.append("\n");
            }
            writer.write(sb.toString());
            writer.close();
            System.out.println("File saved!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       /* finally {
            writer.flush();
            writer.close();
        }*/
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
