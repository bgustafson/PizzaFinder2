package com.example.brigus.pizzafinder2;

import android.support.v4.app.Fragment;
import com.example.brigus.pizzafinder2.fragments.SettingsFragment;

public class SettingsActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }

}
