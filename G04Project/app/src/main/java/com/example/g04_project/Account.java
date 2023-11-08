package com.example.g04_project;

public class Account {
    private String userID, username, email;
    private Integer hashtag;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashtag(Integer hashtag) {
        this.hashtag = hashtag;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return username;
    }

    public Integer getHashtag() {
        return hashtag;
    }
}
