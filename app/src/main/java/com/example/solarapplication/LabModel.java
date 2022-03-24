package com.example.solarapplication;

public class LabModel {
    String Datev;
    String Name;

    public LabModel() {
    }

    public LabModel(String date, String name) {
        Datev = date;
        Name = name;
    }

    public String getDate() {
        return Datev;
    }

    public void setDate(String date) {
        Datev = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
