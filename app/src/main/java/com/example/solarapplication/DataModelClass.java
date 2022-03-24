package com.example.solarapplication;

public class DataModelClass {
    String CountryName;
    String Date;
    String ModuleManufactureName;
    String CellManufactureName;
    String ModuleDetails;
    String CertificateName;

    public DataModelClass() {
    }

    public DataModelClass(String countryName) {
        CountryName = countryName;
    }

    public DataModelClass(String date, String certificateName) {
        Date = date;
        CertificateName = certificateName;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCertificateName() {
        return CertificateName;
    }

    public void setCertificateName(String certificateName) {
        CertificateName = certificateName;
    }
}
