package com.example.s525721.myapplication;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class PlacesActivity extends ListActivity {
//    private LocationManager locationManager;
//    private LocationListener listener;
    private double latitude;
   private double longitude;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Intent i = getIntent();
        latitude = i.getDoubleExtra("Lat",40.353);
        longitude = i.getDoubleExtra("Lon",-94.8);


//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//        //configure_button();
//        listener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                System.out.print(latitude);
//                System.out.print(longitude);
//
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String s) {
//
//                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(i);
//            }
//        };
//
//        // first check for permissions
//        //if API <23, no need of permissions
//        if (Build.VERSION.SDK_INT < 23) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
//        } else {
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // if no permission -> ask for permission
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            } else {
//                //if we have permissions already..if not not ask for permission result
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
//                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//            }
//        }

        //MainActivity.GetPlaces pl = new MainActivity.GetPlaces(getApplicationContext(), getListView());
        new GetPlaces(this, getListView()).execute();
//pl.execute();




    }

    class GetPlaces extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private Context context;
        private String[] placeName;
        private String[] imageUrl;
        private ListView listView;

        public GetPlaces(Context context, ListView listView) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.listView = listView;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            ArrayAdapter<String> hospitals = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,placeName);
            listView.setAdapter(hospitals);
            hospitals.notifyDataSetChanged();

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(true);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            PlacesService service = new PlacesService("AIzaSyDPBnT7RTIYZuosjXTlxauD9ipFcrHA-IM");
            List<Place> findPlaces = service.findPlaces(latitude,longitude,"hospital");


            // hospiral for hospital
            // atm for ATM

            System.out.println(latitude);
            System.out.println(longitude);

            placeName = new String[findPlaces.size()];
            imageUrl = new String[findPlaces.size()];

            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
                placeDetail.getIcon();

                System.out.println(  placeDetail.getName());
                placeName[i] =placeDetail.getName();

                imageUrl[i] =placeDetail.getIcon();

            }
            return null;
        }

    }



}