package com.pixelcard.project_truyen_as.model;

import java.util.List;

public class Product {
    private String tentruyen;
    private String id;

    private String author;

    private String description;

    private String createDate;


    private String urlhinhsp;

    private String view;

    private List<String> categoryIds;

    public Product(){

    }

    public Product(String tentruyen, String createDate, String id, String author, String description, String hinhsp, String view, List<String> categoryIds) {
        this.tentruyen = tentruyen;
        this.id=id;
        this.createDate=createDate;
        this.author=author;
        this.description=description;
        this.urlhinhsp = hinhsp;
        this.view = view;
        this.categoryIds = categoryIds;
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

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }
}