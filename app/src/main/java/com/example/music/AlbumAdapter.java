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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> implements Filterable {

    public ArrayList<AlbumInfo> albumInfo;
    public ArrayList<AlbumInfo> songsInfoComplete;
    private OnSelectListener onSelectListener;
    private int inflaterLayout;

    public AlbumAdapter(ArrayList<AlbumInfo> albumInfo, OnSelectListener onSelectListener, int inflaterLayout) {
        this.albumInfo = albumInfo;
        this.inflaterLayout = inflaterLayout;
        songsInfoComplete = new ArrayList<>(albumInfo);
        setHasStableIds(true);
        this.onSelectListener = onSelectListener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(inflaterLayout, parent, false);
        return new AlbumViewHolder(view, onSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        AlbumInfo album = albumInfo.get(position);
        holder.title.setText(album.getAlbumName());
        holder.artist.setText(album.getAlbumArtist());
        Picasso.get().load(album.getAlbumArt()).
                error(R.drawable.default_song_icon).
                placeholder(R.drawable.default_song_icon).
                transform(new RoundedCornersTransformation(50, 5)).
                into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return albumInfo.size();
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
            ArrayList<AlbumInfo> filteredAlbum = new ArrayList<AlbumInfo>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredAlbum.addAll(songsInfoComplete);
            } else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for (AlbumInfo songInfo : songsInfoComplete) {
                    if (songInfo.getAlbumName().toLowerCase().contains(pattern)) {
                        filteredAlbum.add(songInfo);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredAlbum;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            albumInfo.clear();
            albumInfo.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        ImageView icon;
        TextView title;
        TextView artist;
        OnSelectListener onSelectListener;

        public AlbumViewHolder(View itemView, OnSelectListener onSelectListener) {
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
            Collections.sort(albumInfo, new Comparator<AlbumInfo>() {
                @Override
                public int compare(AlbumInfo o1, AlbumInfo o2) {
                    return o1.getAlbumName().toLowerCase().compareTo(o2.getAlbumName().toLowerCase());
                }
            });
            //songsInfo.sort((o1, o2) -> o1.getAlbumName().toLowerCase().compareTo(o2.getAlbumName().toLowerCase()));
        } else if (sortById == R.id.artistSort) {
            Collections.sort(albumInfo, new Comparator<AlbumInfo>() {
                @Override
                public int compare(AlbumInfo o1, AlbumInfo o2) {
                    return o1.getAlbumArtist().toLowerCase().compareTo(o2.getAlbumArtist().toLowerCase());
                }
            });
            //albumInfo.sort((o1, o2) -> o1.getAlbumArtist().toLowerCase().compareTo(o2.getAlbumArtist().toLowerCase()));
        } else if (sortById == R.id.albumSort) {
            Collections.sort(albumInfo, new Comparator<AlbumInfo>() {
                @Override
                public int compare(AlbumInfo o1, AlbumInfo o2) {
                    return o1.getAlbumName().toLowerCase().compareTo(o2.getAlbumName().toLowerCase());
                }
            });
            //albumInfo.sort((o1, o2) -> o1.getAlbumName().toLowerCase().compareTo(o2.getAlbumName().toLowerCase()));
        } else if (sortById == R.id.yearSort) {
            Collections.sort(albumInfo, new Comparator<AlbumInfo>() {
                @Override
                public int compare(AlbumInfo o1, AlbumInfo o2) {
                    return o1.getDate().toLowerCase().compareTo(o2.getDate().toLowerCase());
                }
            });
            //albumInfo.sort((o1, o2) -> o1.getDate().toLowerCase().compareTo(o2.getDate().toLowerCase()));
        }

        if (sortOrderId == R.id.ascending) {
            //sortOrderValue = "ascending";
        } else if (sortOrderId == R.id.descending) {
            //sortOrderValue = "descending";
            Collections.reverse(albumInfo);
        }
        songsInfoComplete = new ArrayList<>(albumInfo);
        notifyDataSetChanged();
    }

}
