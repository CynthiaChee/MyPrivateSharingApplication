package com.example.myprivatesharingapplication;

public class User {
    private String fullname;
    private String username;
    private String password;
    private String phoneNumber;
    private int userid;


    public User(String fullname, String username, String password, String phoneNumber) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_id() {
        return userid;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
