package com.example.navigation_smd_7a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductDB {
    public final String DATABASE_NAME = "products_db_assignment3";
    public final String DATABASE_TABLE_NAME = "products";
    public final String KEY_ID = "id";
    public final String KEY_TITLE = "title";
    public final String KEY_DATE = "date";
    public final String KEY_PRICE = "price";
    public final String KEY_STATUS = "status";

    private final int DB_VERSION = 1;
    Context context;
    DBHelper dbHelper;

    ProductDB(Context context)
    {
        this.context = context;
    }

    public void open()
    {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DB_VERSION);
    }

    public void close()
    {
        dbHelper.close();
    }

    public long insert(String title, String date, int price, String status)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_DATE, date);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_STATUS, status);

        return db.insert(DATABASE_TABLE_NAME, null, cv);
    }

    public int remove(int id)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DATABASE_TABLE_NAME, KEY_ID+"=?", new String[]{id+""});
    }

    public ArrayList<Product> fetchProducts(String status) {
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        ArrayList<Product> products = new ArrayList<>();
        String[] columns = new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_PRICE};

        // Use a placeholder for the status and pass it in selectionArgs
        String selection = KEY_STATUS + " = ?";
        String[] selectionArgs = new String[]{status};

        Cursor cursor = readDb.query(DATABASE_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            int id_idx = cursor.getColumnIndex(KEY_ID);
            int title_idx = cursor.getColumnIndex(KEY_TITLE);
            int date_idx = cursor.getColumnIndex(KEY_DATE);
            int price_idx = cursor.getColumnIndex(KEY_PRICE);

            while (cursor.moveToNext()) {
                Product prod = new Product(cursor.getInt(id_idx), cursor.getString(title_idx), cursor.getString(date_idx), cursor.getInt(price_idx), status);
                products.add(prod);
            }
            cursor.close();
        }
        return products;

    }


    public int updateVal(int id, String title, String date, int priceInt, String s) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE,title);
        cv.put(KEY_DATE,date);
        cv.put(KEY_PRICE,priceInt);
        cv.put(KEY_STATUS,s);
        return db.update(DATABASE_TABLE_NAME, cv, KEY_ID+"=?", new String[]{id+""});
    }


    private class DBHelper extends SQLiteOpenHelper
    {

        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            /*
            CREATE TABLE IF NOT EXISTS products_db(
                   id INTEGER PRIMARY KEY AUTOINCREMENT,
                   title TEXT NOT NULL,
                   date TEXT NOT NULL,
                   price INTEGER
            );
             */
            String query = "CREATE TABLE IF NOT EXISTS "+DATABASE_TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +KEY_TITLE+" TEXT NOT NULL,"+KEY_DATE+" TEXT NOT NULL,"+KEY_PRICE+" INTEGER, " +KEY_STATUS +");";
            sqLiteDatabase.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }






}
