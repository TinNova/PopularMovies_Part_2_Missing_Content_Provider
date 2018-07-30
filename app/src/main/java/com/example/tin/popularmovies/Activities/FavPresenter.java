package com.example.tin.popularmovies.Activities;

/**
 * Created by Tin on 30/07/2018.
 */

public class FavPresenter implements FavContract.FavPresenter {

    private static final String TAG = FavPresenter.class.getSimpleName();

    private final FavContract.FavScreen favScreen;

    FavPresenter(FavContract.FavScreen screen) {
        this.favScreen = screen;
    }
}
