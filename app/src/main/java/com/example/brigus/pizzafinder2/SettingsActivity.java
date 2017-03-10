package com.example.brigus.pizzafinder2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {


    public static String DEFAULT_RADIUS = "4000";
    public static String SHARED_PREFS_KEY = "com.example.brigus.pizzafinder2.sharedprefs";
    public static String DEFAULT_RADIUS_KEY = "com.example.brigus.pizzafinder2.defaultradiuskey";
    EditText editText;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editText = (EditText) findViewById(R.id.edittext);
        saveBtn =  (Button) findViewById(R.id.save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToSharedPrefs();
            }
        });

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        String currentVal = preferences.getString(DEFAULT_RADIUS_KEY, DEFAULT_RADIUS);

        editText.setText(currentVal);
    }


    private void saveToSharedPrefs() {

        String newVal = editText.getText().toString();

        if(isValidRadius(newVal)) {

            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(DEFAULT_RADIUS_KEY, newVal);
            editor.apply();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.warning_text);
            builder.setMessage(R.string.dialog_text);
            builder.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.create().show();

        }
    }


    private boolean isValidRadius(String val) {

        return !(val.equals("") || Integer.valueOf(val) > 50000);
    }
}
