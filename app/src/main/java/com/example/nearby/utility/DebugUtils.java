package com.example.nearby.utility;


import android.util.Log;

import com.example.nearby.BuildConfig;

/**
 * Utility class for debug messages
 */

public class DebugUtils {

    public static void d(String tag, String msg){
        if(BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg);
        }
    }

}
