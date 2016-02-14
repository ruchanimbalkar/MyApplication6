package com.example.rucha.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Rucha on 2/10/2016.
 */
public class SignUp extends Activity {


    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnDisplay;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        addListenerOnButton();

    }



        public void addListenerOnButton()
        {
            radioSexGroup = (RadioGroup) findViewById(R.id.TFsex);
            btnDisplay = (Button) findViewById(R.id.btnDisplay);

            btnDisplay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // get selected radio button from radioGroup
                    int selectedId = radioSexGroup.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioSexButton = (RadioButton) findViewById(selectedId);



                }

            });

        }




    public void OnSignUpClick(View v)
    {
        if(v.getId()==R.id.Bsignup){

            EditText name = (EditText)findViewById(R.id.TFname);
            EditText email = (EditText)findViewById(R.id.TFemail);
            EditText username = (EditText)findViewById(R.id.TFuser);
            EditText dob = (EditText) findViewById(R.id.TFbirthdate);


            EditText pass1 = (EditText)findViewById(R.id.TFpass1);
            EditText pass2 = (EditText)findViewById(R.id.TFpass2);

            String namestr =name.getText().toString();
            String emailstr =email.getText().toString();
            String unamestr =username.getText().toString();
            String pass1str =pass1.getText().toString();
            String pass2str =pass2.getText().toString();
            String dobstr = dob.getText().toString();

            if(!pass1.equals(pass2))
            {
                //Display pop-up msg :
                Toast pass= Toast.makeText(SignUp.this,"Passwords Do not Match!",Toast.LENGTH_SHORT);
                pass.show();
            }
        }
    }
}
