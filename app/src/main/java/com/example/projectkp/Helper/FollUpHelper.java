package com.example.projectkp.Helper;

public class FollUpHelper {

    private String title, time, csID, status, result;

    public FollUpHelper() {
    }

    public FollUpHelper(String title, String time, String csID, String status, String result) {
        this.title = title;
        this.time = time;
        this.status = status;
        this.csID = csID;
        this.result = result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSalesID() {
        return csID;
    }

    public void setSalesID(String csID) {
        this.csID = csID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
