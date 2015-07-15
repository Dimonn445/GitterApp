package com.example.dimonn445.gitterappv0001;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;


public class ChatActivity1 extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private static String[] mRoomsTitles;


    private static String BASE_URL = "https://gitter.im";
    private static String BASE_API_URL = "https://api.gitter.im";
    private static String TOKEN="";
    private WebView webChat;
    //private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_chat_activity1);
        TOKEN = getIntent().getStringExtra("token");
        setTitle("Rooms");

        /*

        String[] parts={""};
        try {
            parts = getUserRooms().split(" ");
            Log.d("USERROOM",getUserRooms());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRoomsTitles = parts;

        */

        reloadRooms();
        openWebView();
    }

    private void reloadRooms(){

        //Try AsyncTask
        //new ReloadRooms().execute();

        String[] parts={""};
        try {
            parts = getUserRooms().split(" ");
            Log.d("USERROOM",getUserRooms());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mRoomsTitles = parts;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mRoomsTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                   //host Activity
                mDrawerLayout,          //DrawerLayout object
                R.drawable.ic_drawer,   //nav drawer image to replace 'Up' caret
                R.string.drawer_open,   //"open drawer" description for accessibility
                R.string.drawer_close   //"close drawer" description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void openWebView(){
        webChat = (WebView)findViewById(R.id.web_view_chat);
        webChat.clearCache(true);
        webChat.getSettings().setJavaScriptEnabled(true);
        webChat.getSettings().setDomStorageEnabled(true);
        webChat.getSettings().setBuiltInZoomControls(true);
        webChat.getSettings().setDisplayZoomControls(false);
        webChat.loadUrl(BASE_URL + "/mobile/home");

        webChat.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            public void onPageFinished(WebView webview, String s) {
                webChat.loadUrl((new StringBuilder()).append("javascript:(function() { window.bearerToken = '").append(TOKEN).append("'; } )()").toString());
            }

            public boolean shouldOverrideUrlLoading(WebView webview, String s) {
                if (s.startsWith((new StringBuilder()).append(BASE_URL).append("/mobile/home").toString()))
                    return true;
                if (s.startsWith(BASE_URL))
                    return false;
                return true;
            }
        });
    }
    private void goToUserhome()
    {
        //setTitle("Home");
        String s = webChat.getUrl();
        String s1 = (new StringBuilder()).append(BASE_URL).append("/mobile/home").toString();
        if (!s1.equals(s))
        {
            reloadRooms();
            webChat.loadUrl(s1);
        }
    }

    private void goRoom(String roomName) throws IOException {

        Log.d("goRoom",": activated");
        String s = webChat.getUrl();
        String url=BASE_URL+"/" +roomName;
        if (!url.equals(s)) {
            reloadRooms();
            webChat.loadUrl(url);
        }
    }

    public String getUserRooms() throws IOException {
        String roomsUri="";
        JSONArray jsonarray;
        int i=0;

        ApiClient client = new ApiClient(BASE_API_URL,TOKEN);
        try {
            jsonarray = client.getJsonArray("/v1/rooms");
            if(jsonarray!=null)
            {
                if(jsonarray.length()!=1){
                    while (i<jsonarray.length()){
                        roomsUri += jsonarray.getJSONObject(i).getString("uri")+" ";
                        Log.d("roomsUri: ", roomsUri);
                        i++;
                    }
                }
                else
                    roomsUri = client.getJsonArray("/v1/rooms").getJSONObject(0).getString("uri");
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("MyRequest answer: ", roomsUri);
        return roomsUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_activity1, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.home_gitter:
                Log.d("goToUserhome()",": pressed");
                goToUserhome();
                //Toast.makeText(ChatActivity1.this,"go home pressed",Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                selectItem(position);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectItem(int position) throws IOException {
        // update the main content by replacing fragments

        /*Fragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putInt(RoomFragment.ARG_ROOM_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        //setTitle(mRoomsTitles[position]);
        goRoom(mRoomsTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

   /* @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }*/

    /*
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /*
     * Fragment that appears in the "content_frame", shows a room
     */
    /*public static class RoomFragment extends Fragment {
        public static final String ARG_ROOM_NUMBER = "room_number";

        public RoomFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_room, container, false);
            int i = getArguments().getInt(ARG_ROOM_NUMBER);
            String room = mRoomsTitles[i];

            *//*int imageId = getResources().getIdentifier(room.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);*//*
            getActivity().setTitle(room);
            return rootView;
        }
    }*/

    /*private class ReloadRooms extends AsyncTask<Void,Void,String[]> {
        //private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(Void... params) {
            String[] parts={""};
            try {
                parts = getUserRooms().split(" ");
                Log.d("USERROOM",getUserRooms());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return parts;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }*/
}
