package com.example.s525721.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
    private static final int TAKE_PIC_REQUEST_CODE = 0;
    private static final int CHOOSE_PIC_REQUEST_CODE = 1;
    public static final int MEDIA_TYPE_IMAGE = 2;

    private EditText data;
    private Button submitReport,emergencyContacts;
    private Spinner dropDownComplaintType;

    private Uri mMediaUri;


   public void uploadPhoto(View view){
       AlertDialog.Builder builder = new AlertDialog.Builder (ComplaintActivity.this);
       builder.setTitle("Upload or Take Photo");
       builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {

           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               //upload image
               Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
               choosePictureIntent.setType("image/*");
               startActivityForResult(choosePictureIntent, CHOOSE_PIC_REQUEST_CODE);

           }
       });
       builder.setNegativeButton("Take Photo", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               //take picture
               Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

               if (mMediaUri == null){
                   //display error
                   Toast.makeText(ComplaintActivity.this, "Sorry!! There was an error, Try Again", Toast.LENGTH_SHORT).show();

               }else{
                   takePicture.putExtra(MediaStore.EXTRA_OUTPUT,mMediaUri);
                   startActivityForResult(takePicture,TAKE_PIC_REQUEST_CODE);

               }

           }
       });
       AlertDialog dialog = builder.create();
       dialog.show();
   }
    //inner helper method
    private Uri getOutputMediaFileUri(int mediaTypeImage) {

        if(isExternalStorageAvailable()){
            //get the URI
            //get external storage dir
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "UPLOADIMAGES");
            //create subdirectore if it does not exist
            if(!mediaStorageDir.exists()){
                //create dir
                if(! mediaStorageDir.mkdirs()){

                    return null;
                }
            }
            //create a file name
            //create file
            File mediaFile = null;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

            String path = mediaStorageDir.getPath() + File.separator;
            if(mediaTypeImage == MEDIA_TYPE_IMAGE){
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            }
            //return file uri


            return Uri.fromFile(mediaFile);
        }else {

            return null;
        }

    }
    //check if external storage is mounted. helper method
    private boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        data = (EditText) findViewById(R.id.complaintData);
        submitReport = (Button) findViewById(R.id.submitReportBTN);
        emergencyContacts = (Button)findViewById(R.id.conatactsBTN);
        dropDownComplaintType =(Spinner) findViewById(R.id.complaintTypeSpinner);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_complaint);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });

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
sendEmail();
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

                    Log.d("Enter to fileComplaint","Success3");
                    Toast.makeText(getApplicationContext(), "Report saved", Toast.LENGTH_SHORT).show();
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

 SendMail sm = new SendMail(this, "makkenasrinivasarao1@gmail.com", dropDownComplaintType.getSelectedItem().toString(),  data.getText().toString() + "\n" +  data.getText().toString());
        sm.execute();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == CHOOSE_PIC_REQUEST_CODE){
                if(data == null){
                    Toast.makeText(getApplicationContext(), "Image cannot be null!", Toast.LENGTH_LONG).show();
                }else{
                    mMediaUri = data.getData();
                    //set previews
                    //mPreviewImageView.setImageURI(mMediaUri);
                }
            }else {

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
                //set previews

               // mPreviewImageView.setImageURI(mMediaUri);

            }

        }else if(resultCode != RESULT_CANCELED){
            Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_LONG).show();
        }
    }






}
