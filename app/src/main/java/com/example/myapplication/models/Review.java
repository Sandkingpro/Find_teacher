package com.example.myapplication.models;

public class Review {
    String name;
    String commentary;
    int rating;
    public Review(String name,String commentary,int rating){
        this.name=name;
        this.commentary=commentary;
        this.rating=rating;
    }
    public String getName(){
        return name;
    }
    public String getCommentary(){
        return  commentary;
    }
    public int getRating(){
        return  rating;
    }
}
