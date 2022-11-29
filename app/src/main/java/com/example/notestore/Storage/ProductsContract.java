package com.example.notestore.Storage;

import android.provider.BaseColumns;

public final class ProductsContract {


    private ProductsContract() {}

    public static class ProductsEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGE = "image";
    }


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProductsEntry.TABLE_NAME + " (" +
                    ProductsEntry._ID + " INTEGER PRIMARY KEY," +
                    ProductsEntry.COLUMN_NAME_NAME + " TEXT," +
                    ProductsEntry.COLUMN_NAME_PRICE + " REAL," +
                    ProductsEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    ProductsEntry.COLUMN_NAME_IMAGE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProductsEntry.TABLE_NAME;
}
