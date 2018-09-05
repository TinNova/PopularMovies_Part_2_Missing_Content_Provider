
package com.example.tin.popularmovies.retrofit.movie_detail;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BelongsToCollection implements Parcelable
{

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("backdrop_path")
    @Expose
    public Object backdropPath;
    public final static Creator<BelongsToCollection> CREATOR = new Creator<BelongsToCollection>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BelongsToCollection createFromParcel(Parcel in) {
            return new BelongsToCollection(in);
        }

        public BelongsToCollection[] newArray(int size) {
            return (new BelongsToCollection[size]);
        }

    }
    ;

    protected BelongsToCollection(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        this.backdropPath = ((Object) in.readValue((Object.class.getClassLoader())));
    }

    public BelongsToCollection() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(posterPath);
        dest.writeValue(backdropPath);
    }

    public int describeContents() {
        return  0;
    }

}
