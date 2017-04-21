package com.example.s525721.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class PhoneNumber extends AppCompatActivity {
String phone = "";
    public void call919(View view){

            phone = "9032598039";
            call(phone);
    }

    public void callUPD(View view){

        phone = "9032598039";
        call(phone);
    }
    public void callSexualAssault(View view){

        phone = "9032598039";
        call(phone);
    }
    public void callSSMhealth(View view){

        phone = "9032598039";
        call(phone);
    }
    public void callFire(View view){

        phone = "9032598039";
        call(phone);
    }


public void call(String phoneNo){
    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
            "tel", phoneNo, null));
    startActivity(phoneIntent);
}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);






    }
}
