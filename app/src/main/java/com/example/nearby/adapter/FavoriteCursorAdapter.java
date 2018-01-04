package com.example.nearby.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.nearby.R;
import com.example.nearby.usefulClasses.MyDBHandler;
import com.example.nearby.usefulClasses.ViewHolder;

/**
 * Created by Nandita Rai on 3/28/2017.
 */

public class FavoriteCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    ViewHolder holder;
    private MyDBHandler myDBHandler;

    public FavoriteCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        myDBHandler = MyDBHandler.getInstance();
        holder = new ViewHolder();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.list_item1, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        //get views in holder
        holder.getViews(view);
        //getting the data from database
        String name = cursor.getString(cursor.getColumnIndex("productName"));
        String location = cursor.getString(cursor.getColumnIndex("location"));
        String category = cursor.getString(cursor.getColumnIndex("category"));
        String phone = cursor.getString(cursor.getColumnIndex("phone"));
        String ratingImage = cursor.getString(cursor.getColumnIndex("ratingImage"));
        Integer integerReview = cursor.getInt(cursor.getColumnIndex("reviewCount"));
        String image = cursor.getString(cursor.getColumnIndex("image"));
        String detailUrl = cursor.getString((cursor.getColumnIndex("detailUrl")));

        //setting the views with data
        holder.setViews(mContext, name, location, category, phone, ratingImage, integerReview.toString(), image);
        holder.mFavoriteImg.setChecked(true);

        final int position = cursor.getPosition();

        holder.mFavoriteImg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    cursor.moveToPosition(position);
                    int itemId = cursor.getInt(cursor.getColumnIndex("_id"));
                    myDBHandler.deleteProductByID(itemId);
                    Cursor newCursor = myDBHandler.readData();
                    swapCursor(newCursor);
                    notifyDataSetChanged();
                }
            }
        });
    }


}

