package com.example.tin.popularmovies.ui.detail;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.example.tin.popularmovies.R;
import com.example.tin.popularmovies.TrailerPositionListenter;
import com.example.tin.popularmovies.retrofit.cast.CastResult;
import com.example.tin.popularmovies.retrofit.movie_detail.MovieDetail;
import com.example.tin.popularmovies.retrofit.review.ReviewResult;
import com.example.tin.popularmovies.retrofit.trailer.TrailerResult;
import com.example.tin.popularmovies.room.AppDatabase;
import com.example.tin.popularmovies.room.FavouriteMovieRoom;
import com.example.tin.popularmovies.room.FavouriteMovieRoomDAO;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.example.tin.popularmovies.Constants.BASE_IMAGE_URL;

public class DetailActivity extends AppCompatActivity implements DetailContract.DetailScreen, TrailerPositionListenter {

    // TAG to help catch errors in Log
    private static final String TAG = DetailActivity.class.getSimpleName();

    ScrollView mScrollView;

    private RecyclerView trailerRecyclerView;
    private RecyclerView castMemberRecyclerView;
    private RecyclerView reviewRecyclerView;

    LinearLayoutManager trailerLinearLayoutManager;
    LinearLayoutManager castMemberLinearLayoutManager;
    LinearLayoutManager reviewLinearLayoutManager;

    private ImageView mMoviePosterIV;
    private TextView mMovieTitleTV;
    private TextView mMovieSynopsisTV;
    private TextView mMovieUserRatingTV;
    private TextView mMovieReleaseDateTV;

    private String movieId;
    private int movieIdTEST;
    private String movieTitle;
    private long row_id;

    // This Is For The Favourite Icon In The Menu Item
    private MenuItem favouriteMenu;

    // Determines If A Movie Is Favourite Or Not, 0 = Not Favourite, 1 = Favourite
    private int favourite_NotFavourite;

    private DetailPresenter detailPresenter;
    private FavouriteMovieRoomDAO mFavouriteMovieRoomDAO;

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

        mFavouriteMovieRoomDAO = Room.databaseBuilder(this, AppDatabase.class, "db-contacts")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build()
                .getFavouriteMovieRoomDAO();

        //If DetailActivity was triggered by the popular or top_rated list (aka MainActivity)
        if (intentThatStartedThisActivity.hasExtra("MovieId")) {

            String moviePoster = intentThatStartedThisActivity.getStringExtra("MoviePoster");
            movieTitle = intentThatStartedThisActivity.getStringExtra("MovieTitle");
            String movieSynopsis = intentThatStartedThisActivity.getStringExtra("MovieSynopsis");
            String movieUserRating = Double.toString(intentThatStartedThisActivity.getDoubleExtra("MovieUserRating", 0));
            String movieReleaseDate = intentThatStartedThisActivity.getStringExtra("MovieReleaseDate");
            int movieId = intentThatStartedThisActivity.getIntExtra("MovieId", 0);
            movieIdTEST = intentThatStartedThisActivity.getIntExtra("MovieId", 0);

            Picasso.with(this).load(moviePoster).into(mMoviePosterIV);
            mMovieTitleTV.setText(movieTitle);
            mMovieSynopsisTV.setText(movieSynopsis);
            mMovieUserRatingTV.setText(movieUserRating);
            mMovieReleaseDateTV.setText(movieReleaseDate);

            detailPresenter.getTrailer(movieId);
            detailPresenter.getCast(movieId);
            detailPresenter.getReviews(movieId);
            setupRecyclerViews();

            //Else if DetailActivity was triggered by the FavouriteMoviesActivity
        } else if (intentThatStartedThisActivity.hasExtra("Favourite_Movie")) {

            // The Heart Icon Is Fully While Indicating It Is Favourite
            favourite_NotFavourite = 1;

            // In getLongExtra the second value is the default value that will be used if the Long can't be found
            row_id = intentThatStartedThisActivity.getLongExtra("Row_Id", -1);
            movieId = intentThatStartedThisActivity.getStringExtra("MovieSqlId");
            Log.v(TAG, "Row_ID: " + row_id);

            detailPresenter.getMovieDetails(movieId);
            setupRecyclerViews();
        }
    }

    int isFilmFavourite = -99 ;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        favouriteMenu = menu.findItem(R.id.favourite);

        try {
            isFilmFavourite = mFavouriteMovieRoomDAO.getMovieWithId(movieIdTEST).getMovieId();
            Log.d(TAG, "onCreateOptionsMenu: " + isFilmFavourite);

        } catch (NullPointerException e) {

            Log.d(TAG, "NullPointerException: " + e);

        }

