package com.example.notestore.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notestore.Storage.Cart.CartContract;
import com.example.notestore.Storage.Products.ProductsContract;
import com.example.notestore.Storage.User.UserContract;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NoteProducts.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.createTables(sqLiteDatabase);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        this.dropTables(sqLiteDatabase);
        this.createTables(sqLiteDatabase);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(ProductsContract.SQL_CREATE_ENTRIES);
        db.execSQL(CartContract.SQL_CREATE_ENTRIES);
        db.execSQL(UserContract.SQL_CREATE_ENTRIES);
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL(ProductsContract.SQL_DELETE_ENTRIES);
        db.execSQL(CartContract.SQL_DELETE_ENTRIES);
        db.execSQL(UserContract.SQL_DELETE_ENTRIES);
    }
}

