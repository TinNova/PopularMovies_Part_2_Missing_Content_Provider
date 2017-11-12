package com.example.tin.popularmovies.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tin.popularmovies.Adapters.MovieAdapter;
import com.example.tin.popularmovies.Models.Movie;
import com.example.tin.popularmovies.NetworkUtils;
import com.example.tin.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.tin.popularmovies.NetworkUtils.POPULAR_PATH;
import static com.example.tin.popularmovies.NetworkUtils.TOP_RATED_PATH;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    // TAG to help catch errors in Log
    private static final String TAG = MainActivity.class.getSimpleName();
    // Key Strings For Save Instance State
    private static final String MENU_ITEM_SELECTED = "menu_item_selected";
    private static final String FILTER_TYPE = "filter_type";


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;

    private List<Movie> movies;

    private ImageView noWifiIcon;
    private TextView noWifiText;
    private Button retryWifiButton;

    // How to filter the view, 0 = Popular Films, 1 = Top Rated Films
    private int filterType = 0;
    private int menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noWifiIcon = (ImageView) findViewById(R.id.no_internet_icon);
        noWifiText = (TextView) findViewById(R.id.no_internet_txt);
        retryWifiButton = (Button) findViewById(R.id.retry_internet_connection);

        // This will be used to attach the RecyclerView to the MovieAdapter
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        // This will improve performance by stating that changes in the content will not change
        // the child layout size in the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        /*
         * A LayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView as well as determining the policy for when to recycle item views that
         * are no longer visible to the user.
         */
        GridLayoutManager mGridLayoutManager =
                new GridLayoutManager(this, 2);

        // Set the mRecyclerView to the layoutManager so it can handle the positioning of the items
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        movies = new ArrayList<>();

        if (savedInstanceState != null) {
            // Setting the values retrieved from onStateInstanceState
            menuId = savedInstanceState.getInt(MENU_ITEM_SELECTED);
            filterType = savedInstanceState.getInt(FILTER_TYPE);
        }


        // Check if connected to internet, if false show an error
        // if true start the AsyncTask to fetch the Movie data
        if (!isOnline()) {


            noWifiIcon.setVisibility(View.VISIBLE);
            noWifiText.setVisibility(View.VISIBLE);
            retryWifiButton.setVisibility(View.VISIBLE);

            Toast.makeText(this, "There Is No Internet Connection", Toast.LENGTH_SHORT).show();

            retryWifiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!isOnline()) {

                        Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();

                    } else {

                        MakeMovieDatabaseSearchQuery();

                        noWifiIcon.setVisibility(View.GONE);
                        noWifiText.setVisibility(View.GONE);
                        retryWifiButton.setVisibility(View.GONE);

                    }

                }
            });

        } else {

            MakeMovieDatabaseSearchQuery();

            noWifiIcon.setVisibility(View.GONE);
            noWifiText.setVisibility(View.GONE);
            retryWifiButton.setVisibility(View.GONE);

        }

    }

    /**
     * Menu button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Putting the integer values as filterType and menuId are both int based values
        outState.putInt(MENU_ITEM_SELECTED, menuId);
        outState.putInt(FILTER_TYPE, filterType);
    }

    /**
     * Dropdown list of options when Menu button has been clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Response to a click on the "View By Popularity" menu option
            case R.id.filter_by_popular:

                // If the filterType is already 0 (aka Popular), then do nothing, else, clear the movies list
                // so it's empty, then switch to Popular.
                // This saves us having to create a new internet connection needlessly
                if (filterType == 0) {
                    return true;
                } else {
                    movies.clear();
                    filterType = 0;
                    MakeMovieDatabaseSearchQuery();
                    return true;
                }

                // Response to a click on the "View By Ratings" menu option
            case R.id.filter_by_rating:

                // If the filter is already 1 (aka Top Rated), then do nothing, else, clear the movies list
                // so it's empty, then switch to Top Rated.
                // This saves us having to create a new internet connection needlessly
                if (filterType == 1) {
                    return true;
                } else {
                    movies.clear();
                    filterType = 1;
                    MakeMovieDatabaseSearchQuery();
                    return true;
                }

            case R.id.view_favourite_movies:

                Intent favouriteMoviesIntent = new Intent(this, FavouriteMoviesActivity.class);
                startActivity(favouriteMoviesIntent);

        }
        return super.onOptionsItemSelected(item);
    }

    /* This tells the NetworkUtils Class to build the Url of the Movie Database Feed
    * It then saves the URL as a URL variable called movieDatabaseSearchUrl
    * It then tells the AsyncTask to start a network connection using the newly created URL
    */
    private void MakeMovieDatabaseSearchQuery() {

        // By default we filter by Popularity, therefore we pass in the POPULAR_PATH String, else
        // we pass in the TOP_RATED_PATH String
        if (filterType == 0) {

            // Reset the previously pulled data
            //mFeedResultTextView.setText(null);

            // Tell NetworkUtils to "buildMovieUrl" then saves it as a URL variable
            URL movieDatabaseSearchUrl = NetworkUtils.buildMovieUrl(POPULAR_PATH);

            // We now pass that URL variable to the AsyncTask to create a connection and give us the feed
            new FetchMoviesAsyncTask().execute(movieDatabaseSearchUrl);

        } else {

            // Reset the previously pulled data
            //mFeedResultTextView.setText(null);

            // Tell NetworkUtils to "buildMovieUrl" then saves it as a URL variable
            URL movieDatabaseSearchUrl = NetworkUtils.buildMovieUrl(TOP_RATED_PATH);

            // We now pass that URL variable to the AsyncTask to create a connection and give us the feed
            new FetchMoviesAsyncTask().execute(movieDatabaseSearchUrl);

        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra("MoviePoster", movies.get(clickedItemIndex).getPosterImageUrl());
        intent.putExtra("MovieTitle", movies.get(clickedItemIndex).getMovieTitle());
        intent.putExtra("MovieSynopsis", movies.get(clickedItemIndex).getMovieSynopsis());
        intent.putExtra("MovieUserRating", movies.get(clickedItemIndex).getMovieUserRating());
        intent.putExtra("MovieReleaseDate", movies.get(clickedItemIndex).getMovieReleaseDate());
        intent.putExtra("MovieId", movies.get(clickedItemIndex).getMovieId());

        startActivity(intent);

    }


    private class FetchMoviesAsyncTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieResults = null;

            try {

                movieResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            } catch (IOException e) {
                e.printStackTrace();

            }

            return movieResults;
        }

        @Override
        protected void onPostExecute(String movieResults) {
            if (movieResults != null && !movieResults.equals("")) {

                /** PARSING JSON */

                try {
                    // Define the entire feed as a JSONObject
                    JSONObject theMovieDatabaseJsonObject = new JSONObject(movieResults);
                    // Define the "results" JsonArray as a JSONArray
                    JSONArray resultsArray = theMovieDatabaseJsonObject.getJSONArray("results");
                    // Now we need to get the individual Movie JsonObjects from the resultArray
                    // using a for loop
                    for (int i = 0; i < resultsArray.length(); i++) {

                        JSONObject movieJsonObject = resultsArray.getJSONObject(i);

                        Movie movie = new Movie(
                                movieJsonObject.getString("poster_path"),
                                movieJsonObject.getString("title"),
                                movieJsonObject.getString("overview"),
                                movieJsonObject.getString("vote_average"),
                                movieJsonObject.getString("release_date"),
                                movieJsonObject.getString("id")
                        );

                        movies.add(movie);

                        Log.v(TAG, "Movies List: " + movie);

                    }

                    adapter = new MovieAdapter(movies, getApplicationContext(), MainActivity.this);
                    mRecyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /** Helper Code that checks if device is connected to the internet */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();

    }

}

/**
 * EXTRA TODOS TO MAKE THE APP BETTER
 */
//COMPLETED A. Prevent the same film being added twice to SQL.
//COMPLETED B. DONE Be able to see if Movie is favourite even when checking it from either the MainActivity or the Favourite Activity
//TODO C. Fix the back navigation, If in SQL Detail Activity Mode and go Back you land on MainActivity instead of FavouriteMovie Activity
//TODO D. Switch from FavouriteMovieActivity to Fragment Instead??
//COMPLETED E. When Movie Is Deleted Ensure The FavouriteMovieActivity Refreshes Using The BroadCast Receiver??
//COMPLETED F. When a new film is marked as favourite it auto return to the MainActivity, instead it should stay on the detail activity page
//TODO G. When deleting the last film from the SQL, the last film remains in the list instead of being deleted
//TODO H. Add a Collapsing Toolbar to DetailActivity
//TODO I. Make app fullscreen (remove the blue default header)
//COMPLETED J. App crashed on rotation on Favourites List
//COMPLETED K. When on top_rated on rotation it loads the popular list
//TODO L. Hardcode dimensions
//TODO M. Add a collapsing toolbar to the detail activity
//TODO N. LogCat Error keeps saying "E/RecyclerView: No adapter attached; skipping layout"
//TODO O. Use Parceble in the Model Lists to pass the data
//TODO P. Use a Bundle in the intents to pass data
//TODO Q. Save the scroll position in the DetailActivity
//TODO R. Combine MovieActivity & FavouriteMoviesActivity (consider Fragment or another method)
//TODO S. On MainActivity Landscape mode, use a 4 column grid instead of 2