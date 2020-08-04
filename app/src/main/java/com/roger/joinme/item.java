package com.roger.joinme;

public class item {
    private String from;
    private String type;

//    public item() {
//        super();
//    }

    public item(String from, String type) {
        super();
        this.from = from;
        this.type = type;
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
}
