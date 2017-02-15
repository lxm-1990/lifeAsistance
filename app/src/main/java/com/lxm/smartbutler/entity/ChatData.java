package com.lxm.smartbutler.entity;

/**
 * Created by lxm on 17/2/15.
 */

public class ChatData {

    private int type;
    private String text;

    public ChatData(int type,String text) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
