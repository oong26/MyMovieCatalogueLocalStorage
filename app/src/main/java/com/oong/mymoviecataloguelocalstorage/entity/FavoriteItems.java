package com.oong.mymoviecataloguelocalstorage.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteItems implements Parcelable {
    private int id;
    private String title;
    private String release_date;
    private String popularity;
    private String poster;
    private String desc;
    private int jenis;

    public FavoriteItems(){

    }

    public FavoriteItems(int id, String title, String release_date, String popularity, String poster, String desc, int jenis) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.popularity = popularity;
        this.poster = poster;
        this.desc = desc;
        this.jenis = jenis;
    }

    protected FavoriteItems(Parcel in) {
        id = in.readInt();
        title = in.readString();
        release_date = in.readString();
        popularity = in.readString();
        poster = in.readString();
        desc = in.readString();
        jenis = in.readInt();
    }

    public static final Creator<FavoriteItems> CREATOR = new Creator<FavoriteItems>() {
        @Override
        public FavoriteItems createFromParcel(Parcel in) {
            return new FavoriteItems(in);
        }

        @Override
        public FavoriteItems[] newArray(int size) {
            return new FavoriteItems[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getJenis() {
        return jenis;
    }

    public void setJenis(int jenis) {
        this.jenis = jenis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(popularity);
        dest.writeString(poster);
        dest.writeString(desc);
        dest.writeInt(jenis);
    }
}
