package com.example.g04_project;

public class Registration {
    private String userID, userName;
    private Integer hashtag;
    private Boolean validationSuccess;

    public Boolean getValidationSuccess() {
        return validationSuccess;
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
