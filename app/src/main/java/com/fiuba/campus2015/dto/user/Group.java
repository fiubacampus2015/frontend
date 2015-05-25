package com.fiuba.campus2015.dto.user;

import java.util.List;

public class Group {
    public String _id;
    public String date;
    public String name;
    public String description;
    public String photo;
    public User owner;
    public List<Action> actions;
    public Integer totalMsgs;
    public Integer totalContacts;
    public Integer totalFiles;

    public Group(User user, String name, String description, String photo){
        this.owner = user;
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public Group(String titulo){
        this.name = titulo;
    }


}
