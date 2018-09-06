
package com.example.tin.popularmovies.retrofit.movie;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.example.tin.popularmovies.Constants.BASE_IMAGE_URL;

public class MovieResult implements Parcelable {

    @SerializedName("vote_count")
    @Expose
    public int voteCount;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("video")
    @Expose
    public boolean video;
    @SerializedName("vote_average")
    @Expose
    public double voteAverage;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("popularity")
    @Expose
    public double popularity;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    public List<Integer> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("adult")
    @Expose
    public boolean adult;
    @SerializedName("overview")
    @Expose
    public String synopsis;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    public final static Creator<MovieResult> CREATOR = new Creator<MovieResult>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieResult createFromParcel(Parcel in) {
            return new MovieResult(in);
        }

        public MovieResult[] newArray(int size) {
            return (new MovieResult[size]);
        }

    };

    @Override
    public String toString() {
        return "MovieResult{" +
                "voteCount=" + voteCount +
                ", id=" + id +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                ", title='" + title + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", genreIds=" + genreIds +
                ", backdropPath='" + backdropPath + '\'' +
                ", adult=" + adult +
                ", overview='" + synopsis + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    protected MovieResult(Parcel in) {
        this.voteCount = ((int) in.readValue((int.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.video = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.voteAverage = ((double) in.readValue((double.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((double) in.readValue((double.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        this.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
        this.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.genreIds, (Integer.class.getClassLoader()));
        this.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
        this.adult = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.synopsis = ((String) in.readValue((String.class.getClassLoader())));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MovieResult() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(voteCount);
        dest.writeValue(id);
        dest.writeValue(video);
        dest.writeValue(voteAverage);
        dest.writeValue(title);
        dest.writeValue(popularity);
        dest.writeValue(posterPath);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalTitle);
        dest.writeList(genreIds);
        dest.writeValue(backdropPath);
        dest.writeValue(adult);
        dest.writeValue(synopsis);
        dest.writeValue(releaseDate);
    }

    public int describeContents() {
        return 0;
    }

    public String getPosterPath() {
        return BASE_IMAGE_URL + posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }
}
