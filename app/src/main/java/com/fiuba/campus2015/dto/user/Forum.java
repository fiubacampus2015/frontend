package com.fiuba.campus2015.dto.user;

public class Forum {
    public String _id;
    public String message;
    public String title;
    public String group;

    public Forum(String title, String message){

    }

    public Forum(String id){
        this._id = id;
    }


}
