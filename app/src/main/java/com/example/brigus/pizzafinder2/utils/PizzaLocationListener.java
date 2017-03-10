package com.example.brigus.pizzafinder2.utils;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.example.brigus.pizzafinder2.Tasks.GoogleNearbySearchTask;


public class PizzaLocationListener implements LocationListener {

    private GoogleNearbySearchTask asyncTask;

    public PizzaLocationListener(GoogleNearbySearchTask asyncTask){
        this.asyncTask = asyncTask;
    }


    @Override
    public void onLocationChanged(Location location) {

        //get directions button
        this.asyncTask.execute(location);
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
