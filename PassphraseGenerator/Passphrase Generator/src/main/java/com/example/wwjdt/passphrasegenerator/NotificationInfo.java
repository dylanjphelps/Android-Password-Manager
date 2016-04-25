package com.example.wwjdt.passphrasegenerator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * This service is started when an Alarm has been raised
 *
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 *
 */
public class NotificationInfo extends Service {

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotificationInfo getService() {
            return NotificationInfo.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.example.wwjdt.passphrase.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i("NotificationInfo", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {
        // This is the 'title' of the notification
        CharSequence title = "Passphrase Generator!!";
        // This is the icon to use on the notification
        int icon = com.example.wwjdt.passphrasegenerator.R.drawable.alarmicon;
        // This is the scrolling text of the notification
        CharSequence text = "You should change your password soon!! ";
        // What time to show on the notification
        long time = System.currentTimeMillis();

        Notification notification = new Notification(icon, text, time);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SecondActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        notification = builder.setContentIntent(contentIntent)
                .setSmallIcon(icon).setTicker(text).setWhen(time)
                .setAutoCancel(true).setContentTitle(title)
                .setContentText(text).build();
        mNM.notify(NOTIFICATION, notification);

        // Clear the notification when it is pressed
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Send the notification to the system.
        mNM.notify(NOTIFICATION, notification);

        // Stop the service when we are finished
        stopSelf();
    }
}
