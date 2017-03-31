package com.example.brigus.pizzafinder2.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.brigus.pizzafinder2.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    public static String DEFAULT_RADIUS = "4000";
    public static String SHARED_PREFS_KEY = "com.example.brigus.pizzafinder2.sharedprefs";
    public static String DEFAULT_RADIUS_KEY = "com.example.brigus.pizzafinder2.defaultradiuskey";
    @BindView(R.id.edittext) EditText mEditText;
    @BindView(R.id.save_button) Button mSaveBtn;


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        String currentVal = preferences.getString(DEFAULT_RADIUS_KEY, DEFAULT_RADIUS);

        mEditText.setText(currentVal);

        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event)
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getActivity().setResult(RESULT_CANCELED, new Intent());
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        } );

        return view;
    }


    @OnClick(R.id.save_button)
    private void saveToSharedPrefs() {

        String newVal = mEditText.getText().toString();

        if(isValidRadius(newVal)) {

            SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(DEFAULT_RADIUS_KEY, newVal);
            editor.apply();

            Intent intent = new Intent();
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
