package com.app.ivansuhendra.packinggla;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.ivansuhendra.packinggla.model.BodyNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class NotifFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "NotifFirebaseMessaging";
    int numMessages = 0;
    public static final String EVENT_NOTIF = "EVENT_NOTIF";
    private String FCM_PARAM = "body";
    private String CHANNEL_NAME = "FCM";
    private String CHANNEL_DESC = "Firebase Cloud Messaging";
    Map<String, String> data = null;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        RemoteMessage.Notification notification;
        data = remoteMessage.getData();

        notification = remoteMessage != null ? remoteMessage.getNotification() : null;
        BodyNotification bodyNotify = new BodyNotification();

        bodyNotify.setLocation(data.get("location"));
        bodyNotify.setNotification(data.get("notification"));
        bodyNotify.setStatus(data.get("status"));
        bodyNotify.setSubType(data.get("sub_type"));
        bodyNotify.setType(data.get("type"));
        bodyNotify.setTypeReferenceId(data.get("type_reference_id"));

        if (notification != null) {
            sendNotification(notification, bodyNotify);
            broadcastToActivity(bodyNotify);
        }

    }

    private void sendNotification(RemoteMessage.Notification notification, BodyNotification bodyNotif) {
        String body = notification.getBody();

        Intent intent = new Intent( this, MainActivity.class);
//        intent.putExtra("bodyNotif", (Parcelable) bodyNotif);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity((Context) this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                .setContentTitle(notification.getTitle())
                .setContentText(body != null ? body : "Oops, error on body notification")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_packing))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.ic_packing);

        String picture = data.get(FCM_PARAM);
        if (picture != null && !picture.equals("")) {
            try {
                URL url = new URL(picture);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(body != null ? body : "Oops, error on body notification"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("default", CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100L, 200L, 300L, 400L, 500L});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager == null) {
            throw new AssertionError("Assertion failed");

        } else {
            if (notificationManager != null) {
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }

    private void broadcastToActivity(BodyNotification bodyNotif) {
        Intent intent = new Intent(EVENT_NOTIF);
        intent.putExtra("bodyNotif", (Parcelable) bodyNotif);
        LocalBroadcastManager.getInstance((Context) this).sendBroadcast(intent);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }


}