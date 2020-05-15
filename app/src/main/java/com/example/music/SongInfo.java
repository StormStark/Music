package com.example.music;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class SongInfo implements Parcelable {

    private String name;
    private String artist;
    private Uri url;
    private String album;
    private String date;
    private Uri path;

    public SongInfo(String name, String artist, Uri url, String album, String date, Uri path) {
        this.name = name;
        this.artist = artist;
        this.url = url;
        this.album = album;
        this.date = date;
        this.path = path;
    }

    protected SongInfo(Parcel in) {
        name = in.readString();
        artist = in.readString();
        url = in.readParcelable(Uri.class.getClassLoader());
        album = in.readString();
        date = in.readString();
        path = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<SongInfo> CREATOR = new Creator<SongInfo>() {
        @Override
        public SongInfo createFromParcel(Parcel in) {
            return new SongInfo(in);
        }

        @Override
        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Uri getPath() {
        return path;
    }

    public void setPath(Uri path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(artist);
        parcel.writeParcelable(url, i);
        parcel.writeString(album);
        parcel.writeString(date);
        parcel.writeParcelable(path, i);
    }
}
