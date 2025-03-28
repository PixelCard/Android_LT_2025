package com.pixelcard.project_truyen_as.model;

public class User {
    private String hoten;
    private String email;
    private Integer role;

    public User() {
        // Empty constructor for Firebase
    }

    public User(String hoten, String email, Integer role) {
        this.hoten = hoten;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    public String getHoten() { return hoten; }
    public void setHoten(String hoten) { this.hoten = hoten; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }
}