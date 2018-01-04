package com.example.nearby.listActivities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.nearby.adapter.CustomAdapter;
import com.example.nearby.MyApplication;
import com.example.nearby.R;
import com.example.nearby.usefulClasses.MyDBHandler;
import com.example.nearby.usefulClasses.Product;
import com.example.nearby.usefulClasses.PushNotification;
import com.example.nearby.utility.DebugUtils;
import com.yelp.clientlib.entities.Business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
* This activity displays the searched places in the list and allows
* adding items to favorite list on long click events
* */

public class PlaceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private ArrayList<Business> b;
    private List<Product> products;
    private MyDBHandler mDbHandler;
    private int mNotificationId = 1;
    private Bitmap mBitmapImage;
    public static final String sPlaceKey = "place list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        listView = (ListView)findViewById(R.id.list_of_places);
        mDbHandler = MyDBHandler.getInstance();
        products = new ArrayList<Product>();

        b = (ArrayList<Business>)getIntent().getSerializableExtra(sPlaceKey);

        DebugUtils.d("Result", b.toString() + " ");

        setProducts(b);
        storingImage(b);

       // mCustomAdapter = new CustomAdapter(getApplicationContext(),products );
        CustomAdapter mCustomAdapter = new CustomAdapter(MyApplication.getContext(), products);
        listView.setAdapter(mCustomAdapter);

        //Filtering the places on basis of ratings
        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Collections.sort(products, new Comparator<Product>() {
                    @Override
                    public int compare(Product lhs, Product rhs) {
                        return lhs.getRating().compareTo(rhs.getRating());
                    }
                });
            }
        });

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    /* Adding data to the array list*/
    private void setProducts(ArrayList<Business> business) {
        if(business != null) {
            for (int i = 0; i < business.size(); i++) {
                Product product = new Product(b.get(i).id(), b.get(i).name(), b.get(i).imageUrl(),
                        b.get(i).rating(), b.get(i).displayPhone(), b.get(i).ratingImgUrlLarge(),
                        b.get(i).reviewCount());
                products.add(product);
            }
        }else{
            DebugUtils.d("setProducts",getString(R.string.null_pointer_exception));
        }
    }


    /*Storing image in file*/
    private void storingImage(ArrayList<Business> business) {
        if (business != null) {
            for (Product product : products) {
                mBitmapImage = gettingBitmap(product.getUrl());
                saveToInternalStorage(mBitmapImage, product.getName());
            }
        }else{
            DebugUtils.d("storingImage",getString(R.string.null_pointer_exception));
        }
    }


    /*Saving image to internal storage for future access*/
    private String saveToInternalStorage(Bitmap bitmapImage,String name){
        ContextWrapper cw = new ContextWrapper(MyApplication.getContext());
        // path to /data/data/nearby/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        name = name + ".jpg";
        File myPath=new File(directory,name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
        // Using compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    /*Getting bitmap from url*/
    private Bitmap gettingBitmap(String url) {
        if (url != null) {
            final String imageUrl = url;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(imageUrl);
                        mBitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        DebugUtils.e("Throwable Error", e.getMessage());
                    }

                }
            }).start();
            return mBitmapImage;
        }else {
            DebugUtils.d("gettingBitmap",getString(R.string.null_pointer_exception));
           return null;
        }
    }


    /*Defining on click event to add the item to the favorite list*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView button = (ImageView) view.findViewById(R.id.imageButton_favorite);
        // Check the bookmark icon marked or unmarked
        if (button.getTag().toString().equalsIgnoreCase(getString(R.string.unselected))) {
        mDbHandler.addProduct(products.get(position));
        button.setTag(getString(R.string.selected));
        button.setImageResource(R.drawable.golden_star);
        new PushNotification(getString(R.string.add_favorite),position,products, mNotificationId++);
        } else {
        mDbHandler.deleteProduct(products.get(position));
        button.setTag(getString(R.string.unselected));
        button.setImageResource(R.drawable.gray_star);
        new PushNotification(getString(R.string.remove_favorite),position,products, mNotificationId++);
       }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return true;
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
                Intent i = new Intent(PlaceListActivity.this,FavoriteListActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}