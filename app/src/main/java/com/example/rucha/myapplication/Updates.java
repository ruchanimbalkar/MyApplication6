package com.example.rucha.myapplication;

/**
 * Created by Rucha on 2/18/2016.
 */
        import android.content.Context;
        import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
        import android.location.Location;
        import android.location.LocationManager;
        import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

        import com.google.android.gms.appindexing.AppIndex;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

        import org.json.JSONException;
        import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//

public class Updates extends AppCompatActivity implements View.OnClickListener {
    ImageView imageToUpload;
    Button bSendAll;
    EditText textView;
    Button bImgUpload;
    private GoogleApiClient client;
    private LocationManager locationManager;
    Location location;
    JSONObject geoinfo= new JSONObject ();
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String SERVER_ADDRESS = "http://10.0.2.2:3000";
    private static final String TAG = "STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        //Get lat long timestamp at that instant
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double latitude =location.getLatitude();
        double longitude =location.getLongitude();
        MapsActivity mps =new MapsActivity();
        String timeStamp = mps.getCurrentTimeStamp();
        try {
            geoinfo.put("lat", latitude);
            geoinfo.put("long", longitude);
            geoinfo.put("timestamp", timeStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        bSendAll = (Button) findViewById(R.id.bSendAll);
        textView = (EditText) findViewById(R.id.textInfo);
        bImgUpload = (Button) findViewById(R.id.bUploadImg);
        imageToUpload.setOnClickListener(this);
        bSendAll.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (android.os.Build.VERSION.SDK_INT > 9) {
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
        switch(v.getId()) {
            case R.id.imageToUpload:
                galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.bSendAll:
                Bitmap image;
                if(imageToUpload!=null) {
                    image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                }
                else {
                    image = null;
                }
                String t = textView.getText().toString();
                new UploadAll(image,t).execute();
                break;
            case R.id.bUploadImg:
                galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
    }

    private class UploadAll extends AsyncTask<Void, Void,Void> {

        Bitmap image;
        String text;
        // data input json format:
        String ImageText(String text, byte[] imageBytes) {
            try {
                Json js = new Json();
                int usrid = js.getId();

                String imgStr = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("usrid",usrid);
                jsonObject.put("geoinfo",geoinfo);
                jsonObject.put("text", text);
                jsonObject.put("image",imgStr);
                return jsonObject.toString();
            }
            catch (Exception e) {
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
            if(image!=null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                imageBytes = byteArrayOutputStream.toByteArray();

            }
            else {
                imageBytes = null;
            }
            String json = ImageText(text,imageBytes);
            String link = SERVER_ADDRESS + "/api/app/upload";

            try {
                RequestBody requestBody = RequestBody.create(JSON,json);
                Request request = new Request.Builder()
                        .url(link)
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        e.printStackTrace();
                        Log.e(TAG,request+"FAIL");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if(!response.isSuccessful()) {
                            Log.e(TAG,"FAIL TO GET RESPONSE");
                        }
                        else {
                            System.out.println(response.body().string());
                        }
                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            bImgUpload.setEnabled(false);
        }


    }


}

