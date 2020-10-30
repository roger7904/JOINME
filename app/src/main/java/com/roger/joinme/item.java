package com.roger.joinme;

public class item {
    private String from,id;
    private String type,activityname,lookornot;

//    public item() {
//        super();
//    }

    public item(String from, String type,String activityname,String lookornot,String id) {
        super();
        this.from = from;
        this.type = type;
        this.activityname = activityname;
        this.lookornot=lookornot;
        this.id=id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }
    public String getLookornot() {
        return lookornot;
    }

    public void setLookornot(String lookornot) {
        this.lookornot = lookornot;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
