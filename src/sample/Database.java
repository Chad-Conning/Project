package sample;

import java.sql.*;
import java.time.LocalDate;

public class Database {
    public Connection connection;
    Statement statement;

    public Database() {
        connection = null;
        statement = null;
    }

    public boolean connectDB() throws ClassNotFoundException, SQLException {
        boolean successful;

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");//Loading Driver
            connection = DriverManager.getConnection("jdbc:ucanaccess://Resources\\Bay_Marine_Rescue.accdb");//Establishing Connection
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            successful = true;
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to connect to database.");
            successful = false;
        }
        return successful;
    }

    public ResultSet getStaffList() {
        try {
            String query = "SELECT * FROM Staff";
            ResultSet rs = statement.executeQuery(query);
            return rs;
        }
        catch (Exception e) {
            System.out.println("Failed to execute query "+e.getMessage());
            return null;
        }
    }

    public boolean regAnimal(String aName, Boolean isAdult, String aGender, String aStatus, String aSpecies) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Animal(Animal_Name, is_Adult, Animal_Gender, Animal_Status, Animal_Species) VALUES(?,?,?,?,?) ");
            insertStatement.setString(1, aName);
            insertStatement.setBoolean(2, isAdult);
            insertStatement.setString(3, aGender);
            insertStatement.setString(4, aStatus);
            insertStatement.setString(5, aSpecies);
            insertStatement.execute();

            return true;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public void deRegAnimal(String tagNo) {
        try {
            String query = "DELETE FROM Animal WHERE Tag_No = " + tagNo;
            statement.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getTagNo(String name) {
        try {
            PreparedStatement deleteStatement = connection.prepareStatement("SELECT * FROM Animal WHERE Animal_Name = '" + name + "'");
            ResultSet rs = deleteStatement.executeQuery();
            rs.next();
            return rs.getString("Tag_No");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public boolean admitAnimal(String tagNo, String locationFound, String staffID) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Admission(Admission_Date, Tag_No, Location_Retrieved, Staff_ID) VALUES(?,?,?,?)");
            insertStatement.setDate(1, Date.valueOf(LocalDate.now()));
            insertStatement.setString(2, tagNo);
            insertStatement.setString(3, locationFound);
            insertStatement.setString(4, staffID);
            insertStatement.execute();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean addStaff(String password, String fName, String lName, String contact, String email, String taxNum, String empRole) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Staff(Password, Staff_FName, Staff_LName, Staff_ContactNum, Staff_Email, Staff_TaxNumber, Staff_Type, is_Employed) VALUES(?,?,?,?,?,?,?,?)");
            insertStatement.setString(1, password);
            insertStatement.setString(2, fName);
            insertStatement.setString(3, lName);
            insertStatement.setString(4, contact);
            insertStatement.setString(5, email);
            insertStatement.setString(6, taxNum);
            insertStatement.setString(7, empRole);
            insertStatement.setBoolean(8, true);
            insertStatement.execute();

            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
