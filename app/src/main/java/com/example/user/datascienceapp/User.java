package com.example.user.datascienceapp;

import java.io.Serializable;

public class User implements Serializable {


    private String session;
    private int page;
    public String getSession() {
        return session;
    }
    public void setSession(String session) {
        this.session = session;
    }


    public void setpageNo(int page){
        this.page=page;
    }
    public int getPage(){
        return page;
    }

}
