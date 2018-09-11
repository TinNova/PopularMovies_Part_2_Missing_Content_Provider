
package com.example.tin.popularmovies.retrofit.cast;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CastResult implements Parcelable {

    @SerializedName("cast_id")
    @Expose
    public int castId;
    @SerializedName("character")
    @Expose
    public String character;
    @SerializedName("credit_id")
    @Expose
    public String creditId;
    @SerializedName("gender")
    @Expose
    public int gender;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("order")
    @Expose
    public int order;
    @SerializedName("profile_path")
    @Expose
    public String profilePath;
    public final static Creator<CastResult> CREATOR = new Creator<CastResult>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CastResult createFromParcel(Parcel in) {
            return new CastResult(in);
        }

        public CastResult[] newArray(int size) {
            return (new CastResult[size]);
        }

    };

    protected CastResult(Parcel in) {
        this.castId = ((int) in.readValue((int.class.getClassLoader())));
        this.character = ((String) in.readValue((String.class.getClassLoader())));
        this.creditId = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((int) in.readValue((int.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.order = ((int) in.readValue((int.class.getClassLoader())));
        this.profilePath = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CastResult() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(castId);
        dest.writeValue(character);
        dest.writeValue(creditId);
        dest.writeValue(gender);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(order);
        dest.writeValue(profilePath);
    }

    public int describeContents() {
        return 0;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }
}
