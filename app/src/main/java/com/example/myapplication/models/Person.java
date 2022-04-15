package com.example.myapplication.models;

public class Person {
    private String login;
    private String password;
    public Person(String login, String password){
        this.login=login;
        this.password=password;
    }
    public String getLogin(){
        return login;
    }
    public String getPassword(){
        return password;
    }
}
