package com.example.projectkp.Helper;

public class MYIRHelper {

    private String title, user, time;

    public MYIRHelper() {
    }

    public MYIRHelper(String title, String user, String time) {
        this.title = title;
        this.user = user;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
