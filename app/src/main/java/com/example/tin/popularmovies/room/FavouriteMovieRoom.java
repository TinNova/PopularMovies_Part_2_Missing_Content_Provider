package com.example.tin.popularmovies.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Tin on 20/08/2018.
 */

@Entity(tableName = "favouriteMovieRoom")
public class FavouriteMovieRoom {

    @PrimaryKey
    @NonNull
    private int movieId;
    @NonNull
    private String movieName;
    @NonNull
    private byte[] moviePoster;


    @NonNull
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull int movieId) {
        this.movieId = movieId;
    }

    @NonNull
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(@NonNull String movieName) {
        this.movieName = movieName;
    }

    @NonNull
    public byte[] getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(@NonNull byte[] moviePoster) {
        this.moviePoster = moviePoster;
    }
}
