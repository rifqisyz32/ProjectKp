package com.example.projectkp.loginregister;

public class UserHelper {
    String fullname, username, phone, email;

    public UserHelper() {
    }

    public UserHelper(String fullname, String username, String phone, String email) {
        this.fullname = fullname;
        this.username = username;
        this.phone = phone;
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
