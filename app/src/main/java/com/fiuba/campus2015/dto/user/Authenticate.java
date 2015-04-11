package com.fiuba.campus2015.dto.user;

/**
 * Created by apetalas on 3/4/15.
 */
public class Authenticate {

    private String email;
    private String password;

    public Authenticate(String email, String password){
        this.email = email;
        this.password = password;
    }
}
