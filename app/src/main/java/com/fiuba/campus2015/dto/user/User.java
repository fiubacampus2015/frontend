package com.fiuba.campus2015.dto.user;

/**
 * Created by apetalas on 3/4/15.
 */
public class User {

    public String username;
    public String password;
    public String email;
    public String name;
    public String phone;


    public User(String name, String username, String password, String email){
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }


}
