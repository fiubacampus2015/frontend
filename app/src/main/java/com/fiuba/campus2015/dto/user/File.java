package com.fiuba.campus2015.dto.user;


import com.fiuba.campus2015.extras.Constants;

public class File {
    public String _id;
    public String description;
    public String originalName;
    public String content;
    public String path;
    public String user;
    public Constants.MsgCardType typeOf;

    public File()
    {
    }

    public File(String _id)
    {
        this._id = _id;
    }
}
