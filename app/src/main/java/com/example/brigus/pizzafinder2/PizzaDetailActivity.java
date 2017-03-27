package com.example.brigus.pizzafinder2;

import android.support.v4.app.Fragment;
import com.example.brigus.pizzafinder2.fragments.PizzaDetailFragment;

public class PizzaDetailActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return PizzaDetailFragment.newInstance();
    }
}
