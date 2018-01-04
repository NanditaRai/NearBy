package com.example.nearby.utility;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.example.nearby.MyApplication;

/**
 * Class which handles the permissions requested
 */

public class Permissions {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    /*Check for the location permission
    * */
    public static boolean checkLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    /*Get the permission result whether granted or not
    * */
    public static final boolean permissionResult(int requestCode, int[] grantResults) {
        boolean result = false;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    result = true;            //Permission granted
                  } else {
                        result = false;       //Permission denied
                }
        }
        return result;
    }
}




