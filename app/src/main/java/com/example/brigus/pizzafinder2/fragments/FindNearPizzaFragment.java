package com.example.brigus.pizzafinder2.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.LinearLayout;
import butterknife.*;
import com.example.brigus.pizzafinder2.FavoritesActivity;
import com.example.brigus.pizzafinder2.R;
import com.example.brigus.pizzafinder2.SettingsActivity;
import com.example.brigus.pizzafinder2.adapters.PizzaLocationAdapterAdd;
import com.example.brigus.pizzafinder2.model.PizzaLocation;
import com.example.brigus.pizzafinder2.utils.AsyncResponse;
import com.example.brigus.pizzafinder2.utils.PermissionsManager;
import com.example.brigus.pizzafinder2.utils.PizzaLocationListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.brigus.pizzafinder2.utils.PermissionsManager.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;


public class FindNearPizzaFragment extends Fragment implements AsyncResponse {

    private int SETTINGS_UPDATED = 100;
    private AdView adView;
    @BindString(R.string.ad_unit_id) String AD_Unit_ID;
    private String Test_Hashed_Device_ID;
    private LocationManager mLocationManager;
    private PizzaLocationListener mLocationHelper;
    private Location mLocation;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.progressBarContainer) LinearLayout mProgressBarContainer;
    @BindView(R.id.about_fab) FloatingActionButton mAboutFAB;
    @BindView(R.id.find_cord_layout) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.cord_layout_contents) LinearLayout mCordLayoutContents;
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
            getFragmentManager().beginTransaction().add(mHeadlessSearchFragment, GoogleSearchFragment.TAG)
                    .commit();
        }

        if(mHeadlessSearchFragment != null) {
            mHeadlessSearchFragment.setDelegate(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_find_near_pizza, container, false);
        Activity activity = getActivity();
        mUnbinder = ButterKnife.bind(this, view);

        mLocationHelper = new PizzaLocationListener(mHeadlessSearchFragment.getWrappedAsyncTask());
        mLocationManager = (LocationManager) activity.getSystemService(Activity.LOCATION_SERVICE);
        getLastLocation();

        //Create Ad
        Test_Hashed_Device_ID = new AdvertisingIdClient.Info(AD_Unit_ID, false).getId();
        adView = new AdView(activity);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_Unit_ID);

        AdRequest.Builder adRequestBuilder = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(Test_Hashed_Device_ID);


        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.TOP);
        linearLayout.addView(adView);
        mCordLayoutContents.addView(linearLayout, 0);


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

        boolean hasPermission = PermissionsManager.hasPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);

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
        } else {
            this.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_rerun:
                mRecyclerView.setAdapter(null);
                mProgressBarContainer.setVisibility(View.VISIBLE);
                getLastLocation();
                break;
            case R.id.action_settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_UPDATED);
                break;
            case R.id.action_favorites:
                intent = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {

                    mProgressBarContainer.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.permission_error), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.settings), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.parse("package:" + getActivity().getPackageName()));
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });

                    snackbar.show();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTINGS_UPDATED && resultCode == RESULT_OK) {
            mRecyclerView.setAdapter(null);
            mProgressBarContainer.setVisibility(View.VISIBLE);
            mHeadlessSearchFragment.findNearby(mLocation);
        }
    }


    //update list.  This is the delegate method called from the async task and set on the task before it is run
    @Override
    public void processFinish(List<?> output) {

        if(output != null && !output.isEmpty()) {
            if (output.get(0) instanceof PizzaLocation) {

                PizzaLocationAdapterAdd locationAdapter = new PizzaLocationAdapterAdd(getActivity(), (List<PizzaLocation>) output);
                locationAdapter.getItemTouchHelper().attachToRecyclerView(mRecyclerView);

                //build the ui
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(locationAdapter);
                mProgressBarContainer.setVisibility(View.GONE);
            }
        } else {
            mProgressBarContainer.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(mCoordinatorLayout, R.string.no_locations, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
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
