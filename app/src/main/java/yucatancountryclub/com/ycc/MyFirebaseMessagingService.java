package yucatancountryclub.com.ycc;

/**
 * Created by Zazu on 27/11/18.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by sebastianmendezgiron on 12/04/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        //Log.e("FireBase", remoteMessage.getNotification().getBody());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "From: " + remoteMessage.getMessageType());
        //Log.d(TAG, "From: " + remoteMessage.getNotification().getTag());

        System.out.println("HOLI");


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Log.d(TAG, "Notification Message data: " + remoteMessage.getData().get("section"));
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String notification_title = getResources().getString(R.string.app_name);
            if(remoteMessage.getNotification().getTitle()!=null){
                notification_title += " "+ remoteMessage.getNotification().getTitle();
            }
            //sendNotification(notification_title,remoteMessage.getNotification().getBody(),remoteMessage.getFrom());
        }
    }

    private void sendNotification(String title, String body, String from){
//        startActivity(new Intent(this, WelcomeActivity.class));
        /*System.out.println("Entro pero no envía");
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";

        Intent intent = new Intent(this, ReportHistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(getColor(R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notifyID, mBuilder.build());*/


        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),"id_product")
                .setSmallIcon(R.drawable.ic_stat_name) //your app icon
                .setBadgeIconType(R.drawable.ic_stat_name) //your app icon
                .setChannelId("id_product")
                .setContentTitle(title)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setNumber(1)
                .setColor(getColor(R.color.colorPrimary))
                .setContentText(body)
                .setWhen(System.currentTimeMillis());
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notificationBuilder.build());

        /*
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_app)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager2 = getSystemService(NotificationManager.class);
            notificationManager2.createNotificationChannel(channel);
        }

        notificationManager.notify(0  , notificationBuilder.build());*/



    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        // you can get ur data here
        System.out.println(intent.getExtras());
        System.out.println(intent.getExtras().get("gcm.notification.title"));

        String notification_title = intent.getExtras().get("gcm.notification.title").toString();
        String notification_body = intent.getExtras().get("gcm.notification.body").toString();

        sendNotification(notification_title,notification_body,"");
    }

}