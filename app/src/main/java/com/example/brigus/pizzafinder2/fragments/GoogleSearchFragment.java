package com.example.brigus.pizzafinder2.fragments;


import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.example.brigus.pizzafinder2.Tasks.GoogleNearbySearchTask;
import com.example.brigus.pizzafinder2.utils.AsyncResponse;

public class GoogleSearchFragment extends Fragment {

    private AsyncResponse mDelegate = null;
    private GoogleNearbySearchTask mAsyncTask = new GoogleNearbySearchTask(mDelegate);

    public static final String TAG = "NEARBY_GOOGLE_SEARCH_FRAGMENT";

    public GoogleSearchFragment() {

    }

    public void setDelegate(AsyncResponse delegate){
        this.mDelegate = delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Will stick around even after a rotation
        setRetainInstance(true);
    }


    public void findNearby(Location location) {
        if(mAsyncTask.getStatus() == AsyncTask.Status.RUNNING) return;
        if(mAsyncTask.getStatus() == AsyncTask.Status.FINISHED)
            mAsyncTask = new GoogleNearbySearchTask(mDelegate);
        mAsyncTask.execute(location);
    }


    public void stopSearch() {
        if(mAsyncTask.getStatus() != AsyncTask.Status.RUNNING) return;
        mAsyncTask.cancel(true);
    }
}
