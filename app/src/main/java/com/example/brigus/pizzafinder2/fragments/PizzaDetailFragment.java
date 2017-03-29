package com.example.brigus.pizzafinder2.fragments;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.brigus.pizzafinder2.Model.PizzaLocation;
import com.example.brigus.pizzafinder2.R;
import com.example.brigus.pizzafinder2.Tasks.PhotosCallable;
import com.example.brigus.pizzafinder2.utils.AppUtilityFunctions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
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
import static com.example.brigus.pizzafinder2.utils.PermissionsManager.checkForPermissions;


public class PizzaDetailFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private String TAG = "PIZZA_DETAIL_ACTIVITY";
    private AdView adView;
    private static final String AD_Unit_ID = "ca-app-pub-3892677699343303/2379904475";
    private String Test_Hashed_Device_ID;
    private PizzaLocation location;
    private TextView tvName, tvAddress, tvRating, tvPrice, tvPhone, tvWebSite;
    ImageButton imgBtnPhotos;
    private LinearLayout mainContainer;
    private String idNumber;
    private MapFragment map;
    private LatLng coordinates;
    private GoogleApiClient mGoogleApiClient;
    ExecutorService mPool;
    private Future<HashMap<String, Bitmap>> mFuture;
    private int mPhotoIndex = 0;


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

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mainContainer = (LinearLayout) view.findViewById(R.id.main_details_container);
        location = getActivity().getIntent().getParcelableExtra("location");
        coordinates = new LatLng(location.getLocation().getLatitude(), location.getLocation().getLongitude());

        idNumber = location.getId();

        //Photos: Callable, start running on a background thread
        mPool = Executors.newFixedThreadPool(1);
        Callable<HashMap<String, Bitmap>> photosCallable = new PhotosCallable(mGoogleApiClient, idNumber);
        mFuture = mPool.submit(photosCallable);

        //Name
        tvName = (TextView) view.findViewById(R.id.location_name);
        tvName.setText(location.getName());

        //Address
        tvAddress = (TextView) view.findViewById(R.id.location_address);
        tvAddress.setText(location.getAddress());

        //Rating
        tvRating = (TextView) view.findViewById(R.id.location_rating);
        tvRating.setText(location.getRating());

        //Price
        tvPrice = (TextView) view.findViewById(R.id.location_price);
        int price;
        try{
            price = Integer.parseInt(location.getPrice_level());
        } catch (NumberFormatException ex) {
            price = -1;
        }
        tvPrice.setText(AppUtilityFunctions.convertPrice(price));

        //Phone and WebSite
        tvPhone = (TextView) view.findViewById(R.id.location_phone);
        tvWebSite = (TextView) view.findViewById(R.id.location_website);

        //Map
        map = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        //Photos
        imgBtnPhotos = (ImageButton) view.findViewById(R.id.photos);
        imgBtnPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    TextView text = (TextView) dialog.findViewById(R.id.attribution_text);
                    text.setText(first);
                    ImageView image = (ImageView) dialog.findViewById(R.id.image);
                    image.setImageBitmap(bitmap);

                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    Button nextButton = (Button) dialog.findViewById(R.id.dialogButtonNext);
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
                            TextView text = (TextView) dialog.findViewById(R.id.attribution_text);
                            text.setText(key);
                            ImageView image = (ImageView) dialog.findViewById(R.id.image);
                            image.setImageBitmap(bitmap);
                        }
                    });

                    dialog.show();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


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
        Test_Hashed_Device_ID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
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
    public void onMapReady(GoogleMap googleMap) {

        // Move the camera instantly to hamburg with a zoom of 15.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));

        //Add Marker
        MarkerOptions markerOptions = new MarkerOptions().position(coordinates)
                .title(location.getName())
                .visible(true);
        Marker marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

        boolean hasPermission = checkForPermissions(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        if(hasPermission) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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
