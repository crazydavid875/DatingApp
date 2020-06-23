package com.quintus.labs.datingapp.Listed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.quintus.labs.datingapp.Main.MusicPlayer;
import com.quintus.labs.datingapp.Main.SongInfo;
import com.quintus.labs.datingapp.Models.Song;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class MusicList extends AppCompatActivity implements StartDrag{
    private static final String TAG = "MusicList_Activity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = MusicList.this;
    private int id=0;
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
        /*
        songList.add(new Song(0,new String("abc"),new String("abc")));
        songList.add(new Song(1,"cde","gff"));
        songList.add(new Song(2,"cde","gff"));
        songList.add(new Song(3,"cde","gff"));
        songList.add(new Song(4,"cde","gff"));
         */
        try {
            checkUserPermission();//確認授權&Load file
        } catch (InputMismatchException ex) {
            songList.add(new Song(0,new String("abc"),new String("abc")));
            songList.add(new Song(1,"cde","gff"));
            songList.add(new Song(2,"cde","gff"));
            songList.add(new Song(3,"cde","gff"));
            songList.add(new Song(4,"cde","gff"));
            Log.d("Unknow","load song has some problem");
        }
            loadSongs();
        mAdapter = new McListAdapter(this,songList,false,false);
        ItemTouchHelper.Callback callback =
                new Itemtouch_Helper(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Intent intent=new Intent(MusicList.this, MusicPlayer.class);
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
    private void checkUserPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return;
            }
        }
        loadSongs();
    }
    private void loadSongs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            Log.d("Load File Name",uri.toString());
            if(cursor.moveToFirst()){
                do{
                    Log.d("Load File Name","Start2222222222222");
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    Song s = new Song(id,name,artist);
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    Log.d("Load File Name",url);
                    songList.add(s);
                    id++;
                }while (cursor.moveToNext());
            }
            else{
                Log.d("Load File","can't find any file");
            }
            cursor.close();
        }
    }

}