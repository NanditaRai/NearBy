package com.example.nearby.usefulClasses;

import android.app.Notification;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.nearby.MyApplication;
import com.example.nearby.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * This PushNotification class pushes the notification on wearable
 */

public class PushNotification {

    private final static String mGROUP_KEY = "group_key";
    private Bitmap mBitMap;

    public PushNotification(String title, Integer position, List<Product> products,
                            int notificationId) {

        // Create builder for the main notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(MyApplication.getContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(products.get(position).getName())
                        .setGroup(mGROUP_KEY)
                        .extend(
                                new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true));

        // Create a big text style for the second page
        NotificationCompat.BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
        secondPageStyle.setBigContentTitle(title)
                .bigText(products.get(position).getDisplayPhone());

        // Create second page notification
        Notification secondPageNotification =
                new NotificationCompat.Builder(MyApplication.getContext())
                        .setStyle(secondPageStyle)
                        .build();

        // Extend the notification builder with the second page
        Notification notification = notificationBuilder
                .extend(new NotificationCompat.WearableExtender()
                        .addPage(secondPageNotification)
                        .setBackground(mBitMap))
                .build();

        // Issue the notification
        NotificationManagerCompat notificationManager =
             NotificationManagerCompat.from(MyApplication.getContext());
        notificationManager.notify(notificationId , notification);
    }

}
