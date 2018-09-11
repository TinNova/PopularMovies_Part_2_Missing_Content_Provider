package com.example.tin.popularmovies.retrofit;

import com.example.tin.popularmovies.retrofit.cast.Cast;
import com.example.tin.popularmovies.retrofit.movie.Movie;
import com.example.tin.popularmovies.retrofit.movie_detail.MovieDetail;
import com.example.tin.popularmovies.retrofit.review.Review;
import com.example.tin.popularmovies.retrofit.trailer.Trailer;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.tin.popularmovies.Constants.BASE_URL;


public class RestService {

    private static ApiMethods INSTANCE;
    private static RestService restService;

    public static RestService getInstance() {

        // We only want to create this once
        if (INSTANCE == null) {

            restService = new RestService();

            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(provideOkHttp())
                    .build();

            INSTANCE = retrofit.create(ApiMethods.class);
        }

        return restService;
    }

    private static OkHttpClient provideOkHttp() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }


    public Observable<Movie> getTopRatedFilms(String apiKey) {
        return INSTANCE.getTopRatedFilms(apiKey);
    }

    public Observable<Movie> getPopularFilms(String apiKey) {
        return INSTANCE.getPopularFilms(apiKey);
    }

    public Observable<Trailer> getTrailers(int movieId, String apiKey) {
        return INSTANCE.getTrailers(movieId, apiKey);
    }

    public Observable<Cast> getCast(int movieId, String apiKey) {
        return INSTANCE.getCast(movieId, apiKey);
    }

    public Observable<Review> getReviews(int movieId, String apiKey) {
        return INSTANCE.getReviews(movieId, apiKey);
    }

    public Observable<MovieDetail> getFilmDetails(String movieId, String apiKey) {
        return INSTANCE.getFilmDetails(movieId, apiKey);
    }
}
