package com.example.tin.popularmovies.ui.detail;

import android.util.Log;

import com.example.tin.popularmovies.retrofit.RestService;
import com.example.tin.popularmovies.retrofit.cast.Cast;
import com.example.tin.popularmovies.retrofit.movie.Movie;
import com.example.tin.popularmovies.retrofit.movie.MovieResult;
import com.example.tin.popularmovies.retrofit.movie_detail.MovieDetail;
import com.example.tin.popularmovies.retrofit.review.Review;
import com.example.tin.popularmovies.retrofit.trailer.Trailer;
import com.example.tin.popularmovies.retrofit.trailer.TrailerResult;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.tin.popularmovies.Constants.API_KEY;

/**
 * Created by Tin on 30/07/2018.
 */

public class DetailPresenter implements DetailContract.DetailPresenter {

    private static final String TAG = DetailPresenter.class.getSimpleName();

    private final DetailContract.DetailScreen detailScreen;

    DetailPresenter(DetailContract.DetailScreen screen) {
        this.detailScreen = screen;
    }

    @Override
    public void getMovieDetails(int movieId) {

        RestService.getInstance()
                .getFilmDetails(movieId, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MovieDetail movieDetail) {

                        if (movieDetail != null) {

//                                detailScreen.populateRecyclerView((ArrayList<MovieResult>) movies.getResults());
                                Log.d(TAG, "onNext, MovieDetail: " + movieDetail);

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

    @Override
    public void getTrailer(int movieId) {

        RestService.getInstance()
                .getTrailers(movieId, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Trailer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Trailer trailers) {

                        if (trailers != null) {

                                detailScreen.populateTrailerRecyclerView((ArrayList<TrailerResult>) trailers.getResults());
                                Log.d(TAG, "onNext, Trailer: " + trailers);

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

    @Override
    public void getCast(int movieId) {

        RestService.getInstance()
                .getCast(movieId, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Cast>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Cast cast) {

                        if (cast != null) {

//                                detailScreen.populateRecyclerView((ArrayList<MovieResult>) movies.getResults());
                                Log.d(TAG, "onNext, Cast: " + cast);

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
