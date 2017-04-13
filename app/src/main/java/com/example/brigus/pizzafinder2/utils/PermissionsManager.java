package com.example.brigus.pizzafinder2.utils;

import android.app.Activity;


public class PermissionsManager {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;


    /*public static boolean checkForPermissions(Activity activity, String permission, int requestReturnCode) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[] { permission },
                        requestReturnCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            return false;

        } else {
            return true;
        }
    }*/


    public static boolean hasPermission(Activity activity, String permission) {
        return activity.checkSelfPermission(permission) == 0;
    }
}
