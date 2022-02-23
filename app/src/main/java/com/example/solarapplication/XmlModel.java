package com.example.solarapplication;

public class XmlModel {
    String id;
    String PVModule;
    String Warranty;
    String PVMName;
    String PVCountry;
    String PVDate;
    String CellMName;
    String CellCountry;
    String CellDate;
    String IECLab;
    String IECDate;

    public XmlModel() {
    }

    public XmlModel(String id, String PVModule, String warranty, String PVMName, String PVCountry, String PVDate, String cellMName, String cellCountry, String cellDate, String IECLab, String IECDate) {
        this.id = id;
        this.PVModule = PVModule;
        this.Warranty = warranty;
        this.PVMName = PVMName;
        this.PVCountry = PVCountry;
        this.PVDate = PVDate;
        this.CellMName = cellMName;
        this.CellCountry = cellCountry;
        this.CellDate = cellDate;
        this.IECLab = IECLab;
        this.IECDate = IECDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPVModule() {
        return PVModule;
    }

    public void setPVModule(String PVModule) {
        this.PVModule = PVModule;
    }

    public String getWarranty() {
        return Warranty;
    }

    public void setWarranty(String warranty) {
        Warranty = warranty;
    }

    public String getPVMName() {
        return PVMName;
    }

    public void setPVMName(String PVMName) {
        this.PVMName = PVMName;
    }

    public String getPVCountry() {
        return PVCountry;
    }

    public void setPVCountry(String PVCountry) {
        this.PVCountry = PVCountry;
    }

    public String getPVDate() {
        return PVDate;
    }

    public void setPVDate(String PVDate) {
        this.PVDate = PVDate;
    }

    public String getCellMName() {
        return CellMName;
    }

    public void setCellMName(String cellMName) {
        CellMName = cellMName;
    }

    public String getCellCountry() {
        return CellCountry;
    }

    public void setCellCountry(String cellCountry) {
        CellCountry = cellCountry;
    }

    public String getCellDate() {
        return CellDate;
    }

    public void setCellDate(String cellDate) {
        CellDate = cellDate;
    }

    public String getIECLab() {
        return IECLab;
    }

    public void setIECLab(String IECLab) {
        this.IECLab = IECLab;
    }

    public String getIECDate() {
        return IECDate;
    }

    public void setIECDate(String IECDate) {
        this.IECDate = IECDate;
    }
}
