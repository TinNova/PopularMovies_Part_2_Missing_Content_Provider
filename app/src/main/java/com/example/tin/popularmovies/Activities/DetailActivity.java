package com.example.tin.popularmovies.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tin.popularmovies.Adapters.CastMemberAdapter;
import com.example.tin.popularmovies.Adapters.ReviewAdapter;
import com.example.tin.popularmovies.Adapters.TrailerAdapter;
import com.example.tin.popularmovies.Data.FavouritesContentProvider;
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

import static com.example.tin.popularmovies.NetworkUtils.MOVIE_ID_TRAILERS;
import static com.example.tin.popularmovies.NetworkUtils.MOVIE_ID_CREDITS;
import static com.example.tin.popularmovies.NetworkUtils.MOVIE_ID_REVIEWS;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerListItemClickListener {

    // TAG to help catch errors in Log
    private static final String TAG = DetailActivity.class.getSimpleName();

    // RecyclerView For Trailer
    private final String YOUTUBETRAILERSTART = "https://www.youtube.com/watch?v=";
    private RecyclerView trailerRecyclerView;
    private RecyclerView castMemberRecyclerView;
    private RecyclerView reviewRecyclerView;
    private RecyclerView.Adapter trailerAdapter;
    private List<Trailer> trailers;
    private List<CastMember> castMembers;
    private List<Review> reviews;


    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMovieSynopsis;
    private TextView mMovieUserRating;
    private TextView mMovieReleaseDate;

    //private String mMovieId;
    private String movieId;
    private String movieTitle;
    private long row_id;

    // This Is For The Favourite Icon In The Menu Item
    private MenuItem favouriteMenu;

    // Determines If A Movie Is Favourite Or Not, 0 = Not Favourite, 1 = Favourite
    private int favourite_NotFavourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMoviePoster = (ImageView) findViewById(R.id.movie_image);
        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMovieSynopsis = (TextView) findViewById(R.id.movie_synopsis);
        mMovieUserRating = (TextView) findViewById(R.id.movie_rating);
        mMovieReleaseDate = (TextView) findViewById(R.id.movie_release_date);

        Intent intentThatStartedThisActivity = getIntent();

        //If DetailActivity was triggered by the MainActivity
        if (intentThatStartedThisActivity.hasExtra("MovieId")) {

            // TODO HERE WE SHOULD QUERY THE CONTENT RESOLVER TO QUERY THE DATABASE, IF THE MOVIE ID OF THIS FILM EXISTS IN THE DATABASE THE FAVOURITE ICON SHOULD BE

            String moviePoster = intentThatStartedThisActivity.getStringExtra("MoviePoster");
            movieTitle = intentThatStartedThisActivity.getStringExtra("MovieTitle");
            String movieSynopsis = intentThatStartedThisActivity.getStringExtra("MovieSynopsis");
            String movieUserRating = intentThatStartedThisActivity.getStringExtra("MovieUserRating");
            String movieReleaseDate = intentThatStartedThisActivity.getStringExtra("MovieReleaseDate");
            movieId = intentThatStartedThisActivity.getStringExtra("MovieId");


            // Here We Query To See If The movieId is in the database
            // If it is in the database, we mark it as favourite in the Heart Icon else, it is marked as not favourite
            // This is important to prevent the same movie being added to the database twice.
            Cursor cursor = getContentResolver().query(
                    FavouritesContract.FavouritesEntry.CONTENT_URI,
                    null,
                    FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID + "=" + movieId,
                    new String[]{},
                    FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID
            );

            Log.v(TAG, "Cursor = " + cursor);

            if (cursor.getCount() != 0){ // The movie is in the database
                // The Heart Icon Is Full White Indicating It's Favourite
                row_id = cursor.getLong(cursor.getColumnIndex(FavouritesContract.FavouritesEntry._ID));
                favourite_NotFavourite = 1;

            } else { // The movie is not in the database
                // The Heart Icon Is A White Border Indicating Not Favourite
                favourite_NotFavourite = 0;
            }

            Picasso.with(this).load(moviePoster).into(mMoviePoster);
            mMovieTitle.setText(movieTitle);
            mMovieSynopsis.setText(movieSynopsis);
            mMovieUserRating.setText(movieUserRating);
            mMovieReleaseDate.setText(movieReleaseDate);

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

            URL getDetailSearchUrl = NetworkUtils.buildGetDetailUrl(movieId);
            new FetchGetDetailAsyncTask().execute(getDetailSearchUrl);
            Log.v(TAG, "MakeGetDetailUrlSearchQuery: " + getDetailSearchUrl);


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

        // WE SHOULD QUERY THE DATABASE FIRST

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
                if (favourite_NotFavourite == 0) {

                    // Change the Heart Icon from white outline to white heart
                    favouriteMenu.setIcon(R.drawable.ic_favorite_white_24dp);
                    // Change int to 1 which represents a favourite film
                    favourite_NotFavourite = 1;
                    // Method which adds Movie to SQL
                    convertImageViewAndAddDataToSql(mMoviePoster);
                    Toast.makeText(this, "Added To Favourites!", Toast.LENGTH_LONG).show();


                    // If the movie IS in the favourite database, remove it
                } else if (favourite_NotFavourite == 1) {

                    // Change the Heart Icon from white heart to white outline
                    favouriteMenu.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    // Change into to 0 which represents a non-favourite film
                    favourite_NotFavourite = 0;
                    // Method which deletes Movie from SQL
                    removeMovie(row_id);
                    Toast.makeText(this, "Removed From Favourites!", Toast.LENGTH_LONG).show();

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


    private class FetchTrailersAsyncTask extends AsyncTask<URL, Void, String> {

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
    }

    private class FetchCastMembersAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieResults = null;


            try {

                movieResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            } catch (IOException e) {
                e.printStackTrace();

            }
            Log.v(TAG, "CastMember: " + movieResults);
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
    }

    private class FetchReviewsAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieResults = null;


            try {

                movieResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            } catch (IOException e) {
                e.printStackTrace();

            }
            Log.v(TAG, "Review: " + movieResults);
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
    }

    private class FetchGetDetailAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieResults = null;


            try {

                movieResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            } catch (IOException e) {
                e.printStackTrace();

            }
            Log.v(TAG, "GetDetail: " + movieResults);
            return movieResults;

        }

        @Override
        protected void onPostExecute(String movieResults) {
            if (movieResults != null && !movieResults.equals("")) {

                /** PARSING JSON */

                try {
                    // Define the entire feed as a JSONObject

                    Picasso.with(DetailActivity.this)
                            .load(NetworkUtils.BASE_IMAGE_URL + new JSONObject(movieResults).getString("poster_path"))
                            .into(mMoviePoster);
                    mMovieTitle.setText(new JSONObject(movieResults).getString("original_title"));
                    mMovieSynopsis.setText(new JSONObject(movieResults).getString("overview"));
                    mMovieUserRating.setText(new JSONObject(movieResults).getString("vote_average"));
                    mMovieReleaseDate.setText(new JSONObject(movieResults).getString("release_date"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


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

        addNewMovie(movieId, movieTitle, data);

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

        // We now pass that URL variable to the AsyncTask to create a connection and give us the feed
        new FetchTrailersAsyncTask().execute(trailerSearchUrl);
        new FetchCastMembersAsyncTask().execute(castMembersSearchUrl);
        new FetchReviewsAsyncTask().execute(reviewsSearchUrl);

        Log.v(TAG, "MakeDetailUrlSearchQuery: " + trailerSearchUrl);
        Log.v(TAG, "MakeDetailUrlSearchQuery: " + castMembersSearchUrl);
        Log.v(TAG, "MakeDetailUrlSearchQuery: " + reviewsSearchUrl);


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
        LinearLayoutManager trailerLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager castMemberLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


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
}

/**
 * TODOS Step 1 - Adding Favourites - COMPLETED
 */
//TODO 1 (DONE) Create A Star Button That Launches A Toast (But In The Future It Will Save The Film In The SQL & The Favourites Section Of The App)
//TODO 2 (DONE) Design The SQL Table & Create An SQL Contract
//TODO 3 (DONE) The DetailActivity Needs To Have The Ability To Add A Movie To The DataBase When Clicking On The Heart Icon
//TODO 4 (DONE) Test That It Works In The Logs (Google How To View Contents Of The SQL In Log, Or Find Another Quick Way)
//TODO 5 (DONE) In MainActivity Build A RecyclerView, Adapter & FavouriteMovie Class To View The Favourite Movies DataBase In A GridView (We Have The Image Poster In The Database, That's All We Need For The GridView)
//TODO 6 (DONE) Create A Button To Switch To The FavouriteMoviesActivity GridView, By Making The Current GridView "setVisibility(View.GONE)" And The FavouriteMoviesActivity GridView As Visible
//TODO 7 (DONE) The FavouriteMovie SQL Needs To Contain The MOVIE_ID
//TODO 8a(DONE) Now When A Movie Is Clicked On From The FavouriteMovie GridView It Needs To Launch An Intent To The Detail Activity
//TODO 8b(DONE) TEMPORARILY WITH ItemTouchHelper) Now Pass Through The MOVIE_ID So We Can Create A "Get Details" Call In "TODO 10"
//TODO 9 (DONE) Within The DetailActivity OnCreate Method We Need An If Statement To Check:
// - if the activityThatLaunchedIt contains a MovieTitle it is from the Movie GridView
// - else if the activityThatLaunchedIt contains a MOVIE_ID it is from the FavouriteMovie GridView
// - TODO (DONE) For Full Instructions On The "else if" See TODO 10 Further Up The Code In The OnCreate Method
//TODO 10 (DONE) Launch an AsyncTask on the "Get Details" JSON Feed using the MOVIE_ID, parse it, and display the data in the TextViews & ImageViews
//TODO 11 (DONE) Test That It All Works. We Should Be Able To Add A Movie To Favourites From The DetailActivity Launched By The Movie GridView, We Should Be Able To View The Favourite Movie In A RecyclerView In A Grid Layout & Click Through To See The Movie In The Detail Activity
//TODO 12 (DONE) Now Let's Focus On How To Remove A Movie From The Database!
//  - When viewing DetailActivity from FavouriteMoviesActivity the heart should be white, clicking it should remove the film from the database

/**
 * TODOS Step 2 - Adding Content Provider
 */

/**
 * TODOs Step 3 - LifeCycles & Background Tasks
 */

/**
 * EXTRA TODOS TO MAKE THE APP BETTER
 */
//TODO A. Prevent the same film being added twice to SQL.
//TODO B. Be able to see if Movie is favourite even when checking it from either the MainActivity or the Favourite Activity
//TODO C. Fix the back navigation, If in SQL Detail Activity Mode and go Back you land on MainActivity instead of FavouriteMovie Activity
//TODO D. Switch from FavouriteMovieActivity to Fragment Instead??
//TODO E. When Movie Is Deleted Ensure The FavouriteMovieActivity Refreshes Using The BroadCast Receiver??
//TODO F. When a new film is marked as favourite it auto return to the MainActivity, instead it should stay on the detail activity page
//TODO G. When deleting the last film from the SQL, the last film remains in the list instead of being deleted
//TODO H. Add a header image to the DetailActivity
//TODO I. Make app fullscreen (remove the blue default header)
//TODO J. App crashed on rotation on Favourites List
//TODO K. When on top_rated on rotation it loads the popular list
//TODO L. Hardcode dimensions
//TODO M. Add a collapsing toolbar to the detail activity