package com.roger.joinme;

import android.net.Uri;

public class signuppage {
    public String activityname;
    public Uri image;
    public String location,startTime;

    public signuppage(Uri image, String activityname, String location, String startTime) {
        this.image = image;
        this.activityname=activityname;
        this.location = location;
        this.startTime = startTime;
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

    public String getstartTime() {
        return startTime;
    }

    public void setstartTime(String startTime) {
        this.startTime = startTime;
    }
}
