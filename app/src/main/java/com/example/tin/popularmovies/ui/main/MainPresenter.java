package com.example.tin.popularmovies.ui.main;

import android.util.Log;

import com.example.tin.popularmovies.retrofit.RestService;
import com.example.tin.popularmovies.retrofit.movie.Movie;
import com.example.tin.popularmovies.retrofit.movie.MovieResult;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.tin.popularmovies.Constants.API_KEY;


public class MainPresenter implements MainContract.MainPresenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private final MainContract.MainScreen mainScreen;

    @Inject
    RestService restService;

    MainPresenter(MainContract.MainScreen mainScreen) {
        this.mainScreen = mainScreen;

    }

    @Override
    public void getMovies(boolean popularFilms) {

        if (popularFilms) {
            RestService.getInstance()
                    .getPopularFilms(API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Movie>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Movie movies) {

                            if (movies != null) {

                                mainScreen.populateRecyclerView((ArrayList<MovieResult>) movies.getResults());
                                Log.d(TAG, "onNext, movies: " + movies);

                            } else {
                                Log.e(TAG, "onNext, movies is null.");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.e(TAG, "onError: error while load listings " + Log.getStackTraceString(e));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } else {
            RestService.getInstance()
                    .getTopRatedFilms(API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Movie>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Movie movies) {

                            if (movies != null) {

                                mainScreen.populateRecyclerView((ArrayList<MovieResult>) movies.getResults());
                                Log.d(TAG, "onNext, movies: " + movies);

                            } else {
                                Log.e(TAG, "onNext, movies is null.");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.e(TAG, "onError: error while load listings " + Log.getStackTraceString(e));
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
