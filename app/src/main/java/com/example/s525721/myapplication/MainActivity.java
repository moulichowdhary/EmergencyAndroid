package com.example.s525721.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.s525721.myapplication.R.id.logoutBTN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
     ImageButton locationIBTN,complaintIBTN;
     Button logout;

    private LocationManager locationManager;
    private LocationListener listener;
    private double latitude;
    private double longitude;
    SessionManager session;

    public ImageButton getLocationIBTN() {
        return locationIBTN;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(this);

        session.checkLogin();
        // t = (TextView) findViewById(R.id.textView2);
        locationIBTN = (ImageButton) findViewById(R.id.emergencyIBTN);
        complaintIBTN = (ImageButton) findViewById(R.id.complaintIBTN);
        logout = (Button) findViewById(R.id.logoutBTN);
//        t = (TextView) findViewById(R.id.textView2);
//        b = (Button) findViewById(R.id.locationBTN);
//        kk = (TextView) findViewById(R.id.textView3);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //configure_button();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               // t.setText(location.getLongitude() + " " + location.getLatitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                String address = getCompleteAddressString(latitude, longitude);
                Toast.makeText(getApplicationContext(),address,Toast.LENGTH_SHORT).show();
               // kk.setText(address);


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
        if(Build.VERSION.SDK_INT < 23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }else{

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                // if no permission -> ask for permission
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{
                //if we have permissions already..if not not ask for permission result
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }
        }

        complaintIBTN.setOnClickListener(this);
        logout.setOnClickListener(this);
        locationIBTN.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //request code --> 1
        //permissions array , we do haVE ONLY ONE PERMISSION
        //PERMISSION RESULT

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }else{
                return;
            }

        }

    }

   public void configure_button() {
//        // first check for permissions
//       //if API <23, no need of permissions
//       if(Build.VERSION.SDK_INT < 23){
//           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
//       }else{
//
//           if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//               // if no permission -> ask for permission
//               ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
//           }else{
//               //if we have permissions already..if not not ask for permission result
//               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
//           }
//       }

       // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.complaintIBTN:
                Log.d("complaint", "major activity");
                Intent i = new Intent(this, ComplaintActivity.class);
                startActivity(i);
                break;

            case R.id.logoutBTN:
                session.logoutUser();
                break;

            case R.id.emergencyIBTN:

                //noinspection MissingPermission
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                //void requestLocationUpdates (String provider,  long minTime,  float minDistance, LocationListener listener)


                String subject = getCompleteAddressString(latitude, longitude);

                sendEmail(latitude, longitude, subject);

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
                Log.w("MyCurrentlocationaddr", "" + strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentlocationaddr", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrentlocationaddr", "Cannon get Address!");
        }


        return strAdd;
    }


    protected void sendEmail(double latitude, double longitude, String subject) {

        String sub = subject;
        String body = "http://www.google.com/maps/place/" + String.valueOf(latitude) + "," + String.valueOf(longitude);

        SendMail sm = new SendMail(this, "makkenasrinivasarao1@gmail.com", sub, sub + "\n" + body);
        sm.execute();


    }

}