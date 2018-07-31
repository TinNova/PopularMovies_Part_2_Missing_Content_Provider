package com.example.tin.popularmovies.Activities;

import android.content.Context;

/**
 * Created by Tin on 30/07/2018.
 */

public interface MainContract {

    interface MainScreen {

        void showNoConnection();

        void hideNoConnection();

    }

    interface MainPresenter {

        void isOnline(Context context);

    }
}
