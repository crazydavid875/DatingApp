package com.quintus.labs.datingapp.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.quintus.labs.datingapp.Module.SongState;
import com.quintus.labs.datingapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {
    private boolean onShuffle = false;
    private boolean onRepeat = false;
    private int songNum=0;
    private SongState playing = SongState.PAUSE;
    private boolean scheduled = false;
    //private final Timer timer;
    //private lateinit var mService: MusicService
    //private lateinit var serviceConn: ServiceConnection
    private ImageView previousbtn, nextbtn, pausebtn, playbtn;
    private boolean pause_state=false;
    private MediaPlayer mPlayer = new MediaPlayer();
    private TextView songName, artist, startTime, songTime;
    private SeekBar songProcess;
    private static int oTime = 0, nowTime = 0, eTime = 0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    private  String songPath;
    private ArrayList<SongInfo> _songs = new ArrayList<SongInfo>();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        previousbtn=(ImageView) findViewById(R.id.previous_song);
        nextbtn=(ImageView) findViewById(R.id.next_song);
        playbtn=(ImageView) findViewById(R.id.player_center_icon);
        Log.d("test","PlayInitFinish");
        previousbtn.setClickable(true);
        nextbtn.setClickable(true);
        playbtn.setClickable(true);
        songName=(TextView) findViewById(R.id.song_name);
        startTime=(TextView)findViewById(R.id.player_current_position);
        songTime=(TextView)findViewById(R.id.complete_position);
        songProcess=(SeekBar)findViewById(R.id.player_seekbar);
        songProcess.setClickable(true);
        songPath="";
        checkUserPermission();//確認授權

        try {
            mPlayer.reset();
            mPlayer.setDataSource(_songs.get(songNum).getSongUrl());
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //案播放按鈕
        playbtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {

                //Play music State
                if(playing == SongState.PAUSE&&pause_state==false){
                    Log.d("test","ToPauseState");
                    Toast.makeText(MusicPlayer.this, "Playing Audio", Toast.LENGTH_SHORT).show();
                    playing=SongState.PLAYING;//設定狀態為play
                    playbtn.setImageDrawable(getDrawable(R.drawable.pause));
                    try {
                        mPlayer.reset();
                        mPlayer.setDataSource(_songs.get(songNum).getSongUrl());
                        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mPlayer.prepareAsync();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // 装载完毕回调
                            songProcess.setMax(eTime);
                            mPlayer.start();

                        }
                    });
                    Log.d("NAME",_songs.get(songNum).getSongname());

                    hdlr.postDelayed(UpdateSongTime, 100);
                }
                //繼續撥方
                else if(playing == SongState.PAUSE&&pause_state==true){
                    Toast.makeText(MusicPlayer.this, "Playing Audio", Toast.LENGTH_SHORT).show();
                    playing=SongState.PLAYING;//設定狀態為play
                    playbtn.setImageDrawable(getDrawable(R.drawable.pause));
                    mPlayer.start();
                    pause_state=false;
                    Log.d("NAME",_songs.get(songNum).getSongname());
                    songProcess.setProgress(nowTime);
                    hdlr.postDelayed(UpdateSongTime, 100);

                }
                //pause music State
                else {
                    Log.d("test","ToPlayState");
                    Toast.makeText(MusicPlayer.this, "Pausing Audio", Toast.LENGTH_SHORT).show();
                    playing=SongState.PAUSE;
                    mPlayer.pause();
                    pause_state=true;
                    playbtn.setImageDrawable(getDrawable(R.drawable.play));
                }
                if(oTime == 0){
                    songProcess.setMax(eTime);
                    oTime =1;
                }

                startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(nowTime),
                        TimeUnit.MILLISECONDS.toSeconds(nowTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(nowTime))) );
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songNum>=_songs.size()-1){
                    songNum=0;
                }
                else {
                    songNum+=1;
                }
                try{
                    mPlayer.reset();
                    mPlayer.setDataSource(_songs.get(songNum).getSongUrl());
                    mPlayer.prepare();
                    mPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                    nowTime = 0;
                    mPlayer.seekTo(nowTime);
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }

            }
        });
        previousbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songNum<=0){
                    songNum=_songs.size()-1;
                }
                else{
                    songNum-=1;
                }
                try{
                    mPlayer.reset();
                    mPlayer.setDataSource(_songs.get(songNum).getSongUrl());
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nowTime = 0;
                mPlayer.seekTo(nowTime);
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });
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
    private Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            songName.setText(_songs.get(songNum).getSongname());
            nowTime = mPlayer.getCurrentPosition();
            startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(nowTime),
                    TimeUnit.MILLISECONDS.toSeconds(nowTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(nowTime))) );
            songProcess.setProgress(nowTime);
            eTime = mPlayer.getDuration();
            songTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                    TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
            hdlr.postDelayed(this, 100);
        }
    };
    private void loadSongs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    SongInfo s = new SongInfo(name,artist,url);
                    _songs.add(s);
                    //Log.d("name",url);
                }while (cursor.moveToNext());
            }
            cursor.close();
       }
    }
}


