package me.mariorugeles.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mario on 1/16/16.
 */
public class Movie implements Parcelable {
    private static final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w500/";

    private String title;
    private String poster;
    private String synopsis;
    private float rating;
    private String releaseDate;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return String.format("%s%s",IMAGE_BASE_PATH, poster);
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.synopsis);
        dest.writeString(this.releaseDate);
        dest.writeFloat(this.rating);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(){

    }
    private Movie(Parcel in) {
        this.title = in.readString();
        this.poster = in.readString();
        this.synopsis = in.readString();
        this.releaseDate = in.readString();
        this.rating = in.readFloat();
    }
}
