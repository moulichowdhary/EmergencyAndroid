package com.example.s525721.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText userID, Password;
    private Button button4, rigisterBTN,forgotPasswordBTN;



    // Redirected to main activity if user already logged in
    public void isRedirected(){
        if (ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isRedirected();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_login);
        relativeLayout.setOnClickListener(this);





           // session = new SessionManager(this);
           // sharedpreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
           // editor = sharedpreferences.edit();


            rigisterBTN = (Button) findViewById(R.id.register);
            rigisterBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);

                    startActivity(i);
                }
            });
//forgot password button


            forgotPasswordBTN = (Button) findViewById(R.id.forgotBTN);
            forgotPasswordBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
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


                    if (user919ID.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Invalid id ", Toast.LENGTH_SHORT).show();
//                    success = false;
                    } else if (password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
//                    success = false;
                    } else {
                        ParseUser.logInInBackground(user919ID, password, new LogInCallback() {

                            @Override
                            public void done(ParseUser user, ParseException e) {


                                if (e == null) {
                                    //isRedirected();

                                    //session.createLoginSession(user919ID, password);

                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.putExtra("919", userID.getText().toString());

                                    startActivity(i);
                                    //finish();

                                   // Log.i(sharedpreferences.getString("passKey", ""), "Good ");
                                   // System.out.print(sharedpreferences.getBoolean(
                                            //"IS_Login", true));
                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "No such user exist", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                }
            });
        }


//hides keyboard when click on background layout
    @Override
    public void onClick(View view) {

        TextView login = (TextView) findViewById(R.id.loginLBL);
            if (view.getId() == R.id.activity_login || view.getId() == R.id.register || view.getId() == R.id.loginLBL){
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
    }
}


