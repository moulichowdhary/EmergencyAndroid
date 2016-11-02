package com.example.s525721.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Client client = new Client.Builder("kid_HkHZyB8ll", "4b63fb1b209143dda5f64477e9acdf10", getApplicationContext()).build();

        client.ping(new KinveyPingCallback() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Toast.makeText(LoginActivity.this, "our ping is fine", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

        Button button4 = (Button) findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MajorActivity.class);

                startActivity(i);

            }
        });

    }
}
