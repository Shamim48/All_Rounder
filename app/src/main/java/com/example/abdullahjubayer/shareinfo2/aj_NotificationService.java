package com.example.abdullahjubayer.shareinfo2;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class aj_NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage!=null){

                Intent intent = new Intent(this, aj_DisplayNotification.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(0, mBuilder.build());
        }

    }
}
