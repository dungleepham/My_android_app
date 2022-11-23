package com.example.thi.model;

public class User {

    private String id;
    private String username;
    private String imageURL;

    public User(String id, String userName, String imageURL) {
        this.id = id;
        this.username = userName;
        this.imageURL = imageURL;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
