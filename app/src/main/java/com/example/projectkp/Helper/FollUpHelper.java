package com.example.projectkp.Helper;

public class FollUpHelper {

    private String title, time, salesID, status, result;

    public FollUpHelper() {
    }

    public FollUpHelper(String title, String time, String salesID, String status, String result) {
        this.title = title;
        this.time = time;
        this.status = status;
        this.salesID = salesID;
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
        return salesID;
    }

    public void setSalesID(String salesID) {
        this.salesID = salesID;
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
