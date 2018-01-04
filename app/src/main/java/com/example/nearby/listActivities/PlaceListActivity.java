package com.example.nearby.listActivities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nearby.DetailActivity;
import com.example.nearby.MyApplication;
import com.example.nearby.R;
import com.example.nearby.adapter.CustomAdapter;
import com.example.nearby.usefulClasses.MyDBHandler;
import com.example.nearby.usefulClasses.Product;
import com.example.nearby.utility.DebugUtils;
import com.example.nearby.utility.GeneralUtils;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.nearby.DetailActivity.sDetailKey;

/*
* This activity displays the searched places in the list and allows
* adding items to favorite list on long click events
* */

public class PlaceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Business> mNearbyPlaceList;
    private MyDBHandler mDbHandler;
    public static final String sPlaceKey = "place list";

    ListView mListView;
    CustomAdapter mCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        mListView = (ListView) findViewById(R.id.list_of_places);
        mDbHandler = MyDBHandler.getInstance();

        mNearbyPlaceList = (ArrayList<Business>) getIntent().getSerializableExtra(sPlaceKey);
        DebugUtils.d("Result", mNearbyPlaceList.toString() + " ");

        setProducts(mNearbyPlaceList);
        mListView.setOnItemClickListener(this);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    /* Adding data to the array list*/
    private void setProducts(List<Business> business) {
        List<Product> products = new ArrayList<>();
        if (business != null) {
            for (int i = 0; i < business.size(); i++) {
            //for (Business b: business){
                ArrayList<Category> categories = mNearbyPlaceList.get(i).categories();
                Product product = new Product(mNearbyPlaceList.get(i).id(), mNearbyPlaceList.get(i).name(), mNearbyPlaceList.get(i).imageUrl(),
                        mNearbyPlaceList.get(i).rating(), mNearbyPlaceList.get(i).displayPhone(),
                        mNearbyPlaceList.get(i).ratingImgUrlLarge(), mNearbyPlaceList.get(i).reviewCount(),
                        mNearbyPlaceList.get(i).location().city(), mNearbyPlaceList.get(i).location().countryCode(),
                        categories.get(0).name(), mNearbyPlaceList.get(i).url());
                products.add(product);
            }
            mCustomAdapter = new CustomAdapter(MyApplication.getContext(), products);
            mListView.setAdapter(mCustomAdapter);
        } else {
            DebugUtils.d("setProducts", getString(R.string.null_pointer_exception));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Open the detail activity of the selected item
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(sDetailKey, (Serializable) mNearbyPlaceList.get(position));
        if(mNearbyPlaceList.get(position) != null)
        startActivity(intent);
        else
            GeneralUtils.showToast(MyApplication.getContext(),getString(R.string.no_detail_to_show));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorites:
                Intent i = new Intent(PlaceListActivity.this, FavoriteListActivity.class);
                startActivity(i);
                return true;
            case R.id.sort_filter:
                mCustomAdapter.sort();
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onRestart() {
        mCustomAdapter.notifyDataSetChanged();
        super.onRestart();
    }
}