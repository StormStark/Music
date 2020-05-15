package com.example.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Player extends AppCompatActivity{

    private MediaPlayer mMediaPlayer;
    private ArrayList<SongInfo> _songs;
    private ViewPager imagePager;
    int current = 0;
    private Uri[] songImages;
    private ImageView playPause, nextSong, previousSong, shuffleQueue, repeatSong;
    private SeekBar timeSeekBar, volumeSeekBar;
    private TextView currentTime, totalRunTime, songTitle, artistTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().hide();

        currentTime = findViewById(R.id.currentTime);

        _songs = PlayingQueue.getQueue();
        current = PlayingQueue.getCurrentPosition();
        SongInfo songInfo = _songs.get(current);

        mMediaPlayer = new MediaPlayerHandler().getMediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(getApplicationContext(), songInfo.getPath());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            //mMediaPlayer.seekTo(mMediaPlayer.getDuration()-5000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        songImages = new Uri[_songs.size()];
        int i=0;

        for(SongInfo s : _songs){
            songImages[i++] = s.getUrl();
        }


        Log.d("POSITION", current+"");
        imagePager = findViewById(R.id.songsImageViewer);
        ImagePagerAdapter adapter = new ImagePagerAdapter(songImages);
        imagePager.setAdapter(adapter);
        imagePager.setCurrentItem(current);
    }

    private void setDetails(SongInfo songInfo){

    }

    private class ImagePagerAdapter extends PagerAdapter {
        private Uri[] mImages;

        ImagePagerAdapter(Uri[] songImages){
            mImages = songImages;
        }

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = Player.this;
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            imageView.setLayoutParams(imageParam);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Picasso.get().load(mImages[position]).
                    error(R.drawable.default_song_icon).
                    placeholder(R.drawable.default_song_icon).
                    into(imageView);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }
}
