package com.example.music;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AlbumArtist extends AppCompatActivity implements SongsAdapter.OnSelectListener{

    private ImageView imageView;
    private TextView songs, albums, time, title;
    private ArrayList<SongInfo> _songs;
    private RecyclerView recyclerView;
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_artist_list);

        getSupportActionBar().hide();

        imageView = findViewById(R.id.profilePicture);
        songs = findViewById(R.id.noOfSongs);
        albums = findViewById(R.id.noOfAlbums);
        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recyclerView);
        int inflaterLayout = R.layout.songs_list;

        type = getIntent().getStringExtra("type");
        if(type.equals("artist")) {
            ArtistInfo artistInfo = (ArtistInfo) getIntent().getParcelableExtra("ArtistInfo");

            Picasso.get().load(artistInfo.getAlbumArt()).
                    error(R.drawable.default_song_icon).
                    placeholder(R.drawable.default_song_icon).
                    transform(new RoundedCornersTransformation(50, 5)).
                    into(imageView);
            songs.setText(artistInfo.getNoOfTracks() + " Song");
            albums.setText(artistInfo.getNoOfAlbums() + " Album");
            title.setText(artistInfo.getArtistName());

            String selection = MediaStore.Audio.Media.ARTIST_ID + " = " + artistInfo.getArtistId();
            _songs = GetSongs.getMusicSelection(getContentResolver(), selection);

            SongsAdapter songsAdapter = new SongsAdapter(_songs, AlbumArtist.this, inflaterLayout);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(songsAdapter);
        }
        else if(type.equals("album")) {
            AlbumInfo albumInfo = (AlbumInfo) getIntent().getParcelableExtra("AlbumInfo");

            Picasso.get().load(albumInfo.getAlbumArt()).
                    error(R.drawable.default_song_icon).
                    placeholder(R.drawable.default_song_icon).
                    transform(new RoundedCornersTransformation(50, 5)).
                    into(imageView);
            //songs.setText(albumInfo.getNoOfTracks() + " Song");
            //albums.setText(artistInfo.getNoOfAlbums() + " Album");
            title.setText(albumInfo.getAlbumName());

            String selection = MediaStore.Audio.Media.ALBUM_ID + " = " + albumInfo.getAlbumId();
            _songs = GetSongs.getMusicSelection(getContentResolver(), selection);

            SongsAdapter songsAdapter = new SongsAdapter(_songs, AlbumArtist.this, inflaterLayout);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(songsAdapter);
        }
    }

    @Override
    public void onSongLongClick(int position, View view) {

    }

    @Override
    public void onSongClick(int position, View view) {

    }
}
