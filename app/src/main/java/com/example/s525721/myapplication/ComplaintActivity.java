package com.example.s525721.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kinvey.java.Util;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ComplaintActivity extends AppCompatActivity {
    public EditText data;
    private Button submitReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        data = (EditText) findViewById(R.id.complaintData);
        submitReport = (Button) findViewById(R.id.submitReportBTN);
        Log.d("Enter to fileComplaint","Success");
        submitReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d("Enter to fileComplaint","Success1");
                if (fileComplaint()==true){

                    Toast.makeText(getApplicationContext(), "Success- report added", Toast.LENGTH_SHORT);

                }else{
                    Toast.makeText(getApplicationContext(), "Fail - Report not added", Toast.LENGTH_SHORT);
                }
            }
        });

    }



    private boolean result;

    protected boolean fileComplaint() {

        Log.d("Enter to fileComplaint","Success2");

        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();

        ParseObject parseObject = new ParseObject("EmergencyReport");
        parseObject.put("ComplaintString", data.getText().toString());
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
               // Log.d("Enter to fileComplaint",e.getMessage());
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Report saved", Toast.LENGTH_SHORT);
                   sendEmail();
                    result = true;



                } else {
                    Toast.makeText(getApplicationContext(), "Report not saved", Toast.LENGTH_SHORT);
                    result = false;
                }
            }
        });
        return result;
    }



    protected void sendEmail( ) {

 SendMail sm = new SendMail(this, "moulichowdhary@gmail.com", "New Complaint from a student",  data.getText().toString() + "\n" +  data.getText().toString());
        sm.execute();


    }






}
