package com.fiuba.campus2015.dto.user;

public class Group {
    public String _id;
    public String lastMsgDate;
    public String name;
    public String description;
    public String photo;
    public String ownerid;

    public Group(){
    }

    public Group(String id){
        this._id = id;
    }


}
