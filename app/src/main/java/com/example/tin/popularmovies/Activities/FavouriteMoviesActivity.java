package com.example.tin.popularmovies.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.tin.popularmovies.Adapters.FavouritesAdapter;
import com.example.tin.popularmovies.Data.FavouritesContract;
import com.example.tin.popularmovies.Models.FavouriteMovie;
import com.example.tin.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviesActivity extends AppCompatActivity implements FavouritesAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    Cursor mFavouriteMoviesData;

    // TAG to help catch errors in Log
    private static final String TAG = FavouriteMoviesActivity.class.getSimpleName();
    // Constant to save state of the loader
    private static final String ROTATION_TAG = "rotation_tag";
    // Constant for logging and referring to a unique loader
    private static final int FAVOURITEMOVIES_LOADER_ID = 0;

    private RecyclerView favouriteRecyclerView;
    private List<FavouriteMovie> favouriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);

        favouriteRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_favourite);

        GridLayoutManager favouriteGridLayoutManager =
                new GridLayoutManager(this, 2);
        favouriteRecyclerView.setLayoutManager(favouriteGridLayoutManager);
        favouriteMovies = new ArrayList<>();

        if (savedInstanceState != null) {
            getSupportLoaderManager().restartLoader(savedInstanceState.getInt(ROTATION_TAG), null, this);
        }

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(FAVOURITEMOVIES_LOADER_ID, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // first we clear the favouriteMovies List, otherwise it will present another list on top
        // of the previously loaded list.
        // TODO HOWEVER, do we want to reload the list every time? I think we only want to do that if we know there
        // was an update to the SQLite database, otherwise it's not necessary because nothing would've
        // changed...????
        favouriteMovies.clear();
        // re-queries for all of the favouriteMovies
        getSupportLoaderManager().restartLoader(FAVOURITEMOVIES_LOADER_ID, null, this);
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

        intent.putExtra("Row_Id", favouriteMovies.get(clickedItemIndex).getRow_id());
        intent.putExtra("MovieSqlId", favouriteMovies.get(clickedItemIndex).getMovieSqlId());
        intent.putExtra("MoviePosterSql", favouriteMovies.get(clickedItemIndex).getMoviePosterSql());

        startActivity(intent);

    }

    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return favouriteMovie data as a Cursor or null if an error occurs.
     * <p>
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mFavouriteMoviesData = null;

            @Override
            protected void onStartLoading() {
                if (mFavouriteMoviesData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mFavouriteMoviesData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(
                            FavouritesContract.FavouritesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavouritesContract.FavouritesEntry._ID
                    );

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mFavouriteMoviesData = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        /** Here We Are Accessing The SQLite Query We Received In The Method getAllMovies() Which Is Set To Read All Rows
         * We're Going Through Each Row With A For Loop And Putting Them Into Our FavouriteMovie Model
         *
         * @param cursor
         */

        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            for (int count = 0; count < data.getCount(); count++) {

                FavouriteMovie favouriteMovie = new FavouriteMovie(

                        data.getLong(data.getColumnIndex(FavouritesContract.FavouritesEntry._ID)),
                        data.getString(data.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID)),
                        data.getBlob(data.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_MOVIE_POSTER))

                );

                Log.v(TAG, "Row_Id" + data.getLong(data.getColumnIndex(FavouritesContract.FavouritesEntry._ID)));

                favouriteMovies.add(favouriteMovie);

                Log.v(TAG, "DATA LOADED BY onLoadFinished: " + favouriteMovies);

                data.moveToNext();
            }

            FavouritesAdapter favouritesAdapter = new FavouritesAdapter(favouriteMovies, getApplicationContext(), FavouriteMoviesActivity.this);
            favouriteRecyclerView.setAdapter(favouritesAdapter);

        } else {
            Log.v(TAG, "cursor is Empty");
        }

        assert data != null;
        data.close();
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
