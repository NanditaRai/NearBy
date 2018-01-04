package com.example.nearby.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.nearby.MyApplication;
import com.example.nearby.R;
import com.example.nearby.usefulClasses.MyDBHandler;
import com.example.nearby.usefulClasses.Product;
import com.example.nearby.usefulClasses.ViewHolder;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Product> {

    private final Context context;
    private LayoutInflater mInflater;
    private final List<Product> products;

    public CustomAdapter(Context applicationContext, List<Product> products) {
        super(applicationContext, R.layout.list_item, products);
        this.context = applicationContext;
        this.products = products;
        mInflater = LayoutInflater.from(applicationContext);
    }


   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();

            //getting views in holder
            holder.getViews(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       Product product = getItem(position);
       //setting views in holder
       assert product != null;
       holder.setViews(context, product.getName(),product.getRating().toString(),product.getDisplayPhone(),
               product.getRatingImageUrl(),product.getReviewCount().toString(),product.getUrl());

       if ( MyDBHandler.getInstance().checkIsDataAlreadyInDBorNot(product)) {
            holder.mFavoriteImg.setImageResource(R.drawable.golden_star);
            holder.mFavoriteImg.setTag(MyApplication.getContext().getString(R.string.selected));
       } else {
            holder.mFavoriteImg.setImageResource(R.drawable.gray_star);
            holder.mFavoriteImg.setTag(MyApplication.getContext().getString(R.string.unselected));
       }
       return convertView;
    }

    /*Add item to the list and notify to the adapter*/
    @Override
    public void add(Product product) {
        super.add(product);
        products.add(product);
        notifyDataSetChanged();
    }

    /*Remove item from the list and notify to adapter*/
    @Override
    public void remove(Product product) {
        super.remove(product);
        products.remove(product);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
