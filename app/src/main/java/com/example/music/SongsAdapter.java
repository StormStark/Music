package com.example.music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> implements Filterable {

    public ArrayList<SongInfo> songsInfo;
    public ArrayList<SongInfo> songsInfoComplete;
    private OnSelectListener onSelectListener;
    private int inflaterLayout;

    public SongsAdapter(ArrayList<SongInfo> songsInfo, OnSelectListener onSelectListener, int inflaterLayout) {
        this.songsInfo = songsInfo;
        this.inflaterLayout = inflaterLayout;
        songsInfoComplete = new ArrayList<>(songsInfo);
        setHasStableIds(true);
        this.onSelectListener = onSelectListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(inflaterLayout, parent, false);
        return new SongViewHolder(view, onSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongInfo song = songsInfo.get(position);
        holder.title.setText(song.getName());
        holder.artist.setText(song.getArtist());
        Picasso.get().load(song.getUrl()).
                error(R.drawable.default_song_icon).
                placeholder(R.drawable.default_song_icon).
                transform(new RoundedCornersTransformation(50, 5)).
                into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return songsInfo.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return songsInfoFilter;
    }

    private Filter songsInfoFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<SongInfo> filteredSong = new ArrayList<SongInfo>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredSong.addAll(songsInfoComplete);
            } else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for (SongInfo songInfo : songsInfoComplete) {
                    if (songInfo.getName().toLowerCase().contains(pattern)) {
                        filteredSong.add(songInfo);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredSong;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            songsInfo.clear();
            songsInfo.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        ImageView icon;
        TextView title;
        TextView artist;
        OnSelectListener onSelectListener;

        public SongViewHolder(View itemView, OnSelectListener onSelectListener) {
            super(itemView);
            itemView.setTag(this);
            artist = itemView.findViewById(R.id.artist);
            icon = itemView.findViewById(R.id.albumIcon);
            title = itemView.findViewById(R.id.title);
            this.onSelectListener = onSelectListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            onSelectListener.onSongLongClick(getAdapterPosition(), itemView);
            return true;
        }

        @Override
        public void onClick(View view) {
            onSelectListener.onSongClick(getAdapterPosition(), itemView);
        }
    }

    public interface OnSelectListener {
        void onSongLongClick(int position, View view);

        void onSongClick(int position, View view);
    }

    public void sort(long sortById, long sortOrderId) {
        if (sortById == R.id.titleSort) {
            Collections.sort(songsInfo, new Comparator<SongInfo>() {
                @Override
                public int compare(SongInfo o1, SongInfo o2) {
                    return o1.getName().toLowerCase().trim().compareTo(o2.getName().toLowerCase().trim());
                }
            });
            //songsInfo.sort((o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));
        } else if (sortById == R.id.artistSort) {
            Collections.sort(songsInfo, new Comparator<SongInfo>() {
                @Override
                public int compare(SongInfo o1, SongInfo o2) {
                    return o1.getArtist().toLowerCase().trim().compareTo(o2.getArtist().toLowerCase().trim());
                }
            });
            //songsInfo.sort((o1, o2) -> o1.getArtist().toLowerCase().compareTo(o2.getArtist().toLowerCase()));
        } else if (sortById == R.id.albumSort) {
            Collections.sort(songsInfo, new Comparator<SongInfo>() {
                @Override
                public int compare(SongInfo o1, SongInfo o2) {
                    return o1.getAlbum().toLowerCase().compareTo(o2.getAlbum().toLowerCase());
                }
            });
            //songsInfo.sort((o1, o2) -> o1.getAlbum().toLowerCase().compareTo(o2.getAlbum().toLowerCase()));
        } else if (sortById == R.id.yearSort) {
            Collections.sort(songsInfo, new Comparator<SongInfo>() {
                @Override
                public int compare(SongInfo o1, SongInfo o2) {
                    return o1.getDate().toLowerCase().compareTo(o2.getDate().toLowerCase());
                }
            });
            //songsInfo.sort((o1, o2) -> o1.getDate().toLowerCase().compareTo(o2.getDate().toLowerCase()));
        }

        if (sortOrderId == R.id.ascending) {
            //sortOrderValue = "ascending";
        } else if (sortOrderId == R.id.descending) {
            //sortOrderValue = "descending";
            Collections.reverse(songsInfo);
        }
        songsInfoComplete = new ArrayList<>(songsInfo);
        notifyDataSetChanged();
    }

}
