package com.roger.joinme;

import android.net.Uri;

public class totalEvaluate {
    public String id,activityname;
    public Uri image;
    public String content,name;
    public Double star;

    public totalEvaluate(Uri image, String id, String activityname, String content, String name, Double star) {
        this.image = image;
        this.id=id;
        this.activityname=activityname;
        this.content = content;
        this.name = name; //userName
        this.star = star;
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

    public String getActivityContent() {
        return content;
    }

    public void setActivityContent(String location) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.id = name;
    }

    public Double getStar(){return star;}

    public void setStar(){this.star = star;}
}
