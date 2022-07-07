package com.example.cardiogo;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedPreferenceName = "session";
    String sessionKey = "session_user";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String user){
        //save session of user

        editor.putString(sessionKey, user).commit();
    }

    public String getSession(){
        //return user whose session is saved
        return sharedPreferences.getString(sessionKey, "admin");
    }

    public void removeSession(){
        editor.putString(sessionKey, "admin").commit();
    }
}
