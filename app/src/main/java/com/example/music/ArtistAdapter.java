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

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> implements Filterable {

    public ArrayList<ArtistInfo> artistsInfo;
    public ArrayList<ArtistInfo> songsInfoComplete;
    private OnSelectListener onSelectListener;
    private int inflaterLayout;

    public ArtistAdapter(ArrayList<ArtistInfo> artistsInfo, OnSelectListener onSelectListener, int inflaterLayout) {
        this.artistsInfo = artistsInfo;
        this.inflaterLayout = inflaterLayout;
        songsInfoComplete = new ArrayList<ArtistInfo>(artistsInfo);
        setHasStableIds(true);
        this.onSelectListener = onSelectListener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(inflaterLayout, parent, false);
        return new ArtistViewHolder(view, onSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        ArtistInfo artist = artistsInfo.get(position);
        holder.title.setText(artist.getArtistName());
        Picasso.get().load(artist.getAlbumArt()).
                error(R.drawable.default_song_icon).
                placeholder(R.drawable.default_song_icon).
                transform(new RoundedCornersTransformation(50, 5)).
                into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return artistsInfo.size();
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
            ArrayList<ArtistInfo> filteredSong = new ArrayList<ArtistInfo>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredSong.addAll(songsInfoComplete);
            } else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for (ArtistInfo songInfo : songsInfoComplete) {
                    if (songInfo.getArtistName().toLowerCase().contains(pattern)) {
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
            artistsInfo.clear();
            artistsInfo.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        ImageView icon;
        TextView title;
        OnSelectListener onSelectListener;

        public ArtistViewHolder(View itemView, OnSelectListener onSelectListener) {
            super(itemView);
            itemView.setTag(this);
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
            Collections.sort(artistsInfo, new Comparator<ArtistInfo>() {
                @Override
                public int compare(ArtistInfo o1, ArtistInfo o2) {
                    return o1.getArtistName().toLowerCase().compareTo(o2.getArtistName().toLowerCase());
                }
            });
            //artistsInfo.sort((o1, o2) -> o1.getArtistName().toLowerCase().compareTo(o2.getArtistName().toLowerCase()));
        } else if (sortById == R.id.artistSort) {
            Collections.sort(artistsInfo, new Comparator<ArtistInfo>() {
                @Override
                public int compare(ArtistInfo o1, ArtistInfo o2) {
                    return o1.getArtistName().toLowerCase().compareTo(o2.getArtistName().toLowerCase());
                }
            });
            //artistsInfo.sort((o1, o2) -> o1.getArtistName().toLowerCase().compareTo(o2.getArtistName().toLowerCase()));
        } else if (sortById == R.id.albumSort) {
            Collections.sort(artistsInfo, new Comparator<ArtistInfo>() {
                @Override
                public int compare(ArtistInfo o1, ArtistInfo o2) {
                    return o1.getArtistName().toLowerCase().compareTo(o2.getArtistName().toLowerCase());
                }
            });
            //artistsInfo.sort((o1, o2) -> o1.getArtistName().toLowerCase().compareTo(o2.getArtistName().toLowerCase()));
        } else if (sortById == R.id.yearSort) {
            Collections.sort(artistsInfo, new Comparator<ArtistInfo>() {
                @Override
                public int compare(ArtistInfo o1, ArtistInfo o2) {
                    return o1.getDate().toLowerCase().compareTo(o2.getDate().toLowerCase());
                }
            });
            //artistsInfo.sort((o1, o2) -> o1.getDate().toLowerCase().compareTo(o2.getDate().toLowerCase()));
        }

        if (sortOrderId == R.id.ascending) {
            //sortOrderValue = "ascending";
        } else if (sortOrderId == R.id.descending) {
            //sortOrderValue = "descending";
            Collections.reverse(artistsInfo);
        }
        songsInfoComplete = new ArrayList<>(artistsInfo);
        notifyDataSetChanged();
    }

}
