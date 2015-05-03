package com.fiuba.campus2015.dto.user;

public class Message {
    public String _id;
    public String content;
    public String postDate;
    public User user;

    public Message(String content, User user, String postDate){
        this.user = user;
        this.content = content;
        this.postDate = postDate;
    }

}
