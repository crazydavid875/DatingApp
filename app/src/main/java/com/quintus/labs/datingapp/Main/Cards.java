package com.quintus.labs.datingapp.Main;


import android.net.Uri;

import com.quintus.labs.datingapp.R;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class Cards {
    private String userId;
    private String name, profileImageUrl, bio, interest,auther;
    private int age;
    private int distance;
    private Uri music;

    public Cards(String userId, String name,String auther, String profileImageUrl, String bio, String interest, int distance,int id) {
        this.userId = userId;
        this.name = name;
        this.age = 0;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.auther = auther;
        this.interest = interest;
        this.distance = distance;

        this.music = Uri.parse("android.resource://com.quintus.labs.datingapp/"+ id);

    }

    public Cards(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public Uri getMusic(){return  music;}
    public int getDistance() {
        return distance;
    }
    public String getAuther() {
        return auther;
    }
    public String getBio() {
        return bio;
    }

    public String getInterest() {
        return interest;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
