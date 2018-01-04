package com.example.nearby.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.example.nearby.R;
import com.example.nearby.usefulClasses.MyDBHandler;
import com.example.nearby.usefulClasses.Product;
import com.example.nearby.usefulClasses.ViewHolder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Product> {

    private final Context context;
    private LayoutInflater mInflater;
    private final List<Product> products;
    private MyDBHandler mDbHandler;

    public CustomAdapter(Context applicationContext, List<Product> products) {
        super(applicationContext, R.layout.list_item1, products);
        this.context = applicationContext;
        this.products = products;
        mInflater = LayoutInflater.from(applicationContext);
        mDbHandler = MyDBHandler.getInstance();
    }

   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item1, parent, false);
            holder = new ViewHolder();

            //getting views in holder
            holder.getViews(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


       final Product product = getItem(position);
       //setting views in holder
       assert product != null;
       holder.setViews(context, product.getName(),product.getLocation(),product.getCategory(),product.getDisplayPhone(),
               product.getRatingImageUrl(),product.getReviewCount().toString(),product.getImageUrl());


       /**
        * Handling check event on the favorite toggle button
        */
       holder.mFavoriteImg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked) {
                   if (!MyDBHandler.getInstance().checkIsDataAlreadyInDBorNot(product))
                       mDbHandler.addProduct(product);
               } else {
                   mDbHandler.deleteProduct(product);
               }
           }
       });

       //Set the checked state after onCheckChangeListener so that state is maintained while scrolling
       setFavoriteButton(holder,product);

       return convertView;
    }

    /*
     Set the favorite button color as checked or unchecked based on whether it is present in the database
    */
    private void setFavoriteButton(ViewHolder holder,Product product) {
        if ( MyDBHandler.getInstance().checkIsDataAlreadyInDBorNot(product)) {
            holder.mFavoriteImg.setChecked(true);
        } else {
            holder.mFavoriteImg.setChecked(false);
        }
    }


    /*Add item to the list and notify to the adapter*/
    @Override
    public void add(Product product) {
        products.add(product);
        notifyDataSetChanged();
    }


    /*Remove item from the list and notify to adapter*/
    @Override
    public void remove(Product product) {
        products.remove(product);
        notifyDataSetChanged();
    }

    public void sort(){
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                if( lhs.getRating().compareTo(rhs.getRating())>0)
                    return -1;
                else return 1;
            }
        });
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
