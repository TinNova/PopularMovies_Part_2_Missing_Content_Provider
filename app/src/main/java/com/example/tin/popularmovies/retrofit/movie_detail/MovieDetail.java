
package com.example.tin.popularmovies.retrofit.movie_detail;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDetail implements Parcelable
{

    @SerializedName("adult")
    @Expose
    public boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("belongs_to_collection")
    @Expose
    public BelongsToCollection belongsToCollection;
    @SerializedName("budget")
    @Expose
    public int budget;
    @SerializedName("genres")
    @Expose
    public List<Genre> genres = null;
    @SerializedName("homepage")
    @Expose
    public String homepage;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("imdb_id")
    @Expose
    public String imdbId;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("popularity")
    @Expose
    public double popularity;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("production_companies")
    @Expose
    public List<ProductionCompany> productionCompanies = null;
    @SerializedName("production_countries")
    @Expose
    public List<ProductionCountry> productionCountries = null;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    @SerializedName("revenue")
    @Expose
    public int revenue;
    @SerializedName("runtime")
    @Expose
    public int runtime;
    @SerializedName("spoken_languages")
    @Expose
    public List<SpokenLanguage> spokenLanguages = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("tagline")
    @Expose
    public String tagline;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("video")
    @Expose
    public boolean video;
    @SerializedName("vote_average")
    @Expose
    public double voteAverage;
    @SerializedName("vote_count")
    @Expose
    public int voteCount;
    public final static Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        public MovieDetail[] newArray(int size) {
            return (new MovieDetail[size]);
        }

    }
    ;

    protected MovieDetail(Parcel in) {
        this.adult = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
        this.belongsToCollection = ((BelongsToCollection) in.readValue((BelongsToCollection.class.getClassLoader())));
        this.budget = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.genres, (Genre.class.getClassLoader()));
        this.homepage = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.imdbId = ((String) in.readValue((String.class.getClassLoader())));
        this.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
        this.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((double) in.readValue((double.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.productionCompanies, (com.example.tin.popularmovies.retrofit.movie_detail.ProductionCompany.class.getClassLoader()));
        in.readList(this.productionCountries, (com.example.tin.popularmovies.retrofit.movie_detail.ProductionCountry.class.getClassLoader()));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.revenue = ((int) in.readValue((int.class.getClassLoader())));
        this.runtime = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.spokenLanguages, (com.example.tin.popularmovies.retrofit.movie_detail.SpokenLanguage.class.getClassLoader()));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.tagline = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.video = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.voteAverage = ((double) in.readValue((double.class.getClassLoader())));
        this.voteCount = ((int) in.readValue((int.class.getClassLoader())));
    }

    public MovieDetail() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adult);
        dest.writeValue(backdropPath);
        dest.writeValue(belongsToCollection);
        dest.writeValue(budget);
        dest.writeList(genres);
        dest.writeValue(homepage);
        dest.writeValue(id);
        dest.writeValue(imdbId);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalTitle);
        dest.writeValue(overview);
        dest.writeValue(popularity);
        dest.writeValue(posterPath);
        dest.writeList(productionCompanies);
        dest.writeList(productionCountries);
        dest.writeValue(releaseDate);
        dest.writeValue(revenue);
        dest.writeValue(runtime);
        dest.writeList(spokenLanguages);
        dest.writeValue(status);
        dest.writeValue(tagline);
        dest.writeValue(title);
        dest.writeValue(video);
        dest.writeValue(voteAverage);
        dest.writeValue(voteCount);
    }

    public int describeContents() {
        return  0;
    }

}
