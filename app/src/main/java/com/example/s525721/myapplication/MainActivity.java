package com.example.s525721.myapplication;

import android.Manifest;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.s525721.myapplication.R.id.logoutBTN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton locationIBTN, complaintIBTN;
    private Button logout;
//    private TextView t;
//    private TextView kk;
    private LocationManager locationManager;
    private LocationListener listener;
    private double latitude;
    private double longitude;
    SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        session = new SessionManager(this);

        session.checkLogin();
        // t = (TextView) findViewById(R.id.textView2);
        locationIBTN = (ImageButton) findViewById(R.id.emergencyIBTN);
        complaintIBTN = (ImageButton) findViewById(R.id.complaintIBTN);
        logout = (Button) findViewById(logoutBTN);
        // kk = (TextView) findViewById(R.id.textView3);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        configure_button();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               // t.setText(location.getLongitude() + " " + location.getLatitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                String address = getCompleteAddressString(latitude, longitude);
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
        locationIBTN.setOnClickListener(this);
        complaintIBTN.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
//        locationIBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //noinspection MissingPermission
//                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
//                sendEmail(latitude, longitude);
//            }
//        });
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "Just a message when location is not loaded";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w(strReturnedAddress.toString(), "My Current location address");
            } else {
                Log.w("My Current location address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current location address", "Cannon get Address!");
        }


        return strAdd;
    }


    private void sendEmail(double latitude, double longitude) {

        String sub = getCompleteAddressString(latitude, longitude);
        String body = "http://www.google.com/maps/place/" + String.valueOf(latitude) + "," + String.valueOf(longitude);

        SendMail sm = new SendMail(this, "moulichowdhary@gmail.com", sub, sub + "\n" + body);
        sm.execute();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emergencyIBTN:
                //configure_button();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
                sendEmail(latitude, longitude);
                break;
            case R.id.complaintIBTN:
                Log.d("complaint","major activity");
                Intent i = new Intent(this, MajorActivity.class);
                startActivity(i);
                break;

            case logoutBTN:
                session.logoutUser();

        }
    }
}


