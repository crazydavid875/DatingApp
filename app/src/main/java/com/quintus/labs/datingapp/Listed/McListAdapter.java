package com.quintus.labs.datingapp.Listed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.Matched.ActiveUserAdapter;
import com.quintus.labs.datingapp.Models.Song;
import com.quintus.labs.datingapp.R;

import java.util.List;

public class McListAdapter extends RecyclerView.Adapter<McListAdapter.MyViewHolder> {
    private List<Song> arraylist;
    private LayoutInflater  mInflater;

    public McListAdapter(Context context, List<Song> arraylist, boolean isPlaylistSong, boolean animate) {
        this.arraylist = arraylist;
        mInflater = LayoutInflater.from(context);
        /*
        this.isPlaylist = isPlaylistSong;
        this.songIDs = getSongIds();
        this.ateKey = Helpers.getATEKey(context);
        this.animate = animate;
        */

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.from(parent.getContext()).inflate(R.layout.fragment_mclist_item, null);
        MyViewHolder ml = new MyViewHolder(v);
        return ml;
    }

    @Override
    public void onBindViewHolder(MyViewHolder itemHolder, int i) {
        Song localItem = arraylist.get(i);

        itemHolder.title.setText(localItem.title);
        itemHolder.artist.setText(localItem.artistName);
        /*
                ImageLoader.getInstance().displayImage(TimberUtils.getAlbumArtUri(localItem.albumId).toString(),
                itemHolder.albumArt, new DisplayImageOptions.Builder().cacheInMemory(true)
                        .showImageOnLoading(R.drawable.ic_empty_music2)
                        .resetViewBeforeLoading(true).build());
        * */
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
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
