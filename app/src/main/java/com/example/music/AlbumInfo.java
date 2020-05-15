package com.example.music;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class AlbumInfo implements Parcelable {

    private long albumId;
    private long artistId;
    private String albumName;
    private Uri albumArt;
    private String albumArtist;
    private String tracks;
    private String date;

    public AlbumInfo(long albumId, String albumName, Uri albumArt, String albumArtist, long artistId, String tracks, String date) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumArt = albumArt;
        this.albumArtist = albumArtist;
        this.tracks = tracks;
        this.date = date;
        this.artistId = artistId;
    }

    protected AlbumInfo(Parcel in) {
        albumId = in.readLong();
        artistId = in.readLong();
        albumName = in.readString();
        albumArt = in.readParcelable(Uri.class.getClassLoader());
        albumArtist = in.readString();
        tracks = in.readString();
        date = in.readString();
    }

    public static final Creator<AlbumInfo> CREATOR = new Creator<AlbumInfo>() {
        @Override
        public AlbumInfo createFromParcel(Parcel in) {
            return new AlbumInfo(in);
        }

        @Override
        public AlbumInfo[] newArray(int size) {
            return new AlbumInfo[size];
        }
    };

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Uri getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Uri albumArt) {
        this.albumArt = albumArt;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getTracks() {
        return tracks;
    }

    public void setTracks(String tracks) {
        this.tracks = tracks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(albumId);
        parcel.writeLong(artistId);
        parcel.writeString(albumName);
        parcel.writeParcelable(albumArt, i);
        parcel.writeString(albumArtist);
        parcel.writeString(tracks);
        parcel.writeString(date);
    }
}
