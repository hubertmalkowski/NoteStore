package com.example.notestore.Storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class StorageManager {

    final String LOG_TAG = "STORAGE MANAGER";

    DBHelper dbHelper;
    SQLiteDatabase db;

    public StorageManager(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.db = dbHelper.getWritableDatabase();
    }

    public void close() {
        this.db.close();
    }

    public long addProduct(String name, Double price, String description, String image) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ProductsContract.ProductsEntry.COLUMN_NAME_NAME, name);
        contentValues.put(ProductsContract.ProductsEntry.COLUMN_NAME_PRICE, price);
        contentValues.put(ProductsContract.ProductsEntry.COLUMN_NAME_DESCRIPTION, description);
        contentValues.put(ProductsContract.ProductsEntry.COLUMN_NAME_IMAGE, image);
        return this.db.insert(ProductsContract.ProductsEntry.TABLE_NAME, null, contentValues);
    }

    private Cursor getCursorProducts() {
        String[] projection = {
                BaseColumns._ID,
                ProductsContract.ProductsEntry.COLUMN_NAME_NAME,
                ProductsContract.ProductsEntry.COLUMN_NAME_DESCRIPTION,
                ProductsContract.ProductsEntry.COLUMN_NAME_PRICE,
                ProductsContract.ProductsEntry.COLUMN_NAME_IMAGE,
        };
        return db.query(
                ProductsContract.ProductsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public ArrayList<Product> getProducts() {
        Cursor cursor = getCursorProducts();
        ArrayList<Product> products = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_DESCRIPTION));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_PRICE));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(ProductsContract.ProductsEntry.COLUMN_NAME_IMAGE));

            products.add(new Product( id,name, description, price, image));
//            Log.i(LOG_TAG, products.get(products.size() - 1).toString());
        }
        return products;
    }


    public void addToCart(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID, id);
        contentValues.put(CartContract.CartEntry.COLUMN_NAME_QUANTITY, 1);
        this.db.insert(CartContract.CartEntry.TABLE_NAME, null, contentValues);
    }

    private Cursor getCursorCart() {
        return db.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }





}
