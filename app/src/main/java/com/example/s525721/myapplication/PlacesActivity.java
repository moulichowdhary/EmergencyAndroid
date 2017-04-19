package com.example.s525721.myapplication;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PlacesActivity extends ListActivity {
//    private LocationManager locationManager;
//    private LocationListener listener;
    private double latitude;
   private double longitude;
    //ListView hospitalPlaces;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Intent i = getIntent();
        latitude = i.getDoubleExtra("Lat",40.353);
        longitude = i.getDoubleExtra("Lon",-94.8);


        //MainActivity.GetPlaces pl = new MainActivity.GetPlaces(getApplicationContext(), getListView());
        new GetPlaces(this, getListView()).execute();
//pl.execute();




    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);



        Toast.makeText(PlacesActivity.this,"Click"+position,Toast.LENGTH_SHORT).show();

    }

    class GetPlaces extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private Context context;
        private ArrayList<String> placeName;
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
            if (placeName.size() == 0){
                placeName.add("No hospitals in 3 miles radius");
            }
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
//list view header
            TextView textView = new TextView(PlacesActivity.this);
            textView.setText("Hospitals in radius of 3 miles");
            textView.setTextSize(30);
            listView.addHeaderView(textView);
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

            placeName = new ArrayList<String>();
            imageUrl = new String[findPlaces.size()];



            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
                placeDetail.getIcon();

                System.out.println(  placeDetail.getName());
                placeName.add(placeDetail.getName());

                imageUrl[i] =placeDetail.getIcon();

            }
            return null;
        }

    }



}