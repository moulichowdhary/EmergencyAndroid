package com.example.s525721.myapplication;

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

import java.util.List;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText Username,password,confirmPwd;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Username = (EditText)findViewById(R.id.usernameET);
        password = (EditText) findViewById(R.id.passwordET);
        confirmPwd = (EditText) findViewById(R.id.confirmPasswordET);
        update = (Button) findViewById(R.id.updatePassword);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username",Username.getText().toString());
                query.setLimit(1);

                Toast.makeText(getApplicationContext(),"Enter",Toast.LENGTH_SHORT).show();

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        Log.i("Objects",Integer.toString(list.size()));

                        if (e == null && list.size()>0){
                            for (ParseUser user:list){

                                if (user.getUsername().equals( Username.getText().toString())) {
                                    Log.i("Objects",user.getUsername());

                                    user.put("password",password.getText().toString());
                                    user.put("confirmPassword",confirmPwd.getText().toString());
                                    user.saveInBackground();
                                    Log.i("Update",password.getText().toString());
                                   // Toast.makeText(getApplicationContext(),password.getText().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });





    }
}
