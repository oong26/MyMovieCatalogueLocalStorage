package com.oong.mymoviecataloguelocalstorage.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class TvShowItems implements Parcelable {

    private String tvShow_id;
    private String poster_path;
    private String photo;
    private String title;
    private String popularity;
    private String description;

    public TvShowItems(){

    }

    protected TvShowItems(Parcel in) {
        tvShow_id = in.readString();
        poster_path = in.readString();
        photo = in.readString();
        title = in.readString();
        popularity = in.readString();
        description = in.readString();
    }

    public static final Creator<TvShowItems> CREATOR = new Creator<TvShowItems>() {
        @Override
        public TvShowItems createFromParcel(Parcel in) {
            return new TvShowItems(in);
        }

        @Override
        public TvShowItems[] newArray(int size) {
            return new TvShowItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tvShow_id);
        dest.writeString(poster_path);
        dest.writeString(photo);
        dest.writeString(title);
        dest.writeString(popularity);
        dest.writeString(description);
    }

    public String getTvShow_id() {
        return tvShow_id;
    }

    public void setTvShow_id(String tvShow_id) {
        this.tvShow_id = tvShow_id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
