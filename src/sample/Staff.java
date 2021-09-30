package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Staff {
    private final SimpleIntegerProperty staffID = new SimpleIntegerProperty();
    private final SimpleStringProperty staffPassword = new SimpleStringProperty();
    private final SimpleStringProperty fName = new SimpleStringProperty();
    private final SimpleStringProperty lName = new SimpleStringProperty();
    private final SimpleStringProperty contactNum = new SimpleStringProperty();
    private final SimpleStringProperty email = new SimpleStringProperty();
    private final SimpleStringProperty taxNum = new SimpleStringProperty();
    private final SimpleStringProperty staffType = new SimpleStringProperty();
    private final SimpleBooleanProperty boolEmp = new SimpleBooleanProperty();

    public String getStaffID() {
        return staffID.getValue().toString();
    }

    public SimpleIntegerProperty staffIDProperty() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID.set(staffID);
    }

    public String getStaffPassword() {
        return staffPassword.get();
    }

    public SimpleStringProperty staffPasswordProperty() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword.set(staffPassword);
    }

    public String getfName() {
        return fName.get();
    }

    public SimpleStringProperty fNameProperty() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName.set(fName);
    }

    public String getlName() {
        return lName.get();
    }

    public SimpleStringProperty lNameProperty() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName.set(lName);
    }

    public String getContactNum() {
        return contactNum.get();
    }

    public SimpleStringProperty contactNumProperty() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum.set(contactNum);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getTaxNum() {
        return taxNum.get();
    }

    public SimpleStringProperty taxNumProperty() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum.set(taxNum);
    }

    public String getStaffType() {
        return staffType.get();
    }

    public SimpleStringProperty staffTypeProperty() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType.set(staffType);
    }

    public boolean isBoolEmp() {
        return boolEmp.get();
    }

    public SimpleBooleanProperty boolEmpProperty() {
        return boolEmp;
    }

    public void setBoolEmp(boolean boolEmp) {
        this.boolEmp.set(boolEmp);
    }

    public Staff() {
    }
}
