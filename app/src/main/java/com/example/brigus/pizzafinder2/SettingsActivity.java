package com.example.brigus.pizzafinder2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.brigus.pizzafinder2.fragments.SettingsFragment;

public class SettingsActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }

}
