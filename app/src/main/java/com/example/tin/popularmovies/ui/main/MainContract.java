package com.example.tin.popularmovies.ui.main;

import com.example.tin.popularmovies.retrofit.movie.MovieResult;

import java.util.ArrayList;

/**
 * Created by Tin on 30/07/2018.
 */

public interface MainContract {

    interface MainScreen {

        void populateRecyclerView(ArrayList<MovieResult> movies);

    }

    interface MainPresenter {


        void getMovies(boolean popularFilms);

    }
}
