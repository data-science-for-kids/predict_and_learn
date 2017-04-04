package com.example.user.datascienceapp.Wrappers;

import java.io.Serializable;

/**
 * @see java.io.Serializable
 * @serial ( Objects of the class to be passed in threads)
 */

public class DataBean implements Serializable {
    private String name;
    private String gender;
    private int id;
    private int activity;
    private String game;
    private int name_age;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getNameAge() {
        return name_age;
    }

    public void setNameAge(int name_age) {
        this.name_age = name_age;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
