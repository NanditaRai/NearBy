package com.example.nearby;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;



public class DetailActivity extends AppCompatActivity {

    public static final String sDetailKey = "detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        //Getting the views
        ImageView mImage = (ImageView) findViewById(R.id.dImage);
        TextView mNameText = (TextView) findViewById(R.id.dNameText);
        TextView mRatingText = (TextView) findViewById(R.id.dRatingText);
        TextView mPhoneText = (TextView) findViewById(R.id.dPhoneText);
        ImageView mUrlImage = (ImageView) findViewById(R.id.dUrlImage);
        TextView mReviewCount = (TextView) findViewById(R.id.dReviewCount);
        TextView mAddress = (TextView) findViewById(R.id.dAddress);

        //Getting the data passed through intent
        Business business = (Business) getIntent().getSerializableExtra(sDetailKey);

        //Setting the views
        Picasso.with(this)
               .load(business.imageUrl())
               .into(mImage);
        mNameText.setText(business.name());
        mRatingText.setText(getString(R.string.rating_text,business.rating().toString()));
        mPhoneText.setText(getString(R.string.phone_text, business.displayPhone()));
        Picasso.with(this)
                .load(business.ratingImgUrlLarge())
                .into(mUrlImage);
        mReviewCount.setText(getString(R.string.review_count,business.reviewCount().toString()));
       // mReviewCount.setText(business.reviewCount().toString() + " " + "reviews");
        StringBuilder builder = new StringBuilder();
        for (String address : business.location().address()) {
            builder.append(address).append("\n");
        }
        mAddress.setText(getString(R.string.location_text,builder.toString()));
    }

}