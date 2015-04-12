package com.fiuba.campus2015.dto.user;

/**
 * Created by gonzalovelasco on 12/4/15.
 */
public class Personal {

    public String photo;
    public String comments;
    public String nacionality;
    public String city;
    public String gender;
    public Phone phones;

    public Personal(String nationality, String gender, Phone phone){
        this.nacionality = nationality;
        this.gender = gender;
        this.phones = phone;

    }


    public Personal(String photo, String comments, String nationality, String city, String gender, Phone phones){
        this.photo = photo;
        this.comments = comments;
        this.nacionality = nationality;
        this.city = city;
        this.gender = gender;
        this.phones = phones;

    }


}

