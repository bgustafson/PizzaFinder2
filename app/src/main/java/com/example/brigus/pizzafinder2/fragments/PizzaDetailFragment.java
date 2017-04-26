package com.example.brigus.pizzafinder2.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.*;
import com.example.brigus.pizzafinder2.model.PizzaLocation;
import com.example.brigus.pizzafinder2.R;
import com.example.brigus.pizzafinder2.tasks.PhotosCallable;
import com.example.brigus.pizzafinder2.utils.AppUtilityFunctions;
import com.example.brigus.pizzafinder2.utils.PermissionsManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.brigus.pizzafinder2.utils.PermissionsManager.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;


public class PizzaDetailFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private String TAG = "PIZZA_DETAIL_ACTIVITY";
    private AdView adView;
    @BindString(R.string.ad_unit_id) String AD_Unit_ID;
    private String Test_Hashed_Device_ID;
    private PizzaLocation location;
    private String idNumber;
    private MapFragment map;
    private LatLng coordinates;
    private GoogleApiClient mGoogleApiClient;
    private ExecutorService mPool;
    private Future<HashMap<String, Bitmap>> mFuture;
    private int mPhotoIndex = 0;

    @BindView(R.id.location_name) TextView tvName;
    @BindView(R.id.location_address) TextView tvAddress;
    @BindView(R.id.location_rating) TextView tvRating;
    @BindView(R.id.location_price) TextView tvPrice;
    @BindView(R.id.location_phone) TextView tvPhone;
    @BindView(R.id.location_website) TextView tvWebSite;
    @BindView(R.id.photos) ImageButton imgBtnPhotos;
    @BindView(R.id.main_details_container) LinearLayout mainContainer;
    private Unbinder mUnbinder;


    public static PizzaDetailFragment newInstance() {
        return new PizzaDetailFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pizza_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        location = getActivity().getIntent().getParcelableExtra("location");
        coordinates = new LatLng(location.getGeometry().getLocation().getLat(), location.getGeometry().getLocation().getLng());

        idNumber = location.getId();

        //Photos: Callable, start running on a background thread
        mPool = Executors.newFixedThreadPool(1);
        Callable<HashMap<String, Bitmap>> photosCallable = new PhotosCallable(mGoogleApiClient, idNumber);
        mFuture = mPool.submit(photosCallable);

        //Name
        tvName.setText(location.getName());

        //Address
        tvAddress.setText(location.getAddress());

        //Rating
        tvRating.setText(location.getRating());

        //Price
        int price;
        try{
            price = Integer.parseInt(location.getPrice_level());
        } catch (NumberFormatException ex) {
            price = -1;
        }
        tvPrice.setText(AppUtilityFunctions.convertPrice(price));

        //Map
        map = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
        
        //Phone, Web Site,
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, idNumber)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {

                            if(places.getCount() > 0) {
                                final Place myPlace = places.get(0);
                                tvPhone.setText(myPlace.getPhoneNumber());
                                tvWebSite.setText(myPlace.getWebsiteUri().toString());
                                Log.i(TAG, "Place found: " + myPlace.getName());
                            }
                        }
                        places.release();
                    }
                });

        //Create Ad
        Test_Hashed_Device_ID = new AdvertisingIdClient.Info(AD_Unit_ID, false).getId();
        adView = new AdView(getActivity());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_Unit_ID);

        AdRequest.Builder adRequestBuilder = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(Test_Hashed_Device_ID);


        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.TOP);
        linearLayout.addView(adView);
        mainContainer.addView(linearLayout, 0);


        // Start loading the ad in the background.
        adView.loadAd(adRequestBuilder.build());

        return view;
    }


    @OnClick(R.id.photos)
    public void showPhotos() {
        try {

            if(!mFuture.isDone()) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.downloading_photos), Toast.LENGTH_SHORT).show();
                return;
            }

            final HashMap<String, Bitmap> photos = mFuture.get();
            if(photos.size() == 0 && mFuture.isDone()) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.no_photos), Toast.LENGTH_SHORT).show();
                return;
            }

            final Object[] keys = photos.keySet().toArray();
            String first = keys[mPhotoIndex].toString();
            Bitmap bitmap = photos.get(first);


            // custom dialog
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.image_dialog);
            dialog.setTitle(R.string.location_images);

            // set the custom dialog components - text, image and button
            TextView text = ButterKnife.findById(dialog, R.id.attribution_text);
            text.setText(first);
            ImageView image = ButterKnife.findById(dialog, R.id.image);
            image.setImageBitmap(bitmap);

            Button dialogButton = ButterKnife.findById(dialog, R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button nextButton = ButterKnife.findById(dialog, R.id.dialogButtonNext);
            // if button is clicked, close the custom dialog
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mPhotoIndex++;

                    if(mPhotoIndex > photos.size() - 1) {
                        mPhotoIndex = 0;
                    }

                    final Object[] keys = photos.keySet().toArray();
                    String key = keys[mPhotoIndex].toString();
                    Bitmap bitmap = photos.get(key);

                    // set the custom dialog components - text, image and button
                    TextView text = ButterKnife.findById(dialog, R.id.attribution_text);
                    text.setText(key);
                    ImageView image = ButterKnife.findById(dialog, R.id.image);
                    image.setImageBitmap(bitmap);
                }
            });

            dialog.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        mPool.shutdown();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Move the camera instantly to hamburg with a zoom of 15.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));

        //Add Marker
        MarkerOptions markerOptions = new MarkerOptions().position(coordinates)
                .title(location.getName())
                .visible(true);
        Marker marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

        boolean hasPermission = PermissionsManager.hasPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);

        if(hasPermission) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else {
            this.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
                break;
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, connectionResult.toString());
    }


    @Override
    public void onConnected(Bundle bundle) {

        Log.d(TAG, "Connected");
    }


    @Override
    public void onConnectionSuspended(int i) {

        Log.d(TAG, "Connection suspended");
    }
}
