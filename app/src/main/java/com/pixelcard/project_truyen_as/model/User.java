package com.pixelcard.project_truyen_as.model;

public class User {
    private String id, email, name, password, role;

    public User() { }

    public User(String id, String email, String name, String password, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
