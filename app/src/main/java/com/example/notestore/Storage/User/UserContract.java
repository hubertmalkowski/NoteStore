package com.example.notestore.Storage.User;

import android.provider.BaseColumns;

public class UserContract {
    //Create user contract for database
    private UserContract() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "phoneNumber";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME_NAME + " TEXT," +
                    UserEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    UserEntry.COLUMN_NAME_PASSWORD + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
}
