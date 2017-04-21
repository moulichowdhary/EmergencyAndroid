package com.example.s525721.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import static com.example.s525721.myapplication.R.id.logoutBTN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button locationIBTN, complaintIBTN;
    Button fineMsgBTN, emergencyContactsBTN, hospitalSearchBTN, settingsBTN;


    private LocationManager locationManager;
    private LocationListener listener;
    private double latitude;
    private double longitude;
    String address;
    String name;
    ArrayList<String> Emails;

    Intent i;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//setTitle("Emergency");
        ParseUser currentUser = ParseUser.getCurrentUser();
//        if (currentUser == null) {
//            Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(mainIntent);
        //  } else{
        // show the signup or login screen
//            footer=(TextView) findViewById(R.id.footer);
//        footer.setText(R.string.footer);
//        userWelcomeTV=(TextView) findViewById(R.id.UserNameTV);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        // ParseUser.

        // userWelcomeTV.setText("Welcome, " +ParseUser.getCurrentUser().getUsername());
        i = getIntent();


        // session = new SessionManager(this);

        //session.checkLogin();
        locationIBTN = (Button) findViewById(R.id.emergencyIBTN);
        complaintIBTN = (Button) findViewById(R.id.complaintIBTN);
        fineMsgBTN = (Button) findViewById(R.id.iamfineBTN);
        hospitalSearchBTN = (Button) findViewById(R.id.hospitalFindBTN);
        emergencyContactsBTN = (Button) findViewById(R.id.emergencyContactsBTN);
        settingsBTN = (Button) findViewById(R.id.settingsBTN);


        //logout = (Button) findViewById(R.id.logoutBTN);

        settingsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, settingsBTN);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                                ParseUser.logOut();
                                Intent sessionIntent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(sessionIntent);
                                break;
                            case R.id.mailRecipients:
                                Intent addMailIntent = new Intent(MainActivity.this, AddingMailsActivity.class);
                                startActivity(addMailIntent);
                                break;

                            //session.logoutUser();
//                break;

                        }

                        //Toast.makeText(MainActivity.this,"You Clicked : " + item.getItemId(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();
            }
        });


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //configure_button();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                address = getCompleteAddressString(latitude, longitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
// first check for permissions
        //if API <23, no need of permissions
        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // if no permission -> ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //if we have permissions already..if not not ask for permission result
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }
        }


        //Emails retrival....
        Emails = new ArrayList<String>();
        fetchData();
        //fetchDatA();


        complaintIBTN.setOnClickListener(this);
        //logout.setOnClickListener(this);
        locationIBTN.setOnClickListener(this);
        emergencyContactsBTN.setOnClickListener(this);
        fineMsgBTN.setOnClickListener(this);
        hospitalSearchBTN.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //request code --> 1
        //permissions array , we do haVE ONLY ONE PERMISSION
        //PERMISSION RESULT

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            } else {
                return;
            }

        }

    }

    public void fetchData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("EmailRecipients");
        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e == null) {


                    //Toast.makeText(MainActivity.this, "Retrieved", Toast.LENGTH_SHORT).show();

                    if (list.size() > 0) {
                        for (ParseObject object : list) {
                            String mail = object.getString("recipientEmail");
                            //Log.i("RetrivdMainActivity",mail);
                            Emails.add(mail);
                            //emailArray.notifyDataSetChanged();
                        }
                    }
                }

            }


        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.complaintIBTN:

                Intent i = new Intent(this, ComplaintActivity.class);
                i.putExtra("Lat", latitude);
                i.putExtra("Lng", longitude);
                startActivity(i);
                break;

            case R.id.emergencyContactsBTN:

                Intent intentContacts = new Intent(this, PhoneNumber.class);
                startActivity(intentContacts);
                //session.logoutUser();
                break;

            case R.id.emergencyIBTN:

                //noinspection MissingPermission
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                //void requestLocationUpdates (String provider,  long minTime,  float minDistance, LocationListener listener)


                String subject = getCompleteAddressString(latitude, longitude);

                sendEmail(latitude, longitude, address);

                break;
            case R.id.iamfineBTN:

                //noinspection MissingPermission
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                //void requestLocationUpdates (String provider,  long minTime,  float minDistance, LocationListener listener)


                // String sub = getCompleteAddressString(latitude, longitude);

                sentWrongEmail();

                break;
            case R.id.hospitalFindBTN:
                Intent hospitalsIntent = new Intent(MainActivity.this, PlacesActivity.class);
                hospitalsIntent.putExtra("Lat", latitude);
                hospitalsIntent.putExtra("Lng", longitude);
                startActivity(hospitalsIntent);
                break;


        }
    }

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "Location is not loaded";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    if (returnedAddress.getAddressLine(i) != null) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                }
                strAdd = strReturnedAddress.toString();
                //Log.w("MyCurrentlocationaddr", "" + strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentlocationaddr", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrentlocationaddr", "Cannot get Address!");
        }


        return strAdd;
    }


    protected void sendEmail(double latitude, double longitude, String subject) {


        String sub = subject;
        //sub.concat("\n\n My 919 ID: "+ intent.getStringExtra("919"));
        String body = "http://www.google.com/maps/place/" + String.valueOf(latitude) + "," + String.valueOf(longitude);

        String user919 = ParseUser.getCurrentUser().getUsername();

        SendMail sm = new SendMail(this, "makkenasrinivasarao1@gmail.com", "Alert! StudentID: "+user919+" is in Emergency. Requires immediate action ", user919+", is in danger. \n\nLast identified location\n"+"\n"+sub  + body);
        sm.execute();
        for (String mail : Emails) {
            Log.i("Mail", mail);
            SendMail sm1 = new SendMail(this, mail, user919, sub + "Alert! StudentID: "+user919+" is in Emergency. Requires immediate action "+"\n"+"\n" + body);
            sm1.execute();
        }


    }

//    public void fetchDatA() {
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> list, ParseException e) {
//                if (e==null) {
//
//
//                    //Toast.makeText(AddingMailsActivity.this, "Retrieved", Toast.LENGTH_SHORT).show();
//
//                    if (list.size() > 0) {
//                        for (ParseObject object : list) {
//                            Log.i("Name",Integer.toString(list.size()));
//                            String name=  object.getString("name");
//                            Log.i("Name",name);
//                            // Log.i("Retrivd",mail);
//                            //emails.add(mail);
//                            //emailArray.notifyDataSetChanged();
//                        }
//                    }
//                }
//
//            }
//        });
//    }

        protected void sentWrongEmail(){
        String user919 = ParseUser.getCurrentUser().getUsername();
        String body = "http://www.google.com/maps/place/" + String.valueOf(latitude) + "," + String.valueOf(longitude);
        SendMail sm = new SendMail(this, "makkenasrinivasarao1@gmail.com", user919 +"says I'm OK !  ", "I just want to let you know that I am ok now!\n" + "" + "\n" + body);
        sm.execute();
    }


}