package com.example.tin.popularmovies.Models;

/**
 * Created by Tin on 21/10/2017.
 */

public class Review {

    private final String userName;
    private final String userComment;

    public Review(String userName, String userComment) {

        this.userName = userName;
        this.userComment = userComment;

    }

    // The Getters
    public String getUserName() {
        return userName;
    }

    public String getUserComment() {
        return userComment;
    }
}
