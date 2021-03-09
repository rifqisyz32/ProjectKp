package com.example.projectkp.Sales;

public class SalesHelper {
    String JenisProduct, DeskripsiProduct;
    SalesHelper(){
    }
    public SalesHelper(String jenisProduct, String deskripsiProduct) {
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
