package com.example.music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

public class MediaPlayerHandler implements MediaPlayer.OnCompletionListener{
    private MediaPlayer mediaPlayer;

    MediaPlayerHandler(){
        mediaPlayer = new MediaPlayer();
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d("T", "Aw");
    }
}
