package com.example.tin.popularmovies;

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
    private String movieId;
    @NonNull
    private String movieName;
//    private blob moviePoster;


    @NonNull
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }

    @NonNull
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(@NonNull String movieName) {
        this.movieName = movieName;
    }
}
