package com.example.s525721.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.kinvey.java.Util;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ComplaintActivity extends AppCompatActivity {
//    private static final int TAKE_PIC_REQUEST_CODE = 0;
//    private static final int CHOOSE_PIC_REQUEST_CODE = 1;
//    public static final int MEDIA_TYPE_IMAGE = 2;

    private EditText data;
    private Button submitReport;
    //private Button hospitalSearch;
    private Spinner dropDownComplaintType;

   // private Uri mMediaUri;

    private double latitude;
    private double longitude;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        data = (EditText) findViewById(R.id.conplaintEditText);
        submitReport = (Button) findViewById(R.id.submitReportBTN);
        //emergencyContacts = (Button) findViewById(R.id.conatactsBTN);
        dropDownComplaintType = (Spinner) findViewById(R.id.complaintTypeSpinner);

        Intent receivingCoOrdinates = getIntent();
        latitude = receivingCoOrdinates.getDoubleExtra("Lat",0.0);
        longitude = receivingCoOrdinates.getDoubleExtra("Lng",0.0);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_complaint);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        //Spinner code, adding emergency types to dropdown
        List<String> list = new ArrayList<String>();
        list.add("Choose complaint Type");
        list.add("Fire Emergency");
        list.add("Health Emergency");
        list.add("Founded Suspicious Activity or Person");
        list.add("Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownComplaintType.setAdapter(dataAdapter);


        //Log.d("Enter to fileComplaint", "Success");



                submitReport.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (dropDownComplaintType.getSelectedItem() == "Choose complaint Type") {

                            Toast.makeText(ComplaintActivity.this, "Please Choose Specific Complaint", Toast.LENGTH_SHORT).show();

                        } else if (data.getText().toString().isEmpty() || data.getText().toString() == "") {

                            Toast.makeText(ComplaintActivity.this, "Please Enter Comlaint data", Toast.LENGTH_SHORT).show();

                        } else {
                            sendEmail();
                            //Log.d("Enter to fileComplaint", "Success1");
                            if (fileComplaint() == true) {
                                //sendMailWithReport.sendEmail(sendMailWithReport.getLatitude(),sendMailWithReport.getLongitude());
                                //Toast.makeText(getApplicationContext(), "Success- report added", Toast.LENGTH_SHORT);

                            } else {
                                Toast.makeText(getApplicationContext(), "Fail - Report not added", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                });




//
//        emergencyContacts.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent contacts = new Intent(getApplicationContext(), PhoneNumber.class);
//                    startActivity(contacts);
//                }
//            });


//Getting location


        //keyboard hiding immediately after loading page
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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

//        ParseUser currentUser = ParseUser.getCurrentUser();
//        currentUser.logOut();

        ParseObject parseObject = new ParseObject("EmergencyReport");
        parseObject.put("ComplaintString", data.getText().toString());
        parseObject.put("Username", ParseUser.getCurrentUser().getUsername());
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
               // Log.d("Enter to fileComplaint",e.getMessage());

                if (e == null) {

                    //Log.d("Enter to fileComplaint","Success3");
                    //Toast.makeText(getApplicationContext(), "Report saved", Toast.LENGTH_SHORT).show();
                   // sendEmail();
                    result = true;



                } else {
                    Toast.makeText(getApplicationContext(), "Report not saved", Toast.LENGTH_SHORT).show();
                    result = false;
                }
            }
        });
        return result;
    }


    protected void sendEmail( ) {
        String body = "http://www.google.com/maps/place/" + String.valueOf(latitude) + "," + String.valueOf(longitude);

        SendMail sm = new SendMail(this, "makkenasrinivasarao1@gmail.com", dropDownComplaintType.getSelectedItem().toString(),  data.getText().toString() + "\n" +body  );
        sm.execute();


    }


}
