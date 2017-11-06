package com.example.tin.popularmovies;

import android.provider.BaseColumns;

/**
 * Created by Tin on 24/10/2017.
 */

public class FavouritesContract {

    // We've made the class private here to prevent anyone accidentally instantiating the contract class
    private FavouritesContract() {}

    // BaseColumns is what creates the automatic Id's for each row.
    // We don't need to create a column for the ID sections as BaseColumns does it automatically
    public static final class FavouritesEntry implements BaseColumns {

        public static final String TABLE_NAME = "favouriteFilms";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";


    }
}
