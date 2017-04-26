package com.example.brigus.pizzafinder2;

import android.support.v4.app.Fragment;
import com.example.brigus.pizzafinder2.fragments.FavoritesFragment;

public class FavoritesActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return FavoritesFragment.newInstance();
    }

}
