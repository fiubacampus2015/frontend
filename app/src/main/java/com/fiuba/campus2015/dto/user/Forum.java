package com.fiuba.campus2015.dto.user;

public class Forum {
    public String _id;
    public String content;
    public String title;
    public String group;

    public Forum(String title, String message){
        this.title = title;
        this.content = message;
    }

    public Forum(String id){
        this._id = id;
    }


}
