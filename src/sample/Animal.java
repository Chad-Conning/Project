package sample;

public class Animal {
    //original declarations
    String tagNo;
    String name;
    Boolean isAdult;
    String gender;
    String species;
    String status;


    //original animal constructor
    public Animal(String tagNo, String name, Boolean isAdult, String gender, String species) {
        this.tagNo = tagNo;
        this.name = name;
        this.isAdult = isAdult;
        this.gender = gender;
        this.species = species;
        status = "In Centre";
    }


    public String getTagNo() {
        return tagNo;
    }

    public String getName() {
        return name;
    }

    public Boolean getAdult() {
        return isAdult;
    }

    public String getGender() {
        return gender;
    }

    public String getSpecies() {
        return species;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String newStatus) {
        status = newStatus;
    }


}
