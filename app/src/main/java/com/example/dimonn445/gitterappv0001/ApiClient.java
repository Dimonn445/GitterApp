package com.example.dimonn445.gitterappv0001;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dimonn445 on 06.07.15.
 */
public class ApiClient {
    String host,TOKEN;

    public ApiClient(String base_address, String token){
        host=base_address;
        TOKEN=token;
    }
    //OkHttpClient client = new OkHttpClient();
    String get(String address) throws IOException {
        //base_address+=address;
        /*Log.d("TOKEN before GET",TOKEN);
        Log.d("host before GET",host);
        Log.d("address before GET",address);
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json")
                .add("Authorization", (new StringBuilder()).append("Bearer ").append(TOKEN).toString())
                .build();

        Request request = new Request.Builder()
                .url((new StringBuilder()).append(host).append(address).toString())
                .headers(headers)
                .build();

        Log.d("gitterapi", (new StringBuilder()).append("Request: ").append(request.toString()).toString());

        //Call call = client.newCall(request);
        //Response response = call.execute();
        Response response = client.newCall(request).execute();

        Log.d("gitterapi", (new StringBuilder()).append("Response: ").append(response.toString()).toString());

        if (!response.isSuccessful()) {
            Log.d("ResponseCode", " " + response.code());
            throw new IOException((new StringBuilder()).append("Unexpected code ").append(response).toString());
        } else {
            Log.i("Response code", response.code() + " ");
            String results = response.body().toString();
            Log.i("OkHTTP Results: ", results);
            return results;
        }*/
        DefaultHttpClient httpClient;
        HttpGet httpGet;
        BasicHttpResponse httpResponse = null;

        Log.d("TOKEN before GET",TOKEN);
        Log.d("host before GET",host);
        Log.d("address before GET",address);
        try {
            address = host+address;
            Log.d("address after try",address);
            httpClient = new DefaultHttpClient();
            httpGet = new HttpGet(address);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Authorization", (new StringBuilder()).append("Bearer ").append(TOKEN).toString());
            httpResponse = (BasicHttpResponse) httpClient.execute(httpGet);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert httpResponse != null;
        BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        String line = "",buff="";
        while ((line = rd.readLine()) != null) {
            buff+=line;
        }
        Log.i("HTTP Results: ", buff);
        return buff;
    }
    public JSONArray getJsonArray(String s) throws IOException, JSONException {
        Log.d("return JSONArray()","OK");
        return new JSONArray(get(s));
    }
}
