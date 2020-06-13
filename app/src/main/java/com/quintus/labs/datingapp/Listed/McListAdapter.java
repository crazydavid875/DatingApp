package com.quintus.labs.datingapp.Listed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.Models.Song;
import com.quintus.labs.datingapp.R;

import java.util.List;

public class McListAdapter extends RecyclerView.Adapter<McListAdapter.MyViewHolder> {
    List<Song> arraylist;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mclist_item, null);
        MyViewHolder ml = new MyViewHolder(v);
        return ml;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        protected TextView title, artist;
        protected ImageView albumArt;


        public MyViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.song_title);
            this.artist = (TextView) view.findViewById(R.id.song_artist);
            this.albumArt = (ImageView) view.findViewById(R.id.albumArt);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            //NavigationUtils.navigateToPlaylistDetail(mContext, getPlaylistType(getAdapterPosition()), (long) albumArt.getTag(), String.valueOf(title.getText()), foregroundColor, arraylist.get(getAdapterPosition()).id, null);

        }
    }
    public Song getSongAt(int i) {
        return arraylist.get(i);
    }

    public void addSongTo(int i, Song song) {
        arraylist.add(i, song);
    }


    public void removeSongAt(int i) {
        arraylist.remove(i);
        //updateDataSet(arraylist);
    }
}
