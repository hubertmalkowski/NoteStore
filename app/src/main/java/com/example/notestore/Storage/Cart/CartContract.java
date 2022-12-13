package com.example.notestore.Storage.Cart;

import android.provider.BaseColumns;

public final class CartContract {

private CartContract() {}

    public static class CartEntry implements BaseColumns {
        public static final String TABLE_NAME = "cart";
        public static final String COLUMN_NAME_PRODUCT_ID = "product_id";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CartEntry.TABLE_NAME + " (" +
                    CartEntry._ID + " INTEGER PRIMARY KEY," +
                    CartEntry.COLUMN_NAME_PRODUCT_ID + " INTEGER," +
                    CartEntry.COLUMN_NAME_QUANTITY + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CartEntry.TABLE_NAME;


}
