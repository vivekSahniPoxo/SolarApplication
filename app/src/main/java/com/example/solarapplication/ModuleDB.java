package com.example.solarapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ModuleDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SolarDataBaseModuleDetails";
    private static final String TABLE_Manfacture = "ModuleDetails";
    private static final String manufactureColumn = "ModuleName";
    private static final String ManufactureDate = "CertificateDate";

    public ModuleDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_Manfacture + "("
                + manufactureColumn + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Manfacture);
        // Create tables again
        onCreate(db);
    }

    public void addContact(ModuleDetailsModel dataModelClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(manufactureColumn, dataModelClass.getModuleName()); // Contact Name

        // Inserting Row
        db.insert(TABLE_Manfacture, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<ModuleDetailsModel> getAllContacts() {
        List<ModuleDetailsModel> contactList = new ArrayList<ModuleDetailsModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Manfacture;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModuleDetailsModel dataModelClass = new ModuleDetailsModel();
                dataModelClass.setModuleName(cursor.getString(0));
                // Adding contact to list
                contactList.add(dataModelClass);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }
}
