package com.example.nearby.usefulClasses;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.nearby.MyApplication;
import com.example.nearby.R;
import com.squareup.picasso.Picasso;

/**
 * Hold the view of the item in the list
 */

public class ViewHolder {

    private TextView mNameText;
    private TextView mCityText;
    private TextView mCategoryText;
    private TextView mPhoneText;
    private ImageView mRatingImage;
    private TextView mReviewCount;
    private ImageView mHotelImage;
    public CheckBox mFavoriteImg;

    /*Getting views*/
    public void getViews(View view){
        mNameText = (TextView)view.findViewById(R.id.nameText);
        mCityText = (TextView) view.findViewById(R.id.locationText);
        mPhoneText = (TextView) view.findViewById(R.id.phoneText);
        mRatingImage = (ImageView) view.findViewById(R.id.urlImage) ;
        mReviewCount = (TextView) view.findViewById(R.id.reviewCount);
        mHotelImage = (ImageView) view.findViewById(R.id.hotelImageView);
        mFavoriteImg = (CheckBox) view.findViewById(R.id.imageButton_favorite);
        mCategoryText = (TextView) view.findViewById(R.id.categoryText);
        
    }

    /*Setting views*/
    public void setViews(Context context, String name, String City, String category, String phone,
                         String ratingImage, String reviewCount, String image){

        Picasso.with(context)
                .load(image)
                .into(mHotelImage);

        if(name != null){
            mNameText.setText(name);
        }else mNameText.setText(MyApplication.getContext().getString(R.string.nullValue));

        if(name != null){
            mCityText.setText(City);
        }else mCityText.setText(MyApplication.getContext().getString(R.string.nullValue));

        if(phone != null) {
            mPhoneText.setText(phone);
        }else mPhoneText.setText(MyApplication.getContext().getString(R.string.nullValue));

        if(reviewCount != null){
            mReviewCount.setText(MyApplication.getContext().getString(R.string.review_count,reviewCount));
        }else mReviewCount.setText(MyApplication.getContext().getString(R.string.no_reviews));

        if(category != null) {
            mCategoryText.setText(category);
        }else mCategoryText.setText(MyApplication.getContext().getString(R.string.no_category));

        Picasso.with(context)
                .load(ratingImage)
                .into(mRatingImage);

    }

}
