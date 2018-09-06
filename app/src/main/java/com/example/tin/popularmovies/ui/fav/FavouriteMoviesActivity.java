package com.example.tin.popularmovies.ui.fav;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tin.popularmovies.Adapters.FavouritesAdapter;
import com.example.tin.popularmovies.room.AppDatabase;
import com.example.tin.popularmovies.room.FavouriteMovieRoom;
import com.example.tin.popularmovies.room.FavouriteMovieRoomDAO;
import com.example.tin.popularmovies.Models.FavouriteMovie;
import com.example.tin.popularmovies.R;
import com.example.tin.popularmovies.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviesActivity extends AppCompatActivity implements FavContract.FavScreen, FavouritesAdapter.ListItemClickListener {

    List<FavouriteMovieRoom> mFavouriteMoviesRooms;

    // TAG to help catch errors in Log
    private static final String TAG = FavouriteMoviesActivity.class.getSimpleName();
    // Constant to save state of the loader
    private static final String ROTATION_TAG = "rotation_tag";
    // Constant for logging and referring to a unique loader
    private static final int FAVOURITEMOVIES_LOADER_ID = 0;

    private RecyclerView favouriteRecyclerView;
    private List<FavouriteMovie> favouriteMovies;

    private FavPresenter favPresenter;

    private FavouritesAdapter favouritesAdapter;
    private FavouriteMovieRoomDAO mFavouriteMovieRoomDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);

        favPresenter = new FavPresenter(this);

        favouriteRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_favourite);

        GridLayoutManager favouriteGridLayoutManager =
                new GridLayoutManager(this, 2);
        favouriteRecyclerView.setLayoutManager(favouriteGridLayoutManager);
        favouriteMovies = new ArrayList<>();


        mFavouriteMovieRoomDAO = Room.databaseBuilder(this, AppDatabase.class, "db-contacts")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build()
                .getFavouriteMovieRoomDAO();

        mFavouriteMoviesRooms = mFavouriteMovieRoomDAO.getFavouriteMovieRooms();


        favouritesAdapter = new FavouritesAdapter(mFavouriteMoviesRooms, getApplicationContext(), FavouriteMoviesActivity.this);
//        favouritesAdapter.updateData(mFavouriteMovieRoomDAO.getFavouriteMovieRooms());

        favouriteRecyclerView.setAdapter(favouritesAdapter);

    }

    public void queryAllData(){

        mFavouriteMoviesRooms = mFavouriteMovieRoomDAO.getFavouriteMovieRooms();
        favouritesAdapter.addItems(mFavouriteMoviesRooms);

    }

    @Override
    protected void onResume() {
        super.onResume();

        queryAllData();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ROTATION_TAG, FAVOURITEMOVIES_LOADER_ID);
    }

    /**
     * Here we are passing passing Extra's into the DetailActivity
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra("Favourite_Movie", true);
        intent.putExtra("MovieSqlId", mFavouriteMoviesRooms.get(clickedItemIndex).getMovieId());
        intent.putExtra("MoviePosterSql", mFavouriteMoviesRooms.get(clickedItemIndex).getMoviePoster());

        startActivity(intent);

    }
}
