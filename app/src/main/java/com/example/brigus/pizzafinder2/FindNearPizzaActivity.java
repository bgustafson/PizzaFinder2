package com.example.brigus.pizzafinder2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.example.brigus.pizzafinder2.Adapters.PizzaLocationArrayAdapter;
import com.example.brigus.pizzafinder2.Model.PizzaLocation;
import com.example.brigus.pizzafinder2.Tasks.GoogleNearbySearchTask;
import com.example.brigus.pizzafinder2.utils.AsyncResponse;
import com.example.brigus.pizzafinder2.utils.PizzaLocationListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import static com.example.brigus.pizzafinder2.utils.PermissionsManager.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.brigus.pizzafinder2.utils.PermissionsManager.checkForPermissions;


public class FindNearPizzaActivity extends AppCompatActivity implements AsyncResponse {

    private int SETTINGS_UPDATED = 100;
    private AdView adView;
    private static final String AD_Unit_ID = "ca-app-pub-3892677699343303/2379904475";
    private String Test_Hashed_Device_ID;
    LocationManager locationManager;
    PizzaLocationListener locationHelper;
    GoogleNearbySearchTask asyncTask;
    Location location;
    String provider;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    ListView listView;
    LinearLayout progressBarContainer, mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_near_pizza);

        listView = (ListView) findViewById(R.id.listView);
        progressBarContainer = (LinearLayout) findViewById(R.id.progressBarContainer);
        mainContainer = (LinearLayout) findViewById(R.id.parent_container);

        asyncTask = new GoogleNearbySearchTask(this);
        locationHelper = new PizzaLocationListener(asyncTask);
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        getLastLocation();

        //Create Ad
        Test_Hashed_Device_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_Unit_ID);

        AdRequest.Builder adRequestBuilder = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(Test_Hashed_Device_ID);


        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.TOP);
        linearLayout.addView(adView);
        mainContainer.addView(linearLayout, 0);


        // Start loading the ad in the background.
        adView.loadAd(adRequestBuilder.build());
    }

    private void getLastLocation(){
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        boolean hasPermission = checkForPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        if(hasPermission) {
            if (gps_enabled) {
                provider = LocationManager.GPS_PROVIDER;
                location = locationManager.getLastKnownLocation(provider);
            } else {
                provider = LocationManager.NETWORK_PROVIDER;
                location = locationManager.getLastKnownLocation(provider);
            }

            asyncTask.equals(location);
            locationManager.requestLocationUpdates(provider, 60000, 1000, locationHelper);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     getLastLocation();
                } else {

                }
                break;
        }
    }


    //update list.  This is the delegate method called from the async task and set on the task before it is run
    public void processFinish(ArrayList<PizzaLocation> output){
        PizzaLocation[] array = output.toArray(new PizzaLocation[output.size()]);

        //build the ui
        PizzaLocationArrayAdapter adapter = new PizzaLocationArrayAdapter(this, array);
        listView.setAdapter(adapter);
        progressBarContainer.setVisibility(View.INVISIBLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PizzaLocation selectedFromList = (PizzaLocation) (listView.getItemAtPosition(position));

                //open the details view here
                Intent i = new Intent(FindNearPizzaActivity.this, PizzaDetailActivity.class);
                i.putExtra("name", selectedFromList.getName());
                i.putExtra("location", selectedFromList);

                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rerun:
                finish();
                startActivity(getIntent());
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_UPDATED);
                break;
        }
        return true;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    protected void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
