package com.example.tin.popularmovies;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Tin on 20/08/2018.
 */
@Dao
public interface FavouriteMovieRoomDAO {

    @Insert
    public void insert(FavouriteMovieRoom favouriteMovieRoom);

    @Update
    public void update(FavouriteMovieRoom favouriteMovieRoom);

    @Delete
    public void delete(FavouriteMovieRoom favouriteMovieRoom);

    @Query("SELECT * FROM favouriteMovieRoom")
    public List<FavouriteMovieRoom> getFavouriteMovieRooms();
}
