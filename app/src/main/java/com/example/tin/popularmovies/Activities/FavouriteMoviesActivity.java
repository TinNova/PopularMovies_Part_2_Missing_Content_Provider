package com.example.tin.popularmovies.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.tin.popularmovies.Adapters.FavouritesAdapter;
import com.example.tin.popularmovies.FavouritesContract;
import com.example.tin.popularmovies.FavouritesDbHelper;
import com.example.tin.popularmovies.Models.FavouriteMovie;
import com.example.tin.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviesActivity extends AppCompatActivity implements FavouritesAdapter.ListItemClickListener {

    // TAG to help catch errors in Log
    private static final String TAG = FavouriteMoviesActivity.class.getSimpleName();

    private SQLiteDatabase mDb;
    private RecyclerView favouriteRecyclerView;
    private FavouritesAdapter favouritesAdapter;
    List<FavouriteMovie> favouriteMovies;


    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);

        Log.v(TAG, "DATA IN FAVOURITE MOV11");

        favouriteRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_favourite);

        GridLayoutManager favouriteGridLayoutManager =
                new GridLayoutManager(this, 2);
        favouriteRecyclerView.setLayoutManager(favouriteGridLayoutManager);
        favouriteMovies = new ArrayList<>();
        // Here we initialise the Database and call how we want to use it, here we only need to read the database
        FavouritesDbHelper dbHelper = new FavouritesDbHelper(this);
        mDb = dbHelper.getReadableDatabase();
        cursor = getAllMovies();
        addMoviesToList(cursor);

    }

    /**
     * Here we query the database for all of the rows, and we return the result of the query
     * @return
     */
    private Cursor getAllMovies() {

        return mDb.query(
                FavouritesContract.FavouritesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavouritesContract.FavouritesEntry._ID
        );
    }

    /** Here We Are Accessing The SQLite Query We Received In The Method getAllMovies() Which Is Set To Read All Rows
     * We're Going Through Each Row With A For Loop And Putting Them Into Our FavouriteMovie Model
     *
     * @param cursor
     */
    private void addMoviesToList(Cursor cursor) {

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int count = 0; count < cursor.getCount(); count++) {

                FavouriteMovie favouriteMovie = new FavouriteMovie(

                        cursor.getLong(cursor.getColumnIndex(FavouritesContract.FavouritesEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID)),
                        cursor.getBlob(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_MOVIE_POSTER))

                );

                Log.v(TAG, "Row_Id" + cursor.getLong(cursor.getColumnIndex(FavouritesContract.FavouritesEntry._ID)));

                favouriteMovies.add(favouriteMovie);

                Log.v(TAG, "DATA IN FAVOURITE Movies: " + favouriteMovies);

                cursor.moveToNext();
            }

            favouritesAdapter = new FavouritesAdapter(favouriteMovies, getApplicationContext(), FavouriteMoviesActivity.this);
            favouriteRecyclerView.setAdapter(favouritesAdapter);

        } else {
            Log.v(TAG, "cursor is Empty");
        }

        cursor.close();
        mDb.close();
    }


    /**
     * Here we are passing passing Extra's into the DetailActivity
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra("Row_Id", favouriteMovies.get(clickedItemIndex).getRow_id());
        intent.putExtra("MovieSqlId", favouriteMovies.get(clickedItemIndex).getMovieSqlId());
        intent.putExtra("MoviePosterSql", favouriteMovies.get(clickedItemIndex).getMoviePosterSql());

        startActivity(intent);

    }
}
