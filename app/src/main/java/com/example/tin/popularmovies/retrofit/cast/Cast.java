
package com.example.tin.popularmovies.retrofit.cast;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cast implements Parcelable {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("cast")
    @Expose
    public List<CastResult> cast = null;
    public final static Creator<Cast> CREATOR = new Creator<Cast>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        public Cast[] newArray(int size) {
            return (new Cast[size]);
        }

    };

    protected Cast(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.cast, (CastResult.class.getClassLoader()));
    }

    public Cast() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(cast);
    }

    public int describeContents() {
        return 0;
    }

    public List<CastResult> getResults() {
        return cast;
    }
}
