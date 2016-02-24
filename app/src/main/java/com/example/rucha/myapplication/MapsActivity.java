package com.example.rucha.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;

import static android.text.format.DateFormat.format;

@SuppressWarnings("deprecation")
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private static GoogleMap mMap;
    private static final int CAPImage_REQUEST_CODE = 100;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private static final long WILDLY_OUT = 15;
    private static final long TOO_OLD = 30000;
    private static final long NO_SAMPLES = 5;
    double lastLocationTime;

    ImageView resultphoto;
    private Uri fileUri;
    private String chosenProvider;
    Location location;
    public boolean k = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private LocationLog mLocationLog;
    private final int TIME_INTERVAL = 200;
    public static int result;
    public static String data;
    String locationProvider = LocationManager.NETWORK_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.e("List: ", locationManager.getProviders(true).toString());
        Log.e("GPS Status: ", locationManager.getGpsStatus(null).toString());
        Log.e("Location: ", String.valueOf(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)));

        LocationListener listener =  new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.e("New location:", "lat:" + location.getLatitude() + " long:" + location.getLongitude());

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                LatLng point1 = calcLatLng(location,longitude,latitude);
                double alat= point1.latitude;
                double alng= point1.longitude;
               Log.e("alat: " +alat,"alng"+alng);
                    String str = getCurrentTimeStamp();

                        JsonGPS sndGPS = new JsonGPS();
                        int res = sndGPS.sendJson(latitude, longitude, str);
                        Log.e("Sent or not sent", " " + res);



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);


    }




    LatLng calcLatLng(Location location, double oldLongitude ,double oldLatitude) {
        double newLongitude = oldLongitude;
        double newLatitude = oldLatitude;

        if (location.getAccuracy() < WILDLY_OUT) {
            newLongitude = (NO_SAMPLES * oldLongitude + location
                    .getLongitude()) / (NO_SAMPLES + 1);
            newLatitude =  (NO_SAMPLES * oldLatitude + location
                    .getLatitude()) / (NO_SAMPLES + 1);
            lastLocationTime = System.currentTimeMillis();
        }

        if (lastLocationTime > TOO_OLD) {
            newLongitude = location.getLongitude();
            newLatitude = location.getLatitude();
        }


        LatLng point = new LatLng(newLatitude,newLongitude);
        return point;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.ctext)
        {
            Log.e("button","clicked");
            Intent i = new Intent(this,Updates.class );
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {


            mMap = googleMap;

        // Add a marker in Sydney and move the camera

//            LatLng sydney = new LatLng(47.2483960, -122.4382950);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Itrajectory ").snippet("Itrajectory is the coolest"));
            Plotpoints();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, listener);
            mMap.setMyLocationEnabled(true);
//           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));


    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rucha.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rucha.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }



    public static String getCurrentTimeStamp(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        String currentTimeStamp= sdf.format(new Date());
        StringBuilder timestamp = new StringBuilder(currentTimeStamp);
        timestamp.insert(currentTimeStamp.length() - 2, ':');

        return timestamp.toString();
    }

    public static void Plotpoints() {


        Json Js = new Json();
        int i = Js.getId();
        JSONObject Jsn = new JSONObject();
        Date edt = null;
        StringBuilder testB = new StringBuilder();
        String http = "http://54.191.90.109:3000/api/geodata";
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

            edt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

            String one = "2016-02-17 13:00:00 -08:00";
            String two = "2016-02-18 00:00:00 -08:00";
            String str2 = getCurrentTimeStamp();
            Date sdt = new Date();

            Jsn.put("usrid", i);
            Jsn.put("startd", one);
            Jsn.put("endd", two);

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(Jsn.toString());
            out.close();

            int HttpResult = urlConnection.getResponseCode();
            result = HttpResult;
            Log.e("testmaps gps result", "test" + HttpResult);
            Log.e("test2mapsgpsdata", "" + HttpResult);
            // if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                testB.append(line + "\n");
            }
            Log.e("Geodata from Server", testB.toString());
            data = testB.toString();
            br.close();
            JSONArray jdat = new JSONArray(testB.toString());
            Log.e("Jdat Length : ", " " + jdat.length());

            ArrayList<LatLng> arrayList = new ArrayList<>();

            for (int j = 0; j < jdat.length(); j++) {

                int k = 0;
                JSONObject location = jdat.getJSONObject(j).getJSONObject("location").getJSONArray("points").getJSONObject(0);

                double lat = location.getDouble("x");
                double lng = location.getDouble("y");
//                Polyline polyline = mMap.addPolyline(new PolylineOptions()
//                        .add(new LatLng(lat,lng), ).width(5)
//                        .color(Color.RED));
                //Log.e("lat " + lat, "lng " + lng);
                LatLng point = new LatLng(lat, lng);
                //Log.e("POINT", "" + point);

                arrayList.add(point);
                //polylineOptions.add(point, new LatLng(0,0));
//                mMap.addMarker(new MarkerOptions().position(point));

            }

            mMap.addPolyline(new PolylineOptions()
                    .addAll(arrayList)
                    .width(5)
                    .color(Color.RED));

            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

//            LatLng sydney = new LatLng(47.2483960, -122.4382950);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Itrajectory ").snippet("Itrajectory is the coolest"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(0), 15));

        } catch (Exception E) {

        }
    }




}