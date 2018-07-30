package com.example.tin.popularmovies.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tin.popularmovies.Adapters.CastMemberAdapter;
import com.example.tin.popularmovies.Adapters.ReviewAdapter;
import com.example.tin.popularmovies.Adapters.TrailerAdapter;
import com.example.tin.popularmovies.Data.FavouritesContract;
import com.example.tin.popularmovies.Models.CastMember;
import com.example.tin.popularmovies.Models.Review;
import com.example.tin.popularmovies.Models.Trailer;
import com.example.tin.popularmovies.NetworkUtils;
import com.example.tin.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.tin.popularmovies.NetworkUtils.MOVIE_ID_CREDITS;
import static com.example.tin.popularmovies.NetworkUtils.MOVIE_ID_REVIEWS;
import static com.example.tin.popularmovies.NetworkUtils.MOVIE_ID_TRAILERS;

public class DetailActivity extends AppCompatActivity implements DetailContract.DetailScreen, TrailerAdapter.TrailerListItemClickListener {

    // TAG to help catch errors in Log
    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final String LIST_STATE_KEY = "list_state_key";
    ScrollView mScrollView;

    private static final String GET_DETAIL_SEARCH_URL = "get_detail_search_url";
    private static final String GET_TRAILER_SEARCH_URL = "get_trailer_search_url";
    private static final String GET_CAST_SEARCH_URL = "get_cast_search_url";
    private static final String GET_REVIEW_SEARCH_URL = "get_review_search_url";

    private static final int GET_DETAIL_LOADER = 1;
    private static final int GET_TRAILER_LOADER = 2;
    private static final int GET_CAST_LOADER = 3;
    private static final int GET_REVIEW_LOADER = 4;

    // RecyclerView For Trailer
    private final String YOUTUBETRAILERSTART = "https://www.youtube.com/watch?v=";
    private RecyclerView trailerRecyclerView;
    private RecyclerView castMemberRecyclerView;
    private RecyclerView reviewRecyclerView;
    private RecyclerView.Adapter trailerAdapter;
    private List<Trailer> trailers;
    private List<CastMember> castMembers;
    private List<Review> reviews;

    private ImageView mMoviePosterIV;
    private TextView mMovieTitleTV;
    private TextView mMovieSynopsisTV;
    private TextView mMovieUserRatingTV;
    private TextView mMovieReleaseDateTV;

    //LayoutManagers
    LinearLayoutManager trailerLinearLayoutManager;
    LinearLayoutManager castMemberLinearLayoutManager;
    LinearLayoutManager reviewLinearLayoutManager;

    //private String mMovieId;
    private String movieId;
    private String movieTitle;
    private long row_id;

    // This Is For The Favourite Icon In The Menu Item
    private MenuItem favouriteMenu;

    // Determines If A Movie Is Favourite Or Not, 0 = Not Favourite, 1 = Favourite
    private int favourite_NotFavourite;

    private DetailPresenter detailPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailPresenter = new DetailPresenter(this);

        mMoviePosterIV = (ImageView) findViewById(R.id.movie_image);
        mMovieTitleTV = (TextView) findViewById(R.id.movie_title);
        mMovieSynopsisTV = (TextView) findViewById(R.id.movie_synopsis);
        mMovieUserRatingTV = (TextView) findViewById(R.id.movie_rating);
        mMovieReleaseDateTV = (TextView) findViewById(R.id.movie_release_date);

        mScrollView = (ScrollView) findViewById(R.id.detail_activity_scroll);

        Intent intentThatStartedThisActivity = getIntent();

