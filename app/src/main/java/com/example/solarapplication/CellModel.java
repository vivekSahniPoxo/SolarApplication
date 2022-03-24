package com.example.solarapplication;

public class CellModel {
    String CellManufacture;

    public CellModel(String cellManufacture) {
        CellManufacture = cellManufacture;
    }

    public CellModel() {
    }

    public String getCellManufacture() {
        return CellManufacture;
    }

    public void setCellManufacture(String cellManufacture) {
        CellManufacture = cellManufacture;
    }
}
