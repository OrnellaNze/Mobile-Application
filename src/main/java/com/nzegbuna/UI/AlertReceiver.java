package com.nzegbuna.UI;


import static android.provider.Settings.System.getString;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.nzegbuna.R;

import java.util.Random;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract the extras from the intent
        String type = intent.getStringExtra("type"); // Could be "start" or "end"
        String title = intent.getStringExtra("title"); // The title of the vacation
        long dateMillis = intent.getLongExtra("date", 0); // The date the alarm is for
        String message = intent.getStringExtra("message"); // Custom message based on the alert type

        // Create a notification channel ID
        String channelId = type.equals("start") ? "vacation_start_alerts" : "vacation_end_alerts";

        // Prepare the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification) // Replace with your app's notification icon drawable
                .setContentTitle(title + " Alert")
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, R.color.black)) // Replace with a color appropriate for your app
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent resultIntent = new Intent(context, MainActivity.class); // Replace MainActivity.class with the relevant activity
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,  PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(resultPendingIntent);

        // Issue the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(type.hashCode(), builder.build()); // Use a unique ID for each notification type
    }


    }




