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
        ResultSet rs;
        try {
            String query = "SELECT * FROM Staff";
            rs = statement.executeQuery(query);
            return rs;
        }
        catch (Exception e) {
            System.out.println("Failed to execute query "+e.getMessage());
            return null;
        }
    }

    public ResultSet getAnimalList() {
        ResultSet rs;
        try {
            String query = "SELECT * FROM Animal";
            rs = statement.executeQuery(query);
            return rs;
        }
        catch (Exception e) {
            System.out.println("Failed to execute query "+e.getMessage());
            return null;
        }
    }

    public ResultSet getAliveAnimals() {
        ResultSet rs;
        try {
            String query = "SELECT * FROM Animal WHERE Animal_Status != 'Deceased'";
            rs = statement.executeQuery(query);
            return rs;
        }
        catch (Exception e) {
            System.out.println("Failed to execute query "+e.getMessage());
            return null;
        }
    }

    public ResultSet getFoodList() {
        ResultSet rs;
        try {
            String query = "SELECT * FROM Food_Intake";
            rs = statement.executeQuery(query);
            return rs;
        }
        catch (Exception e) {
            System.out.println("Failed to execute query "+e.getMessage());
            return null;
        }
    }

    public ResultSet getMedsList() {
        ResultSet rs;
        try {
            String query = "SELECT * FROM Medication_Given";
            rs = statement.executeQuery(query);
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

    public Staff getStaff(String staffID) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Staff WHERE Staff_ID = " + Integer.parseInt(staffID));
            ResultSet rs = statement.executeQuery();
            rs.next();
            Staff temp = new Staff();
            temp.setStaffID(Integer.parseInt(staffID));
            temp.setStaffPassword(rs.getString("Password"));
            temp.setfName(rs.getString("Staff_FName"));
            temp.setlName(rs.getString("Staff_LName"));
            temp.setContactNum(rs.getString("Staff_ContactNum"));
            temp.setEmail(rs.getString("Staff_Email"));
            temp.setTaxNum(rs.getString("Staff_TaxNumber"));
            temp.setStaffType(rs.getString("Staff_Type"));
            temp.setBoolEmp(rs.getBoolean("is_Employed"));

            return temp;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Staff();
    }

    public Staff getStaffBySurname(String surname) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Staff WHERE Staff_LName = '" + surname + "'");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Staff temp = new Staff();
                temp.setStaffID(rs.getInt("Staff_ID"));
                temp.setStaffPassword(rs.getString("Password"));
                temp.setfName(rs.getString("Staff_FName"));
                temp.setlName(rs.getString("Staff_LName"));
                temp.setContactNum(rs.getString("Staff_ContactNum"));
                temp.setEmail(rs.getString("Staff_Email"));
                temp.setTaxNum(rs.getString("Staff_TaxNumber"));
                temp.setStaffType(rs.getString("Staff_Type"));
                temp.setBoolEmp(rs.getBoolean("is_Employed"));

                return temp;
            }
            else return null;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Staff();
    }

    public boolean admitAnimal(String tagNo, String locationFound, String staffID, String txtNotes) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Admission(Admission_Date, Tag_No, Location_Retrieved, Staff_ID, Comments) VALUES(?,?,?,?,?)");
            insertStatement.setDate(1, Date.valueOf(LocalDate.now()));
            insertStatement.setString(2, tagNo);
            insertStatement.setString(3, locationFound);
            insertStatement.setString(4, staffID);
            insertStatement.setString(5, txtNotes);
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

    public boolean modifyStaff(String staffID, String fName, String lName, String contact, String email, String taxNum, String empRole, Boolean isEmployed) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE Staff SET Staff_FName = ?, Staff_LName = ?, Staff_ContactNum = ?, Staff_Email = ?, Staff_TaxNumber = ?, Staff_Type = ?, is_Employed = ? " +
                    "WHERE Staff_ID = " + Integer.parseInt(staffID));
            updateStatement.setString(1, fName);
            updateStatement.setString(2, lName);
            updateStatement.setString(3, contact);
            updateStatement.setString(4, email);
            updateStatement.setString(5, taxNum);
            updateStatement.setString(6, empRole);
            updateStatement.setBoolean(7, isEmployed);
            updateStatement.execute();

            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean modifyAnimalStatus(String tagNo, String status) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE Animal SET Animal_Status = ? WHERE Tag_No = " + Integer.parseInt(tagNo));
            updateStatement.setString(1, status);
            updateStatement.execute();

            return true;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /*public ResultSet getAnimalsToAdmit() {
        ResultSet rs;
        try {
            String query = "SELECT Tag_No FROM Animal NATURAL JOIN Admission";
            rs = statement.executeQuery(query);
            return rs;
        }
        catch (Exception e) {
            System.out.println("Failed to execute query "+e.getMessage());
            return null;
        }
    }*/
}
