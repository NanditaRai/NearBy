package com.example.nearby.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.example.nearby.R;
import com.example.nearby.usefulClasses.ViewHolder;


public class CustomCursorAdapter extends SimpleCursorAdapter {

    private final Cursor cursor;
    private final Context context;
    private LayoutInflater mInflater;

    public CustomCursorAdapter(Context applicationContext, int layout, Cursor cursor,
                               String[] from, int[] to) {
        super(applicationContext, R.layout.list_item, cursor, from, to);
        this.cursor = cursor;
        this.context = applicationContext;
        mInflater = LayoutInflater.from(applicationContext);
    }


    public View getView(int position, View inView, ViewGroup parent) {
        ViewHolder holder ;
        if (inView == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            inView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            //get views in holder
            holder.getViews(inView);
            inView.setTag(holder);
        } else {
            holder = (ViewHolder) inView.getTag();
        }


        this.cursor.moveToPosition(position);
        Double doubleRating = this.cursor.getDouble(this.cursor.getColumnIndex("rating"));
        Integer integerReview = this.cursor.getInt(this.cursor.getColumnIndex("reviewCount")) ;
        //set views in holder
        holder.setViews(context,
                this.cursor.getString(this.cursor.getColumnIndex("productName")),
                doubleRating.toString(),
                this.cursor.getString(this.cursor.getColumnIndex("phone")),
                this.cursor.getString(this.cursor.getColumnIndex("ratingImage")),
                integerReview.toString(),
                this.cursor.getString(this.cursor.getColumnIndex("image")));
        return inView ;
    }

}
