package com.example.nearby;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.nearby.utility.GeneralUtils;

import static com.example.nearby.MapsActivity.YELP_TERM_KEY;

/**
 * This is the fron page of the app which gives user options to select category of places
 * Created by Nandita Rai on 3/19/2017.
 */

public class FrontPage extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);

        final Intent intent = new Intent(this, MapsActivity.class);

        findViewById(R.id.restaurants).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(YELP_TERM_KEY, "restaurants");
                CheckInternetConnection(intent);
            }
        });

        findViewById(R.id.cafe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(YELP_TERM_KEY, "cafe");
                CheckInternetConnection(intent);
            }
        });

        findViewById(R.id.bars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(YELP_TERM_KEY,"bars");
                CheckInternetConnection(intent);
            }
        });

        findViewById(R.id.night_clubs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(YELP_TERM_KEY, "night clubs");
                CheckInternetConnection(intent);
            }
        });

        findViewById(R.id.theater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(YELP_TERM_KEY, "movie theater");
                CheckInternetConnection(intent);
            }
        });

        findViewById(R.id.shopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(YELP_TERM_KEY, "shopping malls");
                CheckInternetConnection(intent);
            }
        });

        findViewById(R.id.hospitals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(YELP_TERM_KEY, "hospitals");
                CheckInternetConnection(intent);
            }
        });

        findViewById(R.id.banks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(YELP_TERM_KEY, "banks");
                CheckInternetConnection(intent);
            }
        });

        findViewById(R.id.airports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(YELP_TERM_KEY, "airports");
                CheckInternetConnection(intent);
            }
        });
    }

    /*
     Check for the internet connection using connectivity manager
     */
    private void CheckInternetConnection(Intent intent) {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
              startActivity(intent);
        } else {
            GeneralUtils.showToast(MyApplication.getContext(),getString(R.string.internet));
        }
    }
}
