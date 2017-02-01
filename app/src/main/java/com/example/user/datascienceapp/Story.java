package com.example.user.datascienceapp;

import java.io.Serializable;

public class Story implements Serializable {


    private String text;
    private boolean image;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public void imagePresent(boolean image) {
        this.image = image;
    }

    public boolean isImage() {
        return image;
    }


}
