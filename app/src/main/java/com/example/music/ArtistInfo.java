package com.example.music;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ArtistInfo implements Parcelable {

    private long artistId;
    private Uri albumArt;
    private String artistName;
    private int noOfTracks;
    private int noOfAlbums;
    private String date;

    public ArtistInfo(long albumId, Uri albumArt, String artistName, int noOfTracks, int noOfAlbums, String date) {
        this.artistId = albumId;
        this.albumArt = albumArt;
        this.artistName = artistName;
        this.noOfTracks = noOfTracks;
        this.noOfAlbums = noOfAlbums;
        this.date = date;
    }

    protected ArtistInfo(Parcel in) {
        artistId = in.readLong();
        albumArt = in.readParcelable(Uri.class.getClassLoader());
        artistName = in.readString();
        noOfTracks = in.readInt();
        noOfAlbums = in.readInt();
        date = in.readString();
    }

    public static final Creator<ArtistInfo> CREATOR = new Creator<ArtistInfo>() {
        @Override
        public ArtistInfo createFromParcel(Parcel in) {
            return new ArtistInfo(in);
        }

        @Override
        public ArtistInfo[] newArray(int size) {
            return new ArtistInfo[size];
        }
    };

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long albumId) {
        this.artistId = albumId;
    }

    public Uri getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Uri albumArt) {
        this.albumArt = albumArt;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getNoOfTracks() {
        return noOfTracks;
    }

    public void setNoOfTracks(int noOfTracks) {
        this.noOfTracks = noOfTracks;
    }

    public int getNoOfAlbums() {
        return noOfAlbums;
    }

    public void setNoOfAlbums(int noOfAlbums) {
        this.noOfAlbums = noOfAlbums;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(artistId);
        parcel.writeParcelable(albumArt, i);
        parcel.writeString(artistName);
        parcel.writeInt(noOfTracks);
        parcel.writeInt(noOfAlbums);
        parcel.writeString(date);
    }
}
