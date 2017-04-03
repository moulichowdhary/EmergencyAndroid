package com.example.s525721.myapplication;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.List;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);





        email = (EditText)findViewById(R.id.usernameET);

        update = (Button) findViewById(R.id.updatePassword);




        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Password reset code using Parse Server

                ParseUser.requestPasswordResetInBackground(email.getText().toString(),
                        new RequestPasswordResetCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(ForgotPasswordActivity.this,"Check a link your email \n to reset password",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this,"Something went wrong"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    // Something went wrong. Look at the ParseException to see what's up.
                                }
                            }
                        });


               
            }
        });





    }
}
