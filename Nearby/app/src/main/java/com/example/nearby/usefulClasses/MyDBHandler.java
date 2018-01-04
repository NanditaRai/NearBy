package com.example.nearby.usefulClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nearby.MyApplication;

/*
* DataBase Handler to store the required details of the favorite list items
* */

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "products.db";
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT_NAME = "productName";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_RATING_IMAGE = "ratingImage";
    public static final String COLUMN_REVIEW_COUNT = "reviewCount";


    private static MyDBHandler sDBHandlerInstance = null;

    public static MyDBHandler getInstance(){
        if(sDBHandlerInstance == null) {
            sDBHandlerInstance = new MyDBHandler(MyApplication.getContext(), null, null, DATABASE_VERSION);
        }
        return sDBHandlerInstance;
    }

    private MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 =" CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_RATING + " DOUBLE, " +
                COLUMN_RATING_IMAGE + " TEXT, " +
                COLUMN_REVIEW_COUNT + " INTEGER " +
                ");";
        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL(" DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    /* Adding items to the table in database*/
    public void addProduct(Product product){
        ContentValues values= new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PHONE, product.getDisplayPhone());
        values.put(COLUMN_IMAGE, product.getUrl());
        values.put(COLUMN_RATING, product.getRating());
        values.put(COLUMN_RATING_IMAGE, product.getRatingImageUrl());
        values.put(COLUMN_REVIEW_COUNT, product.getReviewCount());
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.insert(TABLE_PRODUCTS, null, values);
            db.close();
        }finally {
            if(db!=null)db.close();
        }
    }

    /*Deleting items from the table in database*/
    public void deleteProduct(Product product){
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.execSQL(" DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_NAME + "=\""
                    + product.getName() + "\";");
        }finally {
            if(db!=null)db.close();
        }
    }

    /*Getting Cursor to read data from table*/
    public Cursor readData() {
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            String[] allColumns = new String[]{COLUMN_ID, COLUMN_PRODUCT_NAME, COLUMN_PHONE,
                    COLUMN_IMAGE, COLUMN_RATING, COLUMN_RATING_IMAGE, COLUMN_REVIEW_COUNT};
            Cursor c = db.query(TABLE_PRODUCTS, allColumns, null,
                    null, null, null, null);
            if (c != null) {
                c.moveToFirst();
            }
            return c;
        }finally {
            if(db!=null)db.close();
        }
    }


    /* Checking presence of particular item in database*/
    public boolean checkIsDataAlreadyInDBorNot(Product product) {
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            String[] allColumns = new String[]{COLUMN_ID, COLUMN_PRODUCT_NAME,
                    COLUMN_PHONE, COLUMN_IMAGE, COLUMN_RATING, COLUMN_RATING_IMAGE,
                    COLUMN_REVIEW_COUNT};
            String selection = COLUMN_PRODUCT_NAME + " =?";
            String[] selectionArgs = { product.getName() };
            Cursor mCursor = db.query(TABLE_PRODUCTS,allColumns,selection,selectionArgs,
                    null,null,null);

            if (mCursor.getCount() <= 0) {
                mCursor.close();
                return false;
            }
            mCursor.close();
            return true;
        }finally {
            if(db != null)db.close();
        }
    }

}
