package com.example.nearby.listActivities;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.nearby.adapter.CustomCursorAdapter;
import com.example.nearby.R;
import com.example.nearby.usefulClasses.MyDBHandler;

/*
*  Displays the favorite list stored in the database*/

public class FavoriteListActivity extends AppCompatActivity {
    private MyDBHandler dbHandler;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        dbHandler = MyDBHandler.getInstance();
        ListView favoriteList = (ListView) findViewById(R.id.list_of_places);

        //Initializing cursor adapter
        creatingAdapter();
        //Setting the favorite list into adapter
        favoriteList.setAdapter(adapter);

    }

    private void creatingAdapter(){
        Cursor cursor = dbHandler.readData();
        // Attach The Data From DataBase Into ListView Using Cursor Adapter
        String[] from = new String[]{MyDBHandler.COLUMN_PRODUCT_NAME,
                MyDBHandler.COLUMN_PHONE, MyDBHandler.COLUMN_IMAGE, MyDBHandler.COLUMN_RATING,
                MyDBHandler.COLUMN_RATING_IMAGE, MyDBHandler.COLUMN_REVIEW_COUNT};
        int[] to = new int[]{R.id.nameText, R.id.phoneText, R.id.hotelImageView, R.id.ratingText,
                R.id.urlImage, R.id.reviewCount};

        adapter = new CustomCursorAdapter(
                FavoriteListActivity.this, R.layout.list_item, cursor, from, to);

    }
}