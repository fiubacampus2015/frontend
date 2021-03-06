package com.fiuba.campus2015.services;

import com.fiuba.campus2015.adapter.ContactItem;
import com.fiuba.campus2015.dto.user.Authenticate;
import com.fiuba.campus2015.dto.user.File;
import com.fiuba.campus2015.dto.user.Files;
import com.fiuba.campus2015.dto.user.Forum;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.MemberShip;
import com.fiuba.campus2015.dto.user.Members;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.dto.user.Position;
import com.fiuba.campus2015.dto.user.Subscriptions;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Constants;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

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

    @GET("/api/{token}/users")
    public List<User> getAll(
            @Path("token") String token,
            @Query("name") String name,
            @Query("contacts") String contacts,
            @Query("confirmed") Boolean confirmed
            );

    @GET("/api/{token}/users")
    public List<User> getFriend(
            @Path("token") String token,
            @Query("name") String name
    );

    @GET("/api/{token}/users")
    public List<User> getPeople(
            @Path("token") String token,
            @Query("name") String name,
            @Query("username") String username,
            @Query("education.careers.title") String career,
            @Query("education.careers.branch") String branch,
            @Query("personal.nacionality") String nacionality
    );


    @GET("/api/{token}/users/{user}/friends")
    public List<User> getFriends(
            @Path("token") String token,
            @Path("user") String user
    );

    @PUT("/api/{token}/users/{userId}/{friendId}")
    public Response invite(
            @Path("token") String token,
            @Path("userId") String userId,
            @Path("friendId") String friendId
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

    @GET("/api/{token}/users/{userId}/friends/pending")
    public List<User> getInvitationsPending(
            @Path("token") String token,
            @Path("userId") String user
    );

    @POST("/api/{token}/users/{userId}/{friendId}/delete")
    public  retrofit.client.Response deleteFriend(
            @Path("token") String token,
            @Path("userId") String userId,
            @Path("friendId") String friendId
    );

    @PUT("/api/{token}/users/{userId}/{friendId}/confirm")
    public retrofit.client.Response confirmInvitation(
            @Path("token") String token,
            @Path("userId") String userId,
            @Path("friendId") String friendId

    );

    @PUT("/api/{token}/users/{userId}/{friendId}/reject")
    public retrofit.client.Response rejectInvitation(
            @Path("token") String token,
            @Path("userId") String userId,
            @Path("friendId") String friendId
    );

    @POST("/api/{token}/users/{friendId}/wall")
    public Message postMsgToWall(
            @Path("token") String token,
            @Path("friendId") String friendId,
            @Body Message msg
    );

    @GET("/api/{token}/users/{user}/wall")
    public List<Message> getUserWallMessages(
            @Path("token") String token,
            @Path("user") String user
    );

    @POST("/api/{token}/users/{user}/walldelete")
    public retrofit.client.Response deleteMsg(
            @Path("token") String token,
            @Path("user") String user,
            @Body Message message
    );

    @POST("/api/{token}/users/{user}/position")
    public retrofit.client.Response sendLocation(
            @Path("token") String token,
            @Path("user") String user,
            @Body Position position
    );

    //GRUPOS
    @POST("/api/{token}/groups")
    public retrofit.client.Response createGroup(
            @Path("token") String token,
            @Body Group group
    );

    //GRUPOS
    @PUT("/api/{token}/groups/{groupId}")
    public retrofit.client.Response editGroup(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Body Group group
    );


    @POST("/api/{token}/groups/delete")
    public retrofit.client.Response deleteGroup(
            @Path("token") String token,
            @Body Group group
    );


    @GET("/api/{token}/groups")
    public List<Group> getGroup(
            @Path("token") String token,
            @Query("name") String name
    );

    @POST("/api/{token}/groups/{groupId}/subscribe")
    public MemberShip subscribeGroup(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Body User user
    );

    @POST("/api/{token}/groups/{groupId}/unsubscribe")
    public retrofit.client.Response unSubscribeGroup(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Body User user
    );


    @GET("/api/{token}/groups/{groupId}/members")
    public Members getMembersGroup(
            @Path("token") String token,
            @Path("groupId") String groupId
    );

    @POST("/api/{token}/groups/{groupId}/forums")
    public retrofit.client.Response  createForum(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Body Forum forum
    );


    @GET("/api/{token}/groups/{groupId}/forums")
    public List<Forum> getForum(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Query("title") String title
    );

    @POST("/api/{token}/groups/{groupId}/forums/delete")
    public retrofit.client.Response  deleteForum(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Body Forum forum
    );

    @GET("/api/{token}/groups/{groupId}/subscriptions")
    public Subscriptions getSuscriptors(
            @Path("token") String token,
            @Path("groupId") String groupId
    );

    @PUT("/api/{token}/groups/{groupId}/subscribe/{susId}/resolve")
    public MemberShip subscribeResolve(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Path("susId") String susId,
            @Body MemberShip member
    );


    @GET("/api/{token}/groups/{groupId}/forums/{forumId}/messages")
    public List<Message> getForumMessages(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Path("forumId") String forumId
            );


    @POST("/api/{token}/groups/{groupId}/forums/{forumId}/messages")
    public retrofit.client.Response postMsgToForum(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Path("forumId") String forumId,
            @Body Message message
    );

    @Multipart
    @POST("/api/{token}/groups/{groupId}/forums/{forumId}/messages")
    public retrofit.client.Response postFileToForum(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Path("forumId") String forumId,
            @Part("file") TypedFile file,
            @Part("content") String content,
            @Part("typeOf") String type
            );

    @POST("/api/{token}/groups/{groupId}/forums/{forumId}/messages/delete")
    public retrofit.client.Response deleteForumMessage(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Path("forumId") String forumId,
            @Body Message message
    );

    @GET("/api/{token}/groups/{groupId}/files")
    public Files getGroupFiles(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Query("originalName") String originalName,
            @Query("typeOf") String typeOf

    );


    @Multipart
    @POST("/api/{token}/groups/{groupId}/messages")
    public Message postFiles(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Part("file") TypedFile file,
            @Part("content") String description,
            @Part("typeOf") String type

    );


    @POST("/api/{token}/groups/{groupId}/messages/delete")
    public retrofit.client.Response removeFile(
            @Path("token") String token,
            @Path("groupId") String groupId,
            @Body File file
    );


    @GET("/api/{token}/users/{userId}/groups")
    public List<Group> getMyGroups(
            @Path("token") String token,
            @Path("userId") String userId
    );

}
