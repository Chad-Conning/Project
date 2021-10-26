package sample;

public class Animal {
    static String tagNo;
    static String name;
    static Boolean isAdult;
    static String gender;
    static String species;

    public Animal(String tagNo, String name, Boolean isAdult, String gender, String species) {
        this.tagNo = tagNo;
        this.name = name;
        this.isAdult = isAdult;
        this.gender = gender;
        this.species = species;
    }

    public static String getTagNo() {
        return tagNo;
    }

    public static String getName() {
        return name;
    }

    public static Boolean getAdult() {
        return isAdult;
    }

    public static String getGender() {
        return gender;
    }

    public static String getSpecies() {
        return species;
    }
}
