package com.example.brigus.pizzafinder2.fragments;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.brigus.pizzafinder2.R;
import com.example.brigus.pizzafinder2.adapters.PizzaLocationAdapterRemove;
import com.example.brigus.pizzafinder2.database.PizzaDB;
import com.example.brigus.pizzafinder2.database.PizzaLocationDbHelpers;
import com.example.brigus.pizzafinder2.model.PizzaLocation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.util.List;

public class FavoritesFragment extends Fragment {

    @BindView(R.id.favorites_fragment) LinearLayout mContainerLayout;
    @BindString(R.string.ad_unit_id) String AD_Unit_ID;
    private String Test_Hashed_Device_ID;
    private AdView adView;

    @BindView(R.id.favorites_recycler_view) RecyclerView mRecyclerView;
    private Unbinder mUnbinder;


    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        Activity activity = getActivity();
        mUnbinder = ButterKnife.bind(this, view);

        final SQLiteDatabase db = new PizzaDB(getActivity()).getReadableDatabase();
        List<PizzaLocation> locations = PizzaLocationDbHelpers.getLocations(db);
        PizzaLocationAdapterRemove locationAdapter = new PizzaLocationAdapterRemove(getActivity(), locations);
        locationAdapter.getItemTouchHelper().attachToRecyclerView(mRecyclerView);

        //build the ui
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(locationAdapter);

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
        mContainerLayout.addView(linearLayout, 0);

        // Start loading the ad in the background.
        adView.loadAd(adRequestBuilder.build());

        return view;
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

        super.onDestroy();
    }
}
