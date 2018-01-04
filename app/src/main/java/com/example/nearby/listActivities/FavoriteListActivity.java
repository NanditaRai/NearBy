package com.example.nearby.listActivities;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nearby.R;
import com.example.nearby.adapter.FavoriteCursorAdapter;
import com.example.nearby.usefulClasses.MyDBHandler;

/*
*  Displays the favorite list stored in the database
*  */

public class FavoriteListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private MyDBHandler mDbHandler;
    private FavoriteCursorAdapter mAdapter;
    private Cursor mCursor;
    String detailUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        mDbHandler = MyDBHandler.getInstance();
        final ListView favoriteList = (ListView) findViewById(R.id.list_of_places);
        mCursor = mDbHandler.readData();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mAdapter = new FavoriteCursorAdapter(
                        FavoriteListActivity.this,
                        mCursor,
                        0);

                favoriteList.setAdapter(mAdapter);
            }
        });

        favoriteList.setOnItemClickListener(this);

        //Showing back button on the toolbar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               // NavUtils.navigateUpFromSameTask(this);
                super.onBackPressed();
                return true;
            case R.id.action_delete:
                mDbHandler.deleteAllProducts();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // getting the cursor at clicked item position in the database
        Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
        detailUrl = cursor.getString(cursor.getColumnIndex("detailUrl"));
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(detailUrl));
        startActivity(i);
    }

}