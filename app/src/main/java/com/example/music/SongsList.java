package com.example.music;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SongsList extends AppCompatActivity implements SongsAdapter.OnSelectListener {

    private RecyclerView recyclerView;
    private ArrayList<SongInfo> _songs;
    private ArrayList<Integer> selectedPositions;
    private ArrayList<View> selectedViews;
    private ActionMode actionMode = null;
    private ImageView actionBarSort, actionBarMenu;
    private EditText searchView;
    private boolean filteringEnabled = false;
    private boolean selectionEnabled = false;
    private boolean sortSheetEnabled = false;

    private CoordinatorLayout sortBottomSheetLayout;
    private BottomSheetBehavior sortSheetBehaviour;

    private RadioGroup sortByGroup, sortOrderGroup;
    private FloatingActionButton sortFAB;

    private String sortByValue = "title";
    private String sortOrderValue = "ascending";

    private BottomNavigationView mainBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setBackgroundDrawable((new ColorDrawable(Color.parseColor("#17191C"))));

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View actionBarView = LayoutInflater.from(this).inflate(R.layout.action_bar_search_layout, null);
        getSupportActionBar().setCustomView(actionBarView, lp);

        recyclerView = findViewById(R.id.recyclerView);
        _songs = new ArrayList<SongInfo>();
        selectedPositions = new ArrayList<Integer>();
        selectedViews = new ArrayList<View>();
        _songs = GetSongs.getMusic(getContentResolver());
        int inflaterLayout = R.layout.songs_list;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongsAdapter songsAdapter = new SongsAdapter(_songs, this, inflaterLayout);
        recyclerView.setAdapter(songsAdapter);

        sortBottomSheetLayout = findViewById(R.id.bottom_sheet);
        sortSheetBehaviour = BottomSheetBehavior.from(sortBottomSheetLayout);

        actionBarMenu = actionBarView.findViewById(R.id.actionBarMenu);
        actionBarSort = actionBarView.findViewById(R.id.actionBarSort);
        searchView = actionBarView.findViewById(R.id.actionBarSearch);
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    filteringEnabled = true;
                    actionBarMenu.setImageResource(R.drawable.ic_arrow_back);
                } else {
                    filteringEnabled = false;
                    actionBarMenu.setImageResource(R.drawable.ic_menu);
                }
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                songsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        actionBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filteringEnabled == true) {
                    searchView.clearFocus();
                    hideSoftKeyboard(searchView);
                }
            }
        });

        actionBarSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sortSheetEnabled == false) {
                    sortSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                    sortSheetEnabled = true;
                }
                else {
                    sortSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    sortSheetEnabled = false;
                }
            }
        });

        sortByGroup = (RadioGroup) sortBottomSheetLayout.findViewById(R.id.sortBy);
        sortOrderGroup = (RadioGroup) sortBottomSheetLayout.findViewById(R.id.sortOrder);

        checkSortingOptions(sortByGroup, sortOrderGroup);

        sortFAB = findViewById(R.id.sortFAB);
        sortFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long sortById = sortByGroup.getCheckedRadioButtonId();
                long sortOrderId = sortOrderGroup.getCheckedRadioButtonId();

                songsAdapter.sort(sortById, sortOrderId);

                if (sortById == R.id.titleSort) {
                    sortByValue = "title";
                } else if (sortById == R.id.artistSort) {
                    sortByValue = "artist";
                } else if (sortById == R.id.albumSort) {
                    sortByValue = "album";
                } else if (sortById == R.id.yearSort) {
                    sortByValue = "year";
                }

                if (sortOrderId == R.id.ascending) {
                    sortOrderValue = "ascending";
                } else if (sortOrderId == R.id.descending) {
                    sortOrderValue = "descending";
                }
                checkSortingOptions(sortByGroup, sortOrderGroup);
                sortSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        mainBottomNavigation = findViewById(R.id.mainBottomNavigation);
        mainBottomNavigation.setSelectedItemId(R.id.bottomNavigationSongsMenuId);
        mainBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int previousItem = mainBottomNavigation.getSelectedItemId();
                final int nextItem = item.getItemId();
                if (previousItem != nextItem) {
                    switch (nextItem) {
                        case R.id.bottomNavigationArtistMenuId:
                            // open fragment 1
                            break;
                        case R.id.bottomNavigationAlbumMenuId:
                            //Intent intent = new Intent(SongsList.this, AlbumList.class);
                            //startActivity(intent);
                            int inflaterLayout = R.layout.songs_list;
                            SongsAdapter songsAdapter = new SongsAdapter(_songs, SongsList.this, inflaterLayout);
                            Fragment first = new SongFragment(actionBarView, "song");
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.sampleTest, first);
                            ft.commit();
                            break;
                        case R.id.bottomNavigationPlaylistMenuId:
                            // open fragment 3
                            break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onSongLongClick(int position, View view) {
        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (null != holder) {
            if (selectedPositions.contains(position)) {
                holder.itemView.setBackgroundColor(0x00000000);
                selectedPositions.remove(Integer.valueOf(position));
                selectedViews.remove(view);
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#494949"));
                selectedPositions.add(position);
                selectedViews.add(view);
                actionMode = startActionMode(mActionModeCallback);
            }

            if (!selectedPositions.isEmpty())
                selectionEnabled = true;
            else
                selectionEnabled = false;

        }
    }

    public void onSongClick(int position, View view) {
        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (null != holder) {
            if (selectionEnabled == true) {
                if (selectedPositions.contains(position)) {
                    holder.itemView.setBackgroundColor(0x00000000);
                    selectedPositions.remove(Integer.valueOf(position));
                    selectedViews.remove(view);
                } else {
                    holder.itemView.setBackgroundColor(Color.parseColor("#494949"));
                    selectedPositions.add(position);
                    selectedViews.add(view);
                }

                if (!selectedPositions.isEmpty())
                    selectionEnabled = true;
                else
                    selectionEnabled = false;
            }
        }

    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menuInflater.inflate(R.menu.contextual_song_selected_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            actionMode = null;
            selectionEnabled = false;
            deSelectAll();
        }
    };

    private void selectAll() {

    }

    private void deSelectAll() {
        for (View view : selectedViews) {
            view.setBackgroundColor(0x00000000);
        }
        selectedViews.clear();
        selectedPositions.clear();
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void checkSortingOptions(RadioGroup sortByGroup, RadioGroup sortOrderGroup) {
        if (sortByValue.equals("title"))
            sortByGroup.check(R.id.titleSort);
        else if (sortByValue.equals("artist"))
            sortByGroup.check(R.id.artistSort);
        else if (sortByValue.equals("album"))
            sortByGroup.check(R.id.albumSort);
        else if (sortByValue.equals("year"))
            sortByGroup.check(R.id.yearSort);

        if (sortOrderValue.equals("ascending"))
            sortOrderGroup.check(R.id.ascending);
        else
            sortOrderGroup.check(R.id.descending);
    }
}
