package com.example.notestore.Storage.User;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.notestore.R;
import com.example.notestore.Storage.DBHelper;
import com.example.notestore.Storage.StorageManager;

public class UserManager {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    StorageManager storageManager;
    int currentUserId; //guest id equals -1


    public UserManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.userAdress), Context.MODE_PRIVATE);
        storageManager = new StorageManager(new DBHelper(context), context);
        currentUserId = sharedPreferences.getInt(context.getString(R.string.userId), -1);
    }

    public User getCurrentUser() {
        if (currentUserId == -1) {
            return null;
        }
        return storageManager.getUser(currentUserId);
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    private void setCurrentUser(int id) {
        editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.userId), id);
        editor.apply();
        currentUserId = id;
    }


    //authenticates user and returns true if successful
    public boolean authenticateUser(String name, String password) {
        User user = storageManager.getUser(name, password);
        if (user == null) {
            return false;
        }
        setCurrentUser(user.getId());
        return true;
    }

    //register new user
    public boolean registerUser(String name, String email, String password) {
        User user = storageManager.getUser(name);
        if (user != null) {
            return false;
        }
        storageManager.addUser(name, email, password);
        return true;
    }
    //Logout user
    public void logoutUser() {
        setCurrentUser(-1);
    }
}
