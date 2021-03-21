package com.example.projectkp.CS;

public class CSHelper {

    String title, speed, device, price;

    public CSHelper() {
    }

    public CSHelper(String title, String speed, String device, String price) {
        this.title = title;
        this.speed = speed;
        this.device = device;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
