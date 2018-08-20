package com.example.tin.popularmovies.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Tin on 20/08/2018.
 */

@Database(entities = {FavouriteMovieRoom.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavouriteMovieRoomDAO getFavouriteMovieRoomDAO();

}
