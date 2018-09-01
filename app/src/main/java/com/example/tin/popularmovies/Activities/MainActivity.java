package com.example.tin.popularmovies.Activities;

import android.content.Intent;
import android.os.AsyncTask;
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

public class MainActivity extends AppCompatActivity implements MainContract.MainScreen, MovieAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    // Key Strings For Save Instance State
    private static final String MENU_ITEM_SELECTED = "menu_item_selected";
    private static final String FILTER_TYPE = "filter_type";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;

    private List<Movie> mMovies;

    private ImageView noWifiIcon;
    private TextView noWifiText;
    private Button retryWifiButton;

    // How to filter the view, 0 = Popular Films, 1 = Top Rated Films
    private int filterType = 0;
    private boolean popularFilms = true;
    private int menuId;

    private MainPresenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter(this);

        setupViews(savedInstanceState);

    }


    private void setupViews(Bundle savedInstanceState) {

        noWifiIcon = (ImageView) findViewById(R.id.no_internet_icon);
        noWifiText = (TextView) findViewById(R.id.no_internet_txt);
        retryWifiButton = (Button) findViewById(R.id.retry_internet_connection);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager =
                new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mMovies = new ArrayList<>();

        if (savedInstanceState != null) {
            // Setting the values retrieved from onStateInstanceState
            menuId = savedInstanceState.getInt(MENU_ITEM_SELECTED);
            filterType = savedInstanceState.getInt(FILTER_TYPE);
        }

        /* Checking for internet connection */
        mainPresenter.isOnline(this);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_by_popular:

                // If the popularFilms is true, then do nothing
                if (popularFilms) {
                    return true;
                } else {
                    mMovies.clear();
                    popularFilms = true;
                    MakeMovieDatabaseSearchQuery();
                    return true;
                }

                // Response to a click on the "View By Ratings" menu option
            case R.id.filter_by_rating:

                // If the popularFilms is false, then do nothing
                if (!popularFilms) {
                    return true;
                } else {
                    mMovies.clear();
                    popularFilms = false;
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
        if (popularFilms) {

            URL movieDatabaseSearchUrl = NetworkUtils.buildMovieUrl(POPULAR_PATH);
            new FetchMoviesAsyncTask().execute(movieDatabaseSearchUrl);

        } else {

            URL movieDatabaseSearchUrl = NetworkUtils.buildMovieUrl(TOP_RATED_PATH);
            new FetchMoviesAsyncTask().execute(movieDatabaseSearchUrl);

        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra("MoviePoster", mMovies.get(clickedItemIndex).getPosterImageUrl());
        intent.putExtra("MovieTitle", mMovies.get(clickedItemIndex).getMovieTitle());
        intent.putExtra("MovieSynopsis", mMovies.get(clickedItemIndex).getMovieSynopsis());
        intent.putExtra("MovieUserRating", mMovies.get(clickedItemIndex).getMovieUserRating());
        intent.putExtra("MovieReleaseDate", mMovies.get(clickedItemIndex).getMovieReleaseDate());
        intent.putExtra("MovieId", mMovies.get(clickedItemIndex).getMovieId());

        startActivity(intent);

    }

    @Override
    public void showNoConnection() {

        if (!(noWifiIcon.getVisibility() == View.VISIBLE)) {

            noWifiIcon.setVisibility(View.VISIBLE);
            noWifiText.setVisibility(View.VISIBLE);
            retryWifiButton.setVisibility(View.VISIBLE);

            retryWifiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mainPresenter.isOnline(getBaseContext());
                }
            });
        }
        Toast.makeText(getBaseContext(), "There Is No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideNoConnection() {

        MakeMovieDatabaseSearchQuery();

        noWifiIcon.setVisibility(View.GONE);
        noWifiText.setVisibility(View.GONE);
        retryWifiButton.setVisibility(View.GONE);
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

                        mMovies.add(movie);

                        Log.v(TAG, "Movies List: " + movie);

                    }

                    adapter = new MovieAdapter(mMovies, getApplicationContext(), MainActivity.this);
                    mRecyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Putting the integer values as filterType and menuId are both int based values
        outState.putInt(MENU_ITEM_SELECTED, menuId);
        outState.putInt(FILTER_TYPE, filterType);
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
//TODO M. Add a Collapsing ToolbarLayout to the activity_detail.xml (use the backdrop_path image from the json feed)
//TODO N. LogCat Error keeps saying "E/RecyclerView: No adapter attached; skipping layout"
//TODO O. Use Parceble in the Model Lists to pass the data
//TODO P. Use a Bundle in the intents to pass data
//TODO Q. Save the scroll position in the DetailActivity
//TODO R. Combine MovieActivity & FavouriteMoviesActivity (consider Fragment or another method)
//TODO S. On MainActivity Landscape mode, use a 4 column grid instead of 2
//TODO T. Hard code all of the dimensions

// REVIEWER SUGGESTIONS
//TODO R1 activity_detail.xml Instead of LinearLayout use ConstrainLayout.
// WHY: It is more time consuming to build a layout with LinearLayout and it takes more processing power if the layout is very complex
// Resources:
//           Android: https://developer.android.com/training/constraint-layout/index.html
//TODO R2 Use The Library ButterKnife which makes view initialisation easier by eliminating "findViewById()" - http://jakewharton.github.io/butterknife/
//TODO R3 Instead of Toast you can try a SnakeBar & SnakeBar w/Action - https://developer.android.com/training/snackbar/action.html
//TODO R4 FavouriteDbHelper Dropping a table is not good practise instead it should save the data then update the table
// Resrouces:
//           https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
//           http://zetcode.com/db/sqlite/tables/
//TODO R5 Use RetroFit For The Network Calls - https://square.github.io/retrofit/
//TODO R6 CastMemberAdapter in onBindViewHolder add ".error" to the Picasso code, so that if an image is not
//          available from the JSON it can place a default image instead of an empty space!
// Example: Picasso.with(context)
//              .load(url)
//              .placeholder(R.drawable.user_placeholder)
//              .error(R.drawable.user_placeholder_error)
//              .into(imageView);

// TODOs FOR LEARNING PURPOSES
//TODO Try include a Service in the app: Part 1 Lesson 10
//TODO Try include Notifications in the app: Part 1 Lesson 10
//TODO Try include BroadCast Receivers in the app: Part 1 Lesson 10