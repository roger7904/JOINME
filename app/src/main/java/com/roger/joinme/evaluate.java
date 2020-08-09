package com.roger.joinme;

import android.net.Uri;

public class evaluate {
    public String name,id;
    public Uri image;

    public evaluate()
    {

    }

    public evaluate(String name,  Uri image,String id) {
        this.name = name;
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

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}