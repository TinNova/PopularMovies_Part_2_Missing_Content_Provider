package com.example.tin.popularmovies.ui.detail;

import com.example.tin.popularmovies.retrofit.trailer.Trailer;
import com.example.tin.popularmovies.retrofit.trailer.TrailerResult;

import java.util.ArrayList;

/**
 * Created by Tin on 30/07/2018.
 */

public interface DetailContract {

    interface DetailScreen {

        void populateTrailerRecyclerView(ArrayList<TrailerResult> results);
    }

    interface DetailPresenter {

        void getMovieDetails(int movieId);

        void getTrailer(int movieId);

        void getCast(int movieId);

    }
}
