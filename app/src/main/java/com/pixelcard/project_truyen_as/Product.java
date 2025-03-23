package com.pixelcard.project_truyen_as;

public class Product {
    private String tentruyen;
    private int hinhsp;

    public Product(){

    }

    public Product(String tentruyen, int hinhsp) {
        this.tentruyen = tentruyen;
        this.hinhsp = hinhsp;
    }


    public String getTentruyen() {
        return tentruyen;
    }

    public void setTentruyen(String tentruyen) {
        this.tentruyen = tentruyen;
    }

    public int getHinhsp() {
        return hinhsp;
    }

    public void setHinhsp(int hinhsp) {
        this.hinhsp = hinhsp;
    }

    @Override
    public String toString() {
        return getTentruyen()  + "\t" + getHinhsp();
    }
}
