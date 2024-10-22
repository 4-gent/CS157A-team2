package com.bookie.models;

public class User {
    private String username;
    private String password;
    private String email;
    private String phone;
    private boolean isAdmin;
    private int favoriteAuthorID;
    private int favoriteGenreID;

    // Constructor
    public User(String username, String password, String email, String phone, boolean isAdmin, int favoriteAuthorID, int favoriteGenreID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.isAdmin = isAdmin;
        this.favoriteAuthorID = favoriteAuthorID;
        this.favoriteGenreID = favoriteGenreID;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getFavoriteAuthorID() {
        return favoriteAuthorID;
    }

    public void setFavoriteAuthorID(int favoriteAuthorID) {
        this.favoriteAuthorID = favoriteAuthorID;
    }

    public int getFavoriteGenreID() {
        return favoriteGenreID;
    }

    public void setFavoriteGenreID(int favoriteGenreID) {
        this.favoriteGenreID = favoriteGenreID;
    }
}