package com.pixelcard.project_truyen_as.Comment_Admin;

public class Comment {

    private String userId;        // ID của người dùng
    private String userName;      // Tên người dùng
    private String commentId;     // ID duy nhất cho comment
    private String commentContent;// Nội dung comment
    private String commentName;   // Tên của comment (hoặc tiêu đề)
    private Object timestamp;     // Thời gian tạo comment

    private String productID;

    // Constructor đầy đủ
    public Comment(String userId, String userName, String commentId, String commentContent, String commentName, Object timestamp, String productID) {
        this.userId = userId;
        this.userName = userName;
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentName = commentName;
        this.timestamp = timestamp;
        this.productID = productID;
    }

    // Constructor rỗng bắt buộc cho Firebase
    public Comment() {
    }

    // Getter và Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}

