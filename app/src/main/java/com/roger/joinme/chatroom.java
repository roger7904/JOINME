package com.roger.joinme;

import android.net.Uri;

public class chatroom {
    public String name, newestcontent,id,time,activity,image,date;
    public Integer contentcount;

    public chatroom()
    {

    }

    public chatroom(String name, String newestcontent, String image,String id,Integer contentcount,String time,String activity,String date) {
        this.name = name;
        this.newestcontent = newestcontent;
        this.image = image;
        this.id=id;
        this.contentcount=contentcount;
        this.time=time;
        this.activity=activity;
        this.date=date;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getContentcount() {
        return contentcount;
    }

    public void setContentcount(Integer contentcount) {
        this.contentcount = contentcount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}