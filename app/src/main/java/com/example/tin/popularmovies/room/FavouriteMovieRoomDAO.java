package com.example.tin.popularmovies.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface FavouriteMovieRoomDAO {

    @Insert
    public void insert(FavouriteMovieRoom... favouriteMovieRoom);

    @Update
    public void update(FavouriteMovieRoom... favouriteMovieRoom);

    @Delete
    public void delete(FavouriteMovieRoom favouriteMovieRoom);

    @Query("SELECT * FROM favouriteMovieRoom")
    public List<FavouriteMovieRoom> getFavouriteMovieRooms();

    @Query("SELECT * FROM favouriteMovieRoom WHERE movieId = :id")
    public FavouriteMovieRoom getMovieWithId(String id);
}
