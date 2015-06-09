package com.fiuba.campus2015.services;

/**
 * Created by apetalas on 3/4/15.
 */
public class Response {

    public int status;
    public String reason;
    public String token;
    public String id;
    public String name;
    public String photo;
    public String surname;
    public boolean suspend;
    public Boolean confirmed;

    public Response()
    {

    }
    public Response(int status, String reason)
    {
        this.status = status;
        this.reason = reason;

    }

}
