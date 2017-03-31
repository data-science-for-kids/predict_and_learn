package com.example.user.datascienceapp.Wrappers;

import java.io.Serializable;

public class Session implements Serializable {
   
    private int ses;
    private int card;
    private int page;

    public int getSession() {
        return ses;
    }
    public void setSession(int ses) {
        this.ses = ses;
    }

    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }




}
