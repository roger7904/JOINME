package com.roger.joinme;

import android.net.Uri;

public class verify {
    public String name, gender,id,age,phone,activity;
    public Uri image;

    public verify()
    {

    }

    public verify(Uri image,String name, String gender, String age,String phone,String id,String activity) {
        this.name = name;
        this.gender = gender;
        this.image = image;
        this.id=id;
        this.phone=phone;
        this.age=age;
        this.activity=activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }


    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}