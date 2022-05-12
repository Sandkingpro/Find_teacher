package com.example.myapplication.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String city;
    private String Uid;
    private String type_education;
    private String gender;
    private String photo;
    private double rating=0;
    private String phone;
    private String name;
    private String about_me;
    private int type;
    private List<String> documents=new ArrayList<>();
    private List<Review> reviews=new ArrayList<>();
    public User(){};
    public User(String email,String name,String gender,String photo,double rating,String city,int type,List<String> documents,String type_education,String phone,String Uid){
        this.email=email;
        this.city=city;
        this.gender=gender;
        this.photo=photo;
        this.rating=rating;
        this.name=name;
        this.type=type;
        this.documents=documents;
        this.type_education=type_education;
        this.phone=phone;
        this.Uid=Uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
    public String getPhone(){return phone;}
    public String getGender() {
        return gender;
    }
    public String getAbout_me(){
        return about_me;
    }
    public void setAbout_me(String about_me){
        this.about_me=about_me;
    }
    public double getRating() {
        return rating;
    }

    public String getPhoto() {
        return photo;
    }

    public int getType() {
        return type;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public String getType_education() {
        return type_education;
    }
    public List<Review> getReviews(){
        return reviews;
    }

    public String getUid() {
        return Uid;
    }

    public void setReviews(List<Review> reviews){
        this.reviews=reviews;
    }
    public void setUid(String Uid){this.Uid=Uid;}
    public void setRating(double rating){this.rating=rating;}
    public void setCity(String city){
        this.city=city;
    }
    public void setPhoto(String photo){
        this.photo=photo;
    }
    public void setType_education(String type_education){this.type_education=type_education;}
}
