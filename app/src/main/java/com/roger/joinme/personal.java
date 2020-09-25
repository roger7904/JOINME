package com.roger.joinme;

import android.net.Uri;

public class personal {
    public String id,activityname;
    public Uri image;
    public String location,name;

    public personal(Uri image, String id, String activityname, String location, String name) {
        this.image = image;
        this.id=id;
        this.activityname=activityname;
        this.location = location;
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getActivityLocation() {
        return location;
    }

    public void setActivityLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setNAme(String name) {
        this.id = name;
    }

}
