package com.quintus.labs.datingapp.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
    private MediaPlayer mPlayer;
    private TextView songName, artist, startTime, songTime;
    private SeekBar songProcess;
    private static int oTime = 0, nowTime = 0, eTime = 0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    private  String songPath;



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
        //pausebtn.setClickable(true);

        songName=(TextView) findViewById(R.id.song_name);
        artist=(TextView)findViewById(R.id.artist_name);
        startTime=(TextView)findViewById(R.id.player_current_position);
        songTime=(TextView)findViewById(R.id.complete_position);
        songProcess=(SeekBar)findViewById(R.id.player_seekbar);
        songProcess.setClickable(true);
        songPath="";

        mPlayer = MediaPlayer.create(this, R.raw.test);


        //Uri uri = Uri.parse("android.resource://com.quintus.labs.datingapp/raw/"+R.raw.test);
        /*
        try {
            mPlayer.setDataSource(this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        playbtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                mPlayer.start();
                eTime = mPlayer.getDuration();
                nowTime = mPlayer.getCurrentPosition();
                if(oTime == 0){

                    songProcess.setMax(eTime);
                    oTime =1;
                }
                songTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                        TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(nowTime),
                        TimeUnit.MILLISECONDS.toSeconds(nowTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(nowTime))) );
                songProcess.setProgress(nowTime);
                hdlr.postDelayed(UpdateSongTime, 100);
               // pausebtn.setEnabled(true);
                if(playing == SongState.PAUSE){
                    Log.d("test","ToPauseState");
                    Toast.makeText(MusicPlayer.this, "Playing Audio", Toast.LENGTH_SHORT).show();
                    playing=SongState.PLAYING;
                    playbtn.setImageDrawable(getDrawable(R.drawable.pause));
                }
                else {
                    Log.d("test","ToPlayState");
                    Toast.makeText(MusicPlayer.this, "Pausing Audio", Toast.LENGTH_SHORT).show();
                    playing=SongState.PAUSE;
                    mPlayer.pause();
                    playbtn.setImageDrawable(getDrawable(R.drawable.play));
                }

            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                songNum+=1;
                try{
                    mPlayer.reset();
                    mPlayer.setDataSource("test2");
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
 */
                if((nowTime + fTime) <= eTime)
                {
                    nowTime = nowTime + fTime;
                    mPlayer.seekTo(nowTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }

                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }

            }
        });
        previousbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((nowTime - bTime) > 0)
                {
                    nowTime = nowTime - bTime;
                    mPlayer.seekTo(nowTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });

    }
    private Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            nowTime = mPlayer.getCurrentPosition();
            startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(nowTime),
                    TimeUnit.MILLISECONDS.toSeconds(nowTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(nowTime))) );
            songProcess.setProgress(nowTime);
            hdlr.postDelayed(this, 100);
        }
    };
}


