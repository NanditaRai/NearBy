package com.example.nearby.usefulClasses;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nearby.MyApplication;
import com.example.nearby.R;
import com.squareup.picasso.Picasso;

/**
 * Hold the view of the item in the list
 */

public class ViewHolder {

    private TextView mNameText;
    private TextView mRatingText;
    private TextView mPhoneText;
    private ImageView mRatingImage;
    private TextView mReviewCount;
    private ImageView mHotelImage;
    public ImageView mFavoriteImg;

    /*Getting views*/
    public void getViews(View view){
        mNameText = (TextView)view.findViewById(R.id.nameText);
        mPhoneText = (TextView) view.findViewById(R.id.phoneText);
        mRatingImage = (ImageView) view.findViewById(R.id.urlImage) ;
        mReviewCount = (TextView) view.findViewById(R.id.reviewCount);
        mHotelImage = (ImageView) view.findViewById(R.id.hotelImageView);
        mFavoriteImg = (ImageView) view.findViewById(R.id.imageButton_favorite);
        mRatingText = (TextView) view.findViewById(R.id.ratingText);
    }

    /*Setting views*/
    public void setViews(Context context, String name, String rating, String phone,
                         String ratingImage, String reviewCount, String image){
        mNameText.setText(name);
        mRatingText.setText(rating);
        mPhoneText.setText(phone);
        Picasso.with(context)
                .load(ratingImage)
                .into(this.mRatingImage);
        this.mReviewCount.setText(MyApplication.getContext().getString(R.string.review_count,reviewCount));
        Picasso.with(context)
                .load(image)
                .into(mHotelImage);
    }

}
