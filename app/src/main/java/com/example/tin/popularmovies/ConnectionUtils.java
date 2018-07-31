package com.example.tin.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class ConnectionUtils {

    /** Helper Code that checks if device is connected to the internet */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
