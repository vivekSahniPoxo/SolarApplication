package com.example.solarapplication;

public class ReportModelClass {
    String ID;
    String PVManuName,PVmodleName,CellManuName,LabName,Warranty,CountryPv,CountryCell,DateCell,DatePv,DateLab;

    public ReportModelClass() {
    }

    public ReportModelClass(String PVManuName, String PVmodleName, String cellManuName, String labName, String warranty, String countryPv, String countryCell, String dateCell, String datePv, String dateLab) {

        this.PVManuName = PVManuName;
        this.PVmodleName = PVmodleName;
        CellManuName = cellManuName;
        LabName = labName;
        Warranty = warranty;
        CountryPv = countryPv;
        CountryCell = countryCell;
        DateCell = dateCell;
        DatePv = datePv;
        DateLab = dateLab;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPVManuName() {
        return PVManuName;
    }

    public void setPVManuName(String PVManuName) {
        this.PVManuName = PVManuName;
    }

    public String getPVmodleName() {
        return PVmodleName;
    }

    public void setPVmodleName(String PVmodleName) {
        this.PVmodleName = PVmodleName;
    }

    public String getCellManuName() {
        return CellManuName;
    }

    public void setCellManuName(String cellManuName) {
        CellManuName = cellManuName;
    }

    public String getLabName() {
        return LabName;
    }

    public void setLabName(String labName) {
        LabName = labName;
    }

    public String getWarranty() {
        return Warranty;
    }

    public void setWarranty(String warranty) {
        Warranty = warranty;
    }

    public String getCountryPv() {
        return CountryPv;
    }

    public void setCountryPv(String countryPv) {
        CountryPv = countryPv;
    }

    public String getCountryCell() {
        return CountryCell;
    }

    public void setCountryCell(String countryCell) {
        CountryCell = countryCell;
    }

    public String getDateCell() {
        return DateCell;
    }

    public void setDateCell(String dateCell) {
        DateCell = dateCell;
    }

    public String getDatePv() {
        return DatePv;
    }

    public void setDatePv(String datePv) {
        DatePv = datePv;
    }

    public String getDateLab() {
        return DateLab;
    }

    public void setDateLab(String dateLab) {
        DateLab = dateLab;
    }
}
