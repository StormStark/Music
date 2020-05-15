package com.example.music;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GetSongs {

    private static ArrayList<SongInfo> _songs;
    private static ArrayList<AlbumInfo> _albums;
    private static ArrayList<ArtistInfo> _artist;

    public static ArrayList<SongInfo> getMusic(ContentResolver contentResolver) {
        _songs = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = contentResolver.query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                    long albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumID);
                    long songId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);

                    SongInfo s = new SongInfo(name, artist, albumArtUri, album, date, songUri);
                    _songs.add(s);
                    Collections.sort(_songs, new Comparator<SongInfo>() {
                        @Override
                        public int compare(SongInfo o1, SongInfo o2) {
                            return o1.getName().toLowerCase().trim().compareTo(o2.getName().toLowerCase().trim());
                        }
                    });
                    //_songs.sort((o1, o2) -> o1.getName().toLowerCase().trim().compareTo(o2.getName().toLowerCase().trim()));

                } while (cursor.moveToNext());

            }
            cursor.close();
        }
        return _songs;
    }

    public static ArrayList<AlbumInfo> getAlbums(ContentResolver contentResolver) {
        _albums = new ArrayList<>();

        final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String _id = MediaStore.Audio.Albums._ID;
        final String album_name = MediaStore.Audio.Albums.ALBUM;
        final String artist = MediaStore.Audio.Albums.ARTIST;
        final String artistId = MediaStore.Audio.Albums.ARTIST_ID;
        //final String albumId = MediaStore.Audio.Albums.ALBUM_ID;
        final String date = MediaStore.Audio.AlbumColumns.LAST_YEAR;
        final String tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

        final String[] columns = { _id, album_name, artist, artistId, tracks, date };
        Cursor cursor = contentResolver.query(uri, columns, null,null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String albumTracks = cursor.getString(cursor.getColumnIndex(tracks));
                    if(Integer.parseInt(albumTracks)!=0) {
                        long albumId = cursor.getLong(cursor.getColumnIndex(_id));
                        String albumName = cursor.getString(cursor.getColumnIndex(album_name));
                        String albumArtist = cursor.getString(cursor.getColumnIndex(artist));
                        long artId = cursor.getLong(cursor.getColumnIndex(artistId));
                        String albumDate = cursor.getString(cursor.getColumnIndex(date));
                        if(albumDate == null)
                            albumDate = "";
                        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
                        AlbumInfo albumInfo = new AlbumInfo(albumId, albumName, albumArtUri, albumArtist, artId, albumTracks, albumDate);
                        _albums.add(albumInfo);
                    }
                } while (cursor.moveToNext());
                Collections.sort(_albums, new Comparator<AlbumInfo>() {
                    @Override
                    public int compare(AlbumInfo o1, AlbumInfo o2) {
                        return o1.getAlbumName().toLowerCase().trim().compareTo(o2.getAlbumName().toLowerCase().trim());
                    }
                });
                //_albums.sort((o1, o2) -> o1.getAlbumName().toLowerCase().trim().compareTo(o2.getAlbumName().toLowerCase().trim()));
            }
            cursor.close();
        }
        return _albums;
    }

    public static ArrayList<ArtistInfo> getArtists(ContentResolver contentResolver) {
        _artist = new ArrayList<>();
        _albums = new ArrayList<>();
        _albums = GetSongs.getAlbums(contentResolver);
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    long albumId = 0;
                    String albumDate = null;
                    Uri albumArtUri = null;
                    long artitsId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));

                    for (AlbumInfo album: _albums) {
                        if(album.getArtistId() == artitsId){
                            albumArtUri = album.getAlbumArt();
                            albumDate = album.getDate();
                            break;
                        }
                    }
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                    int noOfTracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                    int noOfAlbums = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));

                    if(albumDate == null)
                        albumDate = "";
                    ArtistInfo artistInfo = new ArtistInfo(artitsId, albumArtUri, name, noOfTracks, noOfAlbums, albumDate);
                    _artist.add(artistInfo);
                } while (cursor.moveToNext());
                Collections.sort(_artist, new Comparator<ArtistInfo>() {
                    @Override
                    public int compare(ArtistInfo o1, ArtistInfo o2) {
                        return o1.getArtistName().toLowerCase().trim().compareTo(o2.getArtistName().toLowerCase().trim());
                    }
                });
                //_artist.sort((o1, o2) -> o1.getArtistName().toLowerCase().trim().compareTo(o2.getArtistName().toLowerCase().trim()));
            }
            cursor.close();
        }
        return _artist;
    }

    public static ArrayList<SongInfo> getMusicSelection(ContentResolver contentResolver, String selection) {
        _songs = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                    long albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                    long songId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumID);

                    SongInfo s = new SongInfo(name, artist, albumArtUri, album, date, songUri);
                    _songs.add(s);
                    Collections.sort(_songs, new Comparator<SongInfo>() {
                        @Override
                        public int compare(SongInfo o1, SongInfo o2) {
                            return o1.getName().toLowerCase().trim().compareTo(o2.getName().toLowerCase().trim());
                        }
                    });
                    //_songs.sort((o1, o2) -> o1.getName().toLowerCase().trim().compareTo(o2.getName().toLowerCase().trim()));

                } while (cursor.moveToNext());

            }
            cursor.close();
        }
        return _songs;
    }
}
