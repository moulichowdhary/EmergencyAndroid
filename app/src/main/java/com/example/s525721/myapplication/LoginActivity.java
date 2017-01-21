package com.example.s525721.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseQuery;

import com.parse.FindCallback;
import  com.parse.ParseObject;
import com.parse.ParseException;



import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.parse.ParseUser;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText userID, Password;
    private Button button4, rigisterBTN;
RegistrationActivity ra = new RegistrationActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rigisterBTN = (Button) findViewById(R.id.register);
        rigisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);

                startActivity(i);
            }
        });

        //log in button
        button4 = (Button) findViewById(R.id.button4);


        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                userID = (EditText) findViewById(R.id.editText);
                Password = (EditText) findViewById(R.id.editText7);

                final String user919ID = userID.getText().toString();


                final String password = Password.getText().toString();
               // String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



                 if (user919ID.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Invalid id ", Toast.LENGTH_SHORT).show();
//                    success = false;
                } else if (password.isEmpty()) {
                     Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
//                    success = false;
                 }else{
                ParseUser.logInInBackground(user919ID, password, new LogInCallback() {

                    @Override
                    public void done(ParseUser user, ParseException e) {


                        if (e == null) {
                            Log.d("here" + user, "");


                            Intent i = new Intent(getApplicationContext(), MainActivity.class);

                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(), "No such user exist", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
            }
        });
    }
}


