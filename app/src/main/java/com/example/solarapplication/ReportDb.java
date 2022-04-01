package com.example.solarapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ReportDb extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NewSolarDataBaseReport";
    private static final String TABLE_Report = "DataReport1";
    private static final String ID = "ID";
    private static final String PVManuName = "PVManufactureName";
    private static final String PVmodleName = "PVmodelName";
    private static final String CellManuName = "CellManufactureName";
    private static final String LabName = "LabName";
    private static final String Warranty = "Warranty";
    private static final String CountryPv = "PVCountry";
    private static final String CountryCell = "CountryCell";
    private static final String DateCell = "DateCell";
    private static final String DatePv = "PVDate";
    private static final String DateLab = "DateLab";


    public ReportDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_Report + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PVManuName + " TEXT,"
                + PVmodleName + " TEXT,"
                + CellManuName + " TEXT,"
                + LabName + " TEXT,"
                + Warranty + " TEXT,"
                + CountryPv + " TEXT,"
                + CountryCell + " TEXT,"
                + DateCell + " TEXT,"
                + DatePv + " TEXT,"
                + DateLab + " TEXT)";


        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Report);
        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addContact(ReportModelClass dataModelClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(ID, dataModelClass.getID());
        values.put(PVManuName, dataModelClass.getPVManuName());
        values.put(PVmodleName, dataModelClass.getPVmodleName());
        values.put(CellManuName, dataModelClass.getCellManuName());
        values.put(LabName, dataModelClass.getLabName());
        values.put(Warranty, dataModelClass.getWarranty());
        values.put(CountryPv, dataModelClass.getCountryPv());
        values.put(CountryCell, dataModelClass.getCountryCell());
        values.put(DateCell, dataModelClass.getDateCell());
        values.put(DatePv, dataModelClass.getDatePv());
        values.put(DateLab, dataModelClass.getDateLab());

        // Inserting Row
        db.insert(TABLE_Report, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<ReportModelClass> getAllContacts() {
        List<ReportModelClass> contactList = new ArrayList<ReportModelClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Report;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReportModelClass dataModelClass = new ReportModelClass();
                dataModelClass.setID(cursor.getString(0));
                dataModelClass.setPVManuName(cursor.getString(1));
                dataModelClass.setPVmodleName(cursor.getString(2));
                dataModelClass.setCellManuName(cursor.getString(3));
                dataModelClass.setLabName(cursor.getString(4));
                dataModelClass.setWarranty(cursor.getString(5));
                dataModelClass.setCountryPv(cursor.getString(6));
                dataModelClass.setCountryCell(cursor.getString(7));
                dataModelClass.setDateCell(cursor.getString(8));
                dataModelClass.setDatePv(cursor.getString(9));
                dataModelClass.setDateLab(cursor.getString(10));
                // Adding contact to list
                contactList.add(dataModelClass);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }


    public List<ReportModelClass> getAllDetails(String Modelname) {
        List<ReportModelClass> contactList = new ArrayList<ReportModelClass>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_Report + "WHERE" + PVmodleName + "=" + Modelname;
        String selectQuery = "SELECT * FROM " + TABLE_Report + " WHERE " + PVmodleName + " = '" + Modelname + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReportModelClass dataModelClass = new ReportModelClass();
                dataModelClass.setID(cursor.getString(0));
                dataModelClass.setPVManuName(cursor.getString(1));
                dataModelClass.setPVmodleName(cursor.getString(2));
                dataModelClass.setCellManuName(cursor.getString(3));
                dataModelClass.setLabName(cursor.getString(4));
                dataModelClass.setWarranty(cursor.getString(5));
                dataModelClass.setCountryPv(cursor.getString(6));
                dataModelClass.setCountryCell(cursor.getString(7));
                dataModelClass.setDateCell(cursor.getString(8));
                dataModelClass.setDatePv(cursor.getString(9));
                dataModelClass.setDateLab(cursor.getString(10));
                // Adding contact to list
                contactList.add(dataModelClass);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }


    // Deleting single contact
    public void deleteContact(ReportModelClass contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Report, ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
        db.close();
    }

    //Method for Update
//    public int updateContact(ReportModelClass contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contact.getName());
//        values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//        // updating row
//        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }

}
