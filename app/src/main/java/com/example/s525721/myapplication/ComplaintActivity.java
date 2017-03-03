package com.example.s525721.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kinvey.java.Util;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ComplaintActivity extends AppCompatActivity {
    private EditText data;
    private Button submitReport,emergencyContacts;
    private Spinner dropDownComplaintType;

    MainActivity sendMailWithReport = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        data = (EditText) findViewById(R.id.complaintData);
        submitReport = (Button) findViewById(R.id.submitReportBTN);
        emergencyContacts = (Button)findViewById(R.id.conatactsBTN);
        dropDownComplaintType =(Spinner) findViewById(R.id.complaintTypeSpinner);

       //Spinner code, adding emergency types to dropdown
        List<String> list = new ArrayList<String>();
        list.add("Choose complaint Type");
        list.add("Fire Emergency");
        list.add("Health Emergency");
        list.add("Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownComplaintType.setAdapter(dataAdapter);



        Log.d("Enter to fileComplaint","Success");
        submitReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d("Enter to fileComplaint","Success1");
                if (fileComplaint()==true){
                    //sendMailWithReport.sendEmail(sendMailWithReport.getLatitude(),sendMailWithReport.getLongitude());
                    Toast.makeText(getApplicationContext(), "Success- report added", Toast.LENGTH_SHORT);

                }else{
                    Toast.makeText(getApplicationContext(), "Fail - Report not added", Toast.LENGTH_SHORT);
                }
            }
        });

        emergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contacts = new Intent(getApplicationContext(),PhoneNumber.class);
                startActivity(contacts);
            }
        });

    }
//    public void sendEmail(double latitude, double longitude) {
//
//        String sub = sendMailWithReport.getCompleteAddressString(latitude, longitude);
//        String body = "http://www.google.com/maps/place/" + String.valueOf(latitude) + "," + String.valueOf(longitude);
//
//        SendMail sm = new SendMail(this, "moulichowdhary@gmail.com", sub, sub + "\n" + body);
//        sm.execute();
//
//
//    }


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

 SendMail sm = new SendMail(this, "makkenasrinivasarao1@gmail.com", dropDownComplaintType.getSelectedItem().toString(),  data.getText().toString() + "\n" +  data.getText().toString());
        sm.execute();


    }






}
