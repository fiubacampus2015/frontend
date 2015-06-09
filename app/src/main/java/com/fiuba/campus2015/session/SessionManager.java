package com.fiuba.campus2015.session;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.fiuba.campus2015.MainActivity;

/*
 * Clase que maneja las sessiones de la aplicacion
 */
public class SessionManager {

    SharedPreferences pref;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "isLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER_ID = "userid";
    public static final String KEY_USER_PHOTO = "photo";
    public static final String KEY_LOCATION_ACTIVE = "location_active";






    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email, String token ,String userId, String name, String surname,String photo){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_SURNAME, surname);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_PHOTO, photo);
        editor.putBoolean(KEY_LOCATION_ACTIVE, false);




        editor.commit();
    }


    public void checkLogin(){

        if(!this.isLoggedIn()){

            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

    }




    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_SURNAME, pref.getString(KEY_SURNAME, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_USER_PHOTO, pref.getString(KEY_USER_PHOTO, null));

        return user;
    }

    public String getToken(){

        return pref.getString(KEY_TOKEN, null);
    }

    public String getUserid(){

        return pref.getString(KEY_USER_ID, null);
    }

    public String getUserMail(){

        return pref.getString(KEY_EMAIL, null);
    }

    public String getUserName(){

        return pref.getString(KEY_NAME, null);
    }

    public String getUserSurname(){

        return pref.getString(KEY_SURNAME, null);
    }

    public String getUserPhoto(){

        return pref.getString(KEY_USER_PHOTO, "");
    }

    public void enableLocation(){
        editor.putBoolean(KEY_LOCATION_ACTIVE,true);
        editor.commit();
    }

    public void disableLocation(){
        editor.putBoolean(KEY_LOCATION_ACTIVE,false);
        editor.commit();
    }

    public boolean isLocationEnabled(){
        return pref.getBoolean(KEY_LOCATION_ACTIVE, false);
    }

    public void logoutUser(){

        editor.clear();
        editor.commit();
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }


    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}