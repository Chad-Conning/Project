package UseCaseControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import sample.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    @FXML private Button btnSearchAnimal;
    @FXML private Button btnViewAll;
    @FXML private TextField tfieldFilter;
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
            addButtonToTable();

            ResultSet rs = queries.getAnimalList();
            populateTableView(rs);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        btnVARClose.setOnAction(actionEvent -> showMainView());

        btnSearchAnimal.setOnAction(actionEvent -> displaySearch(tfieldFilter.getText()));
        tfieldFilter.setOnKeyPressed(actionEvent -> displaySearch(tfieldFilter.getText()));

        btnViewAll.setOnAction(actionEvent -> {
            ResultSet rs = queries.getAnimalList();
            populateTableView(rs);
        });

        btnVARExport.setOnAction(actionEvent -> {
            try {
                writeExcel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void addButtonToTable() {
        TableColumn<ViewAnimal, Void> colBtn = new TableColumn("Action");

        Callback<TableColumn<ViewAnimal, Void>, TableCell<ViewAnimal, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ViewAnimal, Void> call(final TableColumn<ViewAnimal, Void> param) {
                final TableCell<ViewAnimal, Void> cell = new TableCell<>() {

                    private final Button btnEdit = new Button("Edit");

                    {
                        btnEdit.setOnAction((ActionEvent event) -> {
                            showModifyAnimalStatus(getTableView().getItems().get(getIndex()).getTagNo());
                        });
                    }

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
                            HBox pane = new HBox(btnEdit, btnLogs);
                            setGraphic(pane);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);

        animalsTable.getColumns().add(colBtn);
    }

    private void displaySearch(String searchString) {
        ResultSet rs = queries.getAnimalList();
        List<ViewAnimal> animalData = populateList(rs);

        List<ViewAnimal> searchAnimal = new ArrayList<>();
        for (int i = 0; i <= animalData.size() - 1; i++) {
            if (animalData.get(i).getTagNo().equals(searchString) || animalData.get(i).getName().equalsIgnoreCase(searchString))
                searchAnimal.add(animalData.get(i));
        }
        animalsTable.getItems().setAll(searchAnimal);
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
        try {
            queries.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
            queries.connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       /* finally {
            writer.flush();
            writer.close();
        }*/
    }

    private void showModifyAnimalStatus(String tagNo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/modifyAnimalStatus.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            modifyAnimalStatusController controller =
                    loader.getController();   // gets the controller specified in the fxml
            queries.connection.close();
            controller.initSessionID(loginManager, scene, staffUser, tagNo);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
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
