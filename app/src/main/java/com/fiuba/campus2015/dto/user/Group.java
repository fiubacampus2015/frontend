package com.fiuba.campus2015.dto.user;

public class Group {
    public String _id;
    public String date;
    public String name;
    public String description;
    public String photo;
    public String owner;

    public Group(String user, String name, String description, String photo){
        this.owner = user;
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public Group(String titulo){
        this.name = titulo;
    }


}
