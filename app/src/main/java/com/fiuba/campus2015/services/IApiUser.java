package com.fiuba.campus2015.services;

import com.fiuba.campus2015.dto.user.Authenticate;
import com.fiuba.campus2015.dto.user.User;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by apetalas on 3/4/15.
 */
public interface IApiUser {

    @POST("/api/users")
    public retrofit.client.Response register(
            @Body User data
    );

    @POST("/api/users/authenticate")
    public Response authenticate(
            @Body Authenticate data
    );

    @GET("/api/{token}/users/{userId}")
    public User get(
            @Path("token") String token,
            @Path("userId") String userId
    );

    @PUT("/api/{token}/users")
    public Response update(
            @Path("token") String token,
            @Body User data
    );

    @PUT("/api/{token}/users/{userId}")
    public retrofit.client.Response  put(
            @Path("token") String token,
            @Path("userId") String userId,
            @Body User data
    );
}
