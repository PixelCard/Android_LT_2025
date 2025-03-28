package com.pixelcard.project_truyen_as.model;

public class User {
    private String hoten;
    private String email;
    private String role;

    public User() {
        // Empty constructor for Firebase
    }

    public User(String hoten, String email, String role) {
        this.hoten = hoten;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    public String getHoten() { return hoten; }
    public void setHoten(String hoten) { this.hoten = hoten; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}