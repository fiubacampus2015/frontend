package com.fiuba.campus2015.dto.user;

import com.fiuba.campus2015.Configuration;
import com.fiuba.campus2015.extras.Constants;

public class Message {
    public String _id;
    public String content;
    public String postDate;
    public User userFrom;
    public Constants.MsgCardType type;

    public Message(String content, User userFrom, String postDate, Constants.MsgCardType type){
        this.userFrom = userFrom;
        this.content = content;
        this.postDate = postDate;
        this.type = type;
    }


    public Constants.MsgCardType getType() {
        return this.type;
    }

}
