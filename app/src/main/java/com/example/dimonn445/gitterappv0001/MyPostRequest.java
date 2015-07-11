package com.example.dimonn445.gitterappv0001;

import android.app.DownloadManager;
import android.util.Log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/*import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dimonn445 on 01.07.15.
 */
public class MyPostRequest {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String request(String base_address, String address,String token, String s1) throws IOException {
        //base_address+=address;
        Headers headers = new Headers.Builder()
                .add("Content-Type: ","application/json")
                .add("Accept", "application/json")
                .add("Authorization", (new StringBuilder()).append("Bearer ").append(token).toString())
                .build();

        Request request = new Request.Builder()
                .url((new StringBuilder()).append(base_address).append(address).toString())
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, s1))
                .build();

        Log.d("gitterapi", (new StringBuilder()).append("Request: ").append(request.toString()).toString());
        Response response = client.newCall(request).execute();
        Log.d("gitterapi", (new StringBuilder()).append("Response: ").append(response.toString()).toString());
        if (!response.isSuccessful())
        {
            throw new IOException((new StringBuilder()).append("Unexpected code ").append(response).toString());
        } else
        {
            return response.body().string();
        }
        /*RequestBody formBody = new FormEncodingBuilder()
                .add("Accept:", "application/json")
                .add("Authorization:", (new StringBuilder()).append("Bearer ").append(token).toString())
                .build();
        Request request = new Request.Builder()
                .url(base_address)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();*/

        /*Request request = new Request.Builder()
                .url("https://api.gitter.im")
                .addHeader("Accept:", "application/json")
                .addHeader("Authorization:", (new StringBuilder()).append("Bearer ").append(token).toString())
                .build();

        Response responses = null;
        try {
            responses = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Response response = client.newCall(request).execute();

        return responses.body().string();*/
    }


    /*static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public MyPostRequest() {
    }
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    Map<String, String> mapn;
    DefaultHttpClient httpClient;
    HttpPost httpPost;

    public String request(String base_address, String address,String token) {
        // Making HTTP request
        try {
            // DefaultHttpClient
            base_address+=address;
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(base_address);


            params.add(new BasicNameValuePair("Accept:", "application/json"));
            params.add(new BasicNameValuePair("Authorization:", (new StringBuilder()).append("Bearer ").append(token).toString()));


            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();

            json = sb.toString();
            Log.e("JSONStr", json);
        } catch (Exception e) {
            e.getMessage();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return json;
    }*/
}
