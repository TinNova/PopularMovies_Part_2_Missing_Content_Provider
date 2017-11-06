package com.example.tin.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// SQLiteOpenHelper is only used when first creating the Database on a users Android Device and when
// updating the Database
public class FavouritesDbHelper extends SQLiteOpenHelper {

    // The name of the database as it will be saved on the user Android Device
    private static final String DATABASE_NAME = "favouriteMovies.db";
    private static final int DATABASE_VERSION = 4;

    // Constructor that takes a context and calls the parent constructor
    public FavouritesDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITE_MOVIES_TABLE = "CREATE TABLE " +
                FavouritesContract.FavouritesEntry.TABLE_NAME + " (" +
                FavouritesContract.FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavouritesContract.FavouritesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                FavouritesContract.FavouritesEntry.COLUMN_MOVIE_POSTER + " BLOB NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_MOVIES_TABLE);


    }

    // This is only called when the DATABASE_VERSION number is upgraded to let's say 1.1
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouritesContract.FavouritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
