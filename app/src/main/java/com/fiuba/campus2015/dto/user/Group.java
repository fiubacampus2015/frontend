package com.fiuba.campus2015.dto.user;

import java.util.List;

public class Group {
    public String _id;
    public String date;
    public String status;
    public String name;
    public String description;
    public String photo;
    public User owner;
    public List<Action> actions;
    public Integer members;
    public Integer request;
    public Integer msgs;
    public Integer totalFiles;
    public boolean suspend;

    public Group(User user, String name, String description, String photo, String status){
        this.owner = user;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.status = status;
    }

    public Group(String id){
        this._id = id;
    }


}
