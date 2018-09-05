
package com.example.tin.popularmovies.retrofit.movie_detail;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpokenLanguage implements Parcelable
{

    @SerializedName("iso_639_1")
    @Expose
    public String iso6391;
    @SerializedName("name")
    @Expose
    public String name;
    public final static Creator<SpokenLanguage> CREATOR = new Creator<SpokenLanguage>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SpokenLanguage createFromParcel(Parcel in) {
            return new SpokenLanguage(in);
        }

        public SpokenLanguage[] newArray(int size) {
            return (new SpokenLanguage[size]);
        }

    }
    ;

    protected SpokenLanguage(Parcel in) {
        this.iso6391 = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SpokenLanguage() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iso6391);
        dest.writeValue(name);
    }

    public int describeContents() {
        return  0;
    }

}
