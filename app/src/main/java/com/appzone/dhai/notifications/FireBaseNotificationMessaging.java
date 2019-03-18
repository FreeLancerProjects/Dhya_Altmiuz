package com.appzone.dhai.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.models.NotificationModel;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.preferences.Preferences;
import com.appzone.dhai.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;
import java.util.Map;

public class FireBaseNotificationMessaging extends FirebaseMessagingService {
    private Preferences preferences = Preferences.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> map = remoteMessage.getData();
        for (String key : remoteMessage.getData().keySet()) {
            Log.e("Key", key + " " + "value" + " " + remoteMessage.getData().get(key));
        }

        manageNotification(map);


    }

    private void manageNotification(final Map<String, String> map) {
        String session = getSession();
        UserModel userModel = getUserData();
        int receiver_id = Integer.parseInt(map.get("receiver_id"));
        if (session.equals(Tags.session_login)) {
            if (userModel != null && userModel.getId() == receiver_id) {
                new Handler(Looper.getMainLooper())
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CreateNotification(map);
                            }
                        }, 1);

            }
        }
    }

    private void CreateNotification(Map<String, String> map) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreateProfessionalNotification(map);
        } else {
            CreateNativeNotification(map);

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateProfessionalNotification(Map<String, String> map) {
        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        int type = Integer.parseInt(map.get("type"));
        int service_id = Integer.parseInt(map.get("service_id"));
        String service_title_ar = map.get("service_title_ar");
        String service_title_en = map.get("service_title_en");
        long created_at = Long.parseLong(map.get("created_at"));
        int receiver_id = Integer.parseInt(map.get("receiver_id"));

        NotificationModel notificationModel = new NotificationModel(service_id,service_title_ar,service_title_en,created_at,type,receiver_id);
        String CHANNEL_ID = "my_channel_01";
        CharSequence CHANNEL_NAME = "channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .build()
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_path));
        builder.setContentTitle(getString(R.string.admin));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setSmallIcon(R.drawable.ic_notification_icon);
        builder.setLargeIcon(bitmap);
        builder.setColor(ContextCompat.getColor(this,R.color.colorPrimary));
        builder.setColorized(true);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("status",type);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        if (type == Tags.NOTIFICATION_ACCEPT_CHARGE)
        {
            builder.setContentText(getString(R.string.accepted)+" "+getString(R.string.package_1)+" "+" "+notificationModel.getService_title_ar()+" "+getString(R.string.rsa));

        }else if (type==Tags.NOTIFICATION_REFUSE_CHARGE)
        {

            builder.setContentText(getString(R.string.refused)+" "+getString(R.string.package_1)+" "+notificationModel.getService_title_ar()+" "+getString(R.string.rsa));

        }else if (type==Tags.NOTIFICATION_ACCEPT_SERVICE)
        {

            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                builder.setContentText(getString(R.string.accepted)+" "+notificationModel.getService_title_ar());


            }else
            {
                builder.setContentText(getString(R.string.accepted)+" "+notificationModel.getService_title_en());


            }
        }
        else if (type==Tags.NOTIFICATION_REFUSE_SERVICE)
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                builder.setContentText(getString(R.string.refused)+" "+notificationModel.getService_title_ar());

            }else
            {
                builder.setContentText(getString(R.string.refused)+" "+notificationModel.getService_title_en());


            }
        }
        else if (type==Tags.NOTIFICATION_FINISH_SERVICE)
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {

                builder.setContentText(getString(R.string.finished)+" "+notificationModel.getService_title_ar());

            }else
            {
                builder.setContentText(getString(R.string.finished)+" "+notificationModel.getService_title_en());


            }
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
            manager.notify(1, builder.build());
            EventBus.getDefault().post(notificationModel);
        }


    }

    private void CreateNativeNotification(Map<String, String> map) {
        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        int type = Integer.parseInt(map.get("type"));
        int service_id = Integer.parseInt(map.get("service_id"));
        String service_title_ar = map.get("service_title_ar");
        String service_title_en = map.get("service_title_en");
        long created_at = Long.parseLong(map.get("created_at"));
        int receiver_id = Integer.parseInt(map.get("receiver_id"));

        NotificationModel notificationModel = new NotificationModel(service_id,service_title_ar,service_title_en,created_at,type,receiver_id);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSound(Uri.parse(sound_path));
        builder.setContentTitle(getString(R.string.admin));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setSmallIcon(R.drawable.ic_notification_icon);
        builder.setLargeIcon(bitmap);
        builder.setColor(ContextCompat.getColor(this,R.color.colorPrimary));
        builder.setColorized(true);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("status",type);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        if (type == Tags.NOTIFICATION_ACCEPT_CHARGE)
        {
            builder.setContentText(getString(R.string.accepted)+" "+getString(R.string.package_1)+" "+" "+notificationModel.getService_title_ar()+" "+getString(R.string.rsa));

        }else if (type==Tags.NOTIFICATION_REFUSE_CHARGE)
        {

            builder.setContentText(getString(R.string.refused)+" "+getString(R.string.package_1)+" "+notificationModel.getService_title_ar()+" "+getString(R.string.rsa));

        }else if (type==Tags.NOTIFICATION_ACCEPT_SERVICE)
        {

            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                builder.setContentText(getString(R.string.accepted)+" "+notificationModel.getService_title_ar());


            }else
            {
                builder.setContentText(getString(R.string.accepted)+" "+notificationModel.getService_title_en());


            }
        }
        else if (type==Tags.NOTIFICATION_REFUSE_SERVICE)
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                builder.setContentText(getString(R.string.refused)+" "+notificationModel.getService_title_ar());

            }else
            {
                builder.setContentText(getString(R.string.refused)+" "+notificationModel.getService_title_en());


            }
        }
        else if (type==Tags.NOTIFICATION_FINISH_SERVICE)
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {

                builder.setContentText(getString(R.string.finished)+" "+notificationModel.getService_title_ar());

            }else
            {
                builder.setContentText(getString(R.string.finished)+" "+notificationModel.getService_title_en());


            }
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, builder.build());
            EventBus.getDefault().post(notificationModel);
        }
    }


    private UserModel getUserData() {
        return preferences.getUserData(this);
    }

    private String getSession() {
        return preferences.getSession(this);
    }
}