//        Log.d(TAG, "onCreateOptionsMenu: " + testMovieId);
        // Assign Correct Heart Icon: If 0 = Not Favourite, so add the white border icon
        if (isFilmFavourite == -99) {
//        if (favourite_NotFavourite == 0) {
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
                if (isFilmFavourite == -99) {

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

    private void setupRecyclerViews() {

        trailerRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailer);
        castMemberRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_castMember);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_review);

        trailerRecyclerView.setHasFixedSize(true);
        castMemberRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setHasFixedSize(true);

        trailerLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        castMemberLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        trailerRecyclerView.setLayoutManager(trailerLinearLayoutManager);
        castMemberRecyclerView.setLayoutManager(castMemberLinearLayoutManager);
        reviewRecyclerView.setLayoutManager(reviewLinearLayoutManager);
    }

    @Override
    public void populateTrailerRecyclerView(ArrayList<TrailerResult> results) {

        RecyclerView.Adapter trailerAdapter = new TrailerAdapter(results, getApplicationContext(), this);
        trailerRecyclerView.setAdapter(trailerAdapter);
    }

    @Override
    public void populateCastRecyclerView(ArrayList<CastResult> results) {

        RecyclerView.Adapter castMemberAdapter = new CastMemberAdapter(results, getApplicationContext());
        castMemberRecyclerView.setAdapter(castMemberAdapter);
    }

    @Override
    public void populateReviewRecyclerView(ArrayList<ReviewResult> results) {

        RecyclerView.Adapter reviewAdapter = new ReviewAdapter(results, getApplicationContext());
        reviewRecyclerView.setAdapter(reviewAdapter);
    }

    @Override
    public void populateView(MovieDetail movieDetails) {

        // Parse The Raw Json Into The Views
        Picasso.with(DetailActivity.this)
                .load(BASE_IMAGE_URL + movieDetails.getPosterPath())
                .into(mMoviePosterIV);
        mMovieTitleTV.setText(movieDetails.getOriginalTitle());
        mMovieSynopsisTV.setText(movieDetails.getOverview());
        String movieRater = Double.toString(movieDetails.getVoteAverage());
        mMovieUserRatingTV.setText(movieRater);
        mMovieReleaseDateTV.setText(movieDetails.getReleaseDate());
    }

    @Override
    public void trailerItemClick(String trailerUrl) {

        Uri videoURL = Uri.parse(trailerUrl);
        Intent playTrailerIntent = new Intent(Intent.ACTION_VIEW);
        playTrailerIntent.setData(videoURL);
        // Checks if there is an App that can handle this intent
        if (playTrailerIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(playTrailerIntent);
        } else {
            Toast.makeText(this, "Can't Play Video", Toast.LENGTH_LONG).show();
        }
    }

    private void addNewMovie(int movieId, String movieTitle, byte[] moviePosterByteArray) {

        Log.d(TAG, "addNewMovie: " + movieId + " " + movieTitle + " " + moviePosterByteArray);
        FavouriteMovieRoom favouriteMovieRoom = new FavouriteMovieRoom();
        favouriteMovieRoom.setMovieId(movieId);
        favouriteMovieRoom.setMovieName(movieTitle);
        favouriteMovieRoom.setMoviePoster(moviePosterByteArray);

        mFavouriteMovieRoomDAO.insert(favouriteMovieRoom);
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
        Log.d(TAG, "addNewMovie: " + movieId + " " + mMovieTitle2 + " " + data);
        addNewMovie(movieIdTEST, mMovieTitle2, data);

        Log.v(TAG, "addNewMovie Params: " + movieId + ", " + movieTitle + ", " + data);

    }

    private void removeMovie(long id) {

        FavouriteMovieRoom favRoom = mFavouriteMovieRoomDAO.getMovieWithId(movieIdTEST);
        mFavouriteMovieRoomDAO.delete(favRoom);
    }
}
