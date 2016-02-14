package com.example.rucha.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Rucha on 2/9/2016.
 */
public class display extends Activity {


    public String rmem;
    @Override
    protected void onCreate (Bundle SavedInstanceState ){
      super.onCreate(SavedInstanceState);
        setContentView(R.layout.display);
        String username = getIntent().getStringExtra("Username");
        TextView tv = (TextView)findViewById(R.id.Tvusername);
        tv.setText(username);
        EditText mem = (EditText)findViewById(R.id.TFtextmem);
        String memt = mem.getText().toString();
        rmem = memt;
        if (memt != null || !memt.equals(" "))
        {

            Intent i = new Intent(display.this, MapsActivity.class);


        }


    }

    public String  returntext()
    {
        return rmem;
    }


}
