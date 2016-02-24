package com.example.rucha.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Rucha on 2/14/2016.
 */
public class trial extends AppCompatActivity implements View.OnClickListener {

    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.trial);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {

    if(v.getId()==R.id.Bsignup) {

        EditText fname = (EditText) findViewById(R.id.TFfirstname);
        EditText lname = (EditText)findViewById(R.id.TFlastname);
        EditText email = (EditText)findViewById(R.id.TFemail);
        EditText sex= (EditText)findViewById(R.id.TFsex);
        EditText username = (EditText)findViewById(R.id.TFuser);
        EditText dob = (EditText) findViewById(R.id.TFbirthdate);
        EditText pass1= (EditText)findViewById(R.id.TFpass1);
        EditText pass2 = (EditText) findViewById(R.id.TFpass2);

        String fnamestr = fname.getText().toString();
        String lnamestr=lname.getText().toString();
        String emailstr = email.getText().toString();
        String sexstr = sex.getText().toString();
        String unamestr = username.getText().toString();
        String dobst =  dob.getText().toString();
        String pass1str = pass1.getText().toString();
        String pass2str = pass2.getText().toString();


        if (!pass1str.equals(pass2str)) {
            //Display pop-up msg :
            Toast pass = Toast.makeText(trial.this, "Passwords Do not Match!", Toast.LENGTH_SHORT);
            pass.show();
        }

        {
                Json Js = new Json();
                int res=Js.sendJson(fnamestr, lnamestr, emailstr, dobst, sexstr, pass1str, unamestr);
                //if(res==200)
                if(true)
                {
                    Toast pass = Toast.makeText(trial.this, "Data saved!", Toast.LENGTH_LONG);
                    Intent i = new Intent(this,MainActivity.class);
                    startActivity(i);

                }
                else{
                    Toast pass = Toast.makeText(trial.this, "Try again!", Toast.LENGTH_SHORT);
                }


        }

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
                "Main Page", // TODO: Define a title for the content shown.
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
                "Main Page", // TODO: Define a title for the content shown.
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
}
