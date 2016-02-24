package com.example.rucha.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.AddressConstants;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

//

public class Updates extends AppCompatActivity implements View.OnClickListener {
    ImageView imageToUpload;
    Button bSendAll;
    EditText textView;
    Button bImgUpload;
    Button camera;
    TextView coordinates;
    JSONObject geoinfo = new JSONObject();
    private static final int RESULT_LOAD_IMAGE = 1;
    private String mImageFileLocation = "";
    public static final String SERVER_ADDRESS = "http://54.191.90.109:3000";
    public static final String TAG = "STATE";
    public static final int REQUEST_CAPTURE = 1;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updates);

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        bSendAll = (Button) findViewById(R.id.bSendAll);
        textView = (EditText) findViewById(R.id.textInfo);
        bImgUpload = (Button) findViewById(R.id.bUploadImg);
        coordinates = (TextView) findViewById(R.id.displayCoordinates);
        camera = (Button) findViewById(R.id.Bcapture);
        getGeoInfo(getLastKnownLocation());

        Log.e(TAG, geoinfo.toString());
        imageToUpload.setOnClickListener(this);
        bSendAll.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // method to get the current geo coordinates
    // and set up the text to coordinate display
    private void getGeoInfo(Location loc) {

//        MyLocationListener locationListener = new MyLocationListener();
        if (loc == null) Log.e(TAG, "INVALID LOCATION");
        Toast.makeText(
                getBaseContext(),
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        Log.e(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.e(TAG, latitude);

        /*------- To get city name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + ", " + latitude + " City: " + cityName;
        Log.e(TAG, s);
        coordinates.setText(s);
        try {
            geoinfo.put("lat", latitude);
            geoinfo.put("long", longitude);
            geoinfo.put("timestamp", getCurrentTimeStamp());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        Location l = null;
        for (String provider : providers) {
            try {
                l = mLocationManager.getLastKnownLocation(provider);
            } catch (SecurityException e) {
                Log.e(TAG, "Security Exception");
                e.printStackTrace();
            }
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private Location getLocationByProvider(String provider) {
        Location location = null;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(provider)) {
                Log.e(TAG, "Try to connect");
                location = locationManager.getLastKnownLocation(provider);
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Cannot access Provider " + provider);
        } catch (SecurityException e) {
            Log.e(TAG, "security exception");
        }
        return location;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent galleryIntent;
        switch (v.getId()) {
            case R.id.imageToUpload:
                galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                imageToUpload.setVisibility(View.VISIBLE);
                break;
            case R.id.Bcapture:
                imageToUpload = (ImageView) findViewById(R.id.imageToUpload);

                break;
            case R.id.bSendAll:
                Bitmap image;
                if (imageToUpload.getDrawable() != null) {
                    image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                } else {
                    image = null;
                }
                String t = textView.getText().toString();
                new UploadAll(image, t).execute();
                bSendAll.setEnabled(false);
                break;
            case R.id.bUploadImg:
                galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.bCancel:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
        else if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extra = data.getExtras();
//            Bitmap pic = (Bitmap) extra.get("data1");
//            imageToUpload.setImageBitmap(pic);

            Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            imageToUpload.setImageBitmap(photoCapturedBitmap);


        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Updates Page", // TODO: Define a title for the content shown.
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
                "Updates Page", // TODO: Define a title for the content shown.
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

    private class UploadAll extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        String text;

        // data input json format:
        String ImageText(String text, byte[] imageBytes) {
            try {
                String imgStr;
                if (imageBytes != null) {
                    imgStr = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                } else imgStr = null;
                JSONObject jsonObject = new JSONObject();
                Json js = new Json();
                jsonObject.put("usrid",js.getId());
                jsonObject.put("geoinfo", geoinfo);
                jsonObject.put("text", text);
                jsonObject.put("image", imgStr);
                return jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        public UploadAll(Bitmap image, String text) {
            this.image = image;
            this.text = text;
        }

        @Override
        protected Void doInBackground(Void... params) {
            byte[] imageBytes;
            if (image != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                imageBytes = byteArrayOutputStream.toByteArray();

            } else {
                imageBytes = null;
            }
            String json = ImageText(text, imageBytes);
            String link = SERVER_ADDRESS + "/api/app/upload";
            try {
                RequestBody requestBody = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(link)
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, request + "FAIL");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Log.e(" "+response, "Response ");
                        if (!response.isSuccessful()) {
                            Log.e(TAG, "FAIL TO GET RESPONSE");
//                            Toast.makeText(getApplicationContext(), "FAIL TO GET RESPONSE", Toast.LENGTH_SHORT).show();
//                            bSendAll.setEnabled(true);
                        } else {
                            Log.e(TAG, response.body().string());
//                            Intent intent = new Intent(Updates.class, MapsActivity.class);
//                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            //bImgUpload.setEnabled(false);
        }


    }

    public String getCurrentTimeStamp() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        String currentTimeStamp = sdf.format(new Date());
        StringBuilder timestamp = new StringBuilder(currentTimeStamp);
        timestamp.insert(currentTimeStamp.length() - 2, ':');

        return timestamp.toString();
    }


    public void launchCamera(View v) {

        //Launch Camera
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

//        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
//        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        File photoFile = null;
        try{
            photoFile = createImageFile();

        } catch (IOException e){
            e.printStackTrace();
        }
        i.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile) ); // set the image file name

        startActivityForResult(i,REQUEST_CAPTURE);
        Toast.makeText(this, "Camera button pressed", Toast.LENGTH_SHORT).show();

//       onActivityResult(REQUEST_CAPTURE, RESULT_OK, i);

    }



    File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;

    }

//    private static Uri getOutputMediaFileUri(int type){
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//    private static File getOutputMediaFile(int type){
//        // To be safe, you should check that the SDCard is mounted
//        // using Environment.getExternalStorageState() before doing this.
//
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "app");
//        // This location works best if you want the created images to be shared
//        // between applications and persist after your app has been uninstalled.
//
//        // Create the storage directory if it does not exist
//        if (! mediaStorageDir.exists()){
//            if (! mediaStorageDir.mkdirs()){
//                Log.d("MyCameraApp", "failed to create directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE){
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "IMG_"+ timeStamp + ".jpg");
//        }
//        else {
//            return null;
//        }
//
//        return mediaFile;
//    }
//

}
