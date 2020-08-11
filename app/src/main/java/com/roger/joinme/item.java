package com.roger.joinme;

public class item {
    private String from;
    private String type,activityname;

//    public item() {
//        super();
//    }

    public item(String from, String type,String activityname) {
        super();
        this.from = from;
        this.type = type;
        this.activityname = activityname;
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
}
