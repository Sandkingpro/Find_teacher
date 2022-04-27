package com.example.myapplication.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String city;
    private String type_education;
    private String gender;
    private String photo;
    private double rating=0;
    private String phone;
    private String name;
    private int type;
    private List<String> documents=new ArrayList<>();
    public User(){};
    public User(String email,String name,String gender,String photo,double rating,String city,int type,List<String> documents,String type_education,String phone){
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
}
