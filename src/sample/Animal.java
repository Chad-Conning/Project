package sample;

public class Animal {
    String tagNo;
    String name;
    Boolean isAdult;
    String gender;
    String species;

    public Animal(String tagNo, String name, Boolean isAdult, String gender, String species) {
        this.tagNo = tagNo;
        this.name = name;
        this.isAdult = isAdult;
        this.gender = gender;
        this.species = species;
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
}
