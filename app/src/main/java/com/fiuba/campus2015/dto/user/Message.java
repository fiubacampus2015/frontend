package com.fiuba.campus2015.dto.user;

import com.fiuba.campus2015.Configuration;
import com.fiuba.campus2015.extras.Constants;

public class Message {
    public String _id;
    public String content;
    public String postDate;
    public User user;
    public Constants.MsgCardType type;

    public Message(String content, User user, String postDate, Constants.MsgCardType type){
        this.user = user;
        this.content = content;
        this.postDate = postDate;
        this.type = type;
    }


    public Constants.MsgCardType getType() {
        return this.type;
    }

}
