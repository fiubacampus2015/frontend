package com.fiuba.campus2015.dto.user;

public class Group {
    public String _id;
    public String lastMsgDate;
    public String name;
    public String description;
    public String photo;
    public String ownerid;

    public Group(String _id, String name, String description, String photo){
        this._id = _id;
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public Group(String titulo){
        this.name = titulo;
    }


}
