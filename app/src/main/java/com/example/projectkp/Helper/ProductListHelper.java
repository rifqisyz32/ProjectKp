package com.example.projectkp.Helper;

public class ProductListHelper {
    private String title;
    private int icon;

    public ProductListHelper() {
    }

    public ProductListHelper(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
