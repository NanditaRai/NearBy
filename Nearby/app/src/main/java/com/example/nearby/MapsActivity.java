package com.example.nearby;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.nearby.listActivities.FavoriteListActivity;
import com.example.nearby.listActivities.PlaceListActivity;
import com.example.nearby.utility.DebugUtils;
import com.example.nearby.utility.GeneralUtils;
import com.example.nearby.utility.Permissions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.nearby.listActivities.PlaceListActivity.sPlaceKey;
import static com.example.nearby.DetailActivity.sDetailKey;
import static com.example.nearby.utility.Permissions.checkLocationPermission;


/**
 * Main activity which shows the current location and show the markers
 * on nearby restaurants
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ApiConstants {

    private GoogleMap mMap;
    private Map<Marker, Business> allMarkersMap;
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrLocationMarker;
    private SearchResponse mSearchResponse;
    private YelpAPI yelpAPI;
    private Boolean misSearchButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        YelpAPIFactory apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET,
                TOKEN, TOKEN_SECRET);
        yelpAPI = apiFactory.createAPI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission(this);
        }
        //Check Google Play Services Availability
        if (!checkGooglePlayServices()) {
            DebugUtils.d("MapsActivity onCreate",
                    getString(R.string.gps_not_available));
            finish();
        } else {
            DebugUtils.d("MapsActivity onCreate",
                    getString(R.string.gps_available));
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    // Check for the google API availability
    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 0).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            allMarkersMap = new HashMap<Marker, Business>();

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
            //Button to search the location is clicked
            findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchButtonClicked();
                }
            });
            // Button to see the searched places on the list is clicked
            findViewById(R.id.listButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListButtonClicked();
                }
            });
            // Button to see the favorite list items
            findViewById(R.id.favoriteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavoriteListButtonClicked();
                }
            });
        }else{
            DebugUtils.d("onMapReady",getString(R.string.null_pointer_exception));
        }
    }


   // Search Button Clicked
    private void SearchButtonClicked() {
        misSearchButtonClicked = true;
        DebugUtils.d("onClick", getString(R.string.button_clicked));

        // Hiding Soft Keyboard
        GeneralUtils.hideKeyboard(MapsActivity.this);

        EditText locationSearch = (EditText) findViewById(R.id.searchText);
        String mLocation = locationSearch.getText().toString();
        Map<String, String> param = new HashMap<>();

        // general params
        param.put(YELP_TERM_KEY, YELP_TERM_VALUE);
        param.put(YELP_LIMIT_KEY, YELP_LIMIT_VALUE);
        Call<SearchResponse> call = yelpAPI.search(mLocation, param);
        call.enqueue(yelpAPICallback);
    }


   // List Button Clicked
    private void ListButtonClicked() {
        Resources resources = getResources();
        if (misSearchButtonClicked == false) {
            GeneralUtils.showAlert(resources.getString(R.string.no_list_items),
                    resources.getString(R.string.no_list_msg),getResources().getString(R.string.ok),
                    MapsActivity.this);
        } else {
            Intent i = new Intent(this, PlaceListActivity.class);
            i.putExtra(sPlaceKey, mSearchResponse.businesses());
            startActivity(i);
        }
    }


    // Favorite List Button Clicked
    private void FavoriteListButtonClicked() {
        Intent i = new Intent(this, FavoriteListActivity.class);
        startActivity(i);
    }


    /*Building Google API Client*/
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    /* Getting the search response*/
    final private Callback<SearchResponse> yelpAPICallback = new Callback<SearchResponse>() {
        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            if(response.isSuccessful()){
            mSearchResponse = response.body();
            showNearbyPlaces(mSearchResponse.businesses());
            }else{
                //For getting error message
                Log.d("Error message",response.message());
                GeneralUtils.showToast(MyApplication.getContext(),response.message());
                //For getting error code. Code is integer value like 200,404 etc
                Log.d("Error code",String.valueOf(response.code()));
            }
        }
        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            // HTTP error happened, do something to handle it.
            GeneralUtils.showToast(MyApplication.getContext(),t.getMessage());
            Log.d("Error ",t.getMessage());
        }
    };


    /*Marking the places on the map*/
    private void showNearbyPlaces(List<Business> nearbyPlacesList) {
        if (nearbyPlacesList != null) {
            for (int i = 0; i < nearbyPlacesList.size(); i++) {
                final Business business = nearbyPlacesList.get(i);
                double lat = business.location().coordinate().latitude();
                double lng = business.location().coordinate().longitude();
                String placeName = business.name();

                MarkerOptions markerOptions = createMarker(lat, lng, placeName);

                Marker marker = mMap.addMarker(markerOptions);
                allMarkersMap.put(marker, business);
                mMap.setOnInfoWindowClickListener(this);
            }
        }else{
            DebugUtils.d("showNearbyPlaces",getString(R.string.null_pointer_exception));
        }
    }


    /*Showing details on clicking the info window of the marker*/
    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker != null) {
            Business business = allMarkersMap.get(marker);
            Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
            intent.putExtra(sDetailKey, business);
            startActivity(intent);
        }else{
            DebugUtils.d("onInfoWindowClick",getString(R.string.null_pointer_exception));
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }


    /* Updating marker with change in location*/
    @Override
    public void onLocationChanged(Location location) {

        DebugUtils.d("onLocationChanged", "Entered");
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //Creating marker
        MarkerOptions markerOptions = createMarker(latitude, longitude,getString(R.string.current_position));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        DebugUtils.d("onLocationChanged",
                String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        //stop location updates
        stopLocationUpdates();
        DebugUtils.d("onLocationChanged", "Exit");
    }


    /*Create and positioning the marker according to latitude and longitude*/
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


    /*Stopping the location updates*/
    private boolean stopLocationUpdates(){
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            DebugUtils.d("onLocationChanged", "Removing Location Updates");
            return true;
        }else{
            return false;
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        boolean result;
        result = Permissions.permissionResult(requestCode, grantResults);
        if (result) {
            // permission was granted. Do the
            // contacts-related task you need to do.
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (mGoogleApiClient == null) {
                    buildGoogleApiClient();
                }
                mMap.setMyLocationEnabled(true);
            }
        }else{
            // Permission denied, Disable the functionality that depends on this permission.
            GeneralUtils.showToast(MyApplication.getContext(), getString(R.string.permission_denied));
        }
    }
    
}

