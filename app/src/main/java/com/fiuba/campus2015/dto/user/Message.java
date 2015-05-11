package com.fiuba.campus2015.dto.user;

import com.fiuba.campus2015.Configuration;
import com.fiuba.campus2015.extras.Constants;

public class Message {
    public String _id;
    public String content;
    public String date;
    public User user;
    public Constants.MsgCardType typeOf;

    public Message(String content,Constants.MsgCardType type){
        this.content = content;
        this.typeOf = type;
    }

    public Message(String id){
        this._id = id;
    }


    public Constants.MsgCardType getType() {
        return this.typeOf;
    }

}
