package com.example.myapplication.models;

import android.hardware.camera2.CameraManager;

public class Advertisement {
    User user;
    String format;
    String where_educate;
    String subject;
    int price;
    public Advertisement(){};
    public Advertisement(User user,String where_educate,String subject,int price,String format){
        this.user=user;
        this.where_educate=where_educate;
        this.subject=subject;
        this.price=price;
        this.format=format;
    }
    public String getWhere_educate(){
        return where_educate;
    }
    public String getSubject(){
        return subject;
    }
    public int getPrice(){
        return price;
    }

    public String getFormat() {
        return format;
    }

    public User getUser() {
        return user;
    }
}
