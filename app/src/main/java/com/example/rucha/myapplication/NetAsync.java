package com.example.rucha.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Rucha on 2/12/2016.
 */
public class NetAsync extends AsyncTask {

    public NetAsync(String email, String pwd) {

        HashMap<String, String> hmap = new HashMap<String, String>();
        hmap.put("email",email);
        hmap.put("password", pwd);
        JSONObject js = new JSONObject(hmap);


    }


    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}