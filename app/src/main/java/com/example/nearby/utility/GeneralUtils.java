package com.example.nearby.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.nearby.MyApplication;
import com.example.nearby.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * This class contains general utility functions that are reusable
 */

public class GeneralUtils {

    /*Building and showing alert dialog
    * */
    public static void showAlert(String title, String message,String okMessage, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, okMessage,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

   /* Hiding soft keyboard
   * */
    public static void hideKeyboard(@NonNull Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /*Show toast messages
    * */
    public static void showToast(Context mContext,String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    /*Check for the google API availability*/
    public static boolean checkGooglePlayServices(Activity context) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(context, result, 0).show();
            }
            return false;
        }
        return true;
    }
}
