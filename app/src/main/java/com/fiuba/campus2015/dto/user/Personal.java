package com.fiuba.campus2015.dto.user;

/**
 * Created by gonzalovelasco on 12/4/15.
 */
public class Personal {

    public String photo;
    public String comments;
    public String nacionality;
    public String city;
    public Phone phones;


    public Personal(String photo, String comments, String nationality, String city, Phone phones){
        this.photo = photo;
        this.comments = comments;
        this.nacionality = nationality;
        this.city = city;
        this.phones = phones;

    }


}

