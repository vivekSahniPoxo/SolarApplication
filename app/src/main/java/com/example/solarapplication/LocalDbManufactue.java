package com.example.solarapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class LocalDbManufactue extends SQLiteOpenHelper
{private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SolarDataBaseManufacture";
    private static final String TABLE_Manfacture = "Manufacture";
    private static final String ManufactureColumn = "ManufactureName";
    public LocalDbManufactue(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Manufacture_TABLE = "CREATE TABLE " + TABLE_Manfacture + "("
                + ManufactureColumn + ")";
        db.execSQL(CREATE_Manufacture_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Manfacture);
        // Create tables again
        onCreate(db);
    }
    //Manufacture Module Table
    public void addManufacture(ManufactureModel manufactureModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ManufactureColumn, manufactureModel.getManufactureName()); // Contact Name
        // Inserting Row
        db.insert(TABLE_Manfacture, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    public List<ManufactureModel> getallManufactureName() {
        List<ManufactureModel> contactList = new ArrayList<ManufactureModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Manfacture;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ManufactureModel dataModelClass = new ManufactureModel();
                dataModelClass.setManufactureName(cursor.getString(0));
                // Adding contact to list
                contactList.add(dataModelClass);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }
}
