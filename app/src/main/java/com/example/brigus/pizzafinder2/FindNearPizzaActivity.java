package com.example.brigus.pizzafinder2;

import android.support.v4.app.Fragment;
import com.example.brigus.pizzafinder2.fragments.FindNearPizzaFragment;


public class FindNearPizzaActivity extends SingleFragmentActivity {

    //TODO: Using GSON and Retrofit2 Library

    @Override
    protected Fragment createFragment() {
        return FindNearPizzaFragment.newInstance();
    }

}
