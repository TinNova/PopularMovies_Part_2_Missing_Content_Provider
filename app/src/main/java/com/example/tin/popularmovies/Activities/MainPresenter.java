package com.example.tin.popularmovies.Activities;

/**
 * Created by Tin on 30/07/2018.
 */

public class MainPresenter implements MainContract.MainPresenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private final MainContract.MainScreen mainScreen;

    MainPresenter(MainContract.MainScreen screen) {
        this.mainScreen = screen;
    }
}
