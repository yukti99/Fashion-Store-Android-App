package com.example.fashionista;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.Nullable;

public class ProductDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_PRODUCTS = "Products.db";
    public static final String t1 = "Tops";
    public static final String t2 = "SportsWear";
    public static final String t3 = "WinterWear";
    public static final String t4 = "Dresses";
    public static final String t5 = "Goggles";
    public static final String t6 = "Purses";
    public static final String t7 = "Hats";
    public static final String t8 = "Shoes";
    public static final String t9 = "Jewelry";
    public static final String t10 = "HairProducts";
    public static final String t11 = "Makeup";
    public static final String t12 = "Jackets";
    public static final String t13 = "Gowns";
    public static final String t14 = "Rings";
    public static final String t15 = "Pants";

    private ProgressDialog loadingBar;
    public ProductDatabase(@Nullable Context context) {
        super(context, DATABASE_PRODUCTS , null, 1);
    }
    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    public void insertProductData(String name, String price, byte[] image,String Table_name){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Table_Name VALUES (NULL, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindBlob(3, image);

        statement.executeInsert();
    }

    public void updateProductData(String name, String price, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE FOOD SET name = ?, price = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindBlob(3, image);
        statement.bindDouble(4, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteProductData(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM FOOD WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getProductData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
