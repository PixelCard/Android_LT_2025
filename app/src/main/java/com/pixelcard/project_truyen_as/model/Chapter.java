package com.pixelcard.project_truyen_as.model;

public class Chapter {
    private String tenTruyen;
    private long chapterNumber;
    private String chapterContent;
    private String chapterName;
    private String imageUrl;
    private String chapterID;
    private String productID;

    public Chapter() {
        // Required by Firebase
    }

    public Chapter(String tenTruyen, long chapterNumber, String chapterContent,
                   String imageUrl, String chapterID, String productID, String chapterName) {
        this.tenTruyen = tenTruyen;
        this.chapterNumber = chapterNumber;
        this.chapterContent = chapterContent;
        this.imageUrl = imageUrl;
        this.chapterID = chapterID;
        this.productID = productID;
        this.chapterName = chapterName;
    }

    public String getTenTruyen() {
        return tenTruyen;
    }

    public void setTenTruyen(String tenTruyen) {
        this.tenTruyen = tenTruyen;
    }

    public long getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(long chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getChapterContent() {
        return chapterContent;
    }

    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getChapterID() {
        return chapterID;
    }

    public void setChapterID(String chapterID) {
        this.chapterID = chapterID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
