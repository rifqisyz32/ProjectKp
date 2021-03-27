package com.example.projectkp.Helper;

public class ProductHelper {

    private String title, speed, price, device;

    public ProductHelper() {
    }

    public ProductHelper(String title, String speed, String price, String device) {
        this.title = title;
        this.speed = speed;
        this.price = price;
        this.device = device;
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
    