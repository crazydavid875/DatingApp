package com.quintus.labs.datingapp.Listed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.quintus.labs.datingapp.Models.Song;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

public class MusicList extends AppCompatActivity implements StartDrag{
    private static final String TAG = "MusicList_Activity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = MusicList.this;
    private RecyclerView recyclerView;
    private McListAdapter mAdapter;
    private ArrayList<Song> songList;
            private ItemTouchHelper touchHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        setupTopNavigationView();
        recyclerView = (RecyclerView)findViewById(R.id.playlists_rv);

        songList = new ArrayList<Song>();
        songList.add(new Song(0,new String("abc"),new String("abc")));
        songList.add(new Song(1,"cde","gff"));
        songList.add(new Song(2,"cde","gff"));
        songList.add(new Song(3,"cde","gff"));
        songList.add(new Song(4,"cde","gff"));
        mAdapter = new McListAdapter(this,songList,false,false);
        ItemTouchHelper.Callback callback =
                new Itemtouch_Helper(mAdapter);
         touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }
}