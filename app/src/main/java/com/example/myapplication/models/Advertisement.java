package com.example.myapplication.models;

import android.hardware.camera2.CameraManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Advertisement {
    String uid;
    String format;
    List<String> type_students=new ArrayList<>();
    String subject;
    int price;
    List<String> additionally=new ArrayList<>();
    public Advertisement(){};
    public Advertisement(String uid,String subject,int price,String format,List<String> additionally,List<String> type_students){
        this.uid=uid;
        this.subject=subject;
        this.price=price;
        this.format=format;
        this.additionally=additionally;
        this.type_students=type_students;
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
    public List<String> getType_students(){return type_students;}
    public String getUid() {
        return uid;
    }
    public List<String> getAdditionally(){
        return additionally;
    }
    public void setType_students(List<String> type_students){
        this.type_students=type_students;
    }
    public void setSubject(String subject){
        this.subject=subject;
    }
    public void setFormat(String format){
        this.format=format;
    }
    public void setPrice(int price){
        this.price=price;
    }
    public void setAdditionally(List<String> additionally){
        this.additionally=additionally;
    }
}
