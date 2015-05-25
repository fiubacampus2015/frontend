package com.fiuba.campus2015.dto.user;

public class Forum {
    public String _id;
    public Message message;
    public String title;
    public String group;

    public Forum(String title, Message message){
        this.title = title;
        this.message = message;
    }

    public Forum(String id){
        this._id = id;
    }


}
