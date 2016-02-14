package com.example.rucha.myapplication;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Samir on 2/14/2016.
 */
public class jsonGPS {
    private static final String LOGIN_URL = "http://54.191.90.109:3000/api/login";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public int latitude;
    public int longitude;
    public String timeStamp;

    public int id;
    public int result;

    public void JsonGPS(){}
    protected int sendJson(final int lat, final int lng , final String tStamp) {
        latitude  = lat;
        longitude = lng;
        timeStamp = tStamp;


        result = doInBackground();

        return  result;
    }

    protected int doInBackground() {
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
            JSONObject location = new JSONObject();
            location.put("Lat", latitude);
            location.put("Long", longitude);
            location.put("timestamp", timeStamp);

            Json1 userId =new Json1();
            id = userId.getId();

            JSONObject idJson =new JSONObject();
            idJson.put("usrid", id);

            JSONObject mainToSend =new JSONObject();
            mainToSend.put("userid",userId);
            mainToSend.put("location",location);

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(mainToSend.toString());
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
}
