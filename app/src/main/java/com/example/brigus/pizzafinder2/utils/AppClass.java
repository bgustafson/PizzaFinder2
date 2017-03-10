package com.example.brigus.pizzafinder2.utils;

import android.app.Application;
import android.content.Context;


public class AppClass extends Application {

    private static Context mContext = null;
    private static AppClass mInstance = null;


    @Override
    public void onCreate(){

        super.onCreate();

        mInstance = this;
        mContext = this;
    }


    public static Application getInstance() {

        if(mInstance == null) {
            throw new IllegalStateException();
        }

        return mInstance;
    }

    public static Context getContext() {
        return mContext;
    }

}
