package com.pixelcard.project_truyen_as;

public class Product {
    private String tentruyen;
    private String id;

    private String author;

    private String description;

    private String createDate;


    private String urlhinhsp;

    public Product(){

    }

    public Product(String tentruyen,String createDate,String id,String author,String description,String hinhsp) {
        this.tentruyen = tentruyen;
        this.id=id;
        this.createDate=createDate;
        this.author=author;
        this.description=description;
        this.urlhinhsp = hinhsp;
    }

    public String getUrlhinhsp() {
        return urlhinhsp;
    }

    public void setUrlhinhsp(String urlhinhsp) {
        this.urlhinhsp = urlhinhsp;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTentruyen() {
        return tentruyen;
    }

    public void setTentruyen(String tentruyen) {
        this.tentruyen = tentruyen;
    }
}
