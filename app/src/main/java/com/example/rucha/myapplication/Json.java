package com.example.rucha.myapplication;

import android.os.AsyncTask;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rucha on 2/12/2016.
 */
//testin

public class Json extends AsyncTask{
    private static final String LOGIN_URL = "http://54.191.90.109:3000/api/login";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public String email;
    public String pwd;
    public int id;
    String result;
    public void Json(){}
    protected String sendJson(final String e, final String p) {
        email=e;
        pwd=p;

        result = doInBackground();

        return  result;
    }

    protected String doInBackground() {

        result = "fail";
        HashMap<String, String> hmap = new HashMap<String, String>();
        String data = "";

        int tap;
        StringBuilder testB = new StringBuilder();

        String http ="http://54.191.90.109:3000/api/login";

        HttpURLConnection urlConnection = null;
        try {
            URL url2 = new URL(http);
            urlConnection = (HttpURLConnection) url2.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
           urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            //Create JSONObject here
            JSONObject Js = new JSONObject();
            Js.put("email", email);
            Js.put("password", pwd);

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(Js.toString());
            out.close();

            int HttpResult = urlConnection.getResponseCode();

            Log.d("test result", "test" + HttpResult);
            Log.e("test2", ""+ HttpResult);
           // if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    testB.append(line + "\n");
               }
                Log.e("return", testB.toString());
                data=line;
                br.close();

               //get userid from returned string
                JSONObject jdat = new JSONObject(testB.toString());
                 id = jdat.getInt("userid");
                Log.d("id"," " +id);





            //} else
        {
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

//        try{
//            URL url =new URL ("http://54.191.90.109:3000/api/login");
//            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setRequestMethod("POST");
//            httpURLConnection.setConnectTimeout(10000);
//            httpURLConnection.setReadTimeout(10000);
//            httpURLConnection.setRequestProperty("Content-Type","application/json");
//            httpURLConnection.setRequestProperty("Accept", "application/json");
//            httpURLConnection.connect();
//
//            OutputStreamWriter os = new OutputStreamWriter(httpURLConnection.getOutputStream());
//            os.write(Js.toString());
//            os.flush();
//            os.close();
//            InputStream is = httpURLConnection.getInputStream();
//            while ((tap=is.read())!=-1)
//            {
//                data+=(char)tap;
//            }
//            is.close();
//            httpURLConnection.disconnect();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(" " + data);
//        result=data;
        return data;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}

