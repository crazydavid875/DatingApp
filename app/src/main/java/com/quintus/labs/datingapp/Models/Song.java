package com.quintus.labs.datingapp.Models;

import android.content.Intent;

import com.quintus.labs.datingapp.Listed.MusicList;

public class Song {

    //public final long albumId;
   // public final String albumName;
   // public final long artistId;
    public final String artistName;
   // public final int duration;
    public final int id;
    public final String title;

  //  public final int trackNumber;

    public Song() {
        this.id = -1;
      //  this.albumId = -1;
      //  this.artistId = -1;
        this.title = "";
        this.artistName = "";
    //    this.albumName = "";
      //  this.duration = -1;
      //  this.trackNumber = -1;
    }

    public Song(int _id, String _title, String _artistName) {
        this.id = _id;
       // this.albumId = _albumId;
        //this.artistId = _artistId;
        this.title = _title;
        this.artistName = _artistName;
       // this.albumName = _albumName;
        //this.duration = _duration;
        //this.trackNumber = _trackNumber;
    }
}
