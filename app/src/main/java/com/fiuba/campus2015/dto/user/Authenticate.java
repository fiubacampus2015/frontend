package com.fiuba.campus2015.dto.user;

/**
 * Created by apetalas on 3/4/15.
 */
public class Authenticate {

    private String username;
    private String password;

    public Authenticate(String username, String password){
        this.username = username;
        this.password = password;
    }
}
