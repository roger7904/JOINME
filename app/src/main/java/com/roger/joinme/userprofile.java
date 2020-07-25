package com.roger.joinme;

public class userprofile {
    public String name, status, image,id;

    public userprofile()
    {

    }

    public userprofile(String name, String status, String image,String id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}