package com.roger.joinme;

import android.net.Uri;

public class evaluate {
    public String name,id,activityname;
    public Uri image;

    public evaluate()
    {

    }

    public evaluate(String name,  Uri image,String id,String activityname) {
        this.name = name;
        this.image = image;
        this.id=id;
        this.activityname=activityname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return id;
    }


    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }
}