package com.example.nearby;

import android.app.Application;
import android.content.Context;

/*This class holds the context for the whole application*/

public class MyApplication extends Application {
  private static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext(){
        return sContext;
    }
}