        //If DetailActivity was triggered by the popular or top_rated list (aka MainActivity)
        if (intentThatStartedThisActivity.hasExtra("MovieId")) {

            String moviePoster = intentThatStartedThisActivity.getStringExtra("MoviePoster");
            movieTitle = intentThatStartedThisActivity.getStringExtra("MovieTitle");
            String movieSynopsis = intentThatStartedThisActivity.getStringExtra("MovieSynopsis");
            String movieUserRating = intentThatStartedThisActivity.getStringExtra("MovieUserRating");
            String movieReleaseDate = intentThatStartedThisActivity.getStringExtra("MovieReleaseDate");
            movieId = intentThatStartedThisActivity.getStringExtra("MovieId");

            /** Check If The Movie Is Saved As A Favourite Movie In The Database */
            // If it is in the database, we mark it as favourite in the Heart Icon else, it is marked as not favourite
            // This is important to prevent the same movie being added to the database twice.
            isMovieFavouriteFull();

            Picasso.with(this).load(moviePoster).into(mMoviePosterIV);
            mMovieTitleTV.setText(movieTitle);
            mMovieSynopsisTV.setText(movieSynopsis);
            mMovieUserRatingTV.setText(movieUserRating);
            mMovieReleaseDateTV.setText(movieReleaseDate);

            MakeDetailUrlSearchQuery();
            Organise_RecyclerView_And_LayoutManagers();

            //Else if DetailActivity was triggered by the FavouriteMoviesActivity
        } else if (intentThatStartedThisActivity.hasExtra("Row_Id")) {

            // The Heart Icon Is Fully White Indicating It Is Favourite
            favourite_NotFavourite = 1;


            // In getLongExtra the second value is the default value that will be used if the Long can't be found
            row_id = intentThatStartedThisActivity.getLongExtra("Row_Id", -1);
            Log.v(TAG, "Row_ID: " + row_id);

            movieId = intentThatStartedThisActivity.getStringExtra("MovieSqlId");

            makeGetDetailSearchQuery();
            MakeDetailUrlSearchQuery();
            Organise_RecyclerView_And_LayoutManagers();

        }

    }


    /**
     * Menu button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        favouriteMenu = menu.findItem(R.id.favourite);

        // Assign Correct Heart Icon: If 0 = Not Favourite, so add the white border icon
        if (favourite_NotFavourite == 0) {
            favouriteMenu.setIcon(R.drawable.ic_favorite_border_white_24dp);
            // else if 1 = Favourite, so add the full white icon
        } else {
            favouriteMenu.setIcon(R.drawable.ic_favorite_white_24dp);
        }

        return true;
    }

    /**
     * This Code Creates The Menu Section Where You Can Favourite & Unfavourite A Movie
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            case R.id.favourite:
                // If the movie is NOT in the favourite database, add it to favourites
                if (isMovieFavourite() == null) {

                    // Change the Heart Icon from white outline to white heart
                    favouriteMenu.setIcon(R.drawable.ic_favorite_white_24dp);

                    // Method which adds Movie to SQL
                    convertImageViewAndAddDataToSql(mMoviePosterIV);
                    Toast.makeText(this, "Added To Favourites!", Toast.LENGTH_SHORT).show();


                    // Else the movie IS in the favourite database, so remove it
                } else {

                    // Change the Heart Icon from white heart to white outline
                    favouriteMenu.setIcon(R.drawable.ic_favorite_border_white_24dp);

                    // Method which deletes Movie from SQL
                    removeMovie(row_id);
                    Toast.makeText(this, "Removed From Favourites!", Toast.LENGTH_SHORT).show();

                }

        }

        return super.onOptionsItemSelected(item);

    }


    // Method For Starting An Explicit Intent To Launch The Trailer
    private void playTrailer(String url) {

        Uri videoURL = Uri.parse(url);

        Intent playTrailerIntent = new Intent(Intent.ACTION_VIEW);
        playTrailerIntent.setData(videoURL);

        // Checks if there is an App that can handle this intent, if there isn't display a toast
        // Without this if statement the app would crash if it couldn't find an app to open the intent
        if (playTrailerIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(playTrailerIntent);
        } else {
            Toast.makeText(this, "Can't Play Video", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        playTrailer(YOUTUBETRAILERSTART + trailers.get(clickedItemIndex).getTrailerKey());

    }

    private LoaderManager.LoaderCallbacks<String> getDetailLoader = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<String>(getApplicationContext()) {

                // This String will contain the raw JSON from the getDetails search
                String mGetDetailJson;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }

                    // When we begin loading in the background, display the loading indicator to
                    // the user
                    //mLoadingIndicator.setVisibility(View.VISIBLE);

                    // If the raw Json is cached within the String mGetDetailJson, simply deliver that
                    // json, avoid reloading the data. Else if it is not cached then reload the data
                    if (mGetDetailJson != null) {
                        deliverResult(mGetDetailJson);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public String loadInBackground() {

                    // Extract the search query from the args using the constant
                    String searchQueryUrlString = args.getString(GET_DETAIL_SEARCH_URL);

                    try {
                        URL getDetailUrl = new URL(searchQueryUrlString);
                        String getDetailUrlResults = NetworkUtils.getResponseFromHttpUrl(getDetailUrl);

                        return getDetailUrlResults;

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                // Store the raw Json in the String mGetDetailJson
                @Override
                public void deliverResult(String getDetailJson) {
                    mGetDetailJson = getDetailJson;
                    super.deliverResult(getDetailJson);

                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (data != null && !data.equals("")) {

                /** PARSING JSON */
                try {

                    // Parse The Raw Json Into The Views
                    Picasso.with(DetailActivity.this)
                            .load(NetworkUtils.BASE_IMAGE_URL + new JSONObject(data).getString("poster_path"))
                            .into(mMoviePosterIV);
                    mMovieTitleTV.setText(new JSONObject(data).getString("original_title"));
                    mMovieSynopsisTV.setText(new JSONObject(data).getString("overview"));
                    mMovieUserRatingTV.setText(new JSONObject(data).getString("vote_average"));
                    mMovieReleaseDateTV.setText(new JSONObject(data).getString("release_date"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }


        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<String> getTrailerLoader = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<String>(getApplicationContext()) {

                // This String will contain the raw JSON from the getDetails search
                String mGetTrailerJson;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }

                    // When we begin loading in the background, display the loading indicator to
                    // the user
                    //mLoadingIndicator.setVisibility(View.VISIBLE);

                    // If the raw Json is cached within the String mGetDetailJson, simply deliver that
                    // json, avoid reloading the data. Else if it is not cached then reload the data
                    if (mGetTrailerJson != null) {
                        deliverResult(mGetTrailerJson);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public String loadInBackground() {

                    // Extract the search query from the args using the constant
                    String searchQueryUrlString = args.getString(GET_TRAILER_SEARCH_URL);

                    try {

                        URL getTrailerUrl = new URL(searchQueryUrlString);
                        String movieTrailerResults = NetworkUtils.getResponseFromHttpUrl(getTrailerUrl);

                        return movieTrailerResults;

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                // Store the raw Json in the String mGetDetailJson
                @Override
                public void deliverResult(String getJson) {
                    mGetTrailerJson = getJson;
                    super.deliverResult(getJson);

                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (data != null && !data.equals("")) {

                /** PARSING JSON */
                try {
                    // Define the entire feed as a JSONObject
                    JSONObject theMovieDatabaseJsonObject = new JSONObject(data);
                    // Define the "results" JsonArray as a JSONArray
                    JSONArray resultsArray = theMovieDatabaseJsonObject.getJSONArray("results");
                    // Now we need to get the individual Movie JsonObjects from the resultArray
                    // using a for loop
                    for (int i = 0; i < resultsArray.length(); i++) {

                        JSONObject movieJsonObject = resultsArray.getJSONObject(i);

                        Trailer trailer = new Trailer(
                                movieJsonObject.getString("name"),
                                movieJsonObject.getString("key")
                        );

                        trailers.add(trailer);

                        Log.v(TAG, "Trailers List: " + trailers);
                    }

                    trailerAdapter = new TrailerAdapter(trailers, getApplicationContext(), DetailActivity.this);
                    trailerRecyclerView.setAdapter(trailerAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<String> getCastLoader = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<String>(getApplicationContext()) {

                // This String will contain the raw JSON from the getDetails search
                String mGetCastJson;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }

                    // When we begin loading in the background, display the loading indicator to
                    // the user
                    //mLoadingIndicator.setVisibility(View.VISIBLE);

                    // If the raw Json is cached within the String mGetDetailJson, simply deliver that
                    // json, avoid reloading the data. Else if it is not cached then reload the data
                    if (mGetCastJson != null) {
                        deliverResult(mGetCastJson);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public String loadInBackground() {

                    // Extract the search query from the args using the constant
                    String searchQueryUrlString = args.getString(GET_CAST_SEARCH_URL);

                    try {

                        URL getCastUrl = new URL(searchQueryUrlString);
                        String movieCastResults = NetworkUtils.getResponseFromHttpUrl(getCastUrl);

                        return movieCastResults;

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                // Store the raw Json in the String mGetDetailJson
                @Override
                public void deliverResult(String getJson) {
                    mGetCastJson = getJson;
                    super.deliverResult(getJson);

                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (data != null && !data.equals("")) {

                /** PARSING JSON */
                try {
                    // Define the entire feed as a JSONObject
                    JSONObject theMovieDatabaseJsonObject = new JSONObject(data);
                    // Define the "results" JsonArray as a JSONArray
                    JSONArray resultsArray = theMovieDatabaseJsonObject.getJSONArray("cast");
                    // Now we need to get the individual Movie JsonObjects from the resultArray
                    // using a for loop
                    for (int i = 0; i < resultsArray.length(); i++) {

                        JSONObject movieJsonObject = resultsArray.getJSONObject(i);

                        CastMember castMember = new CastMember(
                                movieJsonObject.getString("character"),
                                movieJsonObject.getString("name"),
                                movieJsonObject.getString("profile_path")
                        );

                        castMembers.add(castMember);

                        Log.v(TAG, "CastMembers List: " + castMembers);
                    }

                    RecyclerView.Adapter castMemberAdapter = new CastMemberAdapter(castMembers, getApplicationContext());
                    castMemberRecyclerView.setAdapter(castMemberAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<String> getReviewLoader = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<String>(getApplicationContext()) {

                // This String will contain the raw JSON from the getDetails search
                String mReviewJson;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }

                    // When we begin loading in the background, display the loading indicator to
                    // the user
                    //mLoadingIndicator.setVisibility(View.VISIBLE);

                    // If the raw Json is cached within the String mGetDetailJson, simply deliver that
                    // json, avoid reloading the data. Else if it is not cached then reload the data
                    if (mReviewJson != null) {
                        deliverResult(mReviewJson);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public String loadInBackground() {

                    // Extract the search query from the args using the constant
                    String searchQueryUrlString = args.getString(GET_REVIEW_SEARCH_URL);

                    try {

                        URL getReviewUrl = new URL(searchQueryUrlString);
                        String movieReviewResults = NetworkUtils.getResponseFromHttpUrl(getReviewUrl);

                        return movieReviewResults;

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                // Store the raw Json in the String mGetDetailJson
                @Override
                public void deliverResult(String getJson) {
                    mReviewJson = getJson;
                    super.deliverResult(getJson);

                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (data != null && !data.equals("")) {

                /** PARSING JSON */
                try {
                    // Define the entire feed as a JSONObject
                    JSONObject theMovieDatabaseJsonObject = new JSONObject(data);
                    // Define the "results" JsonArray as a JSONArray
                    JSONArray resultsArray = theMovieDatabaseJsonObject.getJSONArray("results");
                    // Now we need to get the individual Movie JsonObjects from the resultArray
                    // using a for loop
                    for (int i = 0; i < resultsArray.length(); i++) {

                        JSONObject movieJsonObject = resultsArray.getJSONObject(i);

                        Review review = new Review(
                                movieJsonObject.getString("author"),
                                movieJsonObject.getString("content")
                        );

                        reviews.add(review);

                        Log.v(TAG, "Reviews List: " + reviews);
                    }

                    RecyclerView.Adapter reviewAdapter = new ReviewAdapter(reviews, getApplicationContext());
                    reviewRecyclerView.setAdapter(reviewAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    /**
     * Code Which Add A Film To The SQL Database
     */
    // Code that adds a new movie into the Favourite Movies Sql
    private void addNewMovie(String movieIdSql, String movieTitleSql, byte[] moviePosterByteArraySql) {

        // ContentValues passes the values onto the SQLite insert query
        ContentValues cv = new ContentValues();

        // We don't need to include the ID of the row, because BaseColumns in the Contract Class does this
        // for us. If we didn't have the BaseColumns we would have to add the ID ourselves.
        cv.put(FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID, movieIdSql);
        cv.put(FavouritesContract.FavouritesEntry.COLUMN_MOVIE_NAME, movieTitleSql);
        cv.put(FavouritesContract.FavouritesEntry.COLUMN_MOVIE_POSTER, moviePosterByteArraySql);

        // Insert the new movie to the Favourite SQLite Db via a ContentResolver
        Uri uri = getContentResolver().insert(FavouritesContract.FavouritesEntry.CONTENT_URI, cv);

        // Display the URI that's returned with a Toast
        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        }

        // Here we return the mDb.insert Method, and specify the Table Name, and the ContentValues object
        // This will return a new row in the table with the values specified in the cv
        // Note: We didn't insert the Row ID, that's because we specified in the SQL onCreate statement
        //       That the row _ID will autoincrement
        //return mDb.insert(FavouritesContract.FavouritesEntry.TABLE_NAME, null, cv);

    }


    // This code converts the ImageView into a Bitmap, then into a byteArray "byte[]"
    private void convertImageViewAndAddDataToSql(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        String mMovieTitle2 = (String) mMovieTitleTV.getText();
        addNewMovie(movieId, mMovieTitle2, data);

        Log.v(TAG, "addNewMovie Params: " + movieId + ", " + movieTitle + ", " + data);

    }

    // Here we create the GetDetailSearchQuery and Initiate the Loader
    private void makeGetDetailSearchQuery() {
        // Build the URL using the movieId
        URL getDetailSearchUrl = NetworkUtils.buildGetDetailUrl(movieId);

        // Loaders Take A Bundle So Insert The URL build above Into A Bundle
        Bundle getDetailBundle = new Bundle();
        getDetailBundle.putString(GET_DETAIL_SEARCH_URL, getDetailSearchUrl.toString());

        Log.v(TAG, "MakeGetDetailUrlSearchQuery: " + getDetailSearchUrl);

        // Call getSupportLoaderManager and store it in a LoaderManger variable
        LoaderManager loaderManagerGetDetail = getSupportLoaderManager();

        // Get our Loader by calling getLoader and passing the ID we specified for this loader
        Loader<String> getDetailSearchLoader = loaderManagerGetDetail.getLoader(GET_DETAIL_LOADER);

        // If the Loader was null, initialize it. Else restart it.
        if (getDetailSearchLoader == null) {

            loaderManagerGetDetail.initLoader(GET_DETAIL_LOADER, getDetailBundle, getDetailLoader);
        } else {
            loaderManagerGetDetail.restartLoader(GET_DETAIL_LOADER, getDetailBundle, getDetailLoader);
        }
    }

    /**
     * This Method Has Two Parts:
     * 1. It instructs the NetworkUtils to build the URLs for the Trailers, Cast Members & Reviews APIs
     * 2. It sets off the AsyncTasks for each of those APIs
     */
    private void MakeDetailUrlSearchQuery() {

        // Tells NetworkUtils to "buildDetailUrl" then saves it as a URL variable
        URL trailerSearchUrl = NetworkUtils.buildDetailUrl(movieId, MOVIE_ID_TRAILERS);
        URL castMembersSearchUrl = NetworkUtils.buildDetailUrl(movieId, MOVIE_ID_CREDITS);
        URL reviewsSearchUrl = NetworkUtils.buildDetailUrl(movieId, MOVIE_ID_REVIEWS);

        // Loaders Take A Bundle So Insert The URL build above Into A Bundle
        Bundle getTrailerBundle = new Bundle();
        Bundle getCastBundle = new Bundle();
        Bundle getReviewBundle = new Bundle();
        getTrailerBundle.putString(GET_TRAILER_SEARCH_URL, trailerSearchUrl.toString());
        getCastBundle.putString(GET_CAST_SEARCH_URL, castMembersSearchUrl.toString());
        getReviewBundle.putString(GET_REVIEW_SEARCH_URL, reviewsSearchUrl.toString());

        // Call getSupportLoaderManager and store it in a LoaderManger variable
        LoaderManager loaderManagerTrailer = getSupportLoaderManager();
        LoaderManager loaderManagerCast = getSupportLoaderManager();
        LoaderManager loaderManagerReview = getSupportLoaderManager();

        // Get our Loader by calling getLoader and passing the ID we specified for this loader
        Loader<String> getTrailerSearchLoader = loaderManagerTrailer.getLoader(GET_TRAILER_LOADER);
        Loader<String> getCastSearchLoader = loaderManagerCast.getLoader(GET_CAST_LOADER);
        Loader<String> getReviewSearchLoader = loaderManagerReview.getLoader(GET_REVIEW_LOADER);

        // We now pass the Bundle to the Loader to create a connection and give us the feed
        // If the Loader was null, initialize it. Else restart it.
        if (getTrailerSearchLoader == null){

            loaderManagerTrailer.initLoader(GET_TRAILER_LOADER, getTrailerBundle, getTrailerLoader);
        } else {
            loaderManagerTrailer.restartLoader(GET_TRAILER_LOADER, getTrailerBundle, getTrailerLoader);
        }

        if (getCastSearchLoader == null){

            loaderManagerCast.initLoader(GET_CAST_LOADER, getCastBundle, getCastLoader);
        } else {
            loaderManagerCast.restartLoader(GET_CAST_LOADER, getCastBundle, getCastLoader);
        }

        if (getReviewSearchLoader == null){

            loaderManagerReview.initLoader(GET_REVIEW_LOADER, getReviewBundle, getReviewLoader);
        } else {
            loaderManagerReview.restartLoader(GET_REVIEW_LOADER, getReviewBundle, getReviewLoader);
        }

    }

    /**
     * Organising the RecyclerView & Layout Managers For Trailers, CastMembers & Eventually Reviews
     */
    private void Organise_RecyclerView_And_LayoutManagers() {

        // This will be used to attach the RecyclerView to the TrailerAdapter
        trailerRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailer);
        castMemberRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_castMember);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_review);
        // This will improve performance by stating that changes in the content will not change
        // the child layout size in the RecyclerView
        trailerRecyclerView.setHasFixedSize(true);
        castMemberRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setHasFixedSize(true);

            /*
            * A LayoutManager is responsible for measuring and positioning item views within a
            * RecyclerView as well as determining the policy for when to recycle item views that
            * are no longer visible to the user.
            */
        trailerLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        castMemberLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        // Set the mRecyclerView to the layoutManager so it can handle the positioning of the items
        trailerRecyclerView.setLayoutManager(trailerLinearLayoutManager);
        castMemberRecyclerView.setLayoutManager(castMemberLinearLayoutManager);
        reviewRecyclerView.setLayoutManager(reviewLinearLayoutManager);


        trailers = new ArrayList<>();
        castMembers = new ArrayList<>();
        reviews = new ArrayList<>();

    }


    /**
     * This Method Deletes a Movie form the Database
     * - It takes a long as the input which is the ID of the Row
     * - It returns a boolean to say if the deletion was successful or not
     */
    private void removeMovie(long id) {

        // Here we are building up the uri using the row_id in order to tell the ContentResolver
        // to delete the item
        String stringRowId = Long.toString(id);
        Uri uri = FavouritesContract.FavouritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringRowId).build();

        getContentResolver().delete(uri, null, null);

    }

    private Cursor isMovieFavourite() {

        // If it is in the database, we mark it as favourite in the Heart Icon else, it is marked as not favourite
        // This is important to prevent the same movie being added to the database twice.

        // Single Item Query: Checking if the movieId is inside the database
        Cursor cursor = getContentResolver().query(
                FavouritesContract.FavouritesEntry.CONTENT_URI,
                null,
                FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{movieId},
                FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID
        );

        // If the movieId is in the database
        if (cursor.getCount() != 0) {

            return cursor;

        } else {

            return null;

        }
    }

    /**
     * Check If The Movie Is Saved As A Favourite Movie In The Database
     */
    private void isMovieFavouriteFull() {

        Cursor cursor = isMovieFavourite();

        Log.v(TAG, "Cursor = " + cursor);

        // If the movieId is in the database
        if (cursor != null) {

            try {
                cursor.moveToFirst();
                // Assign the _ID to the long variable row_id, this allows the user to delete the Movie from the database
                row_id = cursor.getLong(cursor.getColumnIndex(FavouritesContract.FavouritesEntry._ID));

                // The Heart Icon Is Full White Indicating It's Favourite
                favourite_NotFavourite = 1;

            } catch (Exception e) {
                Log.v(TAG, "Couldn't Recognise Cursor: " + cursor);
            }
            // try finally will guarantee the cursor is closed
            finally {
                cursor.close();
            }

            Log.v(TAG, "ROW_ID: " + row_id);

            // Else, the movie isn't in the database
        } else {
            // The Heart Icon Is A White Border Indicating Not Favourite
            favourite_NotFavourite = 0;
        }

    }

}
