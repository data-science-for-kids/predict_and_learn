package com.example.user.datascienceapp;

public class Response {
    private String exercise_id;
    private String user_id;
    private String card_id;
    private String response;

    public String getUser() {
        return user_id;
    }
    public void setUser(String user_id) {
        this.user_id = user_id;
    }
    public String getExercise() {
        return exercise_id;
    }
    public void setExercise(String exercise_id) {
        this.exercise_id = exercise_id;
    }
    public String getCard() {
        return card_id;
    }
    public void setCard(String card_id) {
        this.card_id = card_id;
    }
    public String getResponse(){
        return response;
    }
    public void setResponse(String response){
        this.response=response;
    }



}
