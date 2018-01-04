package com.example.nearby;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nearby.utility.DebugUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Category;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    public static final String sDetailKey = "detail";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Business mBusiness;

    private ImageView mImage, mRatingImage, mSnippetImage;
    private TextView mNameText,mRatingText,mPhoneText,mReviewCount,mAddress,mSnippetText, mCategory;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail1);

        getSupportActionBar().hide();

        initializeViews();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Getting the data passed through intent
        mBusiness = (Business) getIntent().getSerializableExtra(sDetailKey);

        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        setViews();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    private void bindActivity() {
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    /*
      Initialize the views in detail activity
     */
    private void initializeViews() {
        mImage = (ImageView) findViewById(R.id.dImage);
        mNameText = (TextView) findViewById(R.id.dNameText);
        mRatingText = (TextView) findViewById(R.id.dRatingText);
        mPhoneText = (TextView) findViewById(R.id.dPhoneText);
        mRatingImage = (ImageView) findViewById(R.id.dRatingImage);
        mReviewCount = (TextView) findViewById(R.id.dReviewCount);
        mAddress = (TextView) findViewById(R.id.dAddress);
        mSnippetText = (TextView) findViewById(R.id.snippet_text);
        mSnippetImage = (ImageView) findViewById(R.id.snippet_image);
        mCategory = (TextView) findViewById(R.id.categories);
    }

    /*
    Create marker at the selected place location in the map
     */
    private MarkerOptions createMarker(double lat, double lng, String placeName) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(lat, lng);
        markerOptions.position(latLng);
        markerOptions.title(placeName);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        return markerOptions;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            MarkerOptions markerOptions = createMarker(mBusiness.location().coordinate().latitude(),
                    mBusiness.location().coordinate().longitude(), mBusiness.name() );

            //Initialize Google Play Services
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }

            mMap.addMarker(markerOptions);

        } else {
            DebugUtils.d("onMapReady", getString(R.string.null_pointer_exception));
        }
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected( Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, (LocationListener) this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    /*
    Setting the views with the data received of the selected place as an intent
     */
    private void setViews() {
        Picasso.with(this)
                .load(mBusiness.imageUrl())
                .into(mImage);

        if(mBusiness.name() != null){
            mNameText.setText(mBusiness.name());
        }else mNameText.setText(MyApplication.getContext().getString(R.string.nullValue));

        if(mBusiness.rating() != null) {
            mRatingText.setText(getString(R.string.rating_text, mBusiness.rating().toString()));
        }else mRatingText.setText(MyApplication.getContext().getString(R.string.no_ratings));

        if(mBusiness.displayPhone() != null) {
            mPhoneText.setText(getString(R.string.phone_text, mBusiness.displayPhone()));
        }else mPhoneText.setText(MyApplication.getContext().getString(R.string.nullValue));

        Picasso.with(this)
                .load(mBusiness.ratingImgUrlLarge())
                .into(mRatingImage);

        mReviewCount.setText(getString(R.string.review_count, mBusiness.reviewCount().toString()));
        if(mBusiness.reviewCount() != null){
            mReviewCount.setText(MyApplication.getContext().getString(R.string.review_count, mBusiness.reviewCount().toString()));
        }else mReviewCount.setText(MyApplication.getContext().getString(R.string.no_reviews));

        if(mBusiness.location().displayAddress() != null){
            StringBuilder builder = new StringBuilder();
            for (String address : mBusiness.location().displayAddress()) {
                builder.append(address).append(", ");
            }
            mAddress.setText(getString(R.string.location_text,builder.toString()));
        }else mAddress.setText(MyApplication.getContext().getString(R.string.nullValue));

       if(mBusiness.name() != null){
           mTitle.setText(mBusiness.name());
       }else mTitle.setText(MyApplication.getContext().getString(R.string.nullValue));

        if(mBusiness.snippetText() != null){
            mSnippetText.setText(mBusiness.snippetText());
        }else
            mSnippetText.setText(MyApplication.getContext().getString(R.string.nullValue));

        Picasso.with(this)
                .load(mBusiness.snippetImageUrl())
                .into(mSnippetImage);


        ArrayList<Category> categories = mBusiness.categories();
        if(categories != null)    mCategory.setText(categories.get(0).name());
        else mCategory.setText(MyApplication.getContext().getString(R.string.nullValue));

    }


    @Override
    protected void onDestroy() {
        mMap.clear();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(mBusiness.url()));
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}