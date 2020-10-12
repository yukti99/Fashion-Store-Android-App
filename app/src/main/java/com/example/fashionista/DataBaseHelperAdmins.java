package com.example.fashionista;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import java.security.SecureRandomSpi;


public class DataBaseHelperAdmins extends SQLiteOpenHelper {

    public static final String DATABASE_ADMINS = "Owners.db";
    private ProgressDialog loadingBar;

    // THE TABLE FOR ADMINS OF THE FASHIONISTA APP
    public static final String TABLE_ADMINS = "Admins";
    public static final String A_fname = "fname";
    public static final String A_lname = "lname";
    public static final String A_phone = "phone";
    public static final String A_password = "password";

    public DataBaseHelperAdmins(@Nullable Context context) {
        super(context, DATABASE_ADMINS, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+ TABLE_ADMINS +"('fname' TEXT,'lname' TEXT,'phone' TEXT PRIMARY KEY,'password' TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ADMINS);
        onCreate(db);
    }

    //*****************************************ADMINS DATABASE METHODS**********************************************************************
    public boolean insertDataAdmins(String fname,String lname,String phone,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(A_fname,fname);
        contentValues.put(A_lname,lname);
        contentValues.put(A_phone,phone);
        contentValues.put(A_password,password);
        long result = db.insert(TABLE_ADMINS,null ,contentValues);
        // data not inserted successfully
        if(result == -1) {
            System.out.println("Sorry.. Some error occured, Try again later!!!");
            return false;
        }
        else {
            System.out.println("Congratulations! You are now an Admin!!!");
            return true;
        }
    }
    public String  getAdminPassord(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select fname from "+TABLE_ADMINS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("password"));
            return name;
        }
        cursor.close();
        return null;
    }
    public String getAdminFName(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select fname from "+TABLE_ADMINS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("fname"));
            return name;
        }
        cursor.close();
        return null;
    }
    public Boolean AdminPhoneExists(String phone){
        //System.out.println("\n\n\n\n\nPHONE = "+Phone+"\n\n\n\n\n");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_ADMINS+" where phone = ? ",new String[]{phone});
        if (res.getCount() > 0) {
            // phone number exists
            res.close();
            return true;
        }
        else {
            res.close();
            // phone number does not exist
            return false;
        }

    }
    public Boolean AdminPasswordCorrect(String Phone,String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select password from "+TABLE_ADMINS+" where phone = ? and password = ?",new String[]{Phone,Password});
        if (res.getCount() > 0)
            return true;
        else
            return false;

    }
    public Cursor getAllDataAdmins(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_ADMINS,null);
        return res;
    }
    public boolean updateDataAdmins(String phone,String fname,String lname,String password) {
        // creating an SqliteDatabase instance
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(A_fname,fname);
        contentValues.put(A_lname,lname);
        contentValues.put(A_phone,phone);
        contentValues.put(A_password,password);
        // updation on basis of unique phone no (Primary key)
        db.update(TABLE_ADMINS, contentValues, "PHONE = ?",new String[] { phone });
        return true;
    }
    public Integer deleteDataAdmins(String phone) {
        // taking a database instance to delete the data
        SQLiteDatabase db = this.getWritableDatabase();
        // deleting data on basis of a key
        // new String[] will be the value of ?
        return db.delete(TABLE_ADMINS, "PHONE = ?",new String[] {phone});
    }// returns the number of row deleted
    //*************************************************************************************************************************************
}
