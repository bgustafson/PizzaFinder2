package com.example.brigus.pizzafinder2.Tasks;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;


import java.util.HashMap;
import java.util.concurrent.Callable;



public class PhotosCallable implements Callable, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private String idNumber;
    private GoogleApiClient mGoogleApiClient;

    public PhotosCallable(GoogleApiClient googleApiClient, String id) {

        this.idNumber = id;
        this.mGoogleApiClient = googleApiClient;
    }


    @Override
    public HashMap<String, Bitmap> call() throws Exception {

        HashMap<String, Bitmap> photos = new HashMap();

        final PlacePhotoMetadataResult result = Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, idNumber).await();

        if (result.getStatus().isSuccess()) {


            PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();

            for(PlacePhotoMetadata photo : photoMetadataBuffer) {
                // Get a full-size bitmap for the photo.
                Bitmap image = photo.getPhoto(mGoogleApiClient).await().getBitmap();
                // Get the attribution text.
                CharSequence attribution = photo.getAttributions();

                //Add to HashMap
                photos.put(attribution.toString(), image);
            }

            photoMetadataBuffer.release();

        }

        return photos;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
