package com.fiuba.campus2015.dto.user;

/**
 * Created by apetalas on 3/4/15.
 */
public class User {

    public String username;
    public String password;
    public String email;
    public String name;
    public Personal personal;
    public Education education;
    public Job job;

    public User(String name, String username, String password, String email, Personal personal){
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.personal = personal;

    }

    public User(String name, String username, Personal personal,Education education){
        this.name = name;
        this.username = username;
        this.personal = personal;
        this.education = education;
    }

}
