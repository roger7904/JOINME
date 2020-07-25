package com.roger.joinme;

import android.net.Uri;

public class userprofile {
    public String name, status,id;
    public Uri image;

    public userprofile()
    {

    }

    public userprofile(String name, String status, Uri image,String id) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.id=id;
    }

    public String getName() {
        return name;
    }
    public String getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}