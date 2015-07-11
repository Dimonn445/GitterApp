package com.example.dimonn445.gitterappv0001;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


public class MainActivity extends ActionBarActivity {

    private static String TOKEN="";
    Button auth,chat;

    public String APP_PREFERENCES_TOKEN = "AccessToken_prefs";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        chat = (Button) findViewById(R.id.chat_btn);
        auth = (Button)findViewById(R.id.auth);

        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public  void onClick(View view){
                switch (view.getId()){
                    case R.id.chat_btn:
                        ChatAfterLogin();
                        break;
                    case R.id.auth:
                        Login();
                        break;
                }
            }
        };
        chat.setOnClickListener(onClickListener);
        auth.setOnClickListener(onClickListener);

        prefs = getSharedPreferences("pref_data", Context.MODE_PRIVATE);

        if(isLoggedIn()) {
            Log.d("Token Access_prefs", prefs.getString(APP_PREFERENCES_TOKEN, ""));
            ChatAfterLogin();
        }
        else{
            Log.d("Before Login()","OK");
            Login();
        }
    }

    public String getAccessToken()
    {
        return prefs.getString(APP_PREFERENCES_TOKEN, "");
    }

    public boolean isLoggedIn()
    {
        return prefs.contains(APP_PREFERENCES_TOKEN);
    }

    private void ChatAfterLogin()
    {
        Log.d("ChatAfterLogin", "OK");
        Intent intent = new Intent(MainActivity.this, ChatActivity1.class);
        TOKEN = prefs.getString(APP_PREFERENCES_TOKEN, "");
        Log.d("TOKEN befor send", TOKEN);
        intent.putExtra("token",TOKEN);
        startActivity(intent);
        finish();
    }

    private void Login()
    {
        Log.d("Login","Login()");
        Intent intent = new Intent(MainActivity.this, OauthActivity.class);
        startActivity(intent);
    }
}
