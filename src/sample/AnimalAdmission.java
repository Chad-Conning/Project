package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class AnimalAdmission {
    private final SimpleStringProperty tagNo = new SimpleStringProperty();
    private final SimpleStringProperty animalName = new SimpleStringProperty();
    private final SimpleStringProperty animalGender = new SimpleStringProperty();
    private final SimpleBooleanProperty isAdult = new SimpleBooleanProperty();
    private final SimpleStringProperty animalSpecies = new SimpleStringProperty();
    private final SimpleStringProperty locationRetrieved = new SimpleStringProperty();
    private final SimpleStringProperty admissionDate = new SimpleStringProperty();

    public String getTagNo() {
        return tagNo.get();
    }

    public SimpleStringProperty tagNoProperty() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo.set(tagNo);
    }

    public String getAnimalName() {
        return animalName.get();
    }

    public SimpleStringProperty animalNameProperty() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName.set(animalName);
    }

    public String getAnimalGender() {
        return animalGender.get();
    }

    public SimpleStringProperty animalGenderProperty() {
        return animalGender;
    }

    public void setAnimalGender(String animalGender) {
        this.animalGender.set(animalGender);
    }

    public boolean getIsAdult() {
        return isAdult.get();
    }

    public SimpleBooleanProperty isAdultProperty() {
        return isAdult;
    }

    public void setIsAdult(boolean isAdult) {
        this.isAdult.set(isAdult);
    }

    public String getAnimalSpecies() {
        return animalSpecies.get();
    }

    public SimpleStringProperty animalSpeciesProperty() {
        return animalSpecies;
    }

    public void setAnimalSpecies(String animalSpecies) {
        this.animalSpecies.set(animalSpecies);
    }

    public String getLocationRetrieved() {
        return locationRetrieved.get();
    }

    public SimpleStringProperty locationRetrievedProperty() {
        return locationRetrieved;
    }

    public void setLocationRetrieved(String locationRetrieved) {
        this.locationRetrieved.set(locationRetrieved);
    }

    public String getAdmissionDate() {
        return admissionDate.get();
    }

    public SimpleStringProperty admissionDateProperty() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate.set(admissionDate);
    }
}
