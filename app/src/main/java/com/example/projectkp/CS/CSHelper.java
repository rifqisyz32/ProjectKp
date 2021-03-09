package com.example.projectkp.CS;

public class CSHelper {
    String JenisProduct, DeskripsiProduct;

    CSHelper() {
    }

    public CSHelper(String jenisProduct, String deskripsiProduct) {
        JenisProduct = jenisProduct;
        DeskripsiProduct = deskripsiProduct;
    }

    public String getJenisProduct() {
        return JenisProduct;
    }

    public void setJenisProduct(String jenisProduct) {
        JenisProduct = jenisProduct;
    }

    public String getDeskripsiProduct() {
        return DeskripsiProduct;
    }

    public void setDeskripsiProduct(String deskripsiProduct) {
        DeskripsiProduct = deskripsiProduct;
    }
}
