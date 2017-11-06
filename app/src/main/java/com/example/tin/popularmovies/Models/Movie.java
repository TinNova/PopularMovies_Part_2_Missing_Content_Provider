package com.example.tin.popularmovies.Models;


import com.example.tin.popularmovies.NetworkUtils;

/** Constructor Class Containing Getters */
public class Movie {

    private final String posterImageUrl;
    private final String movieTitle;
    private final String movieSynopsis;
    private final String movieUserRating;
    private final String movieReleaseDate;
    private final String movieId;

    // The Constructor
    public Movie(String posterImageUrl, String movieTitle, String movieSynopsis,
                 String movieUserRating, String movieReleaseDate, String movieId) {

        this.posterImageUrl = posterImageUrl;
        this.movieTitle = movieTitle;
        this.movieSynopsis = movieSynopsis;
        this.movieUserRating = movieUserRating;
        this.movieReleaseDate = movieReleaseDate;
        this.movieId = movieId;

    }

    // The Getters
    public String getPosterImageUrl() {
        return NetworkUtils.BASE_IMAGE_URL + posterImageUrl;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieSynopsis() {
        return movieSynopsis;
    }

    public String getMovieUserRating() {
        return movieUserRating;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMovieId() {
        return movieId;
    }

}
