package com.example.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainSongNavigator extends AppCompatActivity{

    private BottomNavigationView mainBottomNavigation;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_song_navigator);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setBackgroundDrawable((new ColorDrawable(Color.parseColor("#17191C"))));

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View actionBarView = LayoutInflater.from(this).inflate(R.layout.action_bar_search_layout, null);
        getSupportActionBar().setCustomView(actionBarView, lp);

        // Set default List
        Fragment albumFragment = new SongFragment(actionBarView, "song");
        recyclerView = ((SongFragment) albumFragment).getRecyclerView();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentHolder, albumFragment);
        ft.commit();


        mainBottomNavigation = findViewById(R.id.mainBottomNavigation);
        mainBottomNavigation.setSelectedItemId(R.id.bottomNavigationSongsMenuId);
        mainBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int previousItem = mainBottomNavigation.getSelectedItemId();
                final int nextItem = item.getItemId();
                if (previousItem != nextItem) {
                    switch (nextItem) {
                        case R.id.bottomNavigationSongsMenuId:
                            Fragment albumFragment = new SongFragment(actionBarView, "song");
                            recyclerView = ((SongFragment) albumFragment).getRecyclerView();
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.fragmentHolder, albumFragment);
                            ft.commit();
                            break;
                        case R.id.bottomNavigationAlbumMenuId:

                            albumFragment = new SongFragment(actionBarView, "album");
                            recyclerView = ((SongFragment) albumFragment).getRecyclerView();
                            fm = getSupportFragmentManager();
                            ft = fm.beginTransaction();
                            ft.replace(R.id.fragmentHolder, albumFragment);
                            ft.commit();
                            break;
                        case R.id.bottomNavigationArtistMenuId:
                            albumFragment = new SongFragment(actionBarView, "artist");
                            recyclerView = ((SongFragment) albumFragment).getRecyclerView();
                            fm = getSupportFragmentManager();
                            ft = fm.beginTransaction();
                            ft.replace(R.id.fragmentHolder, albumFragment);
                            ft.commit();
                            break;
                    }
                }
                return true;
            }
        });
    }

    private void selectAll() {

    }
}
