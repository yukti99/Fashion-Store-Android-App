package com.example.fashionista;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import java.security.SecureRandomSpi;


public class DataBaseHelper extends SQLiteOpenHelper {
    // THE DATABASE FOR THE WHOLE APP
    public static final String DATABASE_USERS = "UserInfo.db";
    private ProgressDialog loadingBar;
    // THE TABLE FOR USERS OF THE FASHIONISTA APP
    public static final String TABLE_USERS = "Users";
    public static final String U_fname= "fname";
    public static final String U_lname = "lname";
    public static final String U_phone = "phone";
    public static final String U_password = "password";
    public static final String U_address = "address";
    public static final String U_profilePhoto = "profilePhoto";
    public static final String U_security_ans1= "security1";
    public static final String U_security_ans2 = "security2";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_USERS, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        // executes whatever string query we enter as string
        db.execSQL("create table "+ TABLE_USERS +"('fname' TEXT,'lname' TEXT,'phone' TEXT PRIMARY KEY,'password' TEXT,'address' TEXT,'profilePhoto' BLOB,'security1' TEXT,'security2' TEXT)");
        //db.execSQL("create table "+ TABLE_USERS +"('fname' TEXT,'lname' TEXT,'phone' TEXT PRIMARY KEY,'password' TEXT,'address' TEXT,'profilePhoto' BLOB)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        onCreate(db);
    }
    //*****************************************USER DATABASE METHODS**********************************************************************

    public boolean insertDataUsers(String fname,String lname,String phone,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(U_fname,fname);
        contentValues.put(U_lname,lname);
        contentValues.put(U_phone,phone);
        contentValues.put(U_password,password);
        contentValues.put(U_address,"null");
        contentValues.put(U_profilePhoto, "null");
        contentValues.put(U_security_ans1, "null");
        contentValues.put(U_security_ans2, "null");
        long result = db.insert(TABLE_USERS,null ,contentValues);
        db.close();
        // data not inserted successfully
        if(result == -1) {
            System.out.println("Sorry.. Some error occurred, Try again later!!!");
            return false;
        }
        else {
            System.out.println("Congratulations! Your account has been created successfully!!!!");
            return true;
        }
    }
    public boolean enterSecurityQues(String phone,String ques1,String ques2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(U_security_ans1, ques1);
        contentValues.put(U_security_ans2, ques2);
        long result = db.update(TABLE_USERS, contentValues, "phone = ?",new String[] { phone });
        db.close();
        if(result == -1) {
            System.out.println("Sorry.. Some error occurred, Try again later!!!");
            return false;
        }
        else {
            System.out.println("Congratulations! Your account has been created successfully!!!!");
            return true;
        }
    }
    public byte[] getImage(String phone) {
        byte[] data = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT profilePhoto FROM "+ TABLE_USERS+" WHERE phone = ?", new String[]{phone});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            data = cursor.getBlob(0);
            break;  // Assumption: phone number is unique
        }
        cursor.close();
        return data;
        /*SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from Users", null);
        if(c.moveToNext()){
            byte[] image = c.getBlob(5);
            return image;
        }
        return null;*/
    }
    public String getSecurity1(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select security1 from "+TABLE_USERS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            String ans1 = cursor.getString(cursor.getColumnIndex("security1"));
            return ans1;
        }
        cursor.close();
        return null;

    }
    public String getSecurity2(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select security2 from "+TABLE_USERS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            String ans2 = cursor.getString(cursor.getColumnIndex("security2"));
            return ans2;
        }
        cursor.close();
        return null;

    }
    public String getUserPassword(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select fname from "+TABLE_USERS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("password"));
            return name;
        }
        cursor.close();
        return null;
    }
    public boolean changePassword(String phone,String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(U_password,newPassword);
        long result =  db.update(TABLE_USERS, contentValues, "phone = ?",new String[] { phone });
        db.close();
        if(result == -1) {
            System.out.println("Sorry.. Some error occurred, Try again later!!!");
            return false;
        }
        else {
            System.out.println("Congratulations! Your account has been created successfully!!!!");
            return true;
        }


    }
    public String getUserFName(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select fname from "+TABLE_USERS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("fname"));
            return name;
        }
        cursor.close();
        return null;
    }
    public String getUserLName(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select lname from "+TABLE_USERS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("lname"));
            return name;
        }
        cursor.close();
        return null;
    }
    public String getUserAddress(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select address from "+TABLE_USERS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            String address = cursor.getString(cursor.getColumnIndex("address"));
            return address;
        }
        cursor.close();
        return null;
    }
    public Boolean UserPhoneExists(String phone){
        //System.out.println("\n\n\n\n\nPHONE = "+Phone+"\n\n\n\n\n");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_USERS+" where phone = ? ",new String[]{phone});
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
    public Boolean UserPasswordCorrect(String Phone,String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select password from "+TABLE_USERS+" where phone = ? and password = ?",new String[]{Phone,Password});
        if (res.getCount() > 0)
            return true;
        else
            return false;

    }
    public Cursor getAllDataUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_USERS,null);
        return res;
    }
    public boolean updateDataUsers(String oldPhone,String phone,String fname,String lname,String address) {
        // creating an SQLiteDatabase instance
        if (phone == null || oldPhone == null)
                return true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(U_fname,fname);
        contentValues.put(U_lname,lname);
        contentValues.put(U_phone,phone);
        contentValues.put(U_address,address);
        db.update(TABLE_USERS, contentValues, "phone = ?",new String[] { oldPhone });
        db.close();
        return true;
    }
    public Integer deleteDataUsers(String phone) {
        // taking a database instance to delete the data
        SQLiteDatabase db = this.getWritableDatabase();
        // deleting data on basis of a key
        // new String[] will be the value of ?
        return db.delete(TABLE_USERS, "PHONE = ?",new String[] {phone});
    }// returns the number of row deleted
    public void insertProfilePic(String phone,byte[] img) {
        SQLiteDatabase db = this.getWritableDatabase();
        /*String sql = "UPDATE Users SET profilePhoto = ? WHERE phone = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindBlob(1, img);
        statement.bindString(2,phone);
        statement.execute();
        db.close();*/
        ContentValues values = new ContentValues();
        values.put("profilePhoto",img);
        db.insert("Users", null,values);
        db.close();
    }


    //************************************************************************************************************************************

}
