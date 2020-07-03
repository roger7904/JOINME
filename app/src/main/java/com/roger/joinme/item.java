package com.roger.joinme;

public class item {
    private int id;
    private String content;

//    public item() {
//        super();
//    }

    public item(int id, String content) {
        super();
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
