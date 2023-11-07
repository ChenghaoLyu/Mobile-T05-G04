package com.example.g04_project;

public class Account {
    private String userID, userName, email;
    private Integer hashtag;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return userName;
    }

    public Integer getHashtag() {
        return hashtag;
    }
}
