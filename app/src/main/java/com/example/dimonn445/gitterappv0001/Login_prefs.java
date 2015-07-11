package com.example.dimonn445.gitterappv0001;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dimonn445 on 29.06.15.
 */
public class Login_prefs {

    private SharedPreferences prefs;

    public Login_prefs(Context context)
    {
        prefs = context.getSharedPreferences("gitter_login_prefs", Context.MODE_PRIVATE);
    }

    public String getAccessToken()
    {
        return prefs.getString("access_token", null);
    }
    public String getUserId()
    {
        return prefs.getString("user_id", null);
    }

    public boolean isLoggedIn()
    {
        return /*prefs.contains("user_id") &&*/ prefs.contains("access_token");
    }

    public void setLoginData(/*String s, */String s1)
    {
        android.content.SharedPreferences.Editor editor = prefs.edit();
        //editor.putString("user_id", s);
        editor.putString("access_token", s1);
        editor.commit();
    }

}


