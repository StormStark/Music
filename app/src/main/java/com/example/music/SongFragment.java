package com.example.music;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SongFragment extends Fragment implements SongsAdapter.OnSelectListener, AlbumAdapter.OnSelectListener, ArtistAdapter.OnSelectListener {

    private RecyclerView recyclerView;
    private SongsAdapter songsAdapter;
    private AlbumAdapter albumAdapter;
    private ArtistAdapter artistAdapter;
    private ArrayList _songs;
    private ArrayList<Integer> selectedPositions;
    private ActionMode actionMode = null;
    private ArrayList<View> selectedViews;
    private boolean selectionEnabled = false;

    private ImageView actionBarSort, actionBarMenu;
    private EditText searchView;
    private boolean filteringEnabled = false;
    private boolean sortSheetEnabled = false;

    private CoordinatorLayout sortBottomSheetLayout;
    private BottomSheetBehavior sortSheetBehaviour;

    private RadioGroup sortByGroup, sortOrderGroup;
    private FloatingActionButton sortFAB;

    private String sortByValue = "title";
    private String sortOrderValue = "ascending";
    private View actionBarView;
    private String type;

    SongFragment(View actionBarView, String type) {
        this.actionBarView = actionBarView;
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        selectedPositions = new ArrayList<Integer>();
        selectedViews = new ArrayList<View>();
        int inflaterLayout = 0;
        recyclerView = view.findViewById(R.id.recyclerView);
        if (type.equals("album")) {
            _songs = new ArrayList<AlbumInfo>();
            inflaterLayout = R.layout.album_list;
            _songs = GetSongs.getAlbums(getActivity().getApplicationContext().getContentResolver());
            albumAdapter = new AlbumAdapter(_songs, SongFragment.this, inflaterLayout);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setAdapter(albumAdapter);
        } else if (type.equals("song")) {
            _songs = new ArrayList<SongInfo>();
            inflaterLayout = R.layout.songs_list;
            _songs = GetSongs.getMusic(getActivity().getApplicationContext().getContentResolver());
            songsAdapter = new SongsAdapter(_songs, SongFragment.this, inflaterLayout);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(songsAdapter);
        } else if (type.equals("artist")) {
            _songs = new ArrayList<ArtistInfo>();
            inflaterLayout = R.layout.artist_list;
            _songs = GetSongs.getArtists(getActivity().getApplicationContext().getContentResolver());
            artistAdapter = new ArtistAdapter(_songs, SongFragment.this, inflaterLayout);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setAdapter(artistAdapter);
        }

        sortBottomSheetLayout = getActivity().findViewById(R.id.bottom_sheet);
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
                if (type.equals("album"))
                    albumAdapter.getFilter().filter(charSequence);
                else if (type.equals("song"))
                    songsAdapter.getFilter().filter(charSequence);
                else if (type.equals("artist"))
                    artistAdapter.getFilter().filter(charSequence);
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
                if (sortSheetEnabled == false) {
                    sortSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                    sortSheetEnabled = true;
                } else {
                    sortSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    sortSheetEnabled = false;
                }
            }
        });

        sortByGroup = (RadioGroup) sortBottomSheetLayout.findViewById(R.id.sortBy);
        sortOrderGroup = (RadioGroup) sortBottomSheetLayout.findViewById(R.id.sortOrder);

        checkSortingOptions(sortByGroup, sortOrderGroup);

        sortFAB = getActivity().findViewById(R.id.sortFAB);
        sortFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long sortById = sortByGroup.getCheckedRadioButtonId();
                long sortOrderId = sortOrderGroup.getCheckedRadioButtonId();

                if (type.equals("album"))
                    albumAdapter.sort(sortById, sortOrderId);
                else if (type.equals("song"))
                    songsAdapter.sort(sortById, sortOrderId);
                else if (type.equals("artist"))
                    artistAdapter.sort(sortById, sortOrderId);

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

    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
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
                actionMode = SongFragment.this.getActivity().startActionMode(mActionModeCallback);
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
            else {
                if (type.equals("artist")) {
                    ArtistInfo artistInfo = (ArtistInfo) _songs.get(position);
                    Intent intent = new Intent(getActivity(), AlbumArtist.class);
                    intent.putExtra("ArtistInfo", artistInfo);
                    intent.putExtra("type", "artist");
                    getActivity().startActivity(intent);
                }
                else if (type.equals("album")) {
                    AlbumInfo albumInfo = (AlbumInfo) _songs.get(position);
                    Intent intent = new Intent(getActivity(), AlbumArtist.class);
                    intent.putExtra("AlbumInfo", albumInfo);
                    intent.putExtra("type", "album");
                    getActivity().startActivity(intent);
                }
                else if(type.equals("song")){
                    //SongInfo songInfo = (SongInfo) _songs.get(position);
                    PlayingQueue.setQueue(_songs);
                    PlayingQueue.setCurrentPosition(position);
                    Intent intent = new Intent(getActivity(), Player.class);
                    //intent.putExtra("SongInfo", songInfo);
                    //intent.putExtra("type", "album");
                    getActivity().startActivity(intent);
                }
            }
        }
    }

    private void deSelectAll() {
        for (View view : selectedViews) {
            view.setBackgroundColor(0x00000000);
        }
        selectedViews.clear();
        selectedPositions.clear();
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
