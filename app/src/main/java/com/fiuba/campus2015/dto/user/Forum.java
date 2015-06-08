package com.fiuba.campus2015.dto.user;

import java.util.List;

public class Forum {
    public String _id;
    public Message message;
    public List<Action> actions;
    public String title;
    public String group;
    public User owner;
    public boolean suspend;

    public Forum(String title, Message message){
        this.title = title;
        this.message = message;
    }

    public Forum(String id){
        this._id = id;
    }


}
