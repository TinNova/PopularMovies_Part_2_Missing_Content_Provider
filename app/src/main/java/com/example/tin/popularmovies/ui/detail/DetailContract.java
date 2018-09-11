package com.example.tin.popularmovies.ui.detail;

import com.example.tin.popularmovies.retrofit.cast.CastResult;
import com.example.tin.popularmovies.retrofit.movie_detail.MovieDetail;
import com.example.tin.popularmovies.retrofit.review.ReviewResult;
import com.example.tin.popularmovies.retrofit.trailer.Trailer;
import com.example.tin.popularmovies.retrofit.trailer.TrailerResult;

import java.util.ArrayList;

/**
 * Created by Tin on 30/07/2018.
 */

public interface DetailContract {

    interface DetailScreen {

        void populateTrailerRecyclerView(ArrayList<TrailerResult> results);

        void populateCastRecyclerView(ArrayList<CastResult> results);

        void populateReviewRecyclerView(ArrayList<ReviewResult> results);

        void populateView(MovieDetail movieDetails);

    }

    interface DetailPresenter {

        void getMovieDetails(String movieId);

        void getTrailer(int movieId);

        void getCast(int movieId);

        void getReviews(int movieId);

    }
}
