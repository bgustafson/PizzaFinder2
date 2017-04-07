package com.example.brigus.pizzafinder2.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.brigus.pizzafinder2.Adapters.PizzaLocationAdapter;
import com.example.brigus.pizzafinder2.Model.PizzaLocation;
import com.example.brigus.pizzafinder2.R;
import com.example.brigus.pizzafinder2.SettingsActivity;
import com.example.brigus.pizzafinder2.utils.AsyncResponse;
import com.example.brigus.pizzafinder2.utils.PizzaLocationListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.brigus.pizzafinder2.utils.PermissionsManager.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.brigus.pizzafinder2.utils.PermissionsManager.checkForPermissions;


public class FindNearPizzaFragment extends Fragment implements AsyncResponse {

    private int SETTINGS_UPDATED = 100;
    private AdView adView;
    private static final String AD_Unit_ID = "ca-app-pub-3892677699343303/2379904475";
    private String Test_Hashed_Device_ID;
    private LocationManager mLocationManager;
    private PizzaLocationListener mLocationHelper;
    private Location mLocation;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.progressBarContainer) LinearLayout progressBarContainer;
    @BindView(R.id.about_fab) FloatingActionButton mAboutFAB;
    private Unbinder mUnbinder;

    private GoogleSearchFragment mHeadlessSearchFragment;
    private AboutFragment mAboutFragment;


    public static FindNearPizzaFragment newInstance() {
        return new FindNearPizzaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHeadlessSearchFragment = (GoogleSearchFragment) getFragmentManager()
                .findFragmentByTag(GoogleSearchFragment.TAG);
        if(mHeadlessSearchFragment == null) {
            mHeadlessSearchFragment = new GoogleSearchFragment();
            mHeadlessSearchFragment.setDelegate(this);
            getFragmentManager().beginTransaction().add(mHeadlessSearchFragment, GoogleSearchFragment.TAG)
                    .commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_find_near_pizza, container, false);
        Activity activity = getActivity();
        mUnbinder = ButterKnife.bind(this, view);

        mLocationHelper = new PizzaLocationListener(mHeadlessSearchFragment.getWrappedAsyncTask());
        mLocationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        getLastLocation();

        //Create Ad
        Test_Hashed_Device_ID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        adView = new AdView(activity);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_Unit_ID);

        AdRequest.Builder adRequestBuilder = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(Test_Hashed_Device_ID);


        LinearLayout mainContainer = (LinearLayout) view.findViewById(R.id.parent_container);
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.TOP);
        linearLayout.addView(adView);
        mainContainer.addView(linearLayout, 0);


        // Start loading the ad in the background.
        adView.loadAd(adRequestBuilder.build());

        mAboutFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAboutFragment = (AboutFragment) getFragmentManager()
                        .findFragmentByTag(AboutFragment.TAG);
                if(mAboutFragment == null) {
                    mAboutFragment = AboutFragment.newInstance();
                    mAboutFragment.show(getFragmentManager(), AboutFragment.TAG);
                }
            }
        });

        return view;
    }


    private void getLastLocation(){
        boolean gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        boolean hasPermission = checkForPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        if(hasPermission) {
            String provider;
            if (gps_enabled) {
                provider = LocationManager.GPS_PROVIDER;
                mLocation = mLocationManager.getLastKnownLocation(provider);
            } else {
                provider = LocationManager.NETWORK_PROVIDER;
                mLocation = mLocationManager.getLastKnownLocation(provider);
            }

            mHeadlessSearchFragment.findNearby(mLocation);
            mLocationManager.requestLocationUpdates(provider, 60000, 1000, mLocationHelper);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rerun:
                mRecyclerView.setAdapter(null);
                progressBarContainer.setVisibility(View.VISIBLE);
                mHeadlessSearchFragment.findNearby(mLocation);
                break;
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_UPDATED);
                break;
        }
        return true;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTINGS_UPDATED && resultCode == RESULT_OK) {
            mRecyclerView.setAdapter(null);
            progressBarContainer.setVisibility(View.VISIBLE);
            mHeadlessSearchFragment.findNearby(mLocation);
        }
    }


    //update list.  This is the delegate method called from the async task and set on the task before it is run
    @Override
    public void processFinish(List<?> output) {

        if(output != null && !output.isEmpty()) {
            if (output.get(0) instanceof PizzaLocation) {

                PizzaLocationAdapter locationAdapter = new PizzaLocationAdapter(getActivity(), (List<PizzaLocation>) output);
                locationAdapter.getItemTouchHelper().attachToRecyclerView(mRecyclerView);

                //build the ui
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(locationAdapter);
                progressBarContainer.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }


    @Override
    public void onPause() {
        adView.pause();
        mLocationManager.removeUpdates(mLocationHelper);
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
        }

        if(mHeadlessSearchFragment != null) {
            mHeadlessSearchFragment.setDelegate(null);
            mHeadlessSearchFragment = null;
        }

        super.onDestroy();
    }
}
