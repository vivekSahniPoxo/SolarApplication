package com.example.solarapplication;

public class WriteDataModel {
    String SerialNo, date, time, Pmaxnew, FillFactor, Voc, Isc, Vmp, Imp, sno, ModuleID, PVModelNumber, CellMfgName, CellMfgCuntry, CellMfgDate, moduleMfg, ModuleMfgCountry, ModuleMfgDate, IECLab, IECdate;

    public WriteDataModel(String serialNo, String date, String time, String pmaxnew, String fillFactor, String voc, String isc, String vmp, String imp, String sno, String moduleID, String PVModelNumber, String cellMfgName, String cellMfgCuntry, String cellMfgDate, String moduleMfg, String moduleMfgCountry, String moduleMfgDate, String IECLab, String IECdate) {
        SerialNo = serialNo;
        this.date = date;
        this.time = time;
        Pmaxnew = pmaxnew;
        FillFactor = fillFactor;
        Voc = voc;
        Isc = isc;
        Vmp = vmp;
        Imp = imp;
        this.sno = sno;
        ModuleID = moduleID;
        this.PVModelNumber = PVModelNumber;
        CellMfgName = cellMfgName;
        CellMfgCuntry = cellMfgCuntry;
        CellMfgDate = cellMfgDate;
        this.moduleMfg = moduleMfg;
        ModuleMfgCountry = moduleMfgCountry;
        ModuleMfgDate = moduleMfgDate;
        this.IECLab = IECLab;
        this.IECdate = IECdate;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String moduleID) {
        ModuleID = moduleID;
    }

    public String getPVModelNumber() {
        return PVModelNumber;
    }

    public void setPVModelNumber(String PVModelNumber) {
        this.PVModelNumber = PVModelNumber;
    }

    public String getCellMfgName() {
        return CellMfgName;
    }

    public void setCellMfgName(String cellMfgName) {
        CellMfgName = cellMfgName;
    }

    public String getCellMfgCuntry() {
        return CellMfgCuntry;
    }

    public void setCellMfgCuntry(String cellMfgCuntry) {
        CellMfgCuntry = cellMfgCuntry;
    }

    public String getCellMfgDate() {
        return CellMfgDate;
    }

    public void setCellMfgDate(String cellMfgDate) {
        CellMfgDate = cellMfgDate;
    }

    public String getModuleMfg() {
        return moduleMfg;
    }

    public void setModuleMfg(String moduleMfg) {
        this.moduleMfg = moduleMfg;
    }

    public String getModuleMfgCountry() {
        return ModuleMfgCountry;
    }

    public void setModuleMfgCountry(String moduleMfgCountry) {
        ModuleMfgCountry = moduleMfgCountry;
    }

    public String getModuleMfgDate() {
        return ModuleMfgDate;
    }

    public void setModuleMfgDate(String moduleMfgDate) {
        ModuleMfgDate = moduleMfgDate;
    }

    public String getIECLab() {
        return IECLab;
    }

    public void setIECLab(String IECLab) {
        this.IECLab = IECLab;
    }

    public String getIECdate() {
        return IECdate;
    }

    public void setIECdate(String IECdate) {
        this.IECdate = IECdate;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPmaxnew() {
        return Pmaxnew;
    }

    public void setPmaxnew(String pmaxnew) {
        Pmaxnew = pmaxnew;
    }

    public String getFillFactor() {
        return FillFactor;
    }

    public void setFillFactor(String fillFactor) {
        FillFactor = fillFactor;
    }

    public String getVoc() {
        return Voc;
    }

    public void setVoc(String voc) {
        Voc = voc;
    }

    public String getIsc() {
        return Isc;
    }

    public void setIsc(String isc) {
        Isc = isc;
    }

    public String getVmp() {
        return Vmp;
    }

    public void setVmp(String vmp) {
        Vmp = vmp;
    }

    public String getImp() {
        return Imp;
    }

    public void setImp(String imp) {
        Imp = imp;
    }
}
