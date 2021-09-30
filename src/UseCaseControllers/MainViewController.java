package UseCaseControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import sample.Database;
import sample.LoginManager;
import sample.Staff;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainViewController {
    Database queries = new Database();

    @FXML private Button btnLogOut;
    @FXML private ImageView imgAddStaff;
    @FXML private Label lblAddStaff;
    @FXML private ImageView imgRegAnimal;
    @FXML private Label lblRegAnimal;

    @FXML private Pane addStaffPane;
    @FXML private Pane modifyStaffPane;
    @FXML private Pane viewStaffPane;
    @FXML private Pane registerAnimalPane;
    @FXML private Pane admitAnimalPane;
    @FXML private Pane viewLogsPane;
    @FXML private Pane logbookPane;
    Staff staffUser;

    private Scene scene;

    public void initSessionID(final LoginManager loginManager, Scene scene, Staff staffUser) {
        this.staffUser = staffUser;
        if (!staffUser.getStaffType().equals("Administrator")) {
            addStaffPane.setOpacity(0.4);
            addStaffPane.setDisable(true);

            modifyStaffPane.setOpacity(0.4);
            modifyStaffPane.setDisable(true);

            viewStaffPane.setOpacity(0.4);
            viewStaffPane.setDisable(true);

            if (staffUser.getStaffType().equals("Handler")) {
                registerAnimalPane.setOpacity(0.4);
                registerAnimalPane.setDisable(true);

                admitAnimalPane.setOpacity(0.4);
                admitAnimalPane.setDisable(true);
            } else if (staffUser.getStaffType().equals("Admission")) {
                viewLogsPane.setOpacity(0.4);
                viewLogsPane.setDisable(true);

                logbookPane.setOpacity(0.4);
                logbookPane.setDisable(true);
            }
        }
        /*if (staffRole.equals("Handler")) {
            addStaffPane.setOpacity(0.4);
            addStaffPane.setDisable(true);

            modifyStaffPane.setOpacity(0.4);
            modifyStaffPane.setDisable(true);

            viewStaffPane.setOpacity(0.4);
            viewStaffPane.setDisable(true);

            registerAnimalPane.setOpacity(0.4);
            registerAnimalPane.setDisable(true);

            admitAnimalPane.setOpacity(0.4);
            admitAnimalPane.setDisable(true);
        } else if (staffRole.equals("Admission")) {
            addStaffPane.setOpacity(0.4);
            addStaffPane.setDisable(true);

            modifyStaffPane.setOpacity(0.4);
            modifyStaffPane.setDisable(true);

            viewStaffPane.setOpacity(0.4);
            viewStaffPane.setDisable(true);

            viewLogsPane.setOpacity(0.4);
            viewLogsPane.setDisable(true);

            logbookPane.setOpacity(0.4);
            logbookPane.setDisable(true);
        }*/

        this.scene = scene;
        try {
            queries.connectDB();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        btnLogOut.setOnAction(event -> loginManager.logout());

        imgAddStaff.setOnMouseClicked(actionEvent -> {
            showAddStaff();
        });
        lblAddStaff.setOnMouseClicked(actionEvent -> {
            showAddStaff();
        });

        imgRegAnimal.setOnMouseClicked(actionEvent -> {
            showRegAnimals();
        });
        lblRegAnimal.setOnMouseClicked(actionEvent -> {
            showRegAnimals();
        });



        /*searchSong.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                displaySongs(songSearch.getText(), -1);
            }
        });

        albumView.getSelectionModel().selectedItemProperty().addListener(((observableValue, album, t1) ->
                displaySongs("", t1.getCDID())));

        editAlbum.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (albumView.getSelectionModel().getSelectedItem() != null)
                    editAlbum(albumView.getSelectionModel().getSelectedItem());
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an album to edit and then the edit album button.");
                    alert.showAndWait();
                }
            }
        });
        });*/
    }

    private void showAddStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/addStaff.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            addStaffController controller =
                    loader.getController();   // gets the controller specified in the fxml

            controller.initSessionID(scene, staffUser);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showRegAnimals() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/registerAnimal.fxml")   // load fxml
            );
            scene.setRoot(loader.load());   // create scene for mainView screen
            regAnimalController controller =
                    loader.getController();   // gets the controller specified in the fxml

            controller.initSessionID(scene, staffUser);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   /* public void displayAlbums(String phrase) {
        String sql;
        if (phrase.equals(""))
            sql = "SELECT * FROM Staff";
        else
            sql = "SELECT * FROM Staff WHERE Title LIKE '%" + phrase + "%'";

        staffData = FXCollections.observableArrayList();
        try {
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                Staff emp = new Staff();
                emp.setStaffID(rs.getInt("Staff_ID"));
                emp.setfName(rs.getString("Staff_FName"));
                emp.setlName(rs.getString("Staff_LName"));
                emp.setContactNum(rs.getString("Staff_ContactNum"));
                emp.setEmail(rs.getString("Staff_Email"));
                emp.setTaxNum(rs.getString("Staff_TaxNumber"));
                emp.setStaffType(rs.getString("Staff_Type"));
                emp.setBoolEmp(rs.getBoolean("is_Employed"));
                staffData.add(emp);
            }
            staffID.setCellValueFactory(new PropertyValueFactory<>("staffID"));
            fName.setCellValueFactory(new PropertyValueFactory<>("fName"));
            lName.setCellValueFactory(new PropertyValueFactory<>("lName"));
            contactNum.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            taxNum.setCellValueFactory(new PropertyValueFactory<>("taxNum"));
            staffType.setCellValueFactory(new PropertyValueFactory<>("staffType"));
            boolEmp.setCellValueFactory(new PropertyValueFactory<>("boolEmp"));
            staffTableView.setItems(staffData);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }*/

   /* private void editAlbum(Album toEdit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to edit this album?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Stage editing = createEditingStage(new Stage(), 0);
            TextField txtTitle = (TextField) editing.getScene().lookup("#txtTitle");
            TextField txtYear = (TextField) editing.getScene().lookup("#txtYear");

            txtTitle.textProperty().set(String.valueOf(toEdit.getTitle()));
            txtYear.textProperty().setValue(String.valueOf(toEdit.getYear()));

            // Restore old position if had one.
            if(oldX != 0) {
                editing.setX(oldX);
                editing.setY(oldY);
            }

            // Show and wait - waits for stage to close.
            editing.showAndWait();

            // Remember current position as the "new" old position.
            oldX = editing.getX();
            oldY = editing.getY();

            try {
                PreparedStatement insertStatement = connection.prepareStatement("UPDATE CD SET Title = ?, Year = ? WHERE CDID = " + toEdit.getCDID());

                insertStatement.setString(1, txtTitle.textProperty().get());
                insertStatement.setInt(2, Integer.parseInt(txtYear.textProperty().get()));
                insertStatement.execute();
                displayAlbums("");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }*/

   /* private void deleteAlbum(Album toDelete) {
        String sql;
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to delete this album and its songs?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                //do stuff
                sql = "DELETE FROM Track WHERE CDID = " + toDelete.getCDID();
                statement.execute(sql);

                sql = "DELETE FROM CD WHERE CDID = " + toDelete.getCDID();
                statement.execute(sql);

                displayAlbums("");
                displaySongs("", -1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Stage createEditingStage(Stage owner, int CDID) {
        Stage stage = new Stage();
        Button btnOK;

        VBox root = new VBox();
        root.setSpacing(10);
        root.setFillWidth(true);
        root.setPadding(new Insets(5));

        if (CDID == 0) {
            TextField txtTitle = new TextField();
            txtTitle.setId("txtTitle");
            TextField txtYear = new TextField();
            txtYear.setId("txtYear");

            btnOK = new Button("Save");
            btnOK.setId("ok");
            btnOK.setMaxWidth(Double.MAX_VALUE);

            root.getChildren().addAll(
                    new Label("Title"), txtTitle,
                    new Label("Year"), txtYear,
                    btnOK
            );
            // set stage details
            stage.setTitle("Album details");
            btnOK.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful. The CD table has been updated.");
                alert.showAndWait();
                stage.hide();
            });
        }
        else {
            TextField txtCDID = new TextField();
            txtCDID.setId("txtCDID");
            TextField txtTrackNum = new TextField();
            txtTrackNum.setId("txtTrackNum");
            TextField txtName = new TextField();
            txtName.setId("txtName");
            TextField txtArtist = new TextField();
            txtArtist.setId("txtArtist");

            btnOK = new Button("Save");
            btnOK.setId("ok");
            btnOK.setMaxWidth(Double.MAX_VALUE);

            root.getChildren().addAll(
                    new Label("CDID"), txtCDID,
                    new Label("Track Number"), txtTrackNum,
                    new Label("Name"), txtName,
                    new Label("Artist"), txtArtist,
                    btnOK
            );
            // set stage details
            stage.setTitle("Song details");
            btnOK.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful. The Track table has been updated.");
                alert.showAndWait();
                stage.hide();
            });
        }

        // bit of a cheat, but really too simple to create a new controller

        stage.setScene(new Scene(root));

        // only has close button and cannot be resized
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);

        // rest of application cannot be interacted with until this stage is closed
        stage.initModality(Modality.APPLICATION_MODAL);

        // this stage belongs to another, i.e. will close if the owner does
        stage.initOwner(owner);

        return stage;
    }*/

}
