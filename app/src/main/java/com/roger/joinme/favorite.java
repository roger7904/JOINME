package com.roger.joinme;

import android.net.Uri;

public class favorite {
    public String name, place,time;
    public Uri image;

    public favorite()
    {

    }

    public favorite(Uri image,String name, String place, String time) {
        this.name = name;
        this.place = place;
        this.image = image;
        this.time=time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}