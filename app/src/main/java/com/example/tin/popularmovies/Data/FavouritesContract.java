package com.example.tin.popularmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tin on 24/10/2017.
 */

public class FavouritesContract {

    // The Authority, this is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.tin.popularmovies";
    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    // Here we define the possible paths for accessing data in this contract
    // This is the path for the "favouriteFilms" directory (the favouriteFilms table)
    public static final String PATH_FAVOURITE_FILMS = "favouriteFilms";

    // We've made the class private here to prevent anyone accidentally instantiating the contract class
    private FavouritesContract() {}

    // BaseColumns is what creates the automatic Id's for each row.
    // We don't need to create a column for the ID sections as BaseColumns does it automatically
    public static final class FavouritesEntry implements BaseColumns {

        // FavouriteEntry content URI = base content URI + path
        // content://com.example.tin.popularmovies/favouriteFilms
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_FILMS).build();

        public static final String TABLE_NAME = "favouriteFilms";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";

    }
}
