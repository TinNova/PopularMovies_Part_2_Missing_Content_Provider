package com.example.tin.popularmovies.Models;

public class Trailer {

    private final String trailerName;
    private final String trailerKey;

    // The Constructor
    public Trailer (String trailerName, String trailerKey) {

        this.trailerName = trailerName;
        this.trailerKey = trailerKey;

    }

    // The Getters
    public String getTrailerName() {
        return trailerName;
    }

    public String getTrailerKey() {
        return trailerKey;
    }
}
