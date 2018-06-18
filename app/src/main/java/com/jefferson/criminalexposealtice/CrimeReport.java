package com.jefferson.criminalexposealtice;

public class CrimeReport {

    String location;
    String image;
    String description;

    public CrimeReport(){

    }

    public CrimeReport(String location, String image, String description) {
        this.location = location;
        this.image = image;
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
