package com.example.android.thechallenge;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationBroadCastReceiver extends BroadcastReceiver {

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.d("NotificationBroadCastReceiver","The Notification is sent after a set time ");
        sendNotification(context);



//        throw new UnsupportedOperationException("Not yet implemented");

    }

    public void sendNotification(Context context)
    {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Create the pending intent to launch the activity
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager notificationManager;
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.drawable.ic_all_out_black_24dp)
                    .setContentTitle("Journal notification")
                    .setContentText("It is time to act")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setChannelId("default")
                    .setContentIntent(pendingIntent);

            notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(492 /* ID of notification */, notificationBuilder.build());

            Log.d("MessageReceived ", "The message has beed recieved ");
        }
        catch (Exception e)
        {
            Log.d("NotificationError","This is the reasin why the notification is not building " + e.getMessage());
        }

    }


}
