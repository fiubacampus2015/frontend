package com.fiuba.campus2015.dto.user;

import java.util.List;

/**
 * Created by apetalas on 3/4/15.
 */
public class User implements Comparable <User> {
    public String _id;
    public String username;
    public String lastPosition;
    public String password;
    public String status;
    public String email;
    public String name;
    public Boolean friend;
    public Personal personal;
    public Education education;
    public Job job;
    public List<String> contacts;

    public User(String name, String username, String password, String email, Personal personal){
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.personal = personal;

    }

    public User(String name, String username, Personal personal,Education education, Job job){
        this.name = name;
        this.username = username;
        this.personal = personal;
        this.education = education;
        this.job = job;
    }
    public User(String id){
        this._id = id;
    }

    @Override
    public int compareTo(User another) {
        char title = Character.toUpperCase(name.charAt(0));
        char title2= Character.toUpperCase(another.name.charAt(0));

        if(title > title2) {
            return 1;
        } else if (title2 > title){
            return -1;
        }
        return 0;
    }

}
