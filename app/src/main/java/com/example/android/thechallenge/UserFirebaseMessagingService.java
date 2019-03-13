package com.example.android.thechallenge;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.android.thechallenge.AddAThought.returnTime;

public class UserFirebaseMessagingService extends FirebaseMessagingService
{

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
//        This is the token will figure out where to use it
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
       Log.d("MessageReceived","The message is from " + remoteMessage.getFrom());

//       Will set up the alarm manager so that it calls the notification method after that
//        Amount of timeg
//
//        sendNotification(this);

        Intent intent = new Intent(this,NotificationBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long now = SystemClock.elapsedRealtime();
        int time = returnTime();
        int timeNotification = time*360*10000;
        long alarmtimeschedule = Long.valueOf(timeNotification);
        long alarmtime = now + alarmtimeschedule;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME,alarmtime,pendingIntent);

    }



}
