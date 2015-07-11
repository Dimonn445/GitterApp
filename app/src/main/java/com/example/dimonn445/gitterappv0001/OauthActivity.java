package com.example.dimonn445.gitterappv0001;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class OauthActivity extends ActionBarActivity {

    private static String CLIENT_ID = "55462e4ecdddbc9038c907d8671d24ef9193278c";
    //Use your own client id
    private static String CLIENT_SECRET ="165a9150ebbc3314d2cd00fca4ed1cb65a58e008";
    //Use your own client secret
    private static String REDIRECT_URI="http://localhost:7000/login/callback";
    private static String GRANT_TYPE="authorization_code";
    private static String BASE_URL = "https://gitter.im";
    private static String TOKEN_URL ="/login/oauth/token";
    private static String OAUTH_URL ="/login/oauth/authorize";
    private static String TOKEN="";
    public String APP_PREFERENCES_TOKEN = "AccessToken_prefs";
    //public Login_prefs logindata;

    private WebView webView;
    private WebViewClient webViewClient;
    private SharedPreferences prefs;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        setTitle("Authorization");
        prefs = getSharedPreferences("pref_data", Context.MODE_PRIVATE);
        Login();
    }

    public OauthActivity(){
        webViewClient = new WebViewClient() {

            boolean authComplete = false;
            Intent resultIntent = new Intent();

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            String authCode;

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("?code=") && authComplete != true) {
                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    Log.i("", "CODE : " + authCode);
                    authComplete = true;
                    resultIntent.putExtra("code", authCode);
                    OauthActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                    setResult(Activity.RESULT_CANCELED, resultIntent);

                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("Code", authCode);
                    edit.commit();
                    new TokenGet().execute();
                    Log.d("Authorization Code: ", authCode);
                }
                else
                    if (url.contains("error=access_denied")) {
                        Log.i("", "ACCESS_DENIED_HERE");
                        resultIntent.putExtra("code", authCode);
                        authComplete = true;
                        setResult(Activity.RESULT_CANCELED, resultIntent);
                        Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
                        }
            }
        };
    }


    private void Login()
    {
        Log.d("Login", "Login()");
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        webView = (WebView) findViewById(R.id.webv);
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(BASE_URL + OAUTH_URL + "?client_id=" + CLIENT_ID + "&response_type=code" + "&redirect_uri=" + REDIRECT_URI);
    }

    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String Code;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //webView.loadUrl("https://gitter.im/mobile/home");
            pDialog = new ProgressDialog(OauthActivity.this);
            pDialog.setMessage("Contacting ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Code = pref.getString("Code", "");
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            GetAccessToken jParser = new GetAccessToken();
            JSONObject json = jParser.gettoken(BASE_URL,TOKEN_URL,Code,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI,GRANT_TYPE);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            if (json != null)
                try {
                    TOKEN = json.getString("access_token");
                    Log.d("Token Access", TOKEN);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(APP_PREFERENCES_TOKEN, TOKEN);
                    editor.apply();

                    Intent intent = new Intent(OauthActivity.this, MainActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            else
            {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }
    }
}
