package com.example.administrator.myapplication;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 8/9/2559.
 */
public class MyCurrentLoctionListener implements android.location.LocationListener {


    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();

        String myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();

        //I make a log to see the results
        Log.e("xxxxxx", myLocation);
        /*Toast toast = Toast.makeText(MainActivity.getApplicationContext(),
                myLocation + " _____", Toast.LENGTH_SHORT);
        toast.show();*/

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}