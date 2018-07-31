package com.example.tin.popularmovies.Activities;

import android.content.Context;

import com.example.tin.popularmovies.ConnectionUtils;


public class MainPresenter implements MainContract.MainPresenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private final MainContract.MainScreen mainScreen;

    MainPresenter(MainContract.MainScreen screen) {
        this.mainScreen = screen;
    }

    /**
     * Helper Code that checks if device is connected to the internet
     */
    @Override
    public void isOnline(Context context) {

        if (!ConnectionUtils.isOnline(context)){

            mainScreen.showNoConnection();
        } else {

            /** Start connection and hide the no internet icons and textViews */
            mainScreen.hideNoConnection();
        }
    }

}
