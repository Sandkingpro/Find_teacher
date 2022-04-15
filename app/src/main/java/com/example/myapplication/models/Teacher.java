package com.example.myapplication.models;

public class Teacher {
    String name;
    String type_education;
    String where_educate;
    String subject;
    int photo;
    int price;
    public Teacher(String name, String type_education, String where_educate, String subject
    , int price, int photo){
        this.name=name;
        this.type_education=type_education;
        this.where_educate=where_educate;
        this.subject=subject;
        this.price=price;
        this.photo=photo;
    }
    public String getName(){
        return name;
    }
    public String getType_education(){
        return type_education;
    }
    public String getWhere_educate(){
        return where_educate;
    }
    public String getSubject(){
        return subject;
    }
    public int getPhoto(){
        return photo;
    }
    public int getPrice(){
        return price;
    }
}
