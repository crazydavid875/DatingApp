package com.quintus.labs.datingapp.Listed;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Collections;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.Matched.ActiveUserAdapter;
import com.quintus.labs.datingapp.Models.Song;
import com.quintus.labs.datingapp.R;

import java.util.List;

public class McListAdapter extends RecyclerView.Adapter<McListAdapter.MyViewHolder>
        implements Itemtouch_Helper.ItemTouchHelperContract{
    private List<Song> arraylist;
    private LayoutInflater  mInflater;
    private StartDrag mStartDragListener;
    public McListAdapter( final MusicList context, List<Song> arraylist, boolean isPlaylistSong, boolean animate) {
        this.arraylist = arraylist;
        mInflater = LayoutInflater.from(context);

        mStartDragListener = context;

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
        final MyViewHolder ml = new MyViewHolder(v);
        ml.hold.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    mStartDragListener.requestDrag(ml);
                }
                return false;
            }
        });
        return ml;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder itemHolder, int i) {
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
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(arraylist, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(arraylist, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        //myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        protected TextView title, artist;
        protected ImageView albumArt,hold;
        View rowView;

        public MyViewHolder(View view) {
            super(view);
            rowView = view;
            this.title = (TextView) view.findViewById(R.id.song_title);
            this.artist = (TextView) view.findViewById(R.id.song_artist);
            this.albumArt = (ImageView) view.findViewById(R.id.albumArt);
            this.hold = (ImageView) view.findViewById(R.id.reorder) ;

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
