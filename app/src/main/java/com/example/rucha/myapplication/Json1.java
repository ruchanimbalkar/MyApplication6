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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rucha on 2/12/2016.
 */
//testin

public class Json1 extends AsyncTask{
    private static final String LOGIN_URL = "http://54.191.90.109:3000/api/login";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    //Login data
    public String email;
    public String pwd;
    //Signup data
    public String Firstname;
    public String Lastname;
    public String emailid;
    public String password;
    public String sex;
    public String username;
    public String  birthdate;

    public int id,result;

    public void Json(){}
    protected int sendJson(final String e, final String p) {
        email=e;
        pwd=p;

        result = doInBackground();

        return  result;
    }
    protected int sendJson(final String fname, final String lname, final String email, final String dob, final String gender, final String pswd, final String uname){

        Firstname =fname;
        Lastname =lname;
        emailid =email;
        password =pswd;
        sex = gender;
        username= uname;
        birthdate = dob;

        result= doInBackground2();
        return result;
    }


    //Register
    private int doInBackground2() {

        String data = "";

        int tap;
        StringBuilder testB = new StringBuilder();

        String http ="http://54.191.90.109:3000/api/register";
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
            Js.put("firstname",Firstname);
            Js.put("lastname",Lastname);
            Js.put("email",emailid);
            Js.put("password",password);
            Js.put("gender",sex );
            Js.put("username",username);
            Js.put("dob",birthdate);


            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(Js.toString());
            out.close();

            int HttpResult = urlConnection.getResponseCode();
            result= HttpResult;
            Log.d("test result", "test" + HttpResult);
            Log.e("test2", ""+ HttpResult);
            // if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                testB.append(line + "\n");
            }
            Log.e("return", testB.toString());
            data=line;
            br.close();

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

        return result;
    }

    public int getId() {
        return id;
    }

    protected int doInBackground() {

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
            result=HttpResult;
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
                data=testB.toString();
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
        return result;
    }



    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}

