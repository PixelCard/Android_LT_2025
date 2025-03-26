package com.pixelcard.project_truyen_as;

public class Product {
    private String tentruyen;

    private String productID;

    private String Description;

    private String Author;

    private String createDate;
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

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
