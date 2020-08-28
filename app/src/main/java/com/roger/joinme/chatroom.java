package com.roger.joinme;

import android.net.Uri;

public class chatroom {
    public String name, newestcontent,id,time,contentcount;
    public Uri image;

    public chatroom()
    {

    }

    public chatroom(String name, String newestcontent, Uri image,String id,String contentcount,String time) {
        this.name = name;
        this.newestcontent = newestcontent;
        this.image = image;
        this.id=id;
        this.contentcount=contentcount;
        this.time=time;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewestcontent() {
        return newestcontent;
    }

    public void setNewestcontent(String newestcontent) {
        this.newestcontent = newestcontent;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getContentcount() {
        return contentcount;
    }

    public void setContentcount(String contentcount) {
        this.contentcount = contentcount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}