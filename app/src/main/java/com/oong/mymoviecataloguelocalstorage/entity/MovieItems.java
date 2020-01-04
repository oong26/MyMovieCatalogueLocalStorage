package com.oong.mymoviecataloguelocalstorage.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieItems implements Parcelable {

    private String movie_id;
    private String photo;
    private String poster_path;
    private String title;
    private String release_date;
    private String description;

    public MovieItems(){

    }

    protected MovieItems(Parcel in) {
        movie_id = in.readString();
        photo = in.readString();
        poster_path = in.readString();
        title = in.readString();
        release_date = in.readString();
        description = in.readString();
    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel in) {
            return new MovieItems(in);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movie_id);
        dest.writeString(photo);
        dest.writeString(poster_path);
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(description);
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Creator<MovieItems> getCREATOR() {
        return CREATOR;
    }
}
