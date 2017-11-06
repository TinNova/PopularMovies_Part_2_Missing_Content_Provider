package com.example.tin.popularmovies.Models;


import com.example.tin.popularmovies.NetworkUtils;

/** Constructor Class Containing Getters */
public class CastMember {

    private final String characterName;
    private final String actorName;
    private final String actorImageUrl;


    public CastMember(String characterName, String actorName, String actorImageUrl) {
        this.characterName = characterName;
        this.actorName = actorName;
        this.actorImageUrl = actorImageUrl;
    }

    // The Getters
    public String getCharacterName(){
        return characterName;
    }

    public String getActorName(){
        return actorName;
    }

    public String getActorImageUrl() {
        return NetworkUtils.BASE_IMAGE_URL + actorImageUrl;
    }
}
