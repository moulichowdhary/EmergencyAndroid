package com.example.s525721.myapplication;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by S525894 on 11/23/2016.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("81HoKnauaDlvgFHGRd7hBCNrjEt4V70MOSHW4F0w")
                .clientKey("BrXxoHxS4BCALYGmO0QyOxvz3QB9j26SLGFTfZx5")
                .server("https://parseapi.back4app.com") // The trailing slash is important.


                .build()
                //COmmit with this project
        );



    }
}
