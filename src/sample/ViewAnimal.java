package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ViewAnimal {

    private final SimpleStringProperty tagNo = new SimpleStringProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleBooleanProperty isAdult = new SimpleBooleanProperty();
    private final SimpleStringProperty gender = new SimpleStringProperty();
    private final SimpleStringProperty status = new SimpleStringProperty();
    private final SimpleStringProperty species = new SimpleStringProperty();

    public ViewAnimal() {
    }

    public String getTagNo() {
        return tagNo.get();
    }

    public SimpleStringProperty tagNoProperty() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo.set(tagNo);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty animalNameProperty() {
        return name;
    }

    public void setName(String animalName) {
        this.name.set(animalName);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty animalGenderProperty() {
        return gender;
    }

    public void setGender(String animalGender) {
        this.gender.set(animalGender);
    }

    public boolean getAdult() {
        return isAdult.get();
    }

    public SimpleBooleanProperty isAdultProperty() {
        return isAdult;
    }

    public void setAdult(boolean isAdult) {
        this.isAdult.set(isAdult);
    }

    public String getSpecies() {
        return species.get();
    }

    public SimpleStringProperty animalSpeciesProperty() {
        return species;
    }

    public void setSpecies(String animalSpecies) {
        this.species.set(animalSpecies);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty animalStatusProperty() {
        return status;
    }

    public void setStatus(String newStatus) {
        this.status.set(newStatus);
    }
}
