package com.example.s525721.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MajorActivity extends AppCompatActivity {
SessionManager session;
    private Button EmergencyContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);
        session = new SessionManager(getApplicationContext());

//        Button button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//
//                startActivity(i);
//
//            }
//        });

        // action to be executed when emergency contacts button is clicked. It displays phone numbers of different help services.
        EmergencyContacts = (Button) findViewById(R.id.buttonE);
        EmergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneNumber.class);
                startActivity(intent);
            }
        });

    }
}



