
package com.example.tin.popularmovies.retrofit.review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewResult implements Parcelable {

    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("url")
    @Expose
    public String url;
    public final static Creator<ReviewResult> CREATOR = new Creator<ReviewResult>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ReviewResult createFromParcel(Parcel in) {
            return new ReviewResult(in);
        }

        public ReviewResult[] newArray(int size) {
            return (new ReviewResult[size]);
        }

    };

    protected ReviewResult(Parcel in) {
        this.author = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ReviewResult() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(author);
        dest.writeValue(content);
        dest.writeValue(id);
        dest.writeValue(url);
    }

    public int describeContents() {
        return 0;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
