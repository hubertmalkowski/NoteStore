package com.example.notestore.Storage.User;

public class User {
    int id;
    String name;
    String phoneNumber;
    String password;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
