package com.example.tin.popularmovies.ui.detail;

/**
 * Created by Tin on 30/07/2018.
 */

public class DetailPresenter implements DetailContract.DetailPresenter {

    private static final String TAG = DetailPresenter.class.getSimpleName();

    private final DetailContract.DetailScreen detailScreen;

    DetailPresenter(DetailContract.DetailScreen screen) {
        this.detailScreen = screen;
    }
}
