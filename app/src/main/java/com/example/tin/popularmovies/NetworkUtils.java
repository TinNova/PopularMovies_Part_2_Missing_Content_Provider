package com.example.tin.popularmovies;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These NetworkUtils will be used to communicate with the servers at The Movie Database
 */
public class NetworkUtils {

    // TAG to help catch errors in Log
    private static final String TAG = NetworkUtils.class.getSimpleName();

    /* CONSTANTS For The URL*/
    // Base URL
    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";

    /**  The Top Rated Feed
     * "https://api.themoviedb.org/3/movie/top_rated?api_key={{API_KEY}}&language=en-UK&page=1"
     *   The Popular Feed
     * "https://api.themoviedb.org/3/movie/popular?api_key={{API_KEY}}&language=en-UK&page=1"
     *   The Trailers Feed
     * "https://api.themoviedb.org/3/movie/{{MOVIE_ID}}/videos?api_key={{API_KEY}}&language=en-UK"
     *   The Cast Members Feed (Credits)
     * "https://api.themoviedb.org/3/movie/339403/credits?api_key={{API_KEY}}&language=en-UK"
     *   The Reviews Feed
     * "https://api.themoviedb.org/3/movie/339403/reviews?api_key={{API_KEY}}&language=en-US"
     *   The GetDetail Feed
     * "https://api.themoviedb.org/3/movie/339403?api_key=41fe79dd1f576ae823dfb4939a5eaff6&language=en-UK"
     */


    // Top Rated Filter & Popular FILTER_PATH's
    public static final String TOP_RATED_PATH = "top_rated";
    public static final String POPULAR_PATH = "popular";

    // Query Params For Trailers, Cast Members (Credits), Reviews API_TYPE's
    // The movie_id value is passed via the DetailActivity
    public static final String MOVIE_ID_TRAILERS = "videos";
    public static final String MOVIE_ID_CREDITS = "credits";
    public static final String MOVIE_ID_REVIEWS = "reviews";

    // The Query Params
    private static final String API_KEY_PARAM = "api_key";
    private static final String LANGUAGE_PARAM = "language";
    private static final String PAGINATION_PARAM = "page";

    private static final String api_key = BuildConfig.MOVIE_DATABSE_API_KEY;
    private static final String language = "en-UK";
    private static final int page = 1;

    // BASE URL FOR IMAGES (MOVIE IMAGES & CAST MEMBER IMAGES
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342/";

    /**
     * buildMovieUrl builds the URL for the Popular or Top_Rated Movies Feed
     */
    public static URL buildMovieUrl(String FILTER_PATH) {
        Uri builtMovieUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(FILTER_PATH)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(PAGINATION_PARAM, Integer.toString(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtMovieUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Movie URI " + url);

        return url;
    }

    /**
     * buildDetailUrl builds the URLs for Trailers, Cast Members (Credits) and Reviews
     * it has been designed to be cleaner to prevent having to create three seperate buildUrl's for
     * for all of the queries.
     */
    public static URL buildDetailUrl(String MOVIE_ID, String API_TYPE) {
        Uri builtTrailerUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(MOVIE_ID)
                .appendPath(API_TYPE)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();

        URL url = null;
        try {
            url = new URL(builtTrailerUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;

    }

    public static URL buildGetDetailUrl(String MOVIE_ID){
        Uri builtGetDetailUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(MOVIE_ID)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();

        URL url = null;
        try {
            url = new URL(builtGetDetailUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "builtGetDetailUri URI: " + url);

        return url;

    }



    /**
     * This is a copy/paste helper method given to us by Udacity that returns the entire result
     * from the HTTP response.
     *
     * It opens a HTTP connection using the url created above
     * Then we get an InputSteam from the open connection that we take using "urlConnection.getInputStream()"
     * Next we need to read the contents of the InputSteam, this is done using a "Scanner"
     *   By using the "useDelimiter("\\A")" we are forcing the Scanner to read the entire content
     *   of the stream
     * The if statement says if there is a "scanner.hasNext()" (aka: if there is more data) then
     *   continue to take it until there is no more "scanner.hasNext()" (aka: until there is no more data)
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
