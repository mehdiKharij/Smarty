package com.example.smarty;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseReference = FirebaseDatabase.getInstance().getReference("smart-Home");

        // Ajouter un listener pour surveiller les changements dans la base de données
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flameDetected = dataSnapshot.child("flame/flameDetected").getValue(Boolean.class);
                boolean gasDetected = dataSnapshot.child("Gas/gasDetected").getValue(Boolean.class);
                boolean mouvementDetecte = dataSnapshot.child("movement/mouvementDetecte").getValue(Boolean.class);

                if (flameDetected || gasDetected || mouvementDetecte) {
                    displayNotification(flameDetected, gasDetected, mouvementDetecte);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData().get("message"));
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void displayNotification(boolean flameDetected, boolean gasDetected, boolean mouvementDetecte) {
        String messageBody = "";
        if (flameDetected) {
            messageBody += "Flame detected. ";
        }
        if (gasDetected) {
            messageBody += "Gas leak detected. ";
        }
        if (mouvementDetecte) {
            messageBody += "Movement detected. ";
        }
        sendNotification(messageBody);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int pendingIntentFlag = PendingIntent.FLAG_ONE_SHOT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntentFlag |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, pendingIntentFlag);

        String channelId = getString(R.string.default_notification_channel_id);
        createNotificationChannel(channelId);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notif) // Assurez-vous d'avoir une icône appropriée
                .setContentTitle(getString(R.string.fcm_message))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        Log.d(TAG, "Notification sent: " + messageBody);
    }

    private void createNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.default_notification_channel_name);
            String description = getString(R.string.default_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
