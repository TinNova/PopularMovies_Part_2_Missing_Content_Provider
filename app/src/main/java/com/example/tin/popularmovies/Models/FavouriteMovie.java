package com.example.tin.popularmovies.Models;


import java.io.Serializable;

/**
 * Created by Tin on 02/11/2017.
 */

public class FavouriteMovie implements Serializable {

    private final long row_id;
    private final String movieSqlId;
    private final byte[] moviePosterSql;



    public FavouriteMovie(long row_id, String movieSqlId, byte[] moviePosterSql){

        this.row_id = row_id;
        this.movieSqlId = movieSqlId;
        this.moviePosterSql = moviePosterSql;

    }

    //The Getters
    public long getRow_id() {
        return row_id;
    }

    public String getMovieSqlId() {
        return movieSqlId;
    }

    public byte[] getMoviePosterSql() {

        return moviePosterSql;
    }

}
