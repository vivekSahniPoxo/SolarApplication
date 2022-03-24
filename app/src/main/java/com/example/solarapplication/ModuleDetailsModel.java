package com.example.solarapplication;

public class ModuleDetailsModel {
    String ModuleName;

    public ModuleDetailsModel() {
    }

    public ModuleDetailsModel(String moduleName) {
        ModuleName = moduleName;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }
}
